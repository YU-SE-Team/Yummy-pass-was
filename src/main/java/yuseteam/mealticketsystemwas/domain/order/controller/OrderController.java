package yuseteam.mealticketsystemwas.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.oauthjwt.dto.UserDTO;
import yuseteam.mealticketsystemwas.domain.order.dto.CreateOrderReq;
import yuseteam.mealticketsystemwas.domain.order.dto.OrderCreatedRes;
import yuseteam.mealticketsystemwas.domain.order.dto.OrderSummaryRes;
import yuseteam.mealticketsystemwas.domain.order.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "장바구니 요약", description = "장바구니 항목을 받아 합계를 계산합니다. 재고 부족 시 400 문자열 메시지 반환.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 성공", content = @Content(schema = @Schema(implementation = OrderSummaryRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 재고 부족", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<OrderSummaryRes> summarize(@RequestBody CreateOrderReq req) {
        return ResponseEntity.ok(orderService.summarize(req.getItems()));
    }

    @PostMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "주문 확정(결제)", description = "재고 차감 후 식권을 발급합니다. 실패 시 400 문자열 메시지.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 및 식권 발급 성공", content = @Content(schema = @Schema(implementation = OrderCreatedRes.class))),
            @ApiResponse(responseCode = "400", description = "요청 바디 오류 / 재고 부족 / 유효 항목 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<OrderCreatedRes> checkout(
            @RequestBody CreateOrderReq req,
            @AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(orderService.checkout(user.getId(), req));
    }
}