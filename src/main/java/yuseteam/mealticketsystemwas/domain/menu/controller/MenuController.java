package yuseteam.mealticketsystemwas.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuDetailResponse;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuResponse;

import yuseteam.mealticketsystemwas.domain.menu.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{restaurantId}/{category}")
    public ResponseEntity<List<MenuResponse>> getMenus(
            @PathVariable Long restaurantId,
            @PathVariable String category) {

        List<MenuResponse> menus = menuService.getMenuByRestaurantAndCategory(restaurantId, category);
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuDetails(@PathVariable Long menuId){
        MenuDetailResponse menuDetail = menuService.getMenuDetails(menuId);
        return ResponseEntity.ok(menuDetail);
    }
}
