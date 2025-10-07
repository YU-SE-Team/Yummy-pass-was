package yuseteam.mealticketsystemwas.domain.menu.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;

@Getter
@Schema(description = "메뉴 상세 정보 응답")
public class MenuDetailResponse {
    @Schema(description = "메뉴 ID", example = "1")
    private final Long id;

    @Schema(description = "메뉴 이름", example = "치킨마요덮밥")
    private final String name;

    @Schema(description = "메뉴 가격", example = "8000")
    private final int price;

    @Schema(description = "남은 재고 수량 (총 재고량 - 판매량)", example = "45")
    private final int remainingTickets;

    public MenuDetailResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.remainingTickets = menu.getTotalQuantity() - menu.getCumulativeSoldQuantity();
    }
}
