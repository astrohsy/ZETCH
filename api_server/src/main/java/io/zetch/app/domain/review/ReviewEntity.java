/** Review entity definition */
package io.zetch.app.domain.review;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.domain.user.UserEntity;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

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
  private Integer rating;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "restaurant_id")
  private RestaurantEntity restaurant;
}
