package yuseteam.mealticketsystemwas.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.config.SecurityUtil;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResDTO;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public List<TicketResDTO> getExpiredTickets(){

        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }

        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
        List<Ticket> expiredTickets = ticketRepository.findByUserIdAndIsUsedFalseAndPurchaseTimeBefore(currentUserId, expiredTime);

        return expiredTickets.stream()
                .map(TicketResDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TicketResDTO> getUnusedTickets() {

        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }

        LocalDateTime expiredLimit = LocalDateTime.now().minusDays(1);
        List<Ticket> unusedTickets =
                ticketRepository.findByUserIdAndIsUsedFalseAndPurchaseTimeAfter(
                        currentUserId,
                        expiredLimit
                );

        return unusedTickets.stream()
                .map(TicketResDTO::fromEntity)
                .toList();
    }

    @Transactional
    public TicketResDTO completeReceive(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new IllegalArgumentException("식권을 찾을 수 없습니다."));

        if(ticket.getReceivedTime()!= null){
            throw new IllegalArgumentException("이미 음식 수령 완료 처리되었습니다.");
        }

        if(!Boolean.TRUE.equals((ticket.getIsUsed()))){
            throw new IllegalArgumentException("사용 완료된 식권만 수령이 가능합니다.");
        }

        ticket.completeReceive();
        Ticket saved = ticketRepository.save(ticket);
        return TicketResDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketResDTO> getUsedTickets() {

        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }

        List<Ticket> usedTickets = ticketRepository.findByUserIdAndIsUsedTrue(currentUserId);

        return usedTickets.stream()
                .map(TicketResDTO::fromEntity)
                .toList();
    }

}
