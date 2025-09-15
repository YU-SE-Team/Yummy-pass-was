package yuseteam.mealticketsystemwas.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import yuseteam.mealticketsystemwas.domain.restaurant.entity.Restaurant;
import yuseteam.mealticketsystemwas.domain.restaurant.repository.RestaurantRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final RestaurantRepository restaurantRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (restaurantRepository.count() == 0) {
            log.info("데이터베이스에 식당 정보가 없어 초기 데이터를 생성합니다.");

            Restaurant r1 = new Restaurant("중앙도서관");
            Restaurant r2 = new Restaurant("이과대학");
            Restaurant r3 = new Restaurant("교직원");

            restaurantRepository.saveAll(List.of(r1, r2, r3));
            log.info("식당 초기 데이터 생성이 완료되었습니다.");
        } else {
            log.info("데이터베이스에 이미 식당 정보가 존재합니다.");
        }
    }
}
