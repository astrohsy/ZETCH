package io.zetch.app.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Affiliation {
  STUDENT("student"),
  FACULTY("faculty");

  @Getter private final String value;
}
