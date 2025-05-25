package com.arch.ecommerce.product.adapter.driven.database.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import com.arch.ecommerce.product.domain.Product;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductDatabaseAdapterTest {

  private static final Long PRODUCT_ID = 1L;
  private static final String PRODUCT_NAME = "Test Product";
  private static final String PRODUCT_CODE = "TEST-001";
  private static final String PRODUCT_DESCRIPTION = "Test Description";
  private static final String PRODUCT_CATEGORY = "Test Category";
  private static final int PRODUCT_QUANTITY = 10;

  @InjectMocks private ProductDatabaseAdapter productDatabaseAdapter;

  @Mock private ProductRepository productRepository;
  private Product product;
  private ProductEntity productEntity;

  @BeforeEach
  void setUp() {

    product = new Product();
    product.setId(PRODUCT_ID);
    product.setName(PRODUCT_NAME);
    product.setCode(PRODUCT_CODE);
    product.setDescription(PRODUCT_DESCRIPTION);
    product.setCategory(PRODUCT_CATEGORY);
    product.setQuantity(PRODUCT_QUANTITY);

    productEntity = new ProductEntity();
    productEntity.setId(PRODUCT_ID);
    productEntity.setName(PRODUCT_NAME);
    productEntity.setCode(PRODUCT_CODE);
    productEntity.setDescription(PRODUCT_DESCRIPTION);
    productEntity.setCategory(PRODUCT_CATEGORY);
    productEntity.setQuantity(PRODUCT_QUANTITY);
  }

  @Test
  void save_ShouldSaveProduct_WhenValidProductProvided() {
    when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

    Product savedProduct = productDatabaseAdapter.save(product);

    assertNotNull(savedProduct);
    assertEquals(PRODUCT_ID, savedProduct.getId());
    assertEquals(PRODUCT_NAME, savedProduct.getName());
    verify(productRepository).save(any(ProductEntity.class));
  }

  @Test
  void findById_ShouldReturnProduct_WhenProductExists() {
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntity));

    Product foundProduct = productDatabaseAdapter.findById(PRODUCT_ID);

    assertNotNull(foundProduct);
    assertEquals(PRODUCT_ID, foundProduct.getId());
    assertEquals(PRODUCT_NAME, foundProduct.getName());
    verify(productRepository).findById(PRODUCT_ID);
  }

  @Test
  void findById_ShouldThrowException_WhenProductDoesNotExist() {
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> productDatabaseAdapter.findById(PRODUCT_ID));
    verify(productRepository).findById(PRODUCT_ID);
  }

  @Test
  void findAll_ShouldReturnAllProducts() {
    List<ProductEntity> productEntities = Arrays.asList(productEntity);
    when(productRepository.findAll()).thenReturn(productEntities);

    List<Product> foundProducts = productDatabaseAdapter.findAll();

    assertNotNull(foundProducts);
    assertEquals(1, foundProducts.size());
    assertEquals(PRODUCT_ID, foundProducts.get(0).getId());
    verify(productRepository).findAll();
  }

  @Test
  void findAllByIds_ShouldReturnProducts_WhenProductsExist() {
    List<Long> productIds = List.of(PRODUCT_ID);
    List<ProductEntity> productEntities = Collections.singletonList(productEntity);
    when(productRepository.findAllById(productIds)).thenReturn(productEntities);

    List<Product> foundProducts = productDatabaseAdapter.findAllByIds(productIds);

    assertNotNull(foundProducts);
    assertEquals(1, foundProducts.size());
    assertEquals(PRODUCT_ID, foundProducts.get(0).getId());
    verify(productRepository).findAllById(productIds);
  }

  @Test
  void delete_ShouldDeleteProduct_WhenProductExists() {
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntity));

    productDatabaseAdapter.delete(PRODUCT_ID);

    verify(productRepository).delete(productEntity);
  }

  @Test
  void delete_ShouldThrowException_WhenProductDoesNotExist() {
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> productDatabaseAdapter.delete(PRODUCT_ID));
    verify(productRepository).findById(PRODUCT_ID);
    verify(productRepository, never()).deleteById(any());
  }
}
