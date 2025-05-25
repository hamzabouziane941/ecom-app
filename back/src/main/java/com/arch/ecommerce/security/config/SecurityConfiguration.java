package com.arch.ecommerce.security.config;

import com.arch.ecommerce.security.database.User;
import com.arch.ecommerce.security.database.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    if (!userRepository.existsByEmail("admin@admin.com")) {
      User admin =
          User.builder()
              .email("admin@admin.com")
              .fullname("Admin User")
              .password(passwordEncoder.encode("admin123"))
              .build();
      userRepository.save(admin);
    }
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/token", "/account")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/products")
                    .access(getAdminAuthorizationManager())
                    .requestMatchers(HttpMethod.PATCH, "/products/**")
                    .access(getAdminAuthorizationManager())
                    .requestMatchers(HttpMethod.DELETE, "/products/**")
                    .access(getAdminAuthorizationManager())
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private WebExpressionAuthorizationManager getAdminAuthorizationManager() {
    return new WebExpressionAuthorizationManager(
        "authentication.principal.username == 'admin@admin.com'");
  }
}
