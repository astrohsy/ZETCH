package io.zetch.app.repo;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
  boolean existsByName(String name);

  Optional<RestaurantEntity> findByName(String name);
}
