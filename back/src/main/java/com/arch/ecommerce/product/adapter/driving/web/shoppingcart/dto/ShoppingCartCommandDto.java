package com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto;

public record ShoppingCartCommandDto(Long productId, int quantity, CartActionDto action) {}
