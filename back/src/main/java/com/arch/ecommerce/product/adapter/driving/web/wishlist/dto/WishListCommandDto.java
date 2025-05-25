package com.arch.ecommerce.product.adapter.driving.web.wishlist.dto;

import java.util.List;

public record WishListCommandDto(List<Long> productIds, WishListActionDto action) {}
