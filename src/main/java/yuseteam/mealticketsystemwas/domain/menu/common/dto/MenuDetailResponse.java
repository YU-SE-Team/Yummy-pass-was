package yuseteam.mealticketsystemwas.domain.menu.common.dto;

import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;

@Getter
public class MenuDetailResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final int remainingTickets;

    public MenuDetailResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.remainingTickets = menu.getTotalQuantity() - menu.getCumulativeSoldQuantity();
    }
}
