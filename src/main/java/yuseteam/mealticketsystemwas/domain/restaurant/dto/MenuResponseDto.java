package yuseteam.mealticketsystemwas.domain.restaurant.dto;

import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.admin.entity.Menu;

@Getter
public class MenuResponseDto {
    private final Long id;
    private final String name;
    private final String photoUrl;
    private final int price;

    public MenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.photoUrl = menu.getPhotoUrl();
        this.price = menu.getPrice();
    }
}
