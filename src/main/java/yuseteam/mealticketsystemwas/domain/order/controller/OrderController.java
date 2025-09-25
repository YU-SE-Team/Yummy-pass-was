package yuseteam.mealticketsystemwas.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.oauthjwt.dto.UserDTO;
import yuseteam.mealticketsystemwas.domain.order.dto.OrderConfirmationResponse;
import yuseteam.mealticketsystemwas.domain.order.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderConfirmationResponse> getOrderConfirmation(
            @RequestParam Long menuId,
            @AuthenticationPrincipal UserDTO userDetails) {

        Long currentUserId = userDetails.getId();
        OrderConfirmationResponse confirmationInfo = orderService.getOrderConfirmationInfo(currentUserId, menuId);

        return ResponseEntity.ok(confirmationInfo);
    }
}
