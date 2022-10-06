/** User entity definition */
package io.zetch.app.domain.review;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;
import io.zetch.app.domain.user.UserEntity;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ReviewEntity extends BaseEntity {
  private String comment;
  private Integer rating;

  @ManyToOne private UserEntity user;
  @ManyToOne private RestaurantEntity restaurant;
}
