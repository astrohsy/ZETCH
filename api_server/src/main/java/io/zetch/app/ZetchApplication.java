package io.zetch.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "ZETCH API", description = "API Definitions of the ZETCH server"),
    servers = {@Server(url = "/", description = "Default Server URL")}
  )
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
}
