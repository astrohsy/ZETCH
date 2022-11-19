package io.zetch.app.seed;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;

/** Sample users. */
public class SeedUsers {

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
          .displayName("Sam O.")
          .email("sam@from.com")
          .build();
  public static final UserEntity USER6 =
      UserEntity.builder()
          .username("peter")
          .affiliation(Affiliation.OTHER)
          .displayName("Peter G.")
          .email("peter@meat.com")
          .build();
  public static final UserEntity USER7 =
      UserEntity.builder()
          .username("finn")
          .affiliation(Affiliation.OTHER)
          .displayName("Finley R.")
          .email("finn@fish.com")
          .build();
  public static final UserEntity USER8 =
      UserEntity.builder()
          .username("mike")
          .affiliation(Affiliation.OTHER)
          .displayName("Michael T.")
          .email("mike@may.com")
          .build();
  public static final UserEntity USER9 =
      UserEntity.builder()
          .username("beck")
          .affiliation(Affiliation.OTHER)
          .displayName("Beckett W.")
          .email("sam@from.com")
          .build();
  public static final UserEntity USER10 =
      UserEntity.builder()
          .username("archie")
          .affiliation(Affiliation.OTHER)
          .displayName("Archibald F.")
          .email("archie@kat.com")
          .build();
  public static final UserEntity USER11 =
      UserEntity.builder()
          .username("logan")
          .affiliation(Affiliation.OTHER)
          .displayName("Logan T.")
          .email("logan@dog.com")
          .build();
  public static final UserEntity USER12 =
      UserEntity.builder()
          .username("adrien")
          .affiliation(Affiliation.OTHER)
          .displayName("Adrien O.")
          .email("adrien@from.com")
          .build();
  public static final UserEntity USER13 =
      UserEntity.builder()
          .username("caden")
          .affiliation(Affiliation.OTHER)
          .displayName("Caden G.")
          .email("caden@meat.com")
          .build();
  public static final UserEntity USER14 =
      UserEntity.builder()
          .username("leslie")
          .affiliation(Affiliation.OTHER)
          .displayName("Leslie R.")
          .email("leslie@knope.com")
          .build();
  public static final UserEntity USER15 =
      UserEntity.builder()
          .username("ali")
          .affiliation(Affiliation.OTHER)
          .displayName("Ali T.")
          .email("ali@butterfly.com")
          .build();
  public static final UserEntity USER16 =
      UserEntity.builder()
          .username("clem")
          .affiliation(Affiliation.OTHER)
          .displayName("Clem H.")
          .email("clem@hardies.com")
          .build();
  public static final UserEntity USER17 =
      UserEntity.builder()
          .username("jim")
          .affiliation(Affiliation.OTHER)
          .displayName("James H.")
          .email("jim@french.com")
          .build();
  public static final UserEntity USER18 =
      UserEntity.builder()
          .username("vic")
          .affiliation(Affiliation.OTHER)
          .displayName("Victor L.")
          .email("vic@tour.com")
          .build();
  public static final UserEntity USER19 =
      UserEntity.builder()
          .username("bill")
          .affiliation(Affiliation.OTHER)
          .displayName("William K.")
          .email("willy@boy.com")
          .build();
  public static final List<UserEntity> USERS =
      List.of(
          USER1, USER2, USER3, USER4, USER5, USER6, USER7, USER8, USER9, USER10, USER11, USER12,
          USER13, USER14, USER15, USER16, USER17, USER18, USER19);

  private SeedUsers() {
    throw new IllegalStateException("Utility class");
  }
}
