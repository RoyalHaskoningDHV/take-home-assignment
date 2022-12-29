package com.rhdhv.infra.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ProblemDetail handException(final Exception e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public static ProblemDetail handle(final ConstraintViolationException exception) {
    final String errorMessage = exception.getConstraintViolations()
        .stream()
        .map(GlobalExceptionHandler::violationMessage)
        .collect(Collectors.joining(" && "));

    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ProblemDetail handleRequestPropertyBindingError(final WebExchangeBindException webExchangeBindException) {
    log.debug("Bad request!", webExchangeBindException);

    return createFieldErrorResponse(webExchangeBindException.getBindingResult());
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull final MethodArgumentNotValidException ex,
      @NonNull final HttpHeaders headers,
      @NonNull final HttpStatusCode status,
      @NonNull final WebRequest request) {

    log.debug("Method argument not valid. Message: $methodArgumentNotValidException.message", ex);

    final ProblemDetail problemDetail = createFieldErrorResponse(ex.getBindingResult());

    return ResponseEntity.status(status).body(problemDetail);
  }


  private static String violationMessage(final ConstraintViolation<?> violation) {
    return violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage();
  }

  private static ProblemDetail createFieldErrorResponse(final Errors bindingResult) {
    final String errorMessage = bindingResult
        .getFieldErrors().stream()
        .map(fieldError -> "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
        .collect(Collectors.joining(" && "));

    log.debug("Exception occurred while request validation: {}", errorMessage);

    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Wrong fields : " + errorMessage);
  }
}