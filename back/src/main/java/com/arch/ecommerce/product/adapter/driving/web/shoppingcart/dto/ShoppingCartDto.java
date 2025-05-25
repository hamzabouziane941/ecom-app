package com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto;

import java.math.BigDecimal;
import java.util.List;

public record ShoppingCartDto(Long id, List<CartItemDto> items, BigDecimal totalPrice) {}
