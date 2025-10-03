package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "주문 확정 응답")
public class OrderConfirmationRes {
    @Schema(description = "주문자명", example = "홍길동")
    private String userName;
    @Schema(description = "주문일시", example = "2023-10-03T12:34:56")
    private java.time.LocalDateTime orderDate;
    @Schema(description = "메뉴명", example = "돈까스")
    private String menuName;
    @Schema(description = "가격", example = "5000")
    private int price;
    @Schema(description = "식당명", example = "학생식당")
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
