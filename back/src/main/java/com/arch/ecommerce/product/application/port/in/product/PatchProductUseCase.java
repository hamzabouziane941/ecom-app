package com.arch.ecommerce.product.application.port.in.product;

import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;

public interface PatchProductUseCase {

  Product patch(Long id, Product product) throws EntityNotFoundException;
}
