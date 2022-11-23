package io.zetch.app.seed;

import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReplyRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Component for loading sample data at runtime. */
@Component
public class DataLoader implements CommandLineRunner {

  @Autowired UserRepository userRepository;
  @Autowired LocationRepository locationRepository;
  @Autowired ReviewRepository reviewRepository;
  @Autowired ReplyRepository replyRepository;

  @Value("${seed-generation-flag}")
  Boolean seedGenerationFlag;

  @Override
  public void run(String... args) {
    if (Boolean.TRUE.equals(seedGenerationFlag)) {
      seedUserData();
      seedLocationData();
      seedReviewData();
      seedReplyData();
    }
  }

  private void seedUserData() {
    if (userRepository.count() == 0) {
      userRepository.saveAll(SeedUsers.USERS);
    }
  }

  private void seedLocationData() {
    if (locationRepository.count() == 0) {
      locationRepository.saveAll(SeedLocations.LOCATIONS);
    }
  }

  private void seedReviewData() {
    if (reviewRepository.count() == 0) {
      reviewRepository.saveAll(SeedReviews.REVIEWS);
    }
  }

  private void seedReplyData() {
    if (replyRepository.count() == 0) {
      replyRepository.saveAll(SeedReplies.REPLIES);
    }
  }
}
