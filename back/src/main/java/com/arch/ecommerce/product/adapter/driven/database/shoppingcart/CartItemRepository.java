package com.arch.ecommerce.product.adapter.driven.database.shoppingcart;

import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {}
