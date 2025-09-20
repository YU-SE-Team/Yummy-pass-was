package yuseteam.mealticketsystemwas.domain.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuseteam.mealticketsystemwas.domain.qr.service.S3Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/qr")
public class QrController {

    private final S3Service s3;

    @PostMapping()
    public ResponseEntity<String> createMealTicketQr() {
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

            String body = """
                    QR 업로드 완료
                    uuid: %s
                    imageUrl: %s
                    """.formatted(uuid, imageUrl);
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            log.error("QR 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("QR 생성/업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/use")
    public ResponseEntity<String> useQr(@RequestParam String uuid) {
        Boolean used = s3.getQrStatus(uuid);
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

    @GetMapping("/info")
    public ResponseEntity<String> getQrInfo(@RequestParam String uuid) {
        Boolean used = s3.getQrStatus(uuid);
        if (used == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        String status = used ? "사용됨" : "미사용";
        return ResponseEntity.ok("QR 상태: " + status);
    }
}
