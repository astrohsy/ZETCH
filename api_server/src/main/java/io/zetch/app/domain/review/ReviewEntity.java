package io.zetch.app.domain.review;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.user.UserEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Review entity definition. */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ReviewEntity extends BaseEntity {
  private String comment;

  @Min(1)
  @Max(5)
  private Integer rating;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private LocationEntity location;

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public ReviewGetDto toGetDto() {
    return new ReviewGetDto(super.getId(), comment, rating, user.toGetDto(), location.toGetDto());
  }
}
