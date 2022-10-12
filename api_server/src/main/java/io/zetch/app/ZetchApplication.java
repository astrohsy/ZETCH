package io.zetch.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "ZETCH API", description = "API Definitions of the ZETCH server"))
@SecurityScheme(
    name = "OAuth2",
    type = SecuritySchemeType.OAUTH2,
    in = SecuritySchemeIn.HEADER,
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(
                    authorizationUrl = "${cognito.auth-url}",
                    tokenUrl = "${cognito.token-url}")))
class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(@Autowired UserRepository userRepository) {
    return args -> {
      userRepository.save(new UserEntity("admin", Affiliation.ADMIN, "Admin.", "admin@zetch.io"));
      userRepository.save(new UserEntity("bob", Affiliation.STUDENT, "Bob R.", "bob@me.com"));
      userRepository.save(new UserEntity("cat", Affiliation.STUDENT, "Kat", "kat@kat.com"));
      userRepository.save(new UserEntity("amy", Affiliation.FACULTY, "Amy", "amy@com.com"));
      userRepository.save(new UserEntity("sam", Affiliation.OTHER, "S.", "sam@goog.com"));
    };
  }
}
