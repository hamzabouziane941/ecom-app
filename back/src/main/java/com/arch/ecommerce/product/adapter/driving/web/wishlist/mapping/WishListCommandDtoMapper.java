package com.arch.ecommerce.product.adapter.driving.web.wishlist.mapping;

import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListCommandDto;
import com.arch.ecommerce.product.domain.WishListCommand;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper
public interface WishListCommandDtoMapper {

  WishListCommandDto toDto(WishListCommand command);
}
