package yuseteam.mealticketsystemwas.domain.menu.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Schema(description = "메뉴 카테고리")
public enum MenuCategory {

    PORK("돈가스", "중앙도서관", 1),
    SPECIAL("스페셜", "중앙도서관", 2),
    KOREAN("한식", "중앙도서관", 3),

    A("A", "이과대학", 4),
    C1("C1", "이과대학", 5),
    C2("C2", "이과대학", 6),
    D("D", "이과대학", 7),

    SET("정식", "교직원", 8);

    private final String category;
    private final String restaurant;
    private final int sort;

    MenuCategory(String category, String restaurant,int sort){
        this.category = category;
        this.restaurant = restaurant;
        this.sort = sort;
    }

    public static List<MenuCategory> getCategoriesByRestaurant(String restaurant) {
        return Arrays.stream(values())
                .filter(c -> c.getRestaurant().equals(restaurant))
                .toList();
    }

}