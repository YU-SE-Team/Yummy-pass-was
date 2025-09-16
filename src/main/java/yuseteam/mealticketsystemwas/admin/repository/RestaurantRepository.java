package yuseteam.mealticketsystemwas.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
