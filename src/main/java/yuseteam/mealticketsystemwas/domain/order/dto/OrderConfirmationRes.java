package yuseteam.mealticketsystemwas.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderConfirmationRes {

    private String userName;
    private LocalDateTime orderDate;
    private String menuName;
    private int price;
    private String restaurantName;

    public static OrderConfirmationRes of(User user, Menu menu) {
        return OrderConfirmationRes.builder()
                .userName(user.getName())
                .orderDate(LocalDateTime.now())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .restaurantName(menu.getRestaurant().getName())
                .build();
    }
}
