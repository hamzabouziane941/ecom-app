package com.arch.ecommerce.product.application.port.in.product;

import com.arch.ecommerce.product.domain.Product;

public interface CreateProductUseCase {

  Product create(Product product);
}
