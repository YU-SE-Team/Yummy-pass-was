package yuseteam.mealticketsystemwas.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.admin.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    List<Menu> findByRestaurantIdAndCategory(Long restaurantId, String category);
}
