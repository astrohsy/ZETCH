package io.zetch.app.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BaseEntityTest {

  private static final String USERNAME = "bob";
  private static final String NAME = "Bob";
  private static final String EMAIL = "bob@example.com";

  @AllArgsConstructor
  public class ConcreteEntity extends BaseEntity {
    private String name;
  }

  @Test
  public void testEquals() {
    ConcreteEntity entityA = new ConcreteEntity("concreteA");
    ConcreteEntity entityB = new ConcreteEntity("concreteB");

    assertEquals(true, entityA.equals(entityA));
    assertEquals(false, entityA.equals(null));
    assertEquals(false, entityA.equals(entityB));
    assertEquals(false, entityA.equals(10));
  }
}
