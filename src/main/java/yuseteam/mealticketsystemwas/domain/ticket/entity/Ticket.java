package yuseteam.mealticketsystemwas.domain.ticket.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;
import yuseteam.mealticketsystemwas.domain.order.entity.OrderItem;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qrCode;

    @Column(nullable = false)
    private Boolean isUsed;

    private LocalDateTime purchaseTime;

    private LocalDateTime receivedTime;

    @Getter
    private LocalDateTime usedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    @Setter
    private OrderItem orderItem;

    @Builder
    public Ticket(String menuName, String qrCode, boolean isUsed, LocalDateTime purchaseTime, User user, OrderItem orderItem) {
        this.qrCode = qrCode;
        this.isUsed = isUsed;
        this.purchaseTime = purchaseTime;
        this.user = user;
        this.orderItem = orderItem;
    }

    public void use() {
        this.isUsed = true;
        this.usedTime = LocalDateTime.now();
    }

    public void completeReceive(){
        this.receivedTime = LocalDateTime.now();
    }

}
