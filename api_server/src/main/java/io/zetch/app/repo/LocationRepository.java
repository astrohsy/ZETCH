package io.zetch.app.repo;

import io.zetch.app.domain.location.LocationEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend representation for the Location data. */
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
  boolean existsByName(String name);

  Optional<LocationEntity> findByName(String name);
}
