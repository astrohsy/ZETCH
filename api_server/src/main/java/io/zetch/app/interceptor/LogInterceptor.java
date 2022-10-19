package io.zetch.app.interceptor;

import java.util.Base64;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.zetch.app.domain.log.LogEntity;
import io.zetch.app.repo.LogRepository;

public class LogInterceptor implements HandlerInterceptor {

  Base64.Decoder decoder = Base64.getUrlDecoder();
  ObjectMapper mapper = new ObjectMapper();

  @Autowired LogRepository logRepository;

  // Using `postHandle` to avoid logging requests that were deemed unauthorized by `@PreAuthorize`
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      @Nullable ModelAndView modelAndView)
      throws Exception {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    // Only log requests that are authenticated (and hence contain a token with client id in the
    // header)
    if (authHeader != null) {
      String requestURI = request.getRequestURI();

      // Get auth details from the header
      String[] chunks = authHeader.split("\\.");
      String payload = new String(decoder.decode(chunks[1]));
      @SuppressWarnings("unchecked")
      Map<String, String> claims = mapper.readValue(payload, Map.class);

      String username = claims.get("username");
      String clientId = claims.get("client_id");

      System.out.println(requestURI);
      System.out.println(username);
      System.out.println(clientId);

      logRepository.save(new LogEntity(clientId, username, requestURI));
    }
  }
}
