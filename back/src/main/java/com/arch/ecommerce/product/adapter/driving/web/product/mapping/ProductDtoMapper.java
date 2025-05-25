package com.arch.ecommerce.product.adapter.driving.web.product.mapping;

import com.arch.ecommerce.product.adapter.driving.web.product.dto.ProductDto;
import com.arch.ecommerce.product.domain.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductDtoMapper {

  ProductDto toDto(Product product);

  Product toDomain(ProductDto productDto);
}
