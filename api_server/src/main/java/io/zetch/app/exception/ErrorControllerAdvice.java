package io.zetch.app.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ErrorControllerAdvice {
  private Map<String, Object> buildErrorResponse(Exception e, HttpServletRequest r, HttpStatus s) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", s.value());
    response.put("error", s.getReasonPhrase());
    response.put("path", r.getRequestURI());
    response.put("message", e.getMessage());
    return response;
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  Map<String, Object> onAccessDeniedException(RuntimeException e, HttpServletRequest request) {
    return buildErrorResponse(e, request, HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseBody
  Map<String, Object> onNoSuchElementException(RuntimeException e, HttpServletRequest request) {
    return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public Map<String, Object> onMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    Map<String, Object> response = buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    Map<String, String> errors = new LinkedHashMap<>();
    e.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    response.put("error_detail", errors);
    return response;
  }
}
