package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SalesDataPoint {
    private LocalDateTime time;
    private Integer salesInInterval;
    private Integer cumulativeAtPoint;
}
