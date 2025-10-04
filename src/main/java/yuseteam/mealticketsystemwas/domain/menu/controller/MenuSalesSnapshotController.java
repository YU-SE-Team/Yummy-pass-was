package yuseteam.mealticketsystemwas.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuseteam.mealticketsystemwas.domain.menu.dto.MenuSalesGraphRes;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuSalesSnapshot;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuSalesSnapshotrepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus/sales-snapshots")
@RequiredArgsConstructor
@Slf4j
public class MenuSalesSnapshotController {

    private final MenuSalesSnapshotrepository menuSalesSnapshotrepository;

    @GetMapping("/sales-diff")
    public ResponseEntity<MenuSalesGraphRes> getSalesDiff() {
        List<MenuSalesSnapshot> snapshots = menuSalesSnapshotrepository.findAll();

        //데이터베이스에 없는 경우
        if(snapshots.isEmpty()) {
            log.warn("저장된 스냅샷 데이터가 없습니다. 스케줄러가 아직 실행되지 않았을 수 있습니다.");
            return ResponseEntity.ok(MenuSalesGraphRes.builder()
                    .salesDiff(new HashMap<>())
                    .lastRecordedAt(null)
                    .previousRecordedAt(null)
                    .build());
        }

        Map<Long, Integer> salesDiffMap = new HashMap<>();
        LocalDateTime latestTime = null;
        LocalDateTime previousTime = null;

        for (MenuSalesSnapshot snapshot : snapshots) {
            Long menuId = snapshot.getMenu().getId();
            int diff = snapshot.getSalesDiff();
            salesDiffMap.put(menuId, diff);

            if (latestTime == null) {
                latestTime = snapshot.getCurrentRecordedAt();
                previousTime = snapshot.getPreviousRecordedAt();
            }
        }

        MenuSalesGraphRes res = MenuSalesGraphRes.builder()
                .salesDiff(salesDiffMap)
                .lastRecordedAt(latestTime)
                .previousRecordedAt(previousTime)
                .build();

        return ResponseEntity.ok(res);
    }
}
