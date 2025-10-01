package yuseteam.mealticketsystemwas.domain.ticket.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentRes {
    private Long ticketId;
    private String menuName;
    private String restaurantName;
    private LocalDateTime purchaseTime;
    private String qrUuid;
    private String category;
    private boolean isUsed;

    public static PaymentRes from(Ticket ticket) {
        return PaymentRes.builder()
                .ticketId(ticket.getId())
                .qrUuid(ticket.getQrCode())
                .isUsed(ticket.getIsUsed())
                .menuName(ticket.getMenuName())
                .purchaseTime(ticket.getPurchaseTime())
                .restaurantName(ticket.getRestaurant())
                .category(ticket.getMenu().getCategory())
                .build();
    }
}
