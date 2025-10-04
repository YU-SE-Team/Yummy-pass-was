package yuseteam.mealticketsystemwas.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;
import yuseteam.mealticketsystemwas.domain.oauthjwt.repository.UserRepository;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrCreateResponse;
import yuseteam.mealticketsystemwas.domain.qr.service.QrService;
import yuseteam.mealticketsystemwas.domain.ticket.dto.PaymentRes;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final TicketRepository ticketRepository;
    private final QrService qrService;

    @Transactional
    public PaymentRes payment(Long userId, Long menuId) {
        //1. 유저, 메뉴 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Menu menu = menuRepository.findAndLockById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        //2. 재고 확인
        if (menu.getCumulativeSoldQuantity() >= menu.getTotalQuantity()) {
            throw new IllegalArgumentException("식권이 모두 판매 되었습니다.");
        }

        //3. 메뉴 판매량 1 카운트
        menu.sellTicket();

        //4. QR 코드 생성
         QrCreateResponse qrInfo = qrService.createAndUploadQr();

        //5. Ticket 생성
        Ticket newTicket = Ticket.builder()
                .menuName(menu.getName())
                .qrCode(qrInfo.getUuid())
                .isUsed(false)
                .purchaseTime(LocalDateTime.now())
                .user(user)
                .build();

        //6. 생성된 식권을 DB에 저장
        ticketRepository.save(newTicket);

        //7. 결과 DTO로 변환하여 반환
        return PaymentRes.from(newTicket);
    }
}
