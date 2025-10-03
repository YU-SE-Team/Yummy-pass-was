package yuseteam.mealticketsystemwas.domain.qr.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImageBytes(byte[] data, String key, String contentType) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        meta.setContentLength(data.length);
        amazonS3.putObject(new PutObjectRequest(bucket, key, new ByteArrayInputStream(data), meta));
        return publicUrl(key);
    }

    public void deleteObject(String key) {
        amazonS3.deleteObject(bucket, key);
    }

    public String imageKey(String uuid) { return "qr-images/%s.png".formatted(uuid); }

    public String publicUrl(String key) {
        String region = amazonS3.getRegionName();
        return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, key);
    }
}