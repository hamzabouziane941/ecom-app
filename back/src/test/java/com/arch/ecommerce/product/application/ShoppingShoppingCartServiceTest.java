package com.arch.ecommerce.product.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.PatchProductUseCase;
import com.arch.ecommerce.product.application.port.out.shoppingcart.FindShoppingCartPort;
import com.arch.ecommerce.product.application.port.out.shoppingcart.SavingShoppingCartPort;
import com.arch.ecommerce.product.application.port.out.user.GetLoggedInUserPort;
import com.arch.ecommerce.product.domain.CartAction;
import com.arch.ecommerce.product.domain.CartItem;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.ShoppingCart;
import com.arch.ecommerce.product.domain.ShoppingCartCommand;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingShoppingCartServiceTest {

  private static final String USERNAME = "testUser";
  private static final Long PRODUCT_ID = 1L;
  private static final String PRODUCT_CODE = "TEST-001";
  private static final int QUANTITY = 2;

  @InjectMocks private ShoppingShoppingCartService shoppingCartService;

  @Mock private SavingShoppingCartPort savingShoppingCartPort;
  @Mock private FindShoppingCartPort findShoppingCartPort;
  @Mock private GetLoggedInUserPort getLoggedInUserPort;
  @Mock private FindProductUseCase findProductUseCase;
  @Mock private PatchProductUseCase patchProductUseCase;

  private Product testProduct;
  private ShoppingCart testShoppingCart;
  private ShoppingCartCommand addToCartCommand;
  private ShoppingCartCommand removeFromCartCommand;

  @BeforeEach
  void setUp() {
    testProduct = new Product();
    testProduct.setId(PRODUCT_ID);
    testProduct.setCode(PRODUCT_CODE);
    testProduct.setPrice(BigDecimal.valueOf(100));
    testProduct.setQuantity(10);

    testShoppingCart = ShoppingCart.builder().loggedInUser(USERNAME).build();

    addToCartCommand = new ShoppingCartCommand(PRODUCT_ID, QUANTITY, CartAction.ADD_TO_CART);
    removeFromCartCommand =
        new ShoppingCartCommand(PRODUCT_ID, QUANTITY, CartAction.REMOVE_FROM_CART);

    lenient().when(getLoggedInUserPort.get()).thenReturn(USERNAME);
    lenient().when(findProductUseCase.findById(PRODUCT_ID)).thenReturn(testProduct);
  }

  @Test
  void execute_ShouldCreateNewShoppingCart_WhenAddingItemAndNoCartExists() throws Exception {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(savingShoppingCartPort.save(any(ShoppingCart.class)))
        .thenAnswer(answer -> answer.getArgument(0));

    ShoppingCart createdCart = shoppingCartService.execute(addToCartCommand);

    assertNotNull(createdCart);
    assertEquals(USERNAME, createdCart.getLoggedInUser());
    assertEquals(1, createdCart.getItems().size());
    CartItem firstItem = createdCart.getItems().iterator().next();
    assertEquals(QUANTITY, firstItem.getQuantity());
    verify(patchProductUseCase).patch(PRODUCT_ID, testProduct);
  }

  @Test
  void execute_ShouldAddItemToExistingCart_WhenAddingItem() throws Exception {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.of(testShoppingCart));
    when(savingShoppingCartPort.save(any(ShoppingCart.class)))
        .thenAnswer(answer -> answer.getArgument(0));

    ShoppingCart updatedCart = shoppingCartService.execute(addToCartCommand);

    assertNotNull(updatedCart);
    assertEquals(1, updatedCart.getItems().size());
    CartItem firstItem = updatedCart.getItems().iterator().next();
    assertEquals(QUANTITY, firstItem.getQuantity());
    verify(patchProductUseCase).patch(PRODUCT_ID, testProduct);
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenRemovingItemAndNoCartExists() {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.empty());

    assertThrows(BusinessException.class, () -> shoppingCartService.execute(removeFromCartCommand));
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenInvalidAction() {
    ShoppingCartCommand invalidCommand = new ShoppingCartCommand(PRODUCT_ID, QUANTITY, null);

    assertThrows(BusinessException.class, () -> shoppingCartService.execute(invalidCommand));
  }

  @Test
  void execute_ShouldThrowBusinessException_WhenSaveFails() throws Exception {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(savingShoppingCartPort.save(any(ShoppingCart.class)))
        .thenThrow(new RuntimeException("Save failed"));

    assertThrows(BusinessException.class, () -> shoppingCartService.execute(addToCartCommand));
  }

  @Test
  void findUserShoppingCart_ShouldReturnCart_WhenExists() {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.of(testShoppingCart));

    ShoppingCart foundCart = shoppingCartService.findUserShoppingCart();

    assertNotNull(foundCart);
    assertEquals(USERNAME, foundCart.getLoggedInUser());
  }

  @Test
  void findUserShoppingCart_ShouldThrowEntityNotFoundException_WhenCartDoesNotExist() {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shoppingCartService.findUserShoppingCart());
  }

  @Test
  void execute_ShouldAddProductQuantityToCart_WhenAddingToCart() throws Exception {
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(savingShoppingCartPort.save(any())).thenReturn(testShoppingCart);

    shoppingCartService.execute(addToCartCommand);

    assertEquals(8, testProduct.getQuantity());
    verify(patchProductUseCase).patch(PRODUCT_ID, testProduct);
  }

  @Test
  void execute_ShouldRemoveProductQuantityFromCart_WhenRemovingFromCart() throws Exception {

    CartItem cartItem = new CartItem(testProduct, QUANTITY);
    Set<CartItem> items = new HashSet<>();
    items.add(cartItem);
    testShoppingCart.setItems(items);
    when(findShoppingCartPort.findByUsername(USERNAME)).thenReturn(Optional.of(testShoppingCart));
    when(savingShoppingCartPort.save(any(ShoppingCart.class))).thenReturn(testShoppingCart);

    shoppingCartService.execute(removeFromCartCommand);

    assertEquals(12, testProduct.getQuantity());
  }
}
