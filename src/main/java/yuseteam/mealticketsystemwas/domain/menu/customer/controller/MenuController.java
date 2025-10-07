package yuseteam.mealticketsystemwas.domain.menu.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.common.dto.MenuDetailResponse;
import yuseteam.mealticketsystemwas.domain.menu.common.dto.MenuResponse;

import yuseteam.mealticketsystemwas.domain.menu.customer.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Tag(name = "Menu Search", description = "고객용 메뉴 조회 API")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{restaurantId}/{category}")
    @Operation(
            summary = "레스토랑별 카테고리 메뉴 목록 조회",
            description = "특정 레스토랑의 특정 카테고리에 해당하는 메뉴 목록을 조회합니다.\n\n" +
                    "카테고리: 'KOREAN', 'SPECIAL', 'PORK', 'A', 'C1', 'C2', 'D'"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청\n\n" +
                            "- 유효하지 않은 카테고리 값\n" +
                            "- restaurantId가 숫자가 아닌 경우",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "레스토랑을 찾을 수 없음\n\n" +
                            "해당 레스토랑 ID가 존재하지 않는 경우",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류\n\n" +
                            "데이터베이스 연결 실패 또는 예상치 못한 오류 발생",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<List<MenuResponse>> getMenus(
            @PathVariable Long restaurantId,
            @PathVariable String category) {

        List<MenuResponse> menus = menuService.getMenuByRestaurantAndCategory(restaurantId, category);
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{menuId}")
    @Operation(
            summary = "메뉴 상세 정보 조회",
            description = "특정 메뉴의 상세 정보를 조회합니다.\n\n" +
                    "메뉴 이름, 가격, 사진, 카테고리, 재고량 등의 정보를 포함합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청\n\n" +
                            "- menuId가 숫자가 아닌 경우\n" +
                            "- menuId가 음수인 경우",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메뉴를 찾을 수 없음\n\n" +
                            "해당 메뉴를 찾을 수 없습니다. id={menuId}",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류\n\n" +
                            "데이터베이스 연결 실패 또는 예상치 못한 오류 발생",
                    content = @Content(mediaType = "text/plain")
            )
    })
    public ResponseEntity<MenuDetailResponse> getMenuDetails(@PathVariable Long menuId){
        MenuDetailResponse menuDetail = menuService.getMenuDetails(menuId);
        return ResponseEntity.ok(menuDetail);
    }
}
