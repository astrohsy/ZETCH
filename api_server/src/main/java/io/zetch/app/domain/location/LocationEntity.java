package io.zetch.app.domain.location;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Location entity definition. */
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
  @JsonIgnore
  private List<UserEntity> owners;
  // Every location must have a name
  @NonNull private String name;
  private String description;
  private String address;
  private Type type;

  /**
   * Converts the Location entity to a Location data transfer object.
   *
   * @return Location DTO
   */
  public LocationDto toDto() {
    return new LocationDto(name, description, address, type.toString());
  }

  /**
   * Converts the Location entity to a GET Location data transfer object.
   *
   * @return Location DTO
   */
  public LocationGetDto toGetDto() {
    return new LocationGetDto(super.getId(), name, description, address, type.toString());
  }
}
