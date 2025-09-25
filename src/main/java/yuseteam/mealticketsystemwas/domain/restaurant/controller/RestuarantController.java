package yuseteam.mealticketsystemwas.domain.restaurant.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.restaurant.dto.RestaurantResponseDto;
import yuseteam.mealticketsystemwas.domain.restaurant.service.RestaurantService;

import java.util.List;

@Tag(name = "Restaurant", description = "식당 관련 API")
@RestController
@RequestMapping("api/restaurants")
@RequiredArgsConstructor
@Slf4j
public class RestuarantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "식당 전체 목록 조회", description = "시스템에 등록된 모든 식당의 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (OK). " +
                    "요청이 성공적으로 처리되었으며, 응답 본문에 식당 목록이 포함됩니다." +
                    "식당이 하나도 없는 경우 빈 배열([])이 반환됩니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponseDto.class),
                    examples = @ExampleObject(
                            name = "성공적인 응답 예시",
                            value = "[{\"id\": 1, \"name\": \"학생회관\"}, {\"id\": 2, \"name\": \"자연계\"}, {\"id\": 3, \"name\": \"교직원\"}]")
                    )),

            @ApiResponse(responseCode = "404", description = "찾을 수 없음 (Not Found)." +
                    "해당 경로(api/restaurants)를 찾을 수 없을 때 발생합니다.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    name = "데이터 없음 예시",
                                    value = "등록된 식당 정보가 없습니다."
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (Internal Server Error)." +
                    "데이터베이스 연결 실패 등 서버 내부에서 예상치 못한 오류가 발생했을 때 반환됩니다.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    name = "서버 오류 예시",
                                    value = "식당 목록 조회 실패: 서버 내부 오류 발생"
                            )
                    ))
    })
    @GetMapping
    public ResponseEntity<?> getRestaurants() {
        try {
            List<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants();

            if (restaurants.isEmpty()) //400
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록된 식당 정보가 없습니다.");

            return ResponseEntity.ok(restaurants);
        }
        catch (Exception e) { //500
            log.error("식당 목록 조회 중 오류 발생", e);
            String errorMessage = "식당 목록 조회 실패: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
