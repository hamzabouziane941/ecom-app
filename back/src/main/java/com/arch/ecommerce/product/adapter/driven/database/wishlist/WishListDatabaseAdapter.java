package com.arch.ecommerce.product.adapter.driven.database.wishlist;

import com.arch.ecommerce.product.adapter.driven.database.wishlist.mapping.WishListEntityMapper;
import com.arch.ecommerce.product.adapter.driven.database.wishlist.model.WishListEntity;
import com.arch.ecommerce.product.application.port.out.wishlist.FindWishListPort;
import com.arch.ecommerce.product.application.port.out.wishlist.SaveWishListPort;
import com.arch.ecommerce.product.domain.WishList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishListDatabaseAdapter implements FindWishListPort, SaveWishListPort {

  private final WishListRepository wishListRepository;
  private static final WishListEntityMapper wishListEntityMapper =
      Mappers.getMapper(WishListEntityMapper.class);

  @Override
  public Optional<WishList> findByUsername(String username) {
    Optional<WishListEntity> wishListEntity = wishListRepository.findByUsername(username);
    return wishListEntity.map(wishListEntityMapper::toDomain);
  }

  @Override
  public WishList save(WishList wishList) {
    WishListEntity wishListEntity = wishListEntityMapper.toEntity(wishList);
    wishListEntity
        .getProducts()
        .forEach(productEntity -> productEntity.addWishList(wishListEntity));
    WishListEntity savedWithListEntity = wishListRepository.save(wishListEntity);
    return wishListEntityMapper.toDomain(savedWithListEntity);
  }
}
