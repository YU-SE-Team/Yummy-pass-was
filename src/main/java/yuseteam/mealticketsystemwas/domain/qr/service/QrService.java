package yuseteam.mealticketsystemwas.domain.qr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.config.SecurityUtil;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrCreateResponse;
import yuseteam.mealticketsystemwas.domain.qr.dto.QrInfoResponse;
import yuseteam.mealticketsystemwas.domain.ticket.entity.Ticket;
import yuseteam.mealticketsystemwas.domain.ticket.repository.TicketRepository;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final S3Service s3Service;
    private final TicketRepository ticketRepository;

    //ticket쪽에 생성하기 위한 메소드가 필요해서 controller->service단으로 이동
    public QrCreateResponse createAndUploadQr() {
        try {
            String uuid = UUID.randomUUID().toString();
            String contents = uuid;

            int width = 200;
            int height = 200;
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(contents, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            byte[] pngBytes = baos.toByteArray();

            String imgKey = s3Service.imageKey(uuid);
            String imageUrl = s3Service.uploadImageBytes(pngBytes, imgKey, MediaType.IMAGE_PNG_VALUE);

            return new QrCreateResponse(uuid, imageUrl);

        } catch (Exception e) {
            throw new RuntimeException("QR 생성 또는 업로드에 실패했습니다.", e);
        }
    }

    public void validateStudentRole() {
        String role = SecurityUtil.getCurrentUserRole();
        if (!"ROLE_STUDENT".equals(role)) {
            throw new AccessDeniedException("STUDENT 권한이 필요합니다.");
        }
    }

    @Transactional
    public Boolean useQr(String uuid) {
        validateStudentRole();

        Ticket ticket = ticketRepository.findByQrCode(uuid).orElse(null);
        if (ticket == null) {
            return null;
        }
        if (Boolean.TRUE.equals(ticket.getIsUsed())) {
            return true;
        }

        ticket.use();

        try {
            s3Service.deleteObject(s3Service.imageKey(uuid));
        } catch (Exception e) {
        }
        return false;
    }

    @Transactional(readOnly = true)
    public QrInfoResponse getQrInfo(String uuid) {
        Ticket ticket = ticketRepository.findByQrCode(uuid).orElse(null);
        if (ticket == null) {
            return null;
        }
        String imageUrl = s3Service.publicUrl(s3Service.imageKey(uuid));
        return new QrInfoResponse(uuid, imageUrl, Boolean.TRUE.equals(ticket.getIsUsed()));
    }

}