package io.zetch.app.repo;

import io.zetch.app.domain.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for User data. */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  boolean existsByUsername(String username);

  Optional<UserEntity> findByUsername(String username);
}
