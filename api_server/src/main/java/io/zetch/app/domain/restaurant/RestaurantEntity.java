/** Restaurant entity definition */
package io.zetch.app.domain.restaurant;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.user.UserDto;
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
public class RestaurantEntity extends BaseEntity {

  @ManyToMany // A restaurant might have multiple owners; a User might own multiple restaurants
  @ToString.Exclude
  private List<UserEntity> owners;
  // Every restaurant must have a name
  @NonNull private String name;
  private String cuisine;
  private String address;

  /**
   * Convert the Restaurant entity to a Restaurant data transfer object
   *
   * @return Restaurant DTO
   */
  public RestaurantDto toDto() {
    return new RestaurantDto(name, cuisine, address);
  }
}
