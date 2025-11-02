package yuseteam.mealticketsystemwas.domain.ticket.dto;

import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResDTO {
    private Long id;
    private String qrCode;
    private Boolean isUsed;
    private LocalDateTime purchaseTime;
    private LocalDateTime receivedTime;
    private String userName;
    private String menuName;

    public static TicketResDTO fromEntity(Ticket ticket) {
        return TicketResDTO.builder()
                .id(ticket.getId())
                .qrCode(ticket.getQrCode())
                .isUsed(ticket.getIsUsed())
                .purchaseTime(ticket.getPurchaseTime())
                .receivedTime(ticket.getReceivedTime())
                .userName(ticket.getUser().getName())
                .menuName(ticket.getOrderItem().getMenu().getName())
                .build();
    }
}