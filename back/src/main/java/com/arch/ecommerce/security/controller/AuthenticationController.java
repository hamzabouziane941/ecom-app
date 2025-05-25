package com.arch.ecommerce.security.controller;

import com.arch.ecommerce.security.service.JwtService;
import com.arch.ecommerce.security.database.User;
import com.arch.ecommerce.security.controller.dto.LoginResponse;
import com.arch.ecommerce.security.controller.dto.LoginUserDto;
import com.arch.ecommerce.security.controller.dto.RegisterUserDto;
import com.arch.ecommerce.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class AuthenticationController {
  private final JwtService jwtService;

  private final AuthenticationService authenticationService;

  public AuthenticationController(
      JwtService jwtService, AuthenticationService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/account")
  public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    User registeredUser = authenticationService.signup(registerUserDto);

    return ResponseEntity.ok(registeredUser);
  }

  @PostMapping("/token")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    User authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponse loginResponse =
        LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

    return ResponseEntity.ok(loginResponse);
  }
}
