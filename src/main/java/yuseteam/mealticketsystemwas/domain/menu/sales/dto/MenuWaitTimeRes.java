package yuseteam.mealticketsystemwas.domain.menu.sales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "메뉴별 예상 대기시간 응답 DTO")
public class MenuWaitTimeRes implements Serializable {

    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;

    @Schema(description = "메뉴명", example = "김치찌개")
    private String menuName;

    @Schema(description = "예상 대기시간(분). 샘플 부족 시 null", example = "12", nullable = true)
    private Integer expectedWaitTime;
}
