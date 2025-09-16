package yuseteam.mealticketsystemwas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yuseteam.mealticketsystemwas.entity.Menu;

@Getter
@AllArgsConstructor
public class MenuCreateResponse {
    private Long id;
    private String name;
    private String phtoUrl;
    private int price;
    private int totalCount;
    private int soldTicket;
    private String category;
    private Boolean visible;

    public static MenuCreateResponse from(Menu menu) {
        return new MenuCreateResponse(
                menu.getId(),
                menu.getName(),
                menu.getPhotoUrl(),
                menu.getPrice(),
                menu.getTotalCount(),
                menu.getSoldTicket(),
                menu.getCategory(),
                menu.getVisible()
        );
    }
}
