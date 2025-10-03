package yuseteam.mealticketsystemwas.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuNameSnapshot;        // 주문 시점 메뉴명 스냅샷
    private String restaurantNameSnapshot;  // 주문 시점 식당명 스냅샷
    private int unitPrice;                  // 주문 시점 단가
    private int quantity;                   // 수량
    private int lineTotal;                  // unitPrice * quantity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;                      // 원본 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Setter
    private Order order;

    public static OrderItem of(Menu menu, int quantity) {
        int unitPrice = menu.getPrice();
        return OrderItem.builder()
                .menu(menu)
                .menuNameSnapshot(menu.getName())
                .restaurantNameSnapshot(menu.getRestaurant().getName())
                .unitPrice(unitPrice)
                .quantity(quantity)
                .lineTotal(unitPrice * quantity)
                .build();
    }
}