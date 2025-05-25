package com.arch.ecommerce.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartCommand {
  private Long productId;
  private Integer quantity;
  private CartAction action;
}
