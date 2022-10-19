package io.zetch.app.repo;

import io.zetch.app.domain.log.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity, Long> {}
