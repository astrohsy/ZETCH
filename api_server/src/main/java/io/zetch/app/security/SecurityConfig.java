package io.zetch.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration to restrict access to our API endpoints. This code is mostly from
 * the Auth0 documentation with modifications for Amazon Cognito: <a
 * href="https://auth0.com/docs/quickstart/backend/java-spring-security5/interactive"></a>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  /** Returns a filter chain with security rules applied. */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable(); // NOSONAR not used in secure contexts

    http.authorizeRequests(
            authReqCustomizer ->
                authReqCustomizer
                    .antMatchers("/swagger-ui/**")
                    .permitAll()
                    .antMatchers("/v3/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer()
        .jwt();

    return http.build();
  }
}
