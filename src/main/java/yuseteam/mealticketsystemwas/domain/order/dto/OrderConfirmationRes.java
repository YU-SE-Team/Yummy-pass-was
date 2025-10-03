package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(name = "OrderConfirmationRes", description = "단일 메뉴 주문 확인 응답 (사용 위치에 따라 선택적)",
        example = "{\n  \"userName\": \"홍길동\",\n  \"orderDate\": \"2025-10-04T12:34:56.789\",\n  \"menuName\": \"비빔밥\",\n  \"price\": 7000,\n  \"restaurantName\": \"학생식당 A\"\n}")
public class OrderConfirmationRes {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;
    @Schema(description = "주문 시각(서버, ISO8601)", example = "2025-10-04T12:34:56.789")
    private LocalDateTime orderDate;
    @Schema(description = "메뉴명", example = "비빔밥")
    private String menuName;
    @Schema(description = "가격(원)", example = "7000")
    private int price;
    @Schema(description = "식당명", example = "학생식당 A")
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
