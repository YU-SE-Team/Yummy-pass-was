package yuseteam.mealticketsystemwas.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "판매 데이터 포인트")
public class SalesDataPoint {
    @Schema(description = "스냅샷 시간", example = "2025-10-07T10:00:00")
    private LocalDateTime time;

    @Schema(description = "해당 구간(5분) 판매량", example = "5")
    private Integer salesInInterval;

    @Schema(description = "해당 시점까지의 누적 판매량", example = "25")
    private Integer cumulativeAtPoint;
}
