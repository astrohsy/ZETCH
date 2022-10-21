package io.zetch.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
class ZetchApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZetchApplication.class, args);
  }
}
