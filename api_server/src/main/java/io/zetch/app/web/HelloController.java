package io.zetch.app.web;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  /** An example of a route getting a username from the token */
  @GetMapping("/private")
  public String index3(JwtAuthenticationToken token) {
    String username = token.getToken().getClaimAsString("username");

    return String.format("Greetings from Spring Boot. You are %s!", username);
  }
}
