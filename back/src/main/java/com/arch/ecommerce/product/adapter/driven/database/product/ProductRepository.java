package com.arch.ecommerce.product.adapter.driven.database.product;

import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  @Override
  List<ProductEntity> findAllById(Iterable<Long> ids);
}
