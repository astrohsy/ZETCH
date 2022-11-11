package io.zetch.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  Map<String, Object> onRuntimeException(RuntimeException e, HttpServletRequest request) {
    Map<String, Object> response = buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    return response;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseBody
  Map<String, Object> onNoSuchElementException(RuntimeException e, HttpServletRequest request) {
    Map<String, Object> response = buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    return response;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onConstraintValidationException(
      ConstraintViolationException e, HttpServletRequest request) {
    Map<String, Object> response = buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    return response;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public Map<String, Object> handleValidationExceptions(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    Map<String, Object> response = buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    Map<String, String> errors = new LinkedHashMap<>();
    e.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    response.put("error_detail", errors);
    return response;
  }
}
