package yuseteam.mealticketsystemwas.domain.qr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrCreateRes;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrInfoRes;
import yuseteam.mealticketsystemwas.domain.qr.service.QrService;
import yuseteam.mealticketsystemwas.domain.qr.service.S3Service;

@Tag(name = "QR", description = "QR 코드 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/qr")
public class QrController {

    private final S3Service s3;
    private final QrService qrService;

    @Operation(
            summary = "QR 사용",
            description = "QR을 사용 처리하고(상태를 true로 갱신), 해당 QR 이미지 파일을 S3에서 삭제합니다.\n\n**권한:** STUDENT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 처리(이미 사용된 경우도 200으로 메시지 안내)",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(name = "성공", value = "QR 사용 성공 (uuid=8f2b1b3e-3c8c-4e47-9a6f-7f8b2c1d0e9a)"),
                                            @ExampleObject(name = "이미 사용됨", value = "이미 사용된 QR입니다.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 QR",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "존재하지 않는 QR입니다.")
                            )
                    )
            }
    )
    @PostMapping("/use")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> useQr(@RequestParam String uuid) {
        Boolean result = qrService.useQr(uuid);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        if (result) {
            return ResponseEntity.ok("이미 사용된 QR입니다.");
        }
        return ResponseEntity.ok("QR 사용 성공 (uuid=" + uuid + ")");
    }

    @Operation(
        summary = "QR 정보 조회",
        description = "uuid로 QR 이미지 URL과 사용 상태를 조회합니다.\n\n**권한:** STUDENT",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "QR 정보 조회 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = QrInfoRes.class),
                    examples = @ExampleObject(
                        name = "성공 예시",
                        value = "{\"uuid\":\"8f2b1b3e-3c8c-4e47-9a6f-7f8b2c1d0e9a\",\"imageUrl\":\"https://your-bucket.s3.ap-northeast-2.amazonaws.com/qr-images/8f2b1b3e-3c8c-4e47-9a6f-7f8b2c1d0e9a.png\",\"used\":false}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 QR",
                content = @Content(
                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "존재하지 않는 QR입니다.")
                )
            )
        }
    )
    @GetMapping("/info")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getQrInfo(@RequestParam String uuid) {
        QrInfoRes info = qrService.getQrInfo(uuid);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        return ResponseEntity.ok(info);
    }
}
