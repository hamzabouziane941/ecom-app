package com.arch.ecommerce.product.adapter.driving.web.shoppingcart.mapping;

import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.CartItemDto;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.ShoppingCartDto;
import com.arch.ecommerce.product.domain.CartItem;
import com.arch.ecommerce.product.domain.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper
public interface ShoppingCartDtoMapper {
  ShoppingCartDto toDto(ShoppingCart shoppingCart);

  ShoppingCart toDomain(ShoppingCartDto shoppingCartDto);

  CartItemDto toDto(CartItem cartItem);

  CartItem toDomain(CartItemDto cartItemDto);
}
