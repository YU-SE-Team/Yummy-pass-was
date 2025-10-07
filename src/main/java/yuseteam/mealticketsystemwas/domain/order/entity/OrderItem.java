package yuseteam.mealticketsystemwas.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;

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

    @Builder.Default
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket> tickets = new java.util.ArrayList<>();

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