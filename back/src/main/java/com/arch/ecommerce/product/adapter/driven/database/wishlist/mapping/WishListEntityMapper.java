package com.arch.ecommerce.product.adapter.driven.database.wishlist.mapping;

import com.arch.ecommerce.product.adapter.driven.database.wishlist.model.WishListEntity;
import com.arch.ecommerce.product.domain.WishList;
import org.mapstruct.Mapper;

@Mapper
public interface WishListEntityMapper {

  WishList toDomain(WishListEntity entity);

  WishListEntity toEntity(WishList wishList);
}
