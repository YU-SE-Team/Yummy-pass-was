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
    private String qrCodeUrl ;

    public static PaymentRes from(Ticket ticket) {
        return PaymentRes.builder()
                .ticketId(ticket.getId())
                .menuName(ticket.getMenuName())
                .restaurantName(ticket.getRestaurant())
                .purchaseTime(ticket.getPurchaseTime())
                .qrCodeUrl(ticket.getQrCode())
                .build();
    }
}
