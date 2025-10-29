package yuseteam.mealticketsystemwas.domain.menu.sales.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.MenuWaitTimeRes;
import yuseteam.mealticketsystemwas.domain.menu.sales.service.MenuWaitTimeService;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sales", description = "판매 관련 API")
public class MenuWaitTimeController {

    private final MenuWaitTimeService menuWaitTimeService;

    @GetMapping("/sales-wait-time/{menuId}")
    @Operation(summary = "메뉴별 예상 대기시간 조회", description = "메뉴의 사용/수령 샘플을 기반으로 평균 처리시간을 계산하여 예상 대기시간(분)을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "메뉴를 찾을 수 없음")
    })
    public ResponseEntity<MenuWaitTimeRes> getMenuWaitTime(@PathVariable Long menuId) {
        try {
            MenuWaitTimeRes res = menuWaitTimeService.getMenuWaitTime(menuId);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            log.warn("메뉴 대기시간 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("메뉴 대기시간 조회 중 에러", e);
            return ResponseEntity.status(500).build();
        }
    }
}
