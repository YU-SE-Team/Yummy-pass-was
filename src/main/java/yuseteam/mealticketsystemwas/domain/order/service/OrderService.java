package yuseteam.mealticketsystemwas.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;
import yuseteam.mealticketsystemwas.domain.oauthjwt.repository.UserRepository;
import yuseteam.mealticketsystemwas.domain.order.dto.CreateOrderReq;
import yuseteam.mealticketsystemwas.domain.order.dto.OrderCreatedRes;
import yuseteam.mealticketsystemwas.domain.order.dto.OrderSummaryRes;
import yuseteam.mealticketsystemwas.domain.order.entity.Order;
import yuseteam.mealticketsystemwas.domain.order.entity.OrderItem;
import yuseteam.mealticketsystemwas.domain.order.repository.OrderRepository;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrCreateResponse;
import yuseteam.mealticketsystemwas.domain.qr.service.QrService;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final QrService qrService;

    /** 장바구니(바디) 미리보기 계산 — DB 저장 없음 */
    @Transactional(readOnly = true)
    public OrderSummaryRes summarize(List<CreateOrderReq.Item> items) {
        int totalQty = 0;
        int totalAmt = 0;
        List<OrderSummaryRes.Line> lines = new ArrayList<>();

        for (CreateOrderReq.Item reqItem : items) {
            Menu menu = menuRepository.findById(reqItem.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            int unit = menu.getPrice();
            int qty = Math.max(0, reqItem.getQuantity());
            int lineTotal = unit * qty;

            totalQty += qty;
            totalAmt += lineTotal;

            lines.add(OrderSummaryRes.Line.builder()
                    .menuId(menu.getId())
                    .menuName(menu.getName())
                    .restaurantName(menu.getRestaurant().getName())
                    .unitPrice(unit)
                    .quantity(qty)
                    .lineTotal(lineTotal)
                    .build());
        }

        return OrderSummaryRes.builder()
                .lines(lines)
                .totalQuantity(totalQty)
                .totalAmount(totalAmt)
                .build();
    }

    /** 결제(주문 확정) + Ticket 발급 */
    @Transactional
    public OrderCreatedRes checkout(Long userId, CreateOrderReq body) {
        if (body == null || body.getItems() == null || body.getItems().isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .build();

        // 1) 각 메뉴 비관적 락으로 조회, 재고 체크/판매량 증가
        for (CreateOrderReq.Item reqItem : body.getItems()) {
            Menu menu = menuRepository.findAndLockById(reqItem.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            int qty = reqItem.getQuantity();
            if (qty <= 0) continue;

            // 재고 확인: soldTicket < totalCount
            for (int i = 0; i < qty; i++) {
                menu.sellTicket(); // 내부에서 초과 시 예외 발생
            }

            // 2) OrderItem 생성/추가
            OrderItem oi = OrderItem.of(menu, qty);
            order.addItem(oi);
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("유효한 주문 항목이 없습니다.");
        }

        // 3) 주문 저장
        orderRepository.save(order);

        // 4) Ticket 발급 (수량만큼)
        List<OrderCreatedRes.IssuedTicket> issuedTickets = new ArrayList<>();
        for (OrderItem oi : order.getItems()) {
            for (int i = 0; i < oi.getQuantity(); i++) {
                QrCreateResponse qr = qrService.createAndUploadQr();

                Ticket t = Ticket.builder()
                        .menuName(oi.getMenuNameSnapshot())
                        .qrCode(qr.getUuid())
                        .restaurant(oi.getRestaurantNameSnapshot())
                        .isUsed(false)
                        .purchaseTime(LocalDateTime.now())
                        .menu(oi.getMenu())
                        .user(user)
                        .build();

                ticketRepository.save(t);

                issuedTickets.add(OrderCreatedRes.IssuedTicket.builder()
                        .ticketId(t.getId())
                        .qrUuid(qr.getUuid())
                        .build());
            }
        }

        // 5) 응답 조립
        List<OrderCreatedRes.CreatedItem> created = new ArrayList<>();
        order.getItems().forEach(oi -> created.add(OrderCreatedRes.CreatedItem.builder()
                .menuId(oi.getMenu().getId())
                .menuName(oi.getMenuNameSnapshot())
                .restaurantName(oi.getRestaurantNameSnapshot())
                .unitPrice(oi.getUnitPrice())
                .quantity(oi.getQuantity())
                .lineTotal(oi.getLineTotal())
                .build()));

        return OrderCreatedRes.builder()
                .orderId(order.getId())
                .totalQuantity(order.getTotalQuantity())
                .totalAmount(order.getTotalAmount())
                .items(created)
                .tickets(issuedTickets)
                .build();
    }
}