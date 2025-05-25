package com.arch.ecommerce.product.adapter.driven.security;

import com.arch.ecommerce.product.application.port.out.user.GetLoggedInUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAdapter implements GetLoggedInUserPort {

  @Override
  public String get() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("User is not authenticated");
    }
    return authentication.getName();
  }
}
