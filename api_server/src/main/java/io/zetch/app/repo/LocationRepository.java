package io.zetch.app.repo;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

/** Backend for the Location data. */
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

  List<LocationEntity> findByOwners_UsernameIgnoreCase(String username);

  boolean existsByName(String name);

  Optional<LocationEntity> findByName(String name);

  @Query(
      """
      select l from LocationEntity l
      where upper(l.name) like upper(concat('%', :name, '%')) or
            upper(l.description) like upper(concat('%', :description, '%')) or
            l.type = :type
      order by l.name
      """)
  List<LocationEntity> search(
      @Nullable String name, @Nullable String description, @Nullable Type type);
}
