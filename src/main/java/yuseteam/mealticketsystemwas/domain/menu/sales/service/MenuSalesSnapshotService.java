package yuseteam.mealticketsystemwas.domain.menu.sales.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.MenuSalesGraphRes;
import yuseteam.mealticketsystemwas.domain.menu.sales.dto.SalesDataPoint;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.sales.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.common.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.sales.repository.MenuSalesSnapshotrepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuSalesSnapshotService {

    private final MenuRepository menuRepository;
    private final MenuSalesSnapshotrepository menuSalesSnapshotrepository;

    //crontab expression: 초, 분, 시, 일, 월, 요일
    //5분마다 실행 (매 시 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55분)
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void recordMenuSalesSnapshot() {
        List<Menu> menus = menuRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Menu menu : menus) {
            // visible=false인 메뉴는 스냅샷 기록 건너뛰기
            if (!menu.getVisible()) {
                log.debug("메뉴 [{}]는 숨김 처리되어 스냅샷 기록을 건너뜁니다.", menu.getName());
                continue;
            }

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

}