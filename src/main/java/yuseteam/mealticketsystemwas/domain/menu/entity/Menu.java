package yuseteam.mealticketsystemwas.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuUpdateRequest;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;
import yuseteam.mealticketsystemwas.entity.Ticket;

import java.util.List;
//하위 - , 가격, 식권 수, 판매 식권 수, 식당, 음식 카테고리, Ticket(Fk)
@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String photoUrl;
    private int price;
    private int totalCount;
    private int soldTicket;
    private String category;
    private Boolean visible = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu")
    private List<Ticket> tickets;

    public void update(AdminMenuUpdateRequest req) {
        if (req.getName() != null) {
            this.name = req.getName();
        }
        if (req.getPrice() != null) {
            this.price = req.getPrice();
        }
        if (req.getTotalCount() != null) {
            this.totalCount = req.getTotalCount();
        }
        if (req.getCategory() != null) {
            this.category = req.getCategory();
        }
        if (req.getVisible() != null) {
            this.visible = req.getVisible();
        }
        }
}
