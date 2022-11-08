package io.zetch.app.repo;

import io.zetch.app.domain.review.ReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for the Review data. */
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  long countByLocation_NameIgnoreCaseAndRating(String name, Integer rating);

  List<ReviewEntity> findByLocation_NameIgnoreCase(String name);

}
