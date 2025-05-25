package com.arch.ecommerce.product.adapter.driving.web.wishlist.dto;

import com.arch.ecommerce.product.adapter.driving.web.product.dto.ProductDto;
import java.util.List;

public record WishListDto(Long id, List<ProductDto> products) {}
