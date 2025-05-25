package com.arch.ecommerce.product.application.exception;

import com.arch.ecommerce.errormanagement.exception.BusinessException;

public class ProductCreationException extends BusinessException {

  public ProductCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
