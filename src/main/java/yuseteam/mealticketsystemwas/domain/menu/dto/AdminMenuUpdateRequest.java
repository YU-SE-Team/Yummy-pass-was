package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminMenuUpdateRequest {
    private String name;
    private Integer price;
    private Integer totalCount;
    private String category;
    private Boolean visible;
}
