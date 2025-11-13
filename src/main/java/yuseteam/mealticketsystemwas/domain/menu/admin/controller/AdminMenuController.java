package yuseteam.mealticketsystemwas.domain.menu.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuCreateReq;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuCreateRes;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuRes;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuUpdateReq;
import yuseteam.mealticketsystemwas.domain.menu.admin.service.AdminMenuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin Menu", description = "관리자 메뉴 관리 API")
public class AdminMenuController {
    private final AdminMenuService menuService;

    //메뉴 등록
    @PostMapping("/menu")
    @Operation(
            summary = "새 메뉴 등록",
            description = "관리자가 새로운 메뉴를 등록합니다.\n" +
                    "요청 본문에는 메뉴 이름, 가격, 카테고리, 식당 ID, 이미지 파일 등을 포함합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "메뉴 등록 성공",
                    content = @Content(schema = @Schema(implementation = AdminMenuCreateRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 형식 또는 필수 값 누락",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "식당 ID를 찾을 수 없음",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류 (파일 업로드 실패, DB 오류 등)",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<AdminMenuCreateRes> createMenu(@ModelAttribute AdminMenuCreateReq req) {
        AdminMenuCreateRes created = menuService.createMenu(req);
        return ResponseEntity.ok(created);
    }

    //메뉴 삭제
    @DeleteMapping("/menu/{menuId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 완료"),
            @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok("삭제 완료");
    }
    //전체 메뉴 조회
    @GetMapping("/menu")
    @Operation(
            summary = "전체 메뉴 목록 조회",
            description = "관리자가 등록된 모든 메뉴 목록을 조회합니다.\n\n" +
                    "메뉴 이름, 카테고리, 가격, 식당명 등을 포함한 리스트를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminMenuRes.class)))
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<AdminMenuRes>> getMenus(){
        return ResponseEntity.ok(menuService.getMenus());
    }
    
    //특정 메뉴 조회
    @GetMapping("/menu/{menuId}")
    @Operation(
            summary = "특정 메뉴 조회",
            description = "관리자가 특정 메뉴의 상세 정보를 조회합니다.\n\n" +
                    "메뉴 이름, 가격, 재고, 카테고리, 이미지 경로 등을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AdminMenuRes.class))),
            @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<AdminMenuRes> getMenu(@PathVariable Long menuId){
        AdminMenuRes response = menuService.getMenu(menuId);
        return ResponseEntity.ok(response);
    }
  
    //메뉴 수정
    @PatchMapping("/menu/{menuId}")
    @Operation(
            summary = "메뉴 수정",
            description = "관리자가 기존 메뉴의 정보를 수정합니다\n\n" +
                    "수정 가능한 항목: 이름, 가격, 설명, 이미지, 재고 등"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = AdminMenuRes.class))),
            @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<AdminMenuRes> updateMenu(@PathVariable Long menuId, @ModelAttribute AdminMenuUpdateReq req){
        AdminMenuRes updateMenu = menuService.updateMenu(menuId, req);
        return ResponseEntity.ok(updateMenu);
    }

    //식당별 카테고리 목록
    @GetMapping("/menu/categories/{restaurantId}")
    @Operation(
            summary = "레스토랑별 카테고리 메뉴 목록 조회",
            description = """
                    특정 레스토랑의 특정 카테고리에 해당하는 메뉴 목록을 조회합니다.
                    **카테고리 구분**
                    - 중앙도서관: `PORK`, `SPECIAL`, `KOREAN`
                    - 학생회관: `A`, `C1`, `C2`, `D`
                    - 교직원: `SET`
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 식당을 찾을 수 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<String>> getCategoreisByRestaurant(@PathVariable Long restaurantId){
        return ResponseEntity.ok(menuService.getCategoriesByRestaurant(restaurantId));
    }
}
