package com.arch.ecommerce.errormanagement;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class RestErrorConstant {

  private static final ErrorMessage FIELD_VALIDATION_ERROR_MESSAGE =
      ErrorMessage.builder()
          .code("field.validation.error")
          .languageMessages(Map.of("en", "The field '%s' %S", "fr", "Le champ '%s' %s"))
          .build();

  private static final ErrorMessage OBJECT_VALIDATION_ERROR_MESSAGE =
      ErrorMessage.builder()
          .code("object.validation.error")
          .languageMessages(Map.of("en", "The object '%s' %S", "fr", "L'objet '%s' %s"))
          .build();

  private static final ErrorMessage ENTITY_NOT_FOUND_MESSAGE =
      ErrorMessage.builder()
          .code("entity.not.found")
          .languageMessages(Map.of("en", "%s not found", "fr", "%s est introuvable"))
          .build();

  private static final ErrorMessage ENTITY_NOT_FOUND_WITH_SEARCH_PARAMS_MESSAGE =
      ErrorMessage.builder()
          .code("entity.not.found.with.search.params")
          .languageMessages(
              Map.of(
                  "en",
                  "%s not found for parameters : %s",
                  "fr",
                  "%s est introuvable pour les paramètres : %s"))
          .build();

  private static final List<ErrorMessage> errorMessages =
      List.of(
          FIELD_VALIDATION_ERROR_MESSAGE,
          OBJECT_VALIDATION_ERROR_MESSAGE,
          ENTITY_NOT_FOUND_MESSAGE,
          ENTITY_NOT_FOUND_WITH_SEARCH_PARAMS_MESSAGE);

  public static String getMessageByCodeAndLocale(String key, Locale locale, String[] parameters) {
    return errorMessages.stream()
        .filter(errorMessage -> errorMessage.getCode().equals(key))
        .findFirst()
        .map(
            errorMessage -> {
              return errorMessage.getByLocale(locale, parameters);
            })
        .orElse(StringUtils.EMPTY);
  }

  @Builder
  public static class ErrorMessage {

    @Getter private String code;
    private Map<String, String> languageMessages;

    public String getByLocale(Locale locale, String... parameters) {
      return String.format(languageMessages.get(locale.getLanguage()), parameters);
    }
  }
}
