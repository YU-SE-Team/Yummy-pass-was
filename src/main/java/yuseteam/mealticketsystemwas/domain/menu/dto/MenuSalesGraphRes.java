package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class MenuSalesGraphRes {
    private Long menuId;
    private String menuName;
    private List<SalesDataPoint> salesDataPoints;
}