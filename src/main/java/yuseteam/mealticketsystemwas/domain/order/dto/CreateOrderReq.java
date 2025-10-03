package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(name = "CreateOrderReq", description = "주문/요약 요청 바디 - 장바구니 항목 목록", example = "{\n  \"items\": [ { \"menuId\": 101, \"quantity\": 2 }, { \"menuId\": 202, \"quantity\": 1 } ]\n}")
public class CreateOrderReq {
    @ArraySchema(schema = @Schema(implementation = Item.class), arraySchema = @Schema(description = "장바구니 메뉴 항목 배열 - 비어있으면 안됨"))
    private List<Item> items;

    @Getter
    public static class Item {
        @Schema(description = "메뉴 ID (양의 정수)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
        private Long menuId;
        @Schema(description = "주문 수량 (0 이상) - 0 은 무시됨", example = "2", minimum = "0", defaultValue = "1")
        private int quantity;
    }
}