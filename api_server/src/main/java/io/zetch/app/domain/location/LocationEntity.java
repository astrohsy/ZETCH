/** Location entity definition */
package io.zetch.app.domain.location;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LocationEntity extends BaseEntity {

  @ManyToMany // A location might have multiple owners; a User might own multiple locations
  @ToString.Exclude
  private List<UserEntity> owners;
  // Every location must have a name
  @NonNull private String name;
  private String cuisine;
  private String address;

  /**
   * Convert the Location entity to a Location data transfer object
   *
   * @return Location DTO
   */
  public LocationDto toDto() {
    return new LocationDto(name, cuisine, address);
  }
}
