package yuseteam.mealticketsystemwas.domain.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByQrCode(String qrCode);

    List<Ticket> findByUserIdAndIsUsedFalseAndPurchaseTimeBefore(Long userId, LocalDateTime expiredTime);

    List<Ticket> findByUserIdAndIsUsedFalseAndPurchaseTimeAfter(
            Long userId,
            LocalDateTime time
    );

    List<Ticket> findByOrderItemMenuIdAndUsedTimeIsNotNullAndReceivedTimeIsNotNull(Long menuId);

    List<Ticket> findByUserIdAndIsUsedTrue(Long userId);
}
