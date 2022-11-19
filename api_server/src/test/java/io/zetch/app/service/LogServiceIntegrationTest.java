package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.hamcrest.core.Is.is;

import io.zetch.app.domain.log.LogEntity;
import io.zetch.app.repo.LogRepository;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
class LogServiceIntegrationTest {

  LogEntity l1 = LogEntity.builder().clientId("client 1").build();
  LogEntity l2 = LogEntity.builder().clientId("client 1").build();
  LogEntity l3 = LogEntity.builder().clientId("client 2").build();

  @Autowired private LogRepository logRepository;

  @AfterEach
  void resetDatabase() {
    logRepository.deleteAll();
  }

  @Test
  void getAllClientLogs() {
    logRepository.save(l1);
    logRepository.save(l2);
    logRepository.save(l3);

    List<LogEntity> logs1 = logRepository.findByClientId("client 1");
    assertThat(logs1.size(), is(2));
    assertThat(logs1.get(0), is(l1));
    assertThat(logs1.get(1), is(l2));

    List<LogEntity> logs2 = logRepository.findByClientId("client 2");
    assertThat(logs2.size(), is(1));
    assertThat(logs2.get(0), is(l3));
  }

  @Test
  void deleteClientLogs() {
    logRepository.save(l1);
    logRepository.save(l2);
    logRepository.save(l3);

    long count1 = logRepository.deleteByClientId("client 1");
    assertThat(count1, is(2L));

    long count2 = logRepository.deleteByClientId("client 2");
    assertThat(count2, is(1L));

    assertThat(logRepository.count(), is(0L));
  }
}
