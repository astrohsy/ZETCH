package io.zetch.app.repo;

import io.zetch.app.domain.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for the Review data. */
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
  long countByLocation_NameIgnoreCaseAndRating(String name, Integer rating);
}
