package yuseteam.mealticketsystemwas.domain.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.oauthjwt.dto.UserDTO;
import yuseteam.mealticketsystemwas.domain.ticket.dto.PaymentReq;
import yuseteam.mealticketsystemwas.domain.ticket.dto.PaymentRes;
import yuseteam.mealticketsystemwas.domain.ticket.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentRes> purchaseTicket(
            @RequestBody PaymentReq req,
            @AuthenticationPrincipal UserDTO userdto) {

        Long userId = userdto.getId();
        Long menuId = req.getMenuId();

        PaymentRes resDto = paymentService.payment(userId, menuId);

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }
}
