package com.arch.ecommerce.security.controller.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
  private String username;
  private String firstname;
  private String password;
  private String email;
}
