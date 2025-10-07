package yuseteam.mealticketsystemwas.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuSalesGraphRes;
import yuseteam.mealticketsystemwas.domain.menu.dto.PopularMenuRes;
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
    public ResponseEntity<MenuSalesGraphRes> getTodaySales(@PathVariable Long menuId) {
        MenuSalesGraphRes response = menuSalesSnapshotService.getMenuSalesSnapshot(menuId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{restaurantId}/popular-menus")
    public ResponseEntity<PopularMenuRes> getPopularMenus(@PathVariable Long restaurantId) {
        PopularMenuRes res = menuSalesSnapshotService
                .getMostPopularMenuByRestaurant(restaurantId);
        return ResponseEntity.ok(res);
    }
}
