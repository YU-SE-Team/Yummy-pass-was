package yuseteam.mealticketsystemwas.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "메뉴 판매 그래프 응답")
public class MenuSalesGraphRes {
    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;

    @Schema(description = "메뉴 이름", example = "치킨마요덮밥")
    private String menuName;

    @Schema(description = "시간대별 판매 데이터 포인트 리스트")
    private List<SalesDataPoint> salesDataPoints;
}