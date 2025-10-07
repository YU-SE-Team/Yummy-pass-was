package yuseteam.mealticketsystemwas.domain.menu.admin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuCreateRequest;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuCreateResponse;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuResponse;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuUpdateRequest;
import yuseteam.mealticketsystemwas.domain.menu.admin.service.AdminMenuService;
import yuseteam.mealticketsystemwas.domain.restaurant.repository.RestaurantRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Tag(name = "Admin Menu", description = "관리자 메뉴 관리 API")
public class AdminMenuController {
    private final AdminMenuService menuService;
    private final RestaurantRepository restaurantRepository;

    //메뉴 등록
    @PostMapping("/menu")
    public ResponseEntity<AdminMenuCreateResponse> createMenu(@ModelAttribute AdminMenuCreateRequest req) {
        AdminMenuCreateResponse created = menuService.createMenu(req);
        return ResponseEntity.ok(created);
    }

    //메뉴 삭제
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok("삭제 완료");
    }
    //전체 메뉴 조회
    @GetMapping("/menu")
    public ResponseEntity<List<AdminMenuResponse>> getMenus(){
        return ResponseEntity.ok(menuService.getMenus());
    }
    
    //특정 메뉴 조회
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<AdminMenuResponse> getMenu(@PathVariable Long menuId){
        AdminMenuResponse response = menuService.getMenu(menuId);
        return ResponseEntity.ok(response);
    }
  
    //메뉴 수정
    @PatchMapping("/menu/{menuId}")
    public ResponseEntity<AdminMenuResponse> updateMenu(@PathVariable Long menuId, @ModelAttribute AdminMenuUpdateRequest req){
        AdminMenuResponse updateMenu = menuService.updateMenu(menuId, req);
        return ResponseEntity.ok(updateMenu);
    }

    //식당별 카테고리 목록
    @GetMapping("/menu/categories/{restaurantId}")
    public ResponseEntity<List<String>> getCategoreisByRestaurant(@PathVariable Long restaurantId){
        return ResponseEntity.ok(menuService.getCategoriesByRestaurant(restaurantId));
    }
}
