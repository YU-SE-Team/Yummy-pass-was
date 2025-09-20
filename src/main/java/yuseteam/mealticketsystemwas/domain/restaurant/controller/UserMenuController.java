package yuseteam.mealticketsystemwas.domain.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.admin.dto.MenuResponse;
import yuseteam.mealticketsystemwas.domain.admin.entity.Menu;
import yuseteam.mealticketsystemwas.domain.restaurant.dto.MenuResponseDto;

import yuseteam.mealticketsystemwas.domain.restaurant.service.UserMenuService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserMenuController {

    private final UserMenuService menuService;

    @GetMapping("/menus/{restaurantId}/{category}")
    public ResponseEntity<List<MenuResponseDto>> getMenus(
            @PathVariable Long restaurantId,
            @PathVariable String category) {

        List<MenuResponseDto> menus = menuService.getMenuByRestaurantAndCategory(restaurantId, category);
        return ResponseEntity.ok(menus);
    }
}
