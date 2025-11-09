package yuseteam.mealticketsystemwas.domain.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.ticket.dto.TicketResDTO;
import yuseteam.mealticketsystemwas.domain.ticket.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets")
@Tag(name = "Ticket", description = "티켓 사용·미사용·만료 조회 API")
public class TicketController {
    private final TicketService ticketService;

    @Operation(
            summary = "만료된 티켓 조회",
            description = "구매일시가 24시간 이전이며 아직 사용되지 않은(미사용) 만료된 티켓 목록을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "만료된 티켓 목록 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResDTO.class),
                            examples = @ExampleObject(value = "[{\n  \"id\": 1,\n  \"qrCode\": \"qr_abc123\",\n  \"isUsed\": false,\n  \"purchaseTime\": \"2025-11-06T09:12:00\",\n  \"receivedTime\": null,\n  \"userName\": \"홍길동\",\n  \"menuName\": \"김치찌개\"\n}]")
                    ))
    })
    @GetMapping("/expired")
    public ResponseEntity<List<TicketResDTO>> getExpiredTickets(){
        List<TicketResDTO> expiredTickets = ticketService.getExpiredTickets();
        return ResponseEntity.ok(expiredTickets);
    }

    @Operation(
            summary = "미사용 티켓 조회",
            description = "현재 사용되지 않은 모든 티켓 목록을 반환합니다. (isUsed = false)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미사용 티켓 목록 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResDTO.class),
                            examples = @ExampleObject(value = "[{\n  \"id\": 2,\n  \"qrCode\": \"qr_def456\",\n  \"isUsed\": false,\n  \"purchaseTime\": \"2025-11-08T10:00:00\",\n  \"receivedTime\": null,\n  \"userName\": \"이영희\",\n  \"menuName\": \"비빔밥\"\n}]")
                    ))
    })
    @GetMapping("/unused")
    public ResponseEntity<List<TicketResDTO>> getUnusedTickets() {
        List<TicketResDTO> unusedTickets = ticketService.getUnusedTickets();
        return ResponseEntity.ok(unusedTickets);
    }

    @Operation(
            summary = "티켓 음식 수령 완료 처리",
            description = "지정한 티켓을 음식 수령 완료 상태로 변경하고, 수령 시각(receivedTime)을 기록합니다. 이미 수령되었거나 사용 상태가 맞지 않으면 오류를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수령 완료 처리 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResDTO.class),
                            examples = @ExampleObject(value = "{\n  \"id\": 3,\n  \"qrCode\": \"qr_xyz789\",\n  \"isUsed\": true,\n  \"purchaseTime\": \"2025-11-08T09:00:00\",\n  \"receivedTime\": \"2025-11-08T09:12:34\",\n  \"userName\": \"박철수\",\n  \"menuName\": \"된장찌개\"\n}")
                    )),
            @ApiResponse(responseCode = "400", description = "요청이 잘못되었거나 티켓 상태가 유효하지 않음. 가능한 오류:\n- 식권 없음: \"식권을 찾을 수 없습니다.\"\n- 이미 수령: \"이미 음식 수령 완료 처리되었습니다.\"\n- 사용 상태 불일치: \"사용 완료된 식권만 수령이 가능합니다.\"",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "식권 없음", value = "\"식권을 찾을 수 없습니다.\""),
                                    @ExampleObject(name = "이미 수령", value = "\"이미 음식 수령 완료 처리되었습니다.\""),
                                    @ExampleObject(name = "사용 상태 불일치", value = "\"사용 완료된 식권만 수령이 가능합니다.\"")
                            },
                            schema = @Schema(implementation = String.class)
                    ))
    })
    @PatchMapping("/{ticketId}/receive")
    public ResponseEntity<TicketResDTO> completeReceive(@PathVariable Long ticketId){
        TicketResDTO updated = ticketService.completeReceive(ticketId);
        return ResponseEntity.ok(updated);
    }

}
