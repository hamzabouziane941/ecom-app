package com.arch.ecommerce.product.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.exception.ProductCreationException;
import com.arch.ecommerce.product.application.port.out.product.DeleteProductPort;
import com.arch.ecommerce.product.application.port.out.product.FindProductPort;
import com.arch.ecommerce.product.application.port.out.product.SavingProductPort;
import com.arch.ecommerce.product.domain.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks private ProductService productService;

  @Mock private SavingProductPort savingProductPort;

  @Mock private FindProductPort findProductPort;

  @Mock private DeleteProductPort deleteProductPort;

  private Product testProduct;

  @BeforeEach
  void setUp() {
    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Test Product");
    testProduct.setDescription("Test Description");
    testProduct.setPrice(BigDecimal.valueOf(99.99));
  }

  @Test
  void create_ShouldCreateProductSuccessfully() {
    when(savingProductPort.save(any(Product.class))).thenReturn(testProduct);

    Product result = productService.create(testProduct);

    assertNotNull(result);
    assertEquals(testProduct.getId(), result.getId());
    assertEquals(testProduct.getName(), result.getName());
    assertNotNull(result.getCreatedAt());
    verify(savingProductPort).save(any(Product.class));
  }

  @Test
  void create_ShouldThrowProductCreationException_WhenSaveFails() {
    when(savingProductPort.save(any(Product.class)))
        .thenThrow(new ProductCreationException("Save failed", new RuntimeException()));

    assertThrows(ProductCreationException.class, () -> productService.create(testProduct));
  }

  @Test
  void findAll_ShouldReturnAllProducts() {
    List<Product> expectedProducts = Arrays.asList(testProduct);
    when(findProductPort.findAll()).thenReturn(expectedProducts);

    List<Product> result = productService.findAll();

    assertNotNull(result);
    assertEquals(expectedProducts.size(), result.size());
    assertEquals(expectedProducts.get(0).getId(), result.get(0).getId());
  }

  @Test
  void findAllByIds_ShouldReturnProductsByIds() {
    List<Long> ids = Arrays.asList(1L, 2L);
    List<Product> expectedProducts = Arrays.asList(testProduct);
    when(findProductPort.findAllByIds(ids)).thenReturn(expectedProducts);

    List<Product> result = productService.findAllByIds(ids);

    assertNotNull(result);
    assertEquals(expectedProducts.size(), result.size());
    assertEquals(expectedProducts.get(0).getId(), result.get(0).getId());
  }

  @Test
  void findById_ShouldReturnProduct_WhenExists() {
    when(findProductPort.findById(1L)).thenReturn(testProduct);

    Product result = productService.findById(1L);

    assertNotNull(result);
    assertEquals(testProduct.getId(), result.getId());
    assertEquals(testProduct.getName(), result.getName());
  }

  @Test
  void findById_ShouldThrowEntityNotFoundException_WhenProductDoesNotExist() {
    when(findProductPort.findById(1L)).thenThrow(new EntityNotFoundException("Product not found"));

    assertThrows(EntityNotFoundException.class, () -> productService.findById(1L));
  }

  @Test
  void patch_ShouldUpdateProductSuccessfully() {
    Product patchProduct = new Product();
    patchProduct.setName("Updated Name");
    patchProduct.setDescription("Updated Description");

    when(findProductPort.findById(1L)).thenReturn(testProduct);
    when(savingProductPort.save(any(Product.class))).thenReturn(testProduct);

    Product result = productService.patch(1L, patchProduct);

    assertNotNull(result);
    verify(findProductPort).findById(1L);
    verify(savingProductPort).save(any(Product.class));
  }

  @Test
  void patch_ShouldThrowEntityNotFoundException_WhenProductDoesNotExist() {
    Product patchProduct = new Product();
    when(findProductPort.findById(1L)).thenThrow(new EntityNotFoundException("Product not found"));

    assertThrows(EntityNotFoundException.class, () -> productService.patch(1L, patchProduct));
  }

  @Test
  void deleteProduct_ShouldDeleteProductSuccessfully() {
    doNothing().when(deleteProductPort).delete(1L);

    productService.deleteProduct(1L);

    verify(deleteProductPort).delete(1L);
  }

  @Test
  void deleteProduct_ShouldThrowEntityNotFoundException_WhenProductDoesNotExist() {
    doThrow(new EntityNotFoundException("Product not found")).when(deleteProductPort).delete(1L);

    assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(1L));
  }
}
