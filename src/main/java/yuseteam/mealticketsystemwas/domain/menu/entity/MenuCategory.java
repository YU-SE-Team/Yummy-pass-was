package yuseteam.mealticketsystemwas.domain.menu.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MenuCategory {

    KOREAN("한식", "학생회관"),
    SPECIAL("스페셜", "학생회관"),
    PORK("돈가스", "학생회관"),

    A("A", "자연계"),
    C1("C1", "자연계"),
    C2("C2", "자연계"),
    D("D", "자연계");

    private final String category;
    private final String restaurant;

    MenuCategory(String category, String restaurant){
        this.category = category;
        this.restaurant = restaurant;
    }

    public static List<MenuCategory> getCategoriesByRestaurant(String restaurant) {
        return Arrays.stream(values())
                .filter(c -> c.getRestaurant().equals(restaurant))
                .toList();
    }

}
