package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularMenuRes {
    private String menuName;
    private Integer totalSalesLast20Minutes;
}