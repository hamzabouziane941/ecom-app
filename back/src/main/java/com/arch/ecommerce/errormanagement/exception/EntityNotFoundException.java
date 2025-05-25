package com.arch.ecommerce.errormanagement.exception;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  public static final String SEARCH_PARAMS_DELIMITER = ",";
  private String entityClass;
  private Map<String, String> searchParams;

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(Class<?> entityClass, Map<String, String> searchParams) {
    this.entityClass = entityClass.getName();
    this.searchParams = searchParams;
  }

  public String getSearchParamsMessage() {
    return searchParams.entrySet().stream()
        .map(entry -> entry.getKey() + ": " + entry.getValue())
        .collect(Collectors.joining(SEARCH_PARAMS_DELIMITER));
  }
}
