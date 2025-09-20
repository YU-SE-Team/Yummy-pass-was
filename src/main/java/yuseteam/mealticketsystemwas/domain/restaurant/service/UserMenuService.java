package yuseteam.mealticketsystemwas.domain.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.admin.entity.Menu;
import yuseteam.mealticketsystemwas.domain.admin.repository.MenuRepository;
import yuseteam.mealticketsystemwas.domain.restaurant.dto.MenuResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenuByRestaurantAndCategory(Long restaurantId, String category) {
        return menuRepository.findByRestaurantIdAndCategory(restaurantId, category).stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }
}
