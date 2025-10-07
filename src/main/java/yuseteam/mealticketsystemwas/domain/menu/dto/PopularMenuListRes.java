package yuseteam.mealticketsystemwas.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "인기 메뉴 리스트 응답")
public class PopularMenuListRes {
    @Schema(description = "인기 메뉴 이름 리스트 (동점인 경우 여러 개)", example = "[\"치킨마요덮밥\", \"돈까스정식\"]")
    private List<String> menuNames;

    @Schema(description = "최근 20분간 총 판매량", example = "15")
    private Integer totalSalesLast20Minutes;
}
