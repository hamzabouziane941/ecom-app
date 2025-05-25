package com.arch.ecommerce.product.application.port.out.wishlist;

import com.arch.ecommerce.product.domain.WishList;

public interface SaveWishListPort {

  WishList save(WishList wishList);
}
