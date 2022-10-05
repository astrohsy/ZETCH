package io.zetch.app.domain.user;

import java.io.Serializable;
import lombok.Data;

/** A DTO for the {@link UserEntity} entity */
@Data
public class UserDto implements Serializable {
  private final String username;
  private final String name;
  private final String email;
}
