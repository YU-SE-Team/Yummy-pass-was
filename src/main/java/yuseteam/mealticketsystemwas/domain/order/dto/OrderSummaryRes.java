package yuseteam.mealticketsystemwas.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderSummaryRes {
    private List<Line> lines;
    private int totalQuantity;
    private int totalAmount;

    @Getter @Builder
    public static class Line {
        private Long menuId;
        private String menuName;
        private String restaurantName;
        private int unitPrice;
        private int quantity;
        private int lineTotal;
    }
}