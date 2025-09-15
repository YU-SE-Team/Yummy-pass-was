package yuseteam.mealticketsystemwas.domain.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuseteam.mealticketsystemwas.domain.restaurant.dto.RestaurantResponseDto;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;
import yuseteam.mealticketsystemwas.domain.restaurant.repository.RestaurantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantResponseDto::new)
                .collect(Collectors.toList());
    }
}
