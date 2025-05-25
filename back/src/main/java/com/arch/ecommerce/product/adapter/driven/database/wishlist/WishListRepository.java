package com.arch.ecommerce.product.adapter.driven.database.wishlist;

import com.arch.ecommerce.product.adapter.driven.database.wishlist.model.WishListEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Long> {

  Optional<WishListEntity> findByUsername(String username);
}
