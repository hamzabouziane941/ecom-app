package com.arch.ecommerce.product.application.port.in.wishlist;

import com.arch.ecommerce.product.domain.WishList;
import com.arch.ecommerce.product.domain.WishListCommand;

public interface ExecuteWishListCommandUseCase {

  WishList execute(WishListCommand command) throws Exception;
}
