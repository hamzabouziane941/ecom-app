package com.arch.ecommerce.product.adapter.driven.database.shoppingcart;

import com.arch.ecommerce.product.adapter.driven.database.product.ProductRepository;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.mapping.ShoppingCartEntityMapper;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.ShoppingCartEntity;
import com.arch.ecommerce.product.application.port.out.shoppingcart.FindShoppingCartPort;
import com.arch.ecommerce.product.application.port.out.shoppingcart.SavingShoppingCartPort;
import com.arch.ecommerce.product.domain.ShoppingCart;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartDatabaseAdapter implements SavingShoppingCartPort, FindShoppingCartPort {

  private static final ShoppingCartEntityMapper SHOPPING_CART_ENTITY_MAPPER =
      Mappers.getMapper(ShoppingCartEntityMapper.class);

  private final ShoppingCartRepository shoppingCartRepository;
  private final ProductRepository productRepository;

  @Transactional
  @Override
  public ShoppingCart save(ShoppingCart shoppingCart) {
    ShoppingCartEntity shoppingCartEntity = SHOPPING_CART_ENTITY_MAPPER.toEntity(shoppingCart);

    ShoppingCartEntity savedShoppingCartEntity = shoppingCartRepository.save(shoppingCartEntity);

    shoppingCartEntity
        .getItems()
        .forEach(
            cartItemEntity -> {
              cartItemEntity.setCart(shoppingCartEntity);
              cartItemEntity.getProduct().addCartItem(cartItemEntity);
              productRepository.save(cartItemEntity.getProduct());
            });

    return SHOPPING_CART_ENTITY_MAPPER.toDomain(savedShoppingCartEntity);
  }

  @Override
  public Optional<ShoppingCart> findByUsername(String username) {
    return shoppingCartRepository
        .findByUsername(username)
        .map(SHOPPING_CART_ENTITY_MAPPER::toDomain);
  }
}
