package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReplyRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
class ReplyServiceIntegrationTest {

  UserEntity u1 = UserEntity.builder().username("cat").build();
  LocationEntity l1 = LocationEntity.builder().name("Restaurant").owners(List.of(u1)).build();

  UserEntity u4 = UserEntity.builder().username("alice").build();
  LocationEntity l2 = LocationEntity.builder().name("Museum").owners(List.of(u4)).build();

  UserEntity u2 = UserEntity.builder().username("bob").build();
  ReviewEntity re1 = ReviewEntity.builder().user(u2).location(l1).rating(1).build();
  ReviewEntity re2 = ReviewEntity.builder().user(u2).location(l2).rating(2).build();

  UserEntity u3 = UserEntity.builder().username("amy").build();
  ReviewEntity re3 = ReviewEntity.builder().user(u3).location(l1).rating(3).build();
  ReviewEntity re4 = ReviewEntity.builder().user(u3).location(l2).rating(4).build();

  ReplyEntity rp1 =
      ReplyEntity.builder().review(re1).replyUser(u1).replyComment("Reply Test 1").build();
  ReplyEntity rp2 =
      ReplyEntity.builder().review(re2).replyUser(u1).replyComment("Reply Test 2").build();
  ReplyEntity rp3 =
      ReplyEntity.builder().review(re4).replyUser(u4).replyComment("Reply Test 3").build();

  @Autowired private ReplyService replyService;
  @Autowired private UserRepository userRepository;
  @Autowired private LocationRepository locationRepository;
  @Autowired private ReviewRepository reviewRepository;
  @Autowired private ReplyRepository replyRepository;

  @BeforeEach
  void setupPrerequisiteData() {
    userRepository.saveAll(List.of(u1, u2, u3, u4));
    locationRepository.saveAll(List.of(l1, l2));
    reviewRepository.saveAll(List.of(re1, re2, re3, re4));
  }

  @AfterEach
  void resetDatabase() {
    reviewRepository.deleteAll();
    locationRepository.deleteAll();
    userRepository.deleteAll();
    replyRepository.deleteAll();
  }

  @Test
  void getByUser() {
    replyRepository.saveAll(List.of(rp1, rp2, rp3));

    List<ReplyEntity> replies = replyService.getRepliesByUser(u1.getId());

    assertThat(replies.size(), is(2));
    assertThat(replies.get(0), is(rp1));
    assertThat(replies.get(1), is(rp2));
  }

  @Test
  void getByReview() {
    replyRepository.saveAll(List.of(rp1, rp2, rp3));

    List<ReplyEntity> replies = replyService.getRepliesByReview(re1.getId());

    assertThat(replies.size(), is(1));
    assertThat(replies.get(0), is(rp1));
  }

  @Test
  void createNew() {
    ReplyEntity newReply = replyService.createNew("New Reply", u1.getId(), re1.getId());

    assertThat(replyService.getOne(newReply.getId()), is(newReply));
    assertThat(newReply.getReplyComment(), is("New Reply"));
    assertThat(newReply.getReplyUser(), is(u1));
    assertThat(newReply.getReview(), is(re1));
  }

  @Test
  void createNew_UserDoesNotExists() {
    Long id = re1.getId();
    assertThrows(
        NoSuchElementException.class, () -> replyService.createNew("New Reply", 123456L, id));
  }

  @Test
  void createNew_ReviewDoesNotExists() {
    Long id = u1.getId();
    assertThrows(
        NoSuchElementException.class, () -> replyService.createNew("New Reply", id, 123456L));
  }

  @Test
  void createNew_UserNotOwner() {
    Long id = u4.getId();
    Long id2 = re1.getId();
    assertThrows(
        IllegalArgumentException.class, () -> replyService.createNew("New Reply", id, id2));
  }

  @Test
  void deleteOne() {
    ReplyEntity newReply = replyService.createNew("New Reply", u1.getId(), re1.getId());
    Long id = newReply.getId();
    replyService.deleteOne(id);

    assertThrows(NoSuchElementException.class, () -> replyService.getOne(id));
  }
}
