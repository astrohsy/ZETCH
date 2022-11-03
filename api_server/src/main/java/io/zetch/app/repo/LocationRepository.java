package io.zetch.app.repo;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for the Location data. */
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
  boolean existsByName(String name);

  Optional<LocationEntity> findByName(String name);

  List<LocationEntity> findByNameAndType(String name, Type type);
}
