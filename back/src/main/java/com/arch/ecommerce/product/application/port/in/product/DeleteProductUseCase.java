package com.arch.ecommerce.product.application.port.in.product;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;

public interface DeleteProductUseCase {

  void deleteProduct(Long id) throws EntityNotFoundException;
}
