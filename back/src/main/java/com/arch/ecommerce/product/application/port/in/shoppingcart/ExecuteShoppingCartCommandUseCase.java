package com.arch.ecommerce.product.application.port.in.shoppingcart;

import com.arch.ecommerce.product.domain.ShoppingCartCommand;
import com.arch.ecommerce.product.domain.ShoppingCart;

public interface ExecuteShoppingCartCommandUseCase {

  ShoppingCart execute(ShoppingCartCommand shoppingCartCommand);
}
