package com.arch.ecommerce.product.application.port.out.shoppingcart;

import com.arch.ecommerce.product.domain.ShoppingCart;

public interface SavingShoppingCartPort {

  ShoppingCart save(ShoppingCart shoppingCart) throws Exception;
}
