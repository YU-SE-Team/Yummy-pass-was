package yuseteam.mealticketsystemwas.domain.menu.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "관리자 메뉴 등록 요청 DTO")
public class AdminMenuCreateReq {
    @Schema(description = "식당 ID", example = "1")
    private Long restaurantId;

    @Schema(description = "메뉴 이름", example = "치킨마요덮밥")
    @NotBlank
    private String name;

    @Schema(
            description = "메뉴 이미지 파일 (multipart/form-data 업로드용)",
            type = "string",
            format = "binary"
    )
    private MultipartFile image;

    @Schema(description = "메뉴 가격", example = "8000")
    @NotNull
    private Integer price;

    @Schema(description = "총 수량", example = "50")
    private Integer totalCount;

    @Schema(description = "메뉴 카테고리", example = "KOREAN", implementation = MenuCategory.class)
    private MenuCategory category;

    @Schema(description = "메뉴 노출 여부", example = "true")
    private Boolean visible;
}
