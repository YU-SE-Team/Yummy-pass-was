package yuseteam.mealticketsystemwas.domain.admin.dto;

import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.admin.entity.Menu;

@Getter
public class MenuDetailResponseDto {
    private final Long id;
    private final String name;
    private final int price;
    private final int remainingTickets;

    public MenuDetailResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.remainingTickets = menu.getTotalCount() - menu.getSoldTicket();
    }
}
