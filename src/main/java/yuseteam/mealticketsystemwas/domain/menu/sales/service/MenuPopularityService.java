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
        //레스토랑의 모든 메뉴 조회
        List<Menu> getAllMenus = menuRepository.findByRestaurantId(restaurantId);

        if (getAllMenus.isEmpty()) {
            throw new IllegalArgumentException("해당 레스토랑에 메뉴가 없습니다.");
        }

        //20분 전 시간 계산
        LocalDateTime twentyMinutesAgo = LocalDateTime.now().minusMinutes(20);

        //최근 20분간의 스냅샷 조회
        List<MenuSalesSnapshot> snapshots = menuSalesSnapshotrepository
                .findByMenuRestaurantIdAndSnapshotTimeGreaterThanEqual(
                        restaurantId,
                        twentyMinutesAgo
                );

        //스냅샷을 메뉴별로 그룹화
        Map<Long, Integer> menuSalesMap = snapshots.stream()
                .collect(Collectors.groupingBy(
                        snapshot -> snapshot.getMenu().getId(),
                        Collectors.summingInt(MenuSalesSnapshot::getSalesInInterval)
                ));

        //가장 높은 판매량 찾기
        Integer maxSales = getAllMenus.stream()
                .map(menu -> menuSalesMap.getOrDefault(menu.getId(), 0))
                .max(Integer::compareTo)
                .orElse(0);

        //판매량이 0인 경우 빈 리스트 반환
        if (maxSales == 0) {
            log.info("레스토랑 [{}] 최근 20분 판매된 메뉴가 없습니다.", restaurantId);
            return new PopularMenuListRes(
                    List.of(),
                    0
            );
        }

        //최대 판매량과 동일한 판매량을 가진 모든 메뉴 찾기
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

