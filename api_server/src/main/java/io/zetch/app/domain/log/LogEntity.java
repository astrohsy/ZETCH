package io.zetch.app.domain.log;

import io.zetch.app.domain.BaseEntity;
import java.time.Instant;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Log entity definition. */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LogEntity extends BaseEntity {
  private String clientId;
  private String username;
  private String request;
  private String method;
  private Instant timestamp;
}
