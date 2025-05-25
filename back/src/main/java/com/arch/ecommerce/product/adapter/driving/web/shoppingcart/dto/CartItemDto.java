package com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto;

import com.arch.ecommerce.product.adapter.driving.web.product.dto.ProductDto;

public record CartItemDto(Long id, ProductDto product, Integer quantity) {}
