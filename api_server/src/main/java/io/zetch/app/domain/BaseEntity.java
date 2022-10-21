package io.zetch.app.domain;

import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

/** Base Entity providing common attributes/functionality for all subclass entities. */
@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id @GeneratedValue private Long id;

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (this == o) {
      return true;
    }

    if (o instanceof BaseEntity that) {
      return this.id != null && Objects.equals(this.id, that.id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
