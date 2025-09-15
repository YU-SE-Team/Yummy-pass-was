package yuseteam.mealticketsystemwas.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private String qrCode;
    private String restaurant;

    @Column(nullable = false)
    private Boolean isUsed;

    private LocalDateTime purchaseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="menu_id")
    private Menu menu;

    //User(FK)

    //임시
    @Builder // 빌더 패턴 적용
    public Ticket(String menuName, String qrCode, String restaurant, boolean isUsed, LocalDateTime purchaseTime, Menu menu) {
        this.menuName = menuName;
        this.qrCode = qrCode;
        this.restaurant = restaurant;
        this.isUsed = isUsed;
        this.purchaseTime = purchaseTime;
        this.menu = menu;
    }
}
