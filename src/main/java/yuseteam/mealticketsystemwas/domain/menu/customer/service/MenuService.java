package yuseteam.mealticketsystemwas.domain.menu.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.menu.common.dto.MenuDetailResponse;
import yuseteam.mealticketsystemwas.domain.menu.common.entity.Menu;
import yuseteam.mealticketsystemwas.domain.menu.common.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.menu.common.dto.MenuResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenuByRestaurantAndCategory(Long restaurantId, String category) {
        return menuRepository.findByRestaurantIdAndCategory(restaurantId, category).stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuDetailResponse getMenuDetails(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다. id=" + menuId));

        return new MenuDetailResponse(menu);
    }
}
