package io.zetch.app.interceptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.log.LogEntity;
import io.zetch.app.repo.LogRepository;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class LogInterceptorTest {

  @Mock private LogRepository logRepositoryMock;
  @InjectMocks private LogInterceptor logInterceptor;

  private static final String USERNAME = "bob";
  private static final String CLIENT_ID = "some_client";
  private static final String REQUEST = "/users";
  private static final String METHOD = "GET";
  private static final String AUTH_HEADER =
      "eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJzb21lX2NsaWVudCIsInVzZXJuYW1lIjoiYm9iIn0.pYRBKKLKJLNLaLhas-vpoVbk5U-uBzOAw_DmSfy4_oQ";

  @Test
  void postHandle() throws Exception {
    // Prepare to capture a Log object
    ArgumentCaptor<LogEntity> logCaptor = ArgumentCaptor.forClass(LogEntity.class);

    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRequestURI()).thenReturn(REQUEST);
    when(mockRequest.getMethod()).thenReturn(METHOD);
    when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(AUTH_HEADER);

    logInterceptor.postHandle(mockRequest, null, null, null);

    // Verify save() invoked
    verify(logRepositoryMock).save(logCaptor.capture());

    // Verify the attributes of the User object
    LogEntity value = logCaptor.getValue();
    assertThat(value.getUsername(), is(USERNAME));
    assertThat(value.getRequest(), is(REQUEST));
    assertThat(value.getMethod(), is(METHOD));
    assertThat(value.getClientId(), is(CLIENT_ID));
  }
}
