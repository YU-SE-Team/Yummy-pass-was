package yuseteam.mealticketsystemwas.domain.menu.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.MenuCategory;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "관리자 메뉴 수정 요청 DTO")
public class AdminMenuUpdateReq {
    @Schema(description = "수정할 메뉴 이름", example = "돈까스정식")
    private String name;

    @Schema(description = "수정할 가격", example = "8500")
    private Integer price;

    @Schema(description = "수정할 총 수량", example = "40")
    private Integer totalCount;

    @Schema(description = "수정할 카테고리", example = "SPECIAL", implementation = MenuCategory.class)
    private MenuCategory category;

    @Schema(description = "노출 여부", example = "false")
    private Boolean visible;

    @Schema(
            description = "새로운 메뉴 이미지 파일 (선택)",
            type = "string",
            format = "binary"
    )
    private MultipartFile image;
}
