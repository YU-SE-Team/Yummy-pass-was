package yuseteam.mealticketsystemwas.domain.menu.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.MenuCategory;

@Getter
@Setter
@NoArgsConstructor
public class AdminMenuCreateRequest {

    private Long restaurantId;
    @NotBlank
    private String name;
    private MultipartFile image;

    @NotNull
    private Integer price;

    private Integer totalCount;
    private MenuCategory category;
    private Boolean visible;
}
