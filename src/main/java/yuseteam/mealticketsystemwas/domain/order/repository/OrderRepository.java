package yuseteam.mealticketsystemwas.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}