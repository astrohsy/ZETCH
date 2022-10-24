package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.c4_soft.springaddons.security.oauth2.test.annotations.Claims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import io.zetch.app.domain.log.LogEntity;
import io.zetch.app.repo.LogRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockJwtAuth(
    claims =
        @OpenIdClaims(
            otherClaims =
                @Claims(
                    stringClaims = {
                      @StringClaim(name = "username", value = "some_user"),
                      @StringClaim(name = "client_id", value = "some_client")
                    })))
class LogControllerTest {

  private static final String LOGS_ENDPOINT = "/logs/";

  LogEntity log1 = new LogEntity("some_client", "bob", "/users", "GET", Instant.now());
  LogEntity log2 = new LogEntity("some_client", "amy", "/locations", "GET", Instant.now());

  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean LogRepository logRepositoryMock;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void getAllClientLogs() throws Exception {
    when(logRepositoryMock.findByClientId("some_client")).thenReturn(List.of(log1, log2));

    mockMvc
        .perform(get(LOGS_ENDPOINT + "some_client").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username", is("bob")))
        .andExpect(jsonPath("$[1].username", is("amy")));
  }

  @Test
  void getAllClientLogs_WrongClient() throws Exception {
    mockMvc
        .perform(get(LOGS_ENDPOINT + "another_client").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void deleteLogs() throws Exception {
    when(logRepositoryMock.deleteByClientId("some_client")).thenReturn(1L);

    mockMvc
        .perform(delete(LOGS_ENDPOINT + "some_client").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.deleted_count", is(1)));
  }
}
