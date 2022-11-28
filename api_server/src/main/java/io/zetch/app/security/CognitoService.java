package io.zetch.app.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

/** Service providing Cognito business logic. */
@Service
public class CognitoService {
  private final CognitoIdentityProviderClient cognito;
  private final Logger logger = LoggerFactory.getLogger(CognitoService.class);

  /** Initialize a Cognito service using AWS and Cognito keys. */
  @Autowired
  public CognitoService(
      @Value("${cognito.access-key-id}") String accessKey,
      @Value("${cognito.secret-key}") String secretKey) {

    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
    this.cognito =
        CognitoIdentityProviderClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
  }

  /**
   * Sign up a new user in Cognito. Every user will share the same simple password for now.
   *
   * @param username User's username
   * @param clientId User's client ID
   */
  public void signUp(String username, String clientId) {
    if (!clientId.equals("test")) {
      SignUpRequest signUpRequest =
          SignUpRequest.builder().username(username).password("123456").clientId(clientId).build();

      try {
        cognito.signUp(signUpRequest);
      } catch (UsernameExistsException e) {
        logger.warn("Username already exists in Cognito. Ignoring.");
      } catch (CognitoIdentityProviderException e) {
        logger.error(String.format("Cognito failed: %s", e.getMessage()));
      }
    }
  }
}
