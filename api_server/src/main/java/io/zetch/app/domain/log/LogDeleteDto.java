package io.zetch.app.domain.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A DTO for the {@link LogEntity} entity. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDeleteDto implements Serializable {
  @JsonProperty("deleted_count")
  long deletedCount;
}
