package yuseteam.mealticketsystemwas.domain.menu.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuCreateRequest;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuCreateResponse;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuResponse;
import yuseteam.mealticketsystemwas.domain.menu.dto.AdminMenuUpdateRequest;
import yuseteam.mealticketsystemwas.domain.menu.entity.MenuCategory;
import yuseteam.mealticketsystemwas.domain.menu.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;
import yuseteam.mealticketsystemwas.domain.restaurant.repository.RestaurantRepository;
import yuseteam.mealticketsystemwas.domain.menu.entity.Menu;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public AdminMenuCreateResponse createMenu(AdminMenuCreateRequest req) {
        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("해당 식당을 찾을 수 없습니다."));

        Menu menu = Menu.builder()
                .name(req.getName())
                .photoUrl(req.getPhotoUrl())
                .price(req.getPrice())
                .totalCount(req.getTotalCount())
                .soldTicket(0)
                .category(req.getCategory())
                .visible(req.getVisible())
                .restaurant(restaurant)
                .build();

        Menu saveMenu = menuRepository.save(menu);
        return AdminMenuCreateResponse.from(saveMenu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public List<AdminMenuResponse> getMenus() {
        return menuRepository.findAll()
                .stream()
                .map(AdminMenuResponse::from)
                .toList();
    }

    @Transactional
    public AdminMenuResponse getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));
        return AdminMenuResponse.from(menu);
    }
  
    @Transactional
    public AdminMenuResponse updateMenu(Long menuId, @Valid AdminMenuUpdateRequest req) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));
        menu.update(req);
        return AdminMenuResponse.from(menu);
    }

    @Transactional
    public List<String> getCategoriesByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("해당 식당을 찾을 수 없습니다."));

        return MenuCategory.getCategoriesByRestaurant(restaurant.getName())
                .stream()
                .map(MenuCategory::getCategory)
                .toList();
    }
}
