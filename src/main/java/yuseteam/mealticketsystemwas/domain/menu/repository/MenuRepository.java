package yuseteam.mealticketsystemwas.domain.menu.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    List<Menu> findByRestaurantIdAndCategory(Long restaurantId, String category);

    List<Menu> findByRestaurantId(Long restaurantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Menu m where m.id = :id")
    Optional<Menu> findAndLockById(Long id);
}