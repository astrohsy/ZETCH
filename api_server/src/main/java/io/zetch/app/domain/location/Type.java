package io.zetch.app.domain.location;

import lombok.AllArgsConstructor;

/** Enum for all possible user types. */
@AllArgsConstructor
public enum Type {
  RESTAURANT("restaurant"),
  MUSEUM("museum"),
  BUILDING("building");

  private final String text;

  /**
   * Returns a `Type` type from a string.
   *
   * @throws IllegalArgumentException If value doesn't match type
   */
  public static Type fromString(String value) {
    for (Type v : values()) {
      if (v.toString().equalsIgnoreCase(value)) {
        return v;
      }
    }

    throw new IllegalArgumentException("Invalid type: " + value);
  }

  @Override
  public String toString() {
    return text;
  }
}
