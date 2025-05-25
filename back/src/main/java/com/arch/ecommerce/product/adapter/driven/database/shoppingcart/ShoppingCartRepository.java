package com.arch.ecommerce.product.adapter.driven.database.shoppingcart;

import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.ShoppingCartEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {

  @Query("SELECT DISTINCT sc FROM ShoppingCartEntity sc " +
          "LEFT JOIN FETCH sc.items " +
          "WHERE sc.username = :username")
  Optional<ShoppingCartEntity> findByUsername(String username);
}
