package com.arch.ecommerce.product.application;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.mapping.ProductMapper;
import com.arch.ecommerce.product.application.port.in.product.CreateProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.DeleteProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.PatchProductUseCase;
import com.arch.ecommerce.product.application.port.out.product.DeleteProductPort;
import com.arch.ecommerce.product.application.port.out.product.FindProductPort;
import com.arch.ecommerce.product.application.port.out.product.SavingProductPort;
import com.arch.ecommerce.product.domain.Product;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService
    implements CreateProductUseCase, FindProductUseCase, PatchProductUseCase, DeleteProductUseCase {

  private final SavingProductPort savingProductPort;
  private final FindProductPort findProductPort;
  private final DeleteProductPort deleteProductPort;

  @Transactional
  @Override
  public Product create(Product product) {
    product.setCreatedAt(LocalDateTime.now());
    return savingProductPort.save(product);
  }

  @Transactional
  @Override
  public List<Product> findAll() {
    return findProductPort.findAll();
  }

  @Transactional
  @Override
  public List<Product> findAllByIds(List<Long> ids) throws EntityNotFoundException {
    return findProductPort.findAllByIds(ids);
  }

  @Override
  public Product findById(Long id) throws EntityNotFoundException {
    return findProductPort.findById(id);
  }

  @Transactional
  @Override
  public Product patch(Long id, Product productPatch) throws EntityNotFoundException {
    Product product = findProductPort.findById(id);
    Mappers.getMapper(ProductMapper.class).mapNonNull(productPatch, product);
    return savingProductPort.save(product);
  }

  @Transactional
  @Override
  public void deleteProduct(Long id) throws EntityNotFoundException {
    deleteProductPort.delete(id);
  }
}
