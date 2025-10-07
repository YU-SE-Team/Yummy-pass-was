package yuseteam.mealticketsystemwas.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuSalesGraphRes;
import yuseteam.mealticketsystemwas.domain.menu.dto.PopularMenuListRes;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuSalesSnapshotrepository;
import yuseteam.mealticketsystemwas.domain.menu.service.MenuSalesSnapshotService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus/sales-snapshots")
@RequiredArgsConstructor
@Slf4j
public class MenuSalesSnapshotController {

    private final MenuSalesSnapshotrepository menuSalesSnapshotrepository;
    private final MenuRepository menuRepository;
    private final MenuSalesSnapshotService menuSalesSnapshotService;

    @GetMapping("/sales-diff")
    @Operation(
            summary = "최근 구간 판매량 조회",
            description = "모든 메뉴의 가장 최근 판매 구간(interval)의 판매량을 조회합니다. " +
                    "메뉴 ID를 키로, 해당 메뉴의 최근 구간 판매량을 값으로 하는 맵을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공"
            )
    })
    public ResponseEntity<Map<Long, Integer>> getLatestIntervalSales() {
        List<Menu> menus = menuRepository.findAll();
        Map<Long, Integer> res = new HashMap<>();

        for(Menu menu : menus) {
            Integer latestSales = menuSalesSnapshotrepository
                    .findTopByMenuOrderBySnapshotTimeDesc(menu)
                    .map(MenuSalesSnapshot::getSalesInInterval)
                    .orElse(0);
            res.put(menu.getId(), latestSales);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{menuId}/today-sales-graph")
    @Operation(
            summary = "오늘의 메뉴 판매 그래프 데이터 조회",
            description = "특정 메뉴의 오늘 하루(자정부터 현재까지) 시간대별 판매량 데이터를 조회합니다.\n\n" +
                    "각 데이터 포인트는 스냅샷 시간, 해당 구간 판매량, 누적 판매량을 포함합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuSalesGraphRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "메뉴를 찾을 수 없습니다.",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<MenuSalesGraphRes> getTodaySales(@PathVariable Long menuId) {
        MenuSalesGraphRes response = menuSalesSnapshotService.getMenuSalesSnapshot(menuId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{restaurantId}/popular-menus")
    @Operation(
            summary = "레스토랑별 인기 메뉴 조회",
            description = "특정 레스토랑의 최근 20분간 가장 많이 팔린 메뉴를 조회합니다.\n\n" +
                    "**특징:**\n" +
                    "- 동일한 판매량을 가진 메뉴가 여러 개면 모두 반환됩니다.\n" +
                    "- 판매 데이터가 없는 메뉴는 0개로 간주됩니다.\n" +
                    "- 모든 메뉴가 0개 판매된 경우, 모든 메뉴가 동점으로 반환됩니다.\n" +
                    "- 매진된 상품은 판매량이 없어 자동으로 순위에서 제외됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PopularMenuListRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "해당 레스토랑에 메뉴가 없습니다.",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<PopularMenuListRes> getPopularMenus(@PathVariable Long restaurantId) {
        PopularMenuListRes res = menuSalesSnapshotService
                .getMostPopularMenuByRestaurant(restaurantId);
        return ResponseEntity.ok(res);
    }
}
