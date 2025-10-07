package yuseteam.mealticketsystemwas.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class AdminMenuUpdateRequest {
    private String name;
    private Integer price;
    private Integer totalCount;
    private String category;
    private Boolean visible;
    private MultipartFile image;
}
