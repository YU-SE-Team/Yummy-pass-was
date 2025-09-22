package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;

@Getter
public class MenuResponse {
    private final Long id;
    private final String name;
    private final String photoUrl;
    private final int price;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.photoUrl = menu.getPhotoUrl();
        this.price = menu.getPrice();
    }
}
