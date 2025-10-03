package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 생성 요청")
public class CreateOrderReq {
    @Schema(description = "주문 항목 목록")
    private List<Item> items;

    @Getter
    @Schema(description = "주문 항목")
    public static class Item {
        @Schema(description = "메뉴 ID", example = "1")
        private Long menuId;
        @Schema(description = "수량", example = "2")
        private int quantity;
    }
}