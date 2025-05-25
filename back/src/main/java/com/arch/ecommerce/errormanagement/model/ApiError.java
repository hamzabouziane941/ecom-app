package com.arch.ecommerce.errormanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

  @Getter @Setter private HttpStatus status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @Setter private String message;

  @Setter private String businessMessage;

  @Setter private String debugMessage;

  private List<ApiSubError> subErrors;

  public ApiError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public ApiError(HttpStatus httpStatus) {
    this.status = httpStatus;
  }

  public void addFieldValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addFieldValidationError);
  }

  public void addObjectValidationErrors(List<ObjectError> objectErrors) {
    objectErrors.forEach(this::mapToObjetValidationError);
  }

  private void addFieldValidationError(FieldError objectError) {
    ApiValidationError subError = mapToFieldValidationError(objectError);
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  private ApiValidationError mapToObjetValidationError(ObjectError fieldError) {
    return ApiValidationError.builder()
        .object(fieldError.getObjectName())
        .message(fieldError.getDefaultMessage())
        .build();
  }

  private ApiValidationError mapToFieldValidationError(FieldError fieldError) {
    return ApiValidationError.builder()
        .object(fieldError.getObjectName())
        .field(fieldError.getField())
        .rejectedValue(fieldError.getRejectedValue())
        .message(fieldError.getDefaultMessage())
        .build();
  }
}
