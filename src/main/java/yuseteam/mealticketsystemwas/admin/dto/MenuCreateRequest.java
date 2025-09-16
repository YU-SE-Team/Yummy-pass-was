package yuseteam.mealticketsystemwas.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {

    private Long restaurantId;
    @NotBlank
    private String name;
    private String photoUrl;

    @NotNull
    private Integer price;

    private Integer totalCount;
    private String category;
    private Boolean visible;
}
