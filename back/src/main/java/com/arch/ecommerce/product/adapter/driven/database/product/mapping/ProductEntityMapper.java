package com.arch.ecommerce.product.adapter.driven.database.product.mapping;

import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import com.arch.ecommerce.product.domain.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductEntityMapper {

  ProductEntity fromDomain(Product product);

  Product toDomain(ProductEntity productEntity);
}
