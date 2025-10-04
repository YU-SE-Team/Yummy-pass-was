package yuseteam.mealticketsystemwas.domain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuSalesSnapshotrepository;

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
}
