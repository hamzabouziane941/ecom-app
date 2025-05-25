package com.arch.ecommerce.errormanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError implements ApiSubError {

  private String object;
  private String field;
  private Object rejectedValue;
  private String message;
}
