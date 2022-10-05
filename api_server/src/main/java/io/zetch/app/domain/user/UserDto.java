package io.zetch.app.domain.user;

import java.io.Serializable;
import lombok.Value;

@Value
public class UserDto implements Serializable {
  String username;
  String name;
  String email;
}
