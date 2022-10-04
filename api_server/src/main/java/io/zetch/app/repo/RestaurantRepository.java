package io.zetch.app.repo;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {}
