package com.arch.ecommerce.product.application.port.out.product;

import com.arch.ecommerce.product.application.exception.ProductCreationException;
import com.arch.ecommerce.product.domain.Product;

public interface SavingProductPort {

  Product save(Product product) throws ProductCreationException;
}
