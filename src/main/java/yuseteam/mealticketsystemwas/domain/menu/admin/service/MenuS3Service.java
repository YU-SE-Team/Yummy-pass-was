package yuseteam.mealticketsystemwas.domain.menu.admin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuS3Service {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadMenuImage(MultipartFile image, Menu menu) {
        if(menu.getPhotoUrl() != null) {
            amazonS3.deleteObject(bucket, getImageKey(menu.getPhotoUrl()));
        }

        String extension = getImageExtension(image);
        String fileName = "menu/" + UUID.randomUUID() + "_" + menu.getName() + "_menu" + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }

        String publicUrl = amazonS3.getUrl(bucket, fileName).toString();
        menu.setPhotoUrl(publicUrl);

        return publicUrl;
    }

    private String getImageKey(String imageUrl) {
        int index = imageUrl.indexOf("menu/");
        if (index != -1) {
            return imageUrl.substring(index);
        }
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    private String getImageExtension(MultipartFile image) {
        String extension = "";
        String originalFilename = image.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        return extension;
    }

    @Transactional
    public void deleteMenuImage(Menu menu) {
        if(menu.getPhotoUrl() != null) {
            amazonS3.deleteObject(bucket, getImageKey(menu.getPhotoUrl()));
        }
    }

}
