package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "주문 요약 응답")
public class OrderSummaryRes {
    @Schema(description = "주문 항목 요약 목록")
    private List<Line> lines;
    @Schema(description = "총 수량", example = "3")
    private int totalQuantity;
    @Schema(description = "총 금액", example = "15000")
    private int totalAmount;

    @Getter @Builder
    @Schema(description = "주문 항목 요약")
    public static class Line {
        @Schema(description = "메뉴 ID", example = "1")
        private Long menuId;
        @Schema(description = "메뉴명", example = "돈까스")
        private String menuName;
        @Schema(description = "식당명", example = "학생식당")
        private String restaurantName;
        @Schema(description = "단가", example = "5000")
        private int unitPrice;
        @Schema(description = "수량", example = "2")
        private int quantity;
        @Schema(description = "항목별 합계", example = "10000")
        private int lineTotal;
    }
}