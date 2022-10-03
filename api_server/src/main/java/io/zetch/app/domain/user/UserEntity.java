/** User entity definition */
package io.zetch.app.domain.user;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "zetch_user") // "User" is a reserved keyword in some DBs
public class UserEntity extends BaseEntity {
  @NonNull
  @Column(unique=true)
  private String username;
  private String name;
  private String email;

  @ManyToMany(mappedBy = "owners") // A restaurant might have multiple owners; a User might own multiplerestaurants
  private List<RestaurantEntity> ownedRestaurants;
  }
