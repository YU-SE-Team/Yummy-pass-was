package yuseteam.mealticketsystemwas.domain.menu.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuWaitTimeRes implements Serializable {
    private Long menuId;
    private String menuName;
    private Integer expectedWaitTime;
}

