package com.arch.ecommerce.product.adapter.driving.web.product.dto;

import java.time.LocalDateTime;

public record ProductDto(
    Long id,
    String code,
    String name,
    String description,
    String imageUrl,
    String category,
    Double price,
    Integer quantity,
    String internalReference,
    Long shellId,
    String inventoryStatus,
    Double rating,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
