package com.arch.ecommerce.errormanagement.utils;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Locale;

import com.arch.ecommerce.errormanagement.RestErrorConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorMessageResolver {

  private final MessageSource messageSource;

  public String getMessageByCodeAndLocale(String code, Locale locale, String... parameters) {
    String errorMessage = getMessageFromI18NConfiguration(code, locale, parameters);
    if (isBlank(errorMessage)) {
      errorMessage = RestErrorConstant.getMessageByCodeAndLocale(code, locale, parameters);
    }
    return errorMessage;
  }

  private String getMessageFromI18NConfiguration(String code, Locale locale, String[] parameters) {
    try {
      return messageSource.getMessage(code, parameters, locale);
    } catch (NoSuchMessageException e) {
      log.warn("No message found from I18N configuration. Trying to find default error message", e);
    } catch (Exception e) {
      log.warn(
          "Error while getting message from message source. Trying to find default error message",
          e);
    }
    return StringUtils.EMPTY;
  }
}
