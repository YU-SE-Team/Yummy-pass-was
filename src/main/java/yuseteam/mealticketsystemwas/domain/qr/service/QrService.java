package yuseteam.mealticketsystemwas.domain.qr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import yuseteam.mealticketsystemwas.config.SecurityUtil;

@Service
@RequiredArgsConstructor
public class QrService {

    private final S3Service s3Service;

    public void validateStudentRole() {
        String role = SecurityUtil.getCurrentUserRole();
        if (!"ROLE_STUDENT".equals(role)) {
            throw new AccessDeniedException("STUDENT 권한이 필요합니다.");
        }
    }

    public Boolean useQr(String uuid) {
        validateStudentRole();

        Boolean used = s3Service.getQrStatus(uuid);
        if (used == null) {
            return null;
        }
        if (used) {
            return true;
        }

        s3Service.saveQrStatus(uuid, true);
        try {
            s3Service.deleteObject(s3Service.imageKey(uuid));
        } catch (Exception e) {
        }
        return false;
    }
}