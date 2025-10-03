package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(name = "OrderSummaryRes", description = "장바구니 요약 응답",
        example = "{\n  \"lines\": [\n    {\n      \"menuId\": 101, \"menuName\": \"비빔밥\", \"restaurantName\": \"학생식당 A\", \"unitPrice\": 7000, \"quantity\": 2, \"lineTotal\": 14000\n    },\n    {\n      \"menuId\": 202, \"menuName\": \"돈까스\", \"restaurantName\": \"학생식당 A\", \"unitPrice\": 6500, \"quantity\": 1, \"lineTotal\": 6500\n    }\n  ],\n  \"totalQuantity\": 3,\n  \"totalAmount\": 20500\n}")
public class OrderSummaryRes {
    @ArraySchema(schema = @Schema(implementation = Line.class), arraySchema = @Schema(description = "요약 라인 목록"))
    private List<Line> lines;
    private int totalQuantity;
    private int totalAmount;

    @Getter @Builder
    public static class Line {
        @Schema(description = "메뉴 ID", example = "101")
        private Long menuId;
        @Schema(description = "메뉴명", example = "비빔밥")
        private String menuName;
        @Schema(description = "식당명", example = "학생식당 A")
        private String restaurantName;
        @Schema(description = "단가(원)", example = "7000")
        private int unitPrice;
        @Schema(description = "요청 수량", example = "2")
        private int quantity;
        @Schema(description = "라인 합계(단가 * 수량)", example = "14000")
        private int lineTotal;
    }
}