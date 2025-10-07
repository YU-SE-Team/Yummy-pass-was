package yuseteam.mealticketsystemwas.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResponse;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public List<TicketResponse> getExpiredTickets(){
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
        List<Ticket> expiredTickets = ticketRepository.findByIsUsedFalseAndPurchaseTimeBefore(expiredTime);

        return expiredTickets.stream()
                .map(TicketResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getUnusedTickets() {
        List<Ticket> unusedTickets = ticketRepository.findByIsUsedFalse();

        return unusedTickets.stream()
                .map(TicketResponse::fromEntity)
                .toList();
    }


}
