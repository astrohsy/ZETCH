package io.zetch.app.domain.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Affiliation {
  STUDENT("student"),
  FACULTY("faculty"),
  OTHER("other"),
  ADMIN("admin");

  private final String text;

  public static Affiliation fromString(String value) {
    for (Affiliation v : values()) {
      if (v.toString().equalsIgnoreCase(value)) return v;
    }

    throw new IllegalArgumentException("Invalid affiliation: " + value);
  }

  @Override
  public String toString() {
    return text;
  }
}
