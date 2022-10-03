package io.zetch.app.domain.user;

import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
public class UserDto implements Serializable {
  @ToString.Exclude
  Long id;
  String username;
  String name;
  String email;

}
