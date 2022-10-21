package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.log.LogEntity;
import io.zetch.app.repo.LogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for the log endpoints. */
@RestController
@RequestMapping(path = "/logs")
@Tag(name = "Logs")
@CrossOrigin(origins = "*") // NOSONAR
public class LogController {

  private final LogRepository logRepository;

  @Autowired
  LogController(LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  @GetMapping("/{clientId}")
  @Operation(summary = "Retrieve all logs for the user's client")
  @SecurityRequirement(name = "OAuth2")
  @PreAuthorize("@securityService.isSelfClient(#token, #clientId)")
  public List<LogEntity> getAllClientLogs(
      JwtAuthenticationToken token, @PathVariable String clientId) {
    return logRepository.findByClientId(clientId);
  }
}
