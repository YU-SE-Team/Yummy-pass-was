package yuseteam.mealticketsystemwas.domain.menu.sales.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.MenuWaitTimeRes;
import yuseteam.mealticketsystemwas.domain.menu.sales.service.MenuWaitTimeService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sales", description = "판매 관련 API")
public class MenuWaitTimeController {

    private final MenuWaitTimeService menuWaitTimeService;

    @GetMapping("/sales-wait-time/{menuId}")
    @Operation(summary = "메뉴별 예상 대기시간 조회", description = "메뉴의 사용/수령 샘플을 기반으로 평균 처리시간을 계산하여 예상 대기시간(분)을 반환합니다. expectedWaitTime이 null이면 샘플이 부족한 경우입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuWaitTimeRes.class),
                            examples = {
                                    @ExampleObject(name = "성공(값 있음)", value = "{\n  \"menuId\": 1,\n  \"menuName\": \"김치찌개\",\n  \"expectedWaitTime\": 12\n}"),
                                    @ExampleObject(name = "성공(샘플부족)", value = "{\n  \"menuId\": 2,\n  \"menuName\": \"비빔밥\",\n  \"expectedWaitTime\": null\n}")
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"메뉴를 찾을 수 없습니다.\""),
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"서버 에러가 발생했습니다.\""),
                            schema = @Schema(implementation = String.class)
                    ))
    })
    public ResponseEntity<MenuWaitTimeRes> getMenuWaitTime(@PathVariable Long menuId) {
        try {
            MenuWaitTimeRes res = menuWaitTimeService.getMenuWaitTime(menuId);
            return ResponseEntity.ok(res);
        } catch (NoSuchElementException e) {
            log.warn("메뉴 대기시간 조회 실패(메뉴 없음): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MenuWaitTimeRes(null, null, null));
        } catch (IllegalArgumentException e) {
            log.warn("메뉴 대기시간 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MenuWaitTimeRes(null, e.getMessage(), null));
        } catch (Exception e) {
            log.error("메뉴 대기시간 조회 중 에러", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MenuWaitTimeRes(null, "서버 에러가 발생했습니다.", null));
        }
    }
}
