package yuseteam.mealticketsystemwas.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuCreateRequest;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuCreateResponse;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuResponse;
import yuseteam.mealticketsystemwas.domain.menu.service.AdminMenuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminMenuController {
    private final AdminMenuService menuService;

    //메뉴 등록
    @PostMapping("/menu")
    public ResponseEntity<AdminMenuCreateResponse> createMenu(@Valid @RequestBody AdminMenuCreateRequest req) {
        AdminMenuCreateResponse created = menuService.createMenu(req);
        return ResponseEntity.ok(created);
    }

    //메뉴 삭제
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok("삭제 완료");
    }
    //메뉴 조회
    @GetMapping("/menu")
    public ResponseEntity<List<AdminMenuResponse>> getMenus(){
        return ResponseEntity.ok(menuService.getMenus());
    }
}
