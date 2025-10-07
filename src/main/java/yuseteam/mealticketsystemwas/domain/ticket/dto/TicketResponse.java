package yuseteam.mealticketsystemwas.domain.ticket.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponse {
    private Long id;
    private String qrCode;
    private Boolean isUsed;
    private LocalDateTime purchaseTime;
    private String userName;
    private String menuName;

    public static TicketResponse fromEntity(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .qrCode(ticket.getQrCode())
                .isUsed(ticket.getIsUsed())
                .purchaseTime(ticket.getPurchaseTime())
                .userName(ticket.getUser().getName())
                .menuName(ticket.getOrderItem().getMenu().getName())
                .build();
    }
}
