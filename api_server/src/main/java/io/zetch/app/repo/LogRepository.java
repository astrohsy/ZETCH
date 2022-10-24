package io.zetch.app.repo;

import io.zetch.app.domain.log.LogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for Logs. */
public interface LogRepository extends JpaRepository<LogEntity, Long> {
  List<LogEntity> findByClientId(String clientId);

  long deleteByClientId(String clientId);
}
