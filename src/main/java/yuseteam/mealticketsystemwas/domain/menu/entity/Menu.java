package yuseteam.mealticketsystemwas.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuUpdateRequest;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;

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
    private int totalQuantity; //재고량
    private int cumulativeSoldQuantity; //지금까지 팔린 총 누적 판매량
  
    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void update(AdminMenuUpdateRequest req) {
        if (req.getName() != null) {
            this.name = req.getName();
        }
        if (req.getPrice() != null) {
            this.price = req.getPrice();
        }
        if (req.getTotalCount() != null) {
            this.totalQuantity = req.getTotalCount();
        }
        if (req.getCategory() != null) {
            this.category = MenuCategory.valueOf(req.getCategory());
        }
        if (req.getVisible() != null) {
            this.visible = req.getVisible();
        }
    }

    //판매량 증가
    public void sellTicket() {
        if (this.cumulativeSoldQuantity >= this.totalQuantity) {
            throw new IllegalArgumentException("재고가 부족하여 식권을 판매할 수 없습니다.");
        }
        this.cumulativeSoldQuantity++;
    }
}
