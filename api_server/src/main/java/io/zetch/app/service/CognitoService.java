package io.zetch.app.service;

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

@Service
public class CognitoService {
  private final CognitoIdentityProviderClient cognito;
  private final String clientId;

  @Autowired
  public CognitoService(
      @Value("${cognito.access-key-id}") String accessKey,
      @Value("${cognito.secret-key}") String secretKey,
      @Value("${cognito.client-id}") String clientId) {
    this.clientId = clientId;
    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
    this.cognito =
        CognitoIdentityProviderClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
  }

  public void signUp(String username) {
    SignUpRequest signUpRequest =
        SignUpRequest.builder().username(username).password("123456").clientId(clientId).build();

    try {
      cognito.signUp(signUpRequest);
    } catch (UsernameExistsException e) {
      // Ignore this exception for now. Since we may not have data persistence during dev, we might
      // create multiple users with the same username.
      System.err.println("Username exists in Cognito. Ignoring.");
    } catch (CognitoIdentityProviderException e) {
      System.err.println("Cognito failed: " + e.getMessage());
    }
  }
}
