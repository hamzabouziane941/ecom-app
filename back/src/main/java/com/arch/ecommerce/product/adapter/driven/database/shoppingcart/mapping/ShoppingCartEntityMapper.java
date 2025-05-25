package com.arch.ecommerce.product.adapter.driven.database.shoppingcart.mapping;

import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.CartItemEntity;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.ShoppingCartEntity;
import com.arch.ecommerce.product.domain.CartItem;
import com.arch.ecommerce.product.domain.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ShoppingCartEntityMapper {

  @Mappings({@Mapping(target = "username", source = "loggedInUser")})
  ShoppingCartEntity toEntity(ShoppingCart shoppingCart);

  @Mappings({@Mapping(target = "loggedInUser", source = "username")})
  ShoppingCart toDomain(ShoppingCartEntity shoppingCartEntity);

  CartItemEntity toItemEntity(CartItem shoppingCartItem);

  CartItem toItemDomain(CartItemEntity shoppingCartItemEntity);
}
