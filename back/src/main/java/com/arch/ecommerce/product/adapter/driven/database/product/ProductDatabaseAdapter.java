package com.arch.ecommerce.product.adapter.driven.database.product;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.adapter.driven.database.product.mapping.ProductEntityMapper;
import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import com.arch.ecommerce.product.application.exception.ProductCreationException;
import com.arch.ecommerce.product.application.port.out.product.DeleteProductPort;
import com.arch.ecommerce.product.application.port.out.product.FindProductPort;
import com.arch.ecommerce.product.application.port.out.product.SavingProductPort;
import com.arch.ecommerce.product.domain.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDatabaseAdapter
    implements SavingProductPort, FindProductPort, DeleteProductPort {

  private static final ProductEntityMapper productEntityMapper =
      Mappers.getMapper(ProductEntityMapper.class);
  private final ProductRepository productRepository;

  @Override
  public Product save(Product product) {
    try {
      ProductEntity createdProduct =
          productRepository.save(productEntityMapper.fromDomain(product));
      return productEntityMapper.toDomain(createdProduct);
    } catch (Exception e) {
      throw new ProductCreationException(e.getMessage(), e);
    }
  }

  @Override
  public Product findById(Long id) {
    return productRepository
        .findById(id)
        .map(productEntityMapper::toDomain)
        .orElseThrow(() -> buildEntityNotFoundException(id));
  }

  @Override
  public List<Product> findAll() {
    return productRepository.findAll().stream().map(productEntityMapper::toDomain).toList();
  }

  @Override
  public List<Product> findAllByIds(List<Long> ids) {
    return productRepository.findAllById(ids).stream().map(productEntityMapper::toDomain).toList();
  }

  @Override
  public void delete(Long id) throws EntityNotFoundException {
    ProductEntity productEntity =
        productRepository.findById(id).orElseThrow(() -> buildEntityNotFoundException(id));
    productRepository.delete(productEntity);
  }

  private EntityNotFoundException buildEntityNotFoundException(Long id) {
    return new EntityNotFoundException(String.format("Product not found with id: %s", id));
  }
}
