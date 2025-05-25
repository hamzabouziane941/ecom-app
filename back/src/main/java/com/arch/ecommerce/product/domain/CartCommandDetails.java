package com.arch.ecommerce.product.domain;

public record CartCommandDetails(Product product, Integer quantity) {}
