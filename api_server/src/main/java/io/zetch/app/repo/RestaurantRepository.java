package io.zetch.app.repo;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    boolean existsByName(String name);

    Optional<RestaurantEntity> findByName(String name);
}