package yuseteam.mealticketsystemwas.domain.menu.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;

@Getter
@Schema(description = "메뉴 목록 응답")
public class MenuResponse {
    @Schema(description = "메뉴 ID", example = "1")
    private final Long id;

    @Schema(description = "메뉴 이름", example = "치킨마요덮밥")
    private final String name;

    @Schema(description = "메뉴 사진 URL", example = "https://s3.amazonaws.com/bucket/menu1.jpg")
    private final String photoUrl;

    @Schema(description = "메뉴 가격", example = "8000")
    private final int price;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.photoUrl = menu.getPhotoUrl();
        this.price = menu.getPrice();
    }
}
