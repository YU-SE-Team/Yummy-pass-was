package yuseteam.mealticketsystemwas.domain.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
import yuseteam.mealticketsystemwas.domain.qr.dto.QrCreateResponse;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrInfoResponse;
import yuseteam.mealticketsystemwas.domain.qr.service.QrService;
import yuseteam.mealticketsystemwas.domain.qr.service.S3Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Tag(name = "QR", description = "QR 코드 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/qr")
public class QrController {

    private final S3Service s3;
    private final QrService qrService;

    @Operation(
            summary = "식권 QR 생성",
            description = "식권용 QR 코드를 생성하여 S3에 PNG로 업로드하고, QR의 사용상태(초기값 false)를 저장합니다.\n\n**권한:** STUDENT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "QR 생성/업로드 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = QrCreateResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 예시",
                                                    value = "{\"uuid\":\"8f2b1b3e-3c8c-4e47-9a6f-7f8b2c1d0e9a\",\"imageUrl\":\"https://your-bucket.s3.ap-northeast-2.amazonaws.com/qr-images/8f2b1b3e-3c8c-4e47-9a6f-7f8b2c1d0e9a.png\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 오류(예: S3 업로드 실패)",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "실패 예시",
                                                    value = "QR 생성/업로드 실패: The bucket is in this region: us-east-1 ..."
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping()
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QrCreateResponse> createMealTicketQr() {
        try {
            String uuid = UUID.randomUUID().toString();
            String contents = "https://localhost:8080/api/qr/use?uuid=" + uuid; // 서버 배포 후 수정 필요

            int width = 200;
            int height = 200;
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(contents, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            byte[] pngBytes = baos.toByteArray();

            String imgKey = s3.imageKey(uuid);
            String imageUrl = s3.uploadImageBytes(pngBytes, imgKey, MediaType.IMAGE_PNG_VALUE);

            s3.saveQrStatus(uuid, false);

            return ResponseEntity.ok(new QrCreateResponse(uuid, imageUrl));

        } catch (Exception e) {
            log.error("QR 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

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
        Boolean used = qrService.useQr(uuid);
        if (used == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        if (used) {
            return ResponseEntity.ok("이미 사용된 QR입니다.");
        }

        s3.saveQrStatus(uuid, true);

        try {
            s3.deleteObject(s3.imageKey(uuid));
        } catch (Exception e) {
            log.warn("QR 이미지 삭제 실패 : {}", e.getMessage());
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
                    schema = @Schema(implementation = QrInfoResponse.class),
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
        Boolean used = s3.getQrStatus(uuid);
        if (used == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        String imageUrl = s3.publicUrl(s3.imageKey(uuid));
        return ResponseEntity.ok(new QrInfoResponse(uuid, imageUrl, used));
    }
}
