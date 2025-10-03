package yuseteam.mealticketsystemwas.domain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void recordMenuSalesSnapshot() {
        LocalDateTime now = LocalDateTime.now();

        try {
            List<Menu> menus = menuRepository.findAll();
            int updatedCount = 0;

            for (Menu menu : menus) {
                int currentSales = menu.getSoldTicket();


                MenuSalesSnapshot snapshot = menuSalesSnapshotrepository.findByMenu(menu)
                        .orElseGet(() -> {
                            log.debug("메뉴 ID {} - 신규 스냅샷 생성", menu.getId());
                            return MenuSalesSnapshot.createNew(menu);
                        });

                snapshot.updateSnapshot(currentSales, now);
                menuSalesSnapshotrepository.save(snapshot);

                log.debug("메뉴 ID: {}, 이름: {}, 이전: {}, 현재: {}, 변화: {}",
                        menu.getId(),
                        menu.getName(),
                        snapshot.getPreviousSalesCount(),
                        snapshot.getCurrentSalesCount(),
                        snapshot.getSalesDiff());

                updatedCount++;
            }

            log.info("=== [판매량 스냅샷 기록] 완료 - 총 {}개 메뉴 업데이트 ===", updatedCount);
        } catch (Exception e) {
            log.error("=== [판매량 스냅샷 기록] 실패 ===", e);
            throw e;
        }
    }
}
