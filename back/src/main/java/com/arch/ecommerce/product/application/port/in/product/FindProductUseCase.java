package com.arch.ecommerce.product.application.port.in.product;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.domain.Product;
import java.util.List;

public interface FindProductUseCase {

  List<Product> findAll();

  List<Product> findAllByIds(List<Long> ids) throws EntityNotFoundException;

  Product findById(Long id) throws EntityNotFoundException;
}
