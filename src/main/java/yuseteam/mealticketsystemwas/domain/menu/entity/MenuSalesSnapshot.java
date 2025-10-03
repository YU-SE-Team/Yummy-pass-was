package yuseteam.mealticketsystemwas.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "menu_sales_snapshot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSalesSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", unique = true)
    private Menu menu;

    //이전 판매량 및 시간
    private Integer previousSalesCount;
    private LocalDateTime previousRecordedAt;

    //현재 판매량 및 시간
    private Integer currentSalesCount;
    private LocalDateTime currentRecordedAt;


    public static MenuSalesSnapshot createNew(Menu menu) {
        MenuSalesSnapshot snapshot = new MenuSalesSnapshot();
        snapshot.menu = menu;
        snapshot.previousSalesCount = 0;
        snapshot.currentSalesCount = 0;
        snapshot.currentRecordedAt = LocalDateTime.now();
        return snapshot;
    }

    public void updateSnapshot(int newSalesCount, LocalDateTime newRecordTime) {
        //현재 -> 이전
        this.previousSalesCount = this.currentSalesCount;
        this.previousRecordedAt = this.currentRecordedAt;

        //신규 -> 현재
        this.currentSalesCount = newSalesCount;
        this.currentRecordedAt = newRecordTime;
    }

    public int getSalesDiff() {
        if (previousSalesCount == null || currentSalesCount == null) {
            return 0;
        }
        return currentSalesCount - previousSalesCount;
    }
}
