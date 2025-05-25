package com.arch.ecommerce.product.application.port.in.shoppingcart;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.domain.ShoppingCart;

public interface FindShoppingCartUseCase {

  ShoppingCart findUserShoppingCart() throws EntityNotFoundException;
}
