package yuseteam.mealticketsystemwas.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}