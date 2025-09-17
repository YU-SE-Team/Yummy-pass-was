package yuseteam.mealticketsystemwas.admin.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateRequest;
import yuseteam.mealticketsystemwas.admin.dto.MenuCreateResponse;
import yuseteam.mealticketsystemwas.admin.dto.MenuResponse;
import yuseteam.mealticketsystemwas.admin.repository.MenuRepository;
import yuseteam.mealticketsystemwas.admin.repository.RestaurantRepository;
import yuseteam.mealticketsystemwas.entity.Menu;
import yuseteam.mealticketsystemwas.entity.Restaurant;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuCreateResponse createMenu(MenuCreateRequest req) {
        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow(()->new EntityNotFoundException("해당 식당을 찾을 수 없습니다."));

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
        return MenuCreateResponse.from(saveMenu);
    }

    @Transactional
    public void deleteMenu(Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }
}
