package yuseteam.mealticketsystemwas.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "menu_sales_snapshot")
@NoArgsConstructor
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
}
