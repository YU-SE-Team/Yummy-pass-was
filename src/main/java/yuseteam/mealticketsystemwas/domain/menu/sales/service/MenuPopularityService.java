package yuseteam.mealticketsystemwas.domain.menu.sales.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.PopularMenuListRes;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.common.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.sales.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.sales.repository.MenuSalesSnapshotrepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuPopularityService {

    private final MenuRepository menuRepository;
    private final MenuSalesSnapshotrepository menuSalesSnapshotrepository;

    public PopularMenuListRes getMostPopularMenuByRestaurant(Long restaurantId) {

        List<Menu> getAllMenus = menuRepository.findByRestaurantIdAndVisibleTrue(restaurantId);

        if (getAllMenus.isEmpty()) {
            throw new IllegalArgumentException("해당 레스토랑에 메뉴가 없습니다.");
        }

        LocalDateTime twentyMinutesAgo = LocalDateTime.now().minusMinutes(20);

        List<MenuSalesSnapshot> snapshots = menuSalesSnapshotrepository
                .findByMenuRestaurantIdAndSnapshotTimeGreaterThanEqual(
                        restaurantId,
                        twentyMinutesAgo
                );

        Map<Long, Integer> menuSalesMap = snapshots.stream()
                .collect(Collectors.groupingBy(
                        snapshot -> snapshot.getMenu().getId(),
                        Collectors.summingInt(MenuSalesSnapshot::getSalesInInterval)
                ));

        Integer maxSales = getAllMenus.stream()
                .map(menu -> menuSalesMap.getOrDefault(menu.getId(), 0))
                .max(Integer::compareTo)
                .orElse(0);

        if (maxSales == 0) {
            log.info("레스토랑 [{}] 최근 20분 판매된 메뉴가 없습니다.", restaurantId);
            return new PopularMenuListRes(
                    List.of(),
                    0
            );
        }

        List<String> popularMenuNames = getAllMenus.stream()
                .filter(menu -> menuSalesMap.getOrDefault(menu.getId(), 0).equals(maxSales))
                .map(Menu::getName)
                .toList();

        log.info("레스토랑 [{}] 최근 20분 인기 메뉴: {} ({}개 판매)",
                restaurantId, popularMenuNames, maxSales);

        return new PopularMenuListRes(
                popularMenuNames,
                maxSales
        );
    }
}

