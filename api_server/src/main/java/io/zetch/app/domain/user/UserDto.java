package io.zetch.app.domain.user;

import java.io.Serializable;
import lombok.ToString;
import lombok.Value;

@Value
public class UserDto implements Serializable {
  @ToString.Exclude Long id;
  String username;
  String name;
  String email;
}
