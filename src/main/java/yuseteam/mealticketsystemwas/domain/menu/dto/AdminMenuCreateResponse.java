package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;

@Getter
@Builder
@AllArgsConstructor
public class AdminMenuCreateResponse {
    private final Long id;
    private final String name;
    private final String photoUrl;
    private final int price;
    private final int totalCount;
    private final int soldTicket;
    private final String category;
    private final Boolean visible;


    public static AdminMenuCreateResponse from(Menu menu) {
        return AdminMenuCreateResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .photoUrl(menu.getPhotoUrl())
                .price(menu.getPrice())
                .totalCount(menu.getTotalCount())
                .soldTicket(menu.getSoldTicket())
                .category(menu.getCategory())
                .visible(menu.getVisible())
                .build();
    }
}
