package yuseteam.mealticketsystemwas.domain.menu.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MenuCategory {

    PORK("돈가스", "학생회관", 1),
    SPECIAL("스페셜", "학생회관", 2),
    KOREAN("한식", "학생회관", 3),

    A("A", "자연계", 4),
    C1("C1", "자연계", 5),
    C2("C2", "자연계", 6),
    D("D", "자연계", 7);

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