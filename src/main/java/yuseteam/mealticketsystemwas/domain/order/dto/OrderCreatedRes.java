package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "주문 생성 응답")
public class OrderCreatedRes {
    @Schema(description = "주문 ID", example = "1001")
    private Long orderId;
    @Schema(description = "총 수량", example = "3")
    private int totalQuantity;
    @Schema(description = "총 금액", example = "15000")
    private int totalAmount;

    @Schema(description = "주문 항목 목록")
    private List<CreatedItem> items;
    @Schema(description = "발급된 식권 목록")
    private List<IssuedTicket> tickets;

    @Getter @Builder
    @Schema(description = "주문 항목 상세")
    public static class CreatedItem {
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

    @Getter @Builder
    @Schema(description = "발급된 식권 정보")
    public static class IssuedTicket {
        @Schema(description = "식권 ID", example = "2001")
        private Long ticketId;
        @Schema(description = "QR UUID", example = "uuid-xxxx-xxxx")
        private String qrUuid;
    }
}