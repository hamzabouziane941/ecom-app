package com.arch.ecommerce.product.application.port.out.product;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;

public interface DeleteProductPort {

  void delete(Long id) throws EntityNotFoundException;
}
