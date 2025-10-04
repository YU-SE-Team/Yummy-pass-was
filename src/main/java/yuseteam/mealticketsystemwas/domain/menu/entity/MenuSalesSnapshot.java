package yuseteam.mealticketsystemwas.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "menu_sales_snapshot", indexes = {
        @Index(name = "idx_menu_snapshot", columnList = "menu_id, snapshot_time")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSalesSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private LocalDateTime snapshotTime; //그래프 X축
    private Integer salesInInterval; //그래프 Y축
    private Integer cumulativeSales;

    public static MenuSalesSnapshot of(Menu menu, LocalDateTime snapshotTime,
                                       Integer salesInInterval, Integer cumulativeSales) {
        MenuSalesSnapshot snapshot = new MenuSalesSnapshot();
        snapshot.menu = menu;
        snapshot.snapshotTime = snapshotTime;
        snapshot.salesInInterval = salesInInterval;
        snapshot.cumulativeSales = cumulativeSales;
        return snapshot;
    }
}
