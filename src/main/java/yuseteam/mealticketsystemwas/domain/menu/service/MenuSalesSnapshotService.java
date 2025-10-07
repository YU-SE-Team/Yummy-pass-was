package yuseteam.mealticketsystemwas.domain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuSalesGraphRes;
import yuseteam.mealticketsystemwas.domain.menu.dto.PopularMenuRes;
import yuseteam.mealticketsystemwas.domain.menu.dto.SalesDataPoint;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuSalesSnapshotrepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuSalesSnapshotService {

    private final MenuRepository menuRepository;
    private final MenuSalesSnapshotrepository menuSalesSnapshotrepository;

    //crontab expression: 초, 분, 시, 일, 월, 요일
    //5분마다 실행 (매 시 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55분)
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void recordMenuSalesSnapshot() {
        List<Menu> menus = menuRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Menu menu : menus) {
            int currentCumulativeSales = menu.getCumulativeSoldQuantity();

            //5분 전의 마지막 스냅샷 조회
            Integer previousCumulativeSales = menuSalesSnapshotrepository
                    .findTopByMenuOrderBySnapshotTimeDesc(menu)
                    .map(MenuSalesSnapshot::getCumulativeSales)
                    .orElse(0); //최초엔 0

            //5분간 판매량 계산
            int salesInInterval = currentCumulativeSales - previousCumulativeSales;

            MenuSalesSnapshot newSnapshot = MenuSalesSnapshot.of(
                    menu,
                    now,
                    salesInInterval,
                    currentCumulativeSales
            );

            menuSalesSnapshotrepository.save(newSnapshot);

            log.info("메뉴 [{}] - 이번 5분간 판매: {}개, 누적: {}개",
                    menu.getName(), salesInInterval, currentCumulativeSales);
        }
    }

    //매일 자정에 모든 메뉴 판매 스냅샷 초기화
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailySnapshots() {
        menuSalesSnapshotrepository.deleteAll();
        log.info("매일 자정에 모든 메뉴 판매 스냅샷이 초기화되었습니다.");
    }

    public MenuSalesGraphRes getMenuSalesSnapshot(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        //오늘 10시를 시작 시간으로 설정
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atTime(0, 0, 0);

        //시작부터 현재까지의 총 데이터를 조회
        List<MenuSalesSnapshot> snapshots = menuSalesSnapshotrepository
                .findByMenuAndSnapshotTimeAfterOrderBySnapshotTimeAsc(menu, startOfDay);

        List<SalesDataPoint> dataPoints = snapshots.stream()
                .map(snapshot -> new SalesDataPoint(
                        snapshot.getSnapshotTime(),
                        snapshot.getSalesInInterval(),
                        snapshot.getCumulativeSales()
                ))
                .toList();

        return new MenuSalesGraphRes(
                menu.getId(),
                menu.getName(),
                dataPoints
        );
    }

    public PopularMenuRes getMostPopularMenuByRestaurant(Long restaurantId) {
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

        //스냅샷을 메뉴벼로 그룹화
        Map<Long, Integer> menuSalesMap = snapshots.stream()
                .collect(Collectors.groupingBy(
                        snapshot -> snapshot.getMenu().getId(),
                        Collectors.summingInt(MenuSalesSnapshot::getSalesInInterval)
                ));
        //메뉴마다 판매량 계산, 가장 많이 팔린 메뉴 찾기
        Menu mostPopularMenu = getAllMenus.stream()
                .max(Comparator
                        .comparingInt((Menu menu) -> menuSalesMap.getOrDefault(menu.getId(), 0))
                        .thenComparing(Menu::getId) //판매량이 같으면 ID가 낮은 메뉴 선택
                )
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        Integer totalSales = menuSalesMap.getOrDefault(mostPopularMenu.getId(), 0);

        log.info("레스토랑 [{}] 최근 20분 인기 메뉴: [{}] ({}개 판매)",
                restaurantId, mostPopularMenu.getName(), totalSales);

        return new PopularMenuRes(
                mostPopularMenu.getName(),
                totalSales
        );
    }
}