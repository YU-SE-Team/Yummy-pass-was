package yuseteam.mealticketsystemwas.domain.ticket.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResDTO;
import yuseteam.mealticketsystemwas.domain.ticket.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets")
@Tag(name = "Ticket", description = "티켓 사용·미사용·만료 조회 API")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/expired")
    public ResponseEntity<List<TicketResDTO>> getExpiredTickets(){
        List<TicketResDTO> expiredTickets = ticketService.getExpiredTickets();
        return ResponseEntity.ok(expiredTickets);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<TicketResDTO>> getUnusedTickets() {
        List<TicketResDTO> unusedTickets = ticketService.getUnusedTickets();
        return ResponseEntity.ok(unusedTickets);
    }

    @PatchMapping("/{ticketId}/receive")
    public ResponseEntity<TicketResDTO> completeReceive(@PathVariable Long ticketId){
        TicketResDTO updated = ticketService.completeReceive(ticketId);
        return ResponseEntity.ok(updated);
    }

}
