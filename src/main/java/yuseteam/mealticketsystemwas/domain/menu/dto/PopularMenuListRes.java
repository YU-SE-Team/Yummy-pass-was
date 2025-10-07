package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PopularMenuListRes {
    private List<String> menuNames;  // 동점 메뉴들의 이름 리스트
    private Integer totalSalesLast20Minutes;
}
