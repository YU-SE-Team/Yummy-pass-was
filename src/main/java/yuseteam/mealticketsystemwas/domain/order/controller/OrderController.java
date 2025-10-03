package yuseteam.mealticketsystemwas.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    /** 장바구니 바디로 보낸 항목 즉석 합계(저장 X) */
    @Operation(summary = "주문 요약", description = "장바구니 항목을 받아 합계 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요약 성공",
            content = @Content(schema = @Schema(implementation = OrderSummaryRes.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/summary")
    public ResponseEntity<OrderSummaryRes> summarize(
            @RequestBody(description = "주문 항목 목록", required = true,
                content = @Content(schema = @Schema(implementation = CreateOrderReq.class)))
            @org.springframework.web.bind.annotation.RequestBody CreateOrderReq req) {
        return ResponseEntity.ok(orderService.summarize(req.getItems()));
    }

    /** 결제(주문 확정) — 바디로 장바구니 항목 전달 */
    @Operation(summary = "주문 결제", description = "주문을 확정하고 식권을 발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 성공",
            content = @Content(schema = @Schema(implementation = OrderCreatedRes.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PostMapping("/checkout")
    public ResponseEntity<OrderCreatedRes> checkout(
            @RequestBody(description = "주문 항목 목록", required = true,
                content = @Content(schema = @Schema(implementation = CreateOrderReq.class)))
            @org.springframework.web.bind.annotation.RequestBody CreateOrderReq req,
            @AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(orderService.checkout(user.getId(), req));
    }
}