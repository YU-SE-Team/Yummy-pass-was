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
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class QrController {

    @GetMapping("/qr")
    public ResponseEntity<String> createMealTicketQr() throws WriterException {

        int width = 200;
        int height = 200;
        String url = "https://naver.com";
        String filePath = "qr-code.png";

        BitMatrix encode = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, width, height);

        try {
            FileOutputStream out = new FileOutputStream(filePath);
            MatrixToImageWriter.writeToStream(encode, "PNG", out);
            out.close();
            return ResponseEntity.ok("QR 코드가 파일로 저장되었습니다: " + filePath);
        } catch (Exception e) {
            log.warn("QR Code 파일 저장 도중 Exception 발생, {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QR 코드 파일 저장 실패");
    }
}
