package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MenuSalesGraphRes {
    private Long menuId;
    private String menuName;
    private List<SalesDataPoint> salesDataPoints;
}