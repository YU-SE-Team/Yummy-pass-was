package yuseteam.mealticketsystemwas.domain.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResponse;
import yuseteam.mealticketsystemwas.domain.ticket.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets")
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
}
