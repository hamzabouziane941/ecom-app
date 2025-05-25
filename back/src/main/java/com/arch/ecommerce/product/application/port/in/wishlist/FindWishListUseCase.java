package com.arch.ecommerce.product.application.port.in.wishlist;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.domain.WishList;

public interface FindWishListUseCase {

  WishList findByCurrentUser() throws EntityNotFoundException;
}
