package com.arch.ecommerce.product.application.port.out.wishlist;

import com.arch.ecommerce.product.domain.WishList;

import java.util.Optional;

public interface FindWishListPort {

  Optional<WishList> findByUsername(String username);
}
