package com.arch.ecommerce.product.adapter.driving.web.wishlist.mapping;

import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListDto;
import com.arch.ecommerce.product.domain.WishList;
import org.mapstruct.Mapper;

@Mapper
public interface WishListDtoMapper {

  WishListDto toDto(WishList wishList);

  WishList toDomain(WishListDto wishListDto);
}
