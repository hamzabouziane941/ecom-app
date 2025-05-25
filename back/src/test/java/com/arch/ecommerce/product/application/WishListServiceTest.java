package com.arch.ecommerce.product.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.out.user.GetLoggedInUserPort;
import com.arch.ecommerce.product.application.port.out.wishlist.FindWishListPort;
import com.arch.ecommerce.product.application.port.out.wishlist.SaveWishListPort;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.WishList;
import com.arch.ecommerce.product.domain.WishListAction;
import com.arch.ecommerce.product.domain.WishListCommand;
import java.util.ArrayList;
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
class WishListServiceTest {
  private static final String USERNAME = "testUser";

  @InjectMocks private WishListService wishListService;

  @Mock private GetLoggedInUserPort getLoggedInUserPort;

  @Mock private FindWishListPort findWishListPort;

  @Mock private SaveWishListPort saveWishListPort;

  @Mock private FindProductUseCase findProductUseCase;

  private Product testProduct;
  private WishList testWishList;

  @BeforeEach
  void setUp() {
    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Test Product");
    testWishList = new WishList(USERNAME);
    lenient().when(getLoggedInUserPort.get()).thenReturn(USERNAME);
  }

  @Test
  void execute_ShouldAddProductsToNewWishList() {
    List<Long> productIds = Collections.singletonList(1L);
    List<Product> products = Collections.singletonList(testProduct);
    WishListCommand command = new WishListCommand(productIds, WishListAction.ADD_TO_WISHLIST);

    when(findProductUseCase.findAllByIds(productIds)).thenReturn(products);
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(saveWishListPort.save(any(WishList.class))).thenAnswer(i -> i.getArgument(0));

    WishList wishList = wishListService.execute(command);

    assertNotNull(wishList);
    assertEquals(USERNAME, wishList.getUsername());
    assertEquals(1, wishList.getProducts().size());
    assertEquals(testProduct.getId(), wishList.getProducts().get(0).getId());
  }

  @Test
  void execute_ShouldAddProductsToExistingWishList() {
    List<Long> productIds = Collections.singletonList(1L);
    List<Product> products = Collections.singletonList(testProduct);
    WishListCommand command = new WishListCommand(productIds, WishListAction.ADD_TO_WISHLIST);

    when(findProductUseCase.findAllByIds(productIds)).thenReturn(products);
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.of(testWishList));
    when(saveWishListPort.save(any(WishList.class))).thenAnswer(i -> i.getArgument(0));

    WishList wishList = wishListService.execute(command);

    assertNotNull(wishList);
    assertEquals(USERNAME, wishList.getUsername());
    assertEquals(1, wishList.getProducts().size());
    assertEquals(testProduct.getId(), wishList.getProducts().get(0).getId());
  }

  @Test
  void execute_ShouldRemoveProductsFromWishList() {
    List<Long> productIds = Collections.singletonList(1L);
    List<Product> products = new ArrayList<>(Collections.singletonList(testProduct));
    WishListCommand command = new WishListCommand(productIds, WishListAction.REMOVE_FROM_WISHLIST);
    testWishList.addProducts(products);

    when(findProductUseCase.findAllByIds(productIds)).thenReturn(products);
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.of(testWishList));
    when(saveWishListPort.save(any(WishList.class))).thenAnswer(i -> i.getArgument(0));

    WishList wishList = wishListService.execute(command);

    assertNotNull(wishList);
    assertEquals(USERNAME, wishList.getUsername());
    assertTrue(wishList.getProducts().isEmpty());
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenNoProductsFound() {
    List<Long> productIds = Collections.singletonList(1L);
    WishListCommand command = new WishListCommand(productIds, WishListAction.ADD_TO_WISHLIST);

    when(findProductUseCase.findAllByIds(productIds)).thenReturn(Collections.emptyList());

    assertThrows(BusinessException.class, () -> wishListService.execute(command));
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenInvalidAction() {
    List<Long> productIds = Collections.singletonList(1L);
    WishListCommand command = new WishListCommand(productIds, null);

    assertThrows(BusinessException.class, () -> wishListService.execute(command));
  }

  @Test
  void findByCurrentUser_ShouldReturnWishList_WhenExists() {
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.of(testWishList));

    WishList wishList = wishListService.findByCurrentUser();

    assertNotNull(wishList);
    assertEquals(USERNAME, wishList.getUsername());
  }

  @Test
  void findByCurrentUser_ShouldThrowEntityNotFoundException_WhenWishListDoesNotExist() {
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> wishListService.findByCurrentUser());
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenSaveFails() {
    List<Long> productIds = Collections.singletonList(1L);
    List<Product> products = Collections.singletonList(testProduct);
    WishListCommand command = new WishListCommand(productIds, WishListAction.ADD_TO_WISHLIST);

    when(findProductUseCase.findAllByIds(productIds)).thenReturn(products);
    when(findWishListPort.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(saveWishListPort.save(any(WishList.class))).thenThrow(new RuntimeException("Save failed"));

    assertThrows(BusinessException.class, () -> wishListService.execute(command));
  }
}
