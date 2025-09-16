package yuseteam.mealticketsystemwas.domain.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class QrController {
    private static final Map<String, Boolean> qrUsageMap = new ConcurrentHashMap<>();

    @GetMapping("/qr")
    public ResponseEntity<String> createMealTicketQr() throws WriterException {

        int width = 200;
        int height = 200;
        String url = "https://naver.com";
        String uuid = UUID.randomUUID().toString();
        String filePath = "qr-code-" + uuid + ".png";

        BitMatrix encode = new MultiFormatWriter()
                .encode(url + "?uuid=" + uuid, BarcodeFormat.QR_CODE, width, height);

        try {
            FileOutputStream out = new FileOutputStream(filePath);
            MatrixToImageWriter.writeToStream(encode, "PNG", out);
            out.close();
            qrUsageMap.put(uuid, false);
            return ResponseEntity.ok("QR 코드가 파일로 저장되었습니다: " + filePath + "\nUUID: " + uuid);
        } catch (Exception e) {
            log.warn("QR Code 파일 저장 도중 Exception 발생, {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QR 코드 파일 저장 실패");
    }

    @GetMapping("/qr/use")
    public ResponseEntity<String> useQr(@RequestParam String uuid) {
        Boolean used = qrUsageMap.get(uuid);
        if (used == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 QR입니다.");
        }
        if (used) {
            return ResponseEntity.ok("이미 사용된 QR입니다.");
        }
        qrUsageMap.put(uuid, true);
        return ResponseEntity.ok("QR 사용 성공");
    }
}
