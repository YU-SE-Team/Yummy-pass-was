package yuseteam.mealticketsystemwas.domain.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByQrCode(String qrCode);

    List<Ticket> findByIsUsedFalseAndPurchaseTimeBefore(LocalDateTime expiredTime);

    List<Ticket> findByIsUsedFalse();
}
