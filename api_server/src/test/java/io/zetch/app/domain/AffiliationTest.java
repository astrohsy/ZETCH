package io.zetch.app.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.zetch.app.domain.user.Affiliation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AffiliationTest {

  @ParameterizedTest
  @CsvSource({"student, STUDENT", "faculty, FACULTY", "other, OTHER", "admin, ADMIN"})
  void fromString(String text, Affiliation affiliation) {
    assertThat(Affiliation.fromString(text), is(affiliation));
  }

  @Test
  void fromString_Failure() {
    assertThrows(IllegalArgumentException.class, () -> Affiliation.fromString("doesNotExist"));
  }
}
