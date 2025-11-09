package yuseteam.mealticketsystemwas.domain.menu.sales.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.MenuWaitTimeRes;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.common.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class MenuWaitTimeService {

    private final MenuRepository menuRepository;
    private final TicketRepository ticketRepository;

    private static final int MAX_SAMPLES = 500;
    private static final int MAX_ACCEPTABLE_MINUTES = 180;
    private static final int RECENT_HOURS = 1;

    public MenuWaitTimeRes getMenuWaitTime(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다."));

        LocalDateTime since = LocalDateTime.now().minusHours(RECENT_HOURS);
        List<Ticket> recentSamples = ticketRepository
                .findByOrderItemMenuIdAndUsedTimeIsNotNullAndReceivedTimeIsNotNull(menuId)
                .stream()
                .filter(t -> t.getUsedTime().isAfter(since))
                .sorted(Comparator.comparing(Ticket::getReceivedTime).reversed())
                .limit(MAX_SAMPLES)
                .toList();

        if (recentSamples.isEmpty()) {
            return new MenuWaitTimeRes(menu.getId(), menu.getName(), null);
        }

        List<Double> validDurations = recentSamples.stream()
                .map(t -> {
                    try {
                        long sec = Duration.between(t.getUsedTime(), t.getReceivedTime()).toSeconds();
                        double min = sec / 60.0;
                        return (min > 0 && min <= MAX_ACCEPTABLE_MINUTES) ? min : null;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        if (validDurations.isEmpty()) {
            return new MenuWaitTimeRes(menu.getId(), menu.getName(), null);
        }

        double avg = validDurations.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        if (Double.isNaN(avg)) {
            return new MenuWaitTimeRes(menu.getId(), menu.getName(), null);
        }

        int expectedWaitTime = (int) Math.ceil(avg);

        return new MenuWaitTimeRes(menu.getId(), menu.getName(), expectedWaitTime);
    }
}