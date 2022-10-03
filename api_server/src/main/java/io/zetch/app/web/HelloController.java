package io.zetch.app.web;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public String index1() {
    return "Greetings from Spring Boot!";
  }

  @GetMapping("/public")
  public String index2() {
    return "Greetings from Spring Boot! This is public";
  }

  @GetMapping("/private")
  public String index3(JwtAuthenticationToken principal) {
    String username = principal.getToken().getClaimAsString("username");

    return String.format("Greetings from Spring Boot, %s!", username);
  }
}
