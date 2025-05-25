package com.arch.ecommerce.product.application.port.out.shoppingcart;

import com.arch.ecommerce.product.domain.ShoppingCart;

import java.util.Optional;

public interface FindShoppingCartPort {

    Optional<ShoppingCart> findByUsername(String username);
}
