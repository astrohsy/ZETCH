package io.zetch.app.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.location.LocationEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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

/** User entity definition. */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity extends BaseEntity {
  @NonNull
  @Column(unique = true)
  private String username;

  private Affiliation affiliation;
  private String displayName;
  private String email;

  // A location might have multiple owners; a User might own multiple locations
  @ManyToMany(mappedBy = "owners")
  @ToString.Exclude
  @JsonIgnore
  private List<LocationEntity> ownedLocations;

  /** Partial constructor that can't be generated by Lombok. */
  public UserEntity(
      @NonNull String username, Affiliation affiliation, String displayName, String email) {
    this.username = username;
    this.affiliation = affiliation;
    this.displayName = displayName;
    this.email = email;
    this.ownedLocations = new ArrayList<>();
  }

  /**
   * Converts the User entity to a User data transfer object.
   *
   * @return User DTO
   */
  public UserDto toDto() {
    return new UserDto(username, displayName, email, affiliation.toString());
  }

  public UserGetDto toGetDto() {
    return new UserGetDto(super.getId(), username, displayName, email, affiliation.toString());
  }
}
