package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuCategory;

@Getter
@Builder
@AllArgsConstructor
public class AdminMenuResponse {
    private final Long id;
    private final String name;
    private final String photoUrl;
    private final int price;
    private final int totalCount;
    private final int soldTicket;
    private final MenuCategory category;
    private final Boolean visible;
    private final String restaurantName;

    public static AdminMenuResponse from(Menu menu) {
        return AdminMenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .photoUrl(menu.getPhotoUrl())
                .price(menu.getPrice())
                .totalCount(menu.getTotalQuantity())
                .soldTicket(menu.getCumulativeSoldQuantity())
                .category(menu.getCategory())
                .visible(menu.getVisible())
                .restaurantName(menu.getRestaurant().getName())
                .build();
    }
}
