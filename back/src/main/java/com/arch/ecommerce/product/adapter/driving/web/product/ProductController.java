package com.arch.ecommerce.product.adapter.driving.web.product;

import com.arch.ecommerce.product.adapter.driving.web.product.dto.ProductDto;
import com.arch.ecommerce.product.adapter.driving.web.product.mapping.ProductDtoMapper;
import com.arch.ecommerce.product.application.exception.ProductCreationException;
import com.arch.ecommerce.product.application.port.in.product.CreateProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.DeleteProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.PatchProductUseCase;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

  private final ProductDtoMapper productDtoMapper = Mappers.getMapper(ProductDtoMapper.class);

  private final CreateProductUseCase createProductUseCase;
  private final PatchProductUseCase patchProductUseCase;
  private final FindProductUseCase findProductUseCase;
  private final DeleteProductUseCase deleteProductUseCase;

  @Override
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
      throws ProductCreationException {

    Product createdProduct = createProductUseCase.create(productDtoMapper.toDomain(productDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(productDtoMapper.toDto(createdProduct));
  }

  @Override
  public ResponseEntity<List<ProductDto>> getAllProducts() {
    List<ProductDto> productDtoList =
        findProductUseCase.findAll().stream().map(productDtoMapper::toDto).toList();
    return ResponseEntity.ok(productDtoList);
  }

  @Override
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long id)
      throws EntityNotFoundException {
    Product product = findProductUseCase.findById(id);
    return ResponseEntity.ok(productDtoMapper.toDto(product));
  }

  @Override
  public ResponseEntity<ProductDto> patchProduct(
      @PathVariable Long id, @RequestBody ProductDto productDto) throws EntityNotFoundException {
    Product patchedProduct = patchProductUseCase.patch(id, productDtoMapper.toDomain(productDto));
    return ResponseEntity.ok(productDtoMapper.toDto(patchedProduct));
  }

  @Override
  public ResponseEntity<Void> deleteProductById(Long id) throws EntityNotFoundException {
    deleteProductUseCase.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
