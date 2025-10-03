package yuseteam.mealticketsystemwas.domain.order.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(name = "OrderCreatedRes", description = "주문 확정(결제) 응답",
        example = "{\n  \"orderId\": 5001,\n  \"totalQuantity\": 3,\n  \"totalAmount\": 20500,\n  \"items\": [\n    { \"menuId\": 101, \"menuName\": \"비빔밥\", \"restaurantName\": \"학생식당 A\", \"unitPrice\": 7000, \"quantity\": 2, \"lineTotal\": 14000 },\n    { \"menuId\": 202, \"menuName\": \"돈까스\", \"restaurantName\": \"학생식당 A\", \"unitPrice\": 6500, \"quantity\": 1, \"lineTotal\": 6500 }\n  ],\n  \"tickets\": [\n    { \"ticketId\": 91001, \"qrUuid\": \"aaaaaaaa-bbbb-cccc-dddd-111111111111\" },\n    { \"ticketId\": 91002, \"qrUuid\": \"bbbbbbbb-bbbb-cccc-dddd-222222222222\" },\n    { \"ticketId\": 91003, \"qrUuid\": \"cccccccc-bbbb-cccc-dddd-333333333333\" }\n  ]\n}")
public class OrderCreatedRes {
    @Schema(description = "생성된 주문 ID", example = "5001")
    private Long orderId;
    @Schema(description = "총 주문 수량(모든 라인 quantity 합)" , example = "3")
    private int totalQuantity;
    @Schema(description = "총 주문 금액(모든 라인 lineTotal 합)", example = "20500")
    private int totalAmount;

    @ArraySchema(schema = @Schema(implementation = CreatedItem.class), arraySchema = @Schema(description = "생성된 주문 라인 목록"))
    private List<CreatedItem> items;
    @ArraySchema(schema = @Schema(implementation = IssuedTicket.class), arraySchema = @Schema(description = "발급된 식권(QR) 목록 — 길이 = totalQuantity"))
    private List<IssuedTicket> tickets;

    @Getter @Builder
    @Schema(name = "OrderCreatedRes.CreatedItem", description = "생성된 주문 단일 항목")
    public static class CreatedItem {
        @Schema(description = "메뉴 ID", example = "101")
        private Long menuId;
        @Schema(description = "메뉴명", example = "비빔밥")
        private String menuName;
        @Schema(description = "식당명", example = "학생식당 A")
        private String restaurantName;
        @Schema(description = "단가(원)", example = "7000")
        private int unitPrice;
        @Schema(description = "주문 수량", example = "2")
        private int quantity;
        @Schema(description = "라인 합계 금액(단가 * 수량)", example = "14000")
        private int lineTotal;
    }

    @Getter @Builder
    @Schema(name = "OrderCreatedRes.IssuedTicket", description = "발급된 단일 식권 정보")
    public static class IssuedTicket {
        @Schema(description = "식권(티켓) ID", example = "91001")
        private Long ticketId;
        @Schema(description = "QR UUID", example = "aaaaaaaa-bbbb-cccc-dddd-111111111111")
        private String qrUuid;
    }
}