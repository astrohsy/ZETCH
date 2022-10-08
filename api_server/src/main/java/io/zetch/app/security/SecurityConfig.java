/**
 * This code is mostly from the Auth0 documentation with modifications for Amazon Cognito:
 * https://auth0.com/docs/quickstart/backend/java-spring-security5/interactive
 */
package io.zetch.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Configures our application with Spring Security to restrict access to our API endpoints. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    /*
    This is where we configure the security required for our endpoints and set up our app to serve as
    an OAuth2 Resource Server, using JWT validation.
    */
    http.cors().and().csrf().disable(); // NOSONAR not used in secure contexts

    http.authorizeRequests(
            authReqCustomizer ->
                authReqCustomizer.antMatchers("/private").authenticated().anyRequest().permitAll())
        .oauth2ResourceServer()
        .jwt();

    return http.build();
  }
}
