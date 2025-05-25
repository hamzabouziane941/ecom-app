package com.arch.ecommerce.product.application.port.out.product;

import com.arch.ecommerce.product.domain.Product;
import java.util.List;

public interface FindProductPort {

  List<Product> findAll();

  Product findById(Long id);

  List<Product> findAllByIds(List<Long> ids);
}
