package yuseteam.mealticketsystemwas.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateRequest;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateResponse;
import yuseteam.mealticketsystemwas.admin.dto.MenuResponse;
import yuseteam.mealticketsystemwas.admin.service.MenuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class MenuController {
    private final MenuService menuService;

    //메뉴 등록
    @PostMapping("/menu")
    public ResponseEntity<MenuCreateResponse> createMenu(@Valid @RequestBody MenuCreateRequest req) {
        MenuCreateResponse created = menuService.createMenu(req);
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
    public ResponseEntity<List<MenuResponse>> getMenus(){
        return ResponseEntity.ok(menuService.getMenus());
    }
}
