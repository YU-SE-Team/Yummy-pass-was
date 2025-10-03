package yuseteam.mealticketsystemwas.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;

import java.util.Optional;

public interface MenuSalesSnapshotrepository extends JpaRepository<MenuSalesSnapshot, Long> {
}
