package io.zetch.app.seed;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;

/** Sample users. */
public class SeedUsers {

  private SeedUsers() {
    throw new IllegalStateException("Utility class");
  }

  public static final UserEntity USER1 =
      UserEntity.builder()
          .username("admin")
          .affiliation(Affiliation.ADMIN)
          .displayName("Admin.")
          .email(null)
          .build();
  public static final UserEntity USER2 =
      UserEntity.builder()
          .username("bob")
          .affiliation(Affiliation.OTHER)
          .displayName("Bob R.")
          .email("bob@me.com")
          .build();
  public static final UserEntity USER3 =
      UserEntity.builder()
          .username("cat")
          .affiliation(Affiliation.OTHER)
          .displayName("Kat F.")
          .email("kat@kat.com")
          .build();
  public static final UserEntity USER4 =
      UserEntity.builder()
          .username("amy")
          .affiliation(Affiliation.OTHER)
          .displayName("Amy T.")
          .email("amy@dog.com")
          .build();
  public static final UserEntity USER5 =
      UserEntity.builder()
          .username("sam")
          .affiliation(Affiliation.OTHER)
          .displayName("Sam 2.")
          .email("sam@from.com")
          .build();

  public static final List<UserEntity> USERS = List.of(USER1, USER2, USER3, USER4, USER5);
}
