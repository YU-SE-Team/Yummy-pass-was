package yuseteam.mealticketsystemwas.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateRequest;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateResponse;
import yuseteam.mealticketsystemwas.admin.service.MenuService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/menu")
    public ResponseEntity<MenuCreateResponse> createMenu(@Valid @RequestBody MenuCreateRequest req) {
        MenuCreateResponse created = menuService.createMenu(req);
        return ResponseEntity.ok(created);
    }
}
