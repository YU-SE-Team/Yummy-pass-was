package yuseteam.mealticketsystemwas.domain.ticket.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResponse;
import yuseteam.mealticketsystemwas.domain.ticket.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets")
@Tag(name = "Ticket", description = "티켓 사용·미사용·만료 조회 API")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/expired")
    public ResponseEntity<List<TicketResponse>> getExpiredTickets(){
        List<TicketResponse> expiredTickets = ticketService.getExpiredTickets();
        return ResponseEntity.ok(expiredTickets);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<TicketResponse>> getUnusedTickets() {
        List<TicketResponse> unusedTickets = ticketService.getUnusedTickets();
        return ResponseEntity.ok(unusedTickets);
    }

    @PatchMapping("/{ticketId}/receive")
    public ResponseEntity<TicketResponse> completeReceive(@PathVariable Long ticketId){
        TicketResponse updated = ticketService.completeReceive(ticketId);
        return ResponseEntity.ok(updated);
    }

}
