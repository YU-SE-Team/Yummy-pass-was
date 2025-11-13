package yuseteam.mealticketsystemwas.domain.menu.common.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuseteam.mealticketsystemwas.domain.menu.admin.dto.AdminMenuUpdateReq;
import yuseteam.mealticketsystemwas.domain.menu.sales.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;

import java.util.ArrayList;
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
    private int totalQuantity; //재고량
    private int cumulativeSoldQuantity; //지금까지 팔린 총 누적 판매량


    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MenuSalesSnapshot> salesSnapshots = new ArrayList<>();



    public void update(@Valid AdminMenuUpdateReq req) {
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
            this.category = req.getCategory();
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

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
