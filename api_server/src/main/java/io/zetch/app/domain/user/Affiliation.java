package io.zetch.app.domain.user;

import lombok.AllArgsConstructor;

/** Enum for all possible user affiliations. */
@AllArgsConstructor
public enum Affiliation {
  OTHER("other"),
  ADMIN("admin");

  private final String text;

  /**
   * Returns an `Affiliation` type from a string.
   *
   * @throws IllegalArgumentException If value doesn't match affiliation
   */
  public static Affiliation fromString(String value) {
    for (Affiliation v : values()) {
      if (v.toString().equalsIgnoreCase(value)) {
        return v;
      }
    }

    throw new IllegalArgumentException("Invalid affiliation: " + value);
  }

  @Override
  public String toString() {
    return text;
  }
}
