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

    private String menuNameSnapshot;
    private String restaurantNameSnapshot;
    private int unitPrice;
    private int quantity;
    private int lineTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

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