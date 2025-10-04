package yuseteam.mealticketsystemwas.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MenuSalesSnapshotrepository extends JpaRepository<MenuSalesSnapshot, Long> {
    //특정 메뉴의 가장 최근 스냅샷 조회(내림차순)
    Optional<MenuSalesSnapshot> findTopByMenuOrderbySnapshotTimeDesc(Menu menu);

    //특정 메뉴의 오늘 데이터만 조회(그래프용)
    List<MenuSalesSnapshot> findByMenuAndSnapshottimeAfterOrderBySnapshotTimeAsc(
            Menu menu,
            LocalDateTime startTime
    );

    @Query("SELECT s FROM MenuSalesSnapshot s WHERE s.menu.id = :menuId " +
            "AND s.snapshotTime >= :startOfDay ORDER BY s.snapshotTime ASC")
    List<MenuSalesSnapshot> findTodaySnapshotsByMenuId(
            @Param("menuId") Long menuId,
            @Param("startOfDay") LocalDateTime startOfDay
    );

}
