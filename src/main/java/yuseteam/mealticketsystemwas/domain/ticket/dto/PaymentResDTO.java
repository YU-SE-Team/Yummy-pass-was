package yuseteam.mealticketsystemwas.domain.ticket.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResDTO {
    private Long ticketId;
    private String menuName;
    private String restaurantName;
    private LocalDateTime purchaseTime;
    private String qrUuid;
    private String category;
    private boolean isUsed;

    public static PaymentResDTO from(Ticket ticket) {
        var oi = ticket.getOrderItem();
        return PaymentResDTO.builder()
                .ticketId(ticket.getId())
                .qrUuid(ticket.getQrCode())
                .isUsed(Boolean.TRUE.equals(ticket.getIsUsed()))
                .purchaseTime(ticket.getPurchaseTime())
                .menuName(oi.getMenuNameSnapshot())
                .restaurantName(oi.getRestaurantNameSnapshot())
                .category(oi.getMenu().getCategory().getCategory())
                .build();
    }
}
