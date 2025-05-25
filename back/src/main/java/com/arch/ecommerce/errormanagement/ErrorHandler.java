package com.arch.ecommerce.errormanagement;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Locale;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.errormanagement.model.ApiError;
import com.arch.ecommerce.errormanagement.utils.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {

  private final ErrorMessageResolver errorMessageResolver;

  /**
   * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter
   * is missing.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex) {
    String errorMessage = String.format("%s parameter is missing", ex.getParameterName());
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, errorMessage, BAD_REQUEST);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well. */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex) {
    String mediaType =
        nonNull(ex.getContentType()) ? ex.getContentType().toString() : StringUtils.EMPTY;
    String supportedMediaTypes = StringUtils.join(ex.getSupportedMediaTypes(), ",");
    String errorMessage =
        String.format(
            "%s media type is not supported. Supported media types are : %s",
            mediaType, supportedMediaTypes);
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, errorMessage, BAD_REQUEST);

    return logExceptionAndBuildResponse(ex, apiError);
  }

  /**
   * TODO nested objects and lists remain to be tested, same with I18N support for field and object
   * names Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {
    ApiError apiError = new ApiError(BAD_REQUEST, "Validation error");
    apiError.setBusinessMessage(buildValidationBusinessMessage(ex));
    apiError.addFieldValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addObjectValidationErrors(ex.getBindingResult().getGlobalErrors());
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /**
   * TODO more refined tests remain to be done Handles EntityNotFoundException. Created to
   * encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, Locale locale) {
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, ex.getMessage(), NOT_FOUND);
    String businessMessage = buildEntityNotFoundBusinessMessage(ex, locale);
    apiError.setBusinessMessage(businessMessage);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle HttpMessageNotReadableException. Happens when request JSON is malformed. */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, "Malformed JSON request", BAD_REQUEST);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle HttpMessageNotReadableException. Happens when response JSON is malformed. */
  @ExceptionHandler(HttpMessageNotWritableException.class)
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
      HttpMessageNotWritableException ex) {
    ApiError apiError =
        mapToErrorWithMessageAndStatus(ex, "Error writing JSON output", INTERNAL_SERVER_ERROR);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle NoHandlerFoundException */
  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex) {
    String errorMessage =
        String.format(
            "Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, errorMessage, BAD_REQUEST);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle NoHandlerFoundException */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleNoHandlerFound(MethodArgumentTypeMismatchException ex) {
    String errorMessage =
        String.format(
            "The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, errorMessage, BAD_REQUEST);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  /** Handle BusinessException */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
    ApiError apiError = mapToErrorWithMessageAndStatus(ex, "Business error", BAD_REQUEST);
    return logExceptionAndBuildResponse(ex, apiError);
  }

  private ResponseEntity<Object> logExceptionAndBuildResponse(Exception ex, ApiError apiError) {
    log.error(apiError.getMessage(), ex);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  private ApiError mapToErrorWithMessageAndStatus(
      Exception ex, String errorMessage, HttpStatus status) {
    return ApiError.builder()
        .message(errorMessage)
        .debugMessage(ex.getMessage())
        .status(status)
        .build();
  }

  private String buildValidationBusinessMessage(MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getBindingResult().getFieldError();
    ObjectError objectError = ex.getBindingResult().getGlobalError();

    if (nonNull(fieldError)) {
      return errorMessageResolver.getMessageByCodeAndLocale(
          "field.validation.error",
          Locale.ENGLISH,
          fieldError.getField(),
          fieldError.getDefaultMessage());
    } else if (nonNull(objectError)) {
      return errorMessageResolver.getMessageByCodeAndLocale(
          "object.validation.error",
          Locale.ENGLISH,
          objectError.getObjectName(),
          objectError.getDefaultMessage());
    }

    return StringUtils.EMPTY;
  }

  private String buildEntityNotFoundBusinessMessage(EntityNotFoundException ex, Locale locale) {
    if (isEmpty(ex.getSearchParams())) {
      return errorMessageResolver.getMessageByCodeAndLocale(
          "entity.not.found", locale, ex.getEntityClass());
    }
    return errorMessageResolver.getMessageByCodeAndLocale(
        "entity.not.found.with.search.params",
        locale,
        ex.getEntityClass(),
        ex.getSearchParamsMessage());
  }
}
