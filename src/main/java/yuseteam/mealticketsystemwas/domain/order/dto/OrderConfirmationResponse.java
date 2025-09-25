package yuseteam.mealticketsystemwas.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;

import java.time.LocalDate;

@Getter
@Builder
public class OrderConfirmationResponse {

    private String userName;
    private LocalDate orderDate;
    private String menuName;
    private int price;
    private String restaurantName;

    public static OrderConfirmationResponse of(User user, Menu menu) {
        return OrderConfirmationResponse.builder()
                .userName(user.getName())
                .orderDate(LocalDate.now())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .restaurantName(menu.getRestaurant().getName())
                .build();
    }
}
