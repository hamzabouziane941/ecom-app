package com.arch.ecommerce.product.adapter.driven.database.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.product.adapter.driven.database.product.ProductRepository;
import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.mapping.ShoppingCartEntityMapper;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.CartItemEntity;
import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.ShoppingCartEntity;
import com.arch.ecommerce.product.domain.CartItem;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.ShoppingCart;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

@ExtendWith(MockitoExtension.class)
class ShoppingCartDatabaseAdapterTest {

  private static final String USERNAME = "testUser";
  private static final Long CART_ID = 1L;
  private static final Long PRODUCT_ID = 1L;
  private static final int QUANTITY = 2;

  @InjectMocks private ShoppingCartDatabaseAdapter shoppingCartDatabaseAdapter;

  @Mock private ShoppingCartRepository shoppingCartRepository;
  @Mock private ProductRepository productRepository;
  @Mock private ShoppingCartEntityMapper shoppingCartEntityMapper;

  private MockedStatic<Mappers> mappersMockedStatic;
  private ShoppingCart shoppingCart;
  private ShoppingCartEntity shoppingCartEntity;
  private Product product;
  private ProductEntity productEntity;
  private CartItem cartItem;
  private CartItemEntity cartItemEntity;

  @BeforeEach
  void setUp() {
    mappersMockedStatic = mockStatic(Mappers.class);
    mappersMockedStatic.when(() -> Mappers.getMapper(ShoppingCartEntityMapper.class))
        .thenReturn(shoppingCartEntityMapper);

    product = new Product();
    product.setId(PRODUCT_ID);

    productEntity = new ProductEntity();
    productEntity.setId(PRODUCT_ID);

    cartItem = new CartItem(product, QUANTITY);
    cartItemEntity = new CartItemEntity();
    cartItemEntity.setProduct(productEntity);
    cartItemEntity.setQuantity(QUANTITY);

    Set<CartItem> cartItems = new HashSet<>();
    cartItems.add(cartItem);

    shoppingCart = ShoppingCart.builder().loggedInUser(USERNAME).build();
    shoppingCart.setId(CART_ID);
    shoppingCart.setItems(cartItems);

    shoppingCartEntity = new ShoppingCartEntity();
    shoppingCartEntity.setId(CART_ID);
    shoppingCartEntity.setUsername(USERNAME);
    shoppingCartEntity.getItems().add(cartItemEntity);
  }

  @AfterEach
  void tearDown() {
    mappersMockedStatic.close();
  }

  @Test
  void findByUsername_ShouldReturnShoppingCart_WhenExists() {
    when(shoppingCartRepository.findByUsername(USERNAME))
        .thenReturn(Optional.of(shoppingCartEntity));

    Optional<ShoppingCart> foundCart = shoppingCartDatabaseAdapter.findByUsername(USERNAME);

    assertTrue(foundCart.isPresent());
    assertEquals(USERNAME, foundCart.get().getLoggedInUser());
    assertEquals(CART_ID, foundCart.get().getId());
    verify(shoppingCartRepository).findByUsername(USERNAME);
  }

  @Test
  void findByUsername_ShouldReturnEmpty_WhenShoppingCartDoesNotExist() {
    when(shoppingCartRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

    Optional<ShoppingCart> foundCart = shoppingCartDatabaseAdapter.findByUsername(USERNAME);

    assertTrue(foundCart.isEmpty());
    verify(shoppingCartRepository).findByUsername(USERNAME);
  }

  @Test
  void save_ShouldSaveShoppingCart_WhenValidCartProvided() {
    when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
    when(shoppingCartRepository.save(any(ShoppingCartEntity.class)))
        .thenReturn(shoppingCartEntity);

    ShoppingCart savedCart = shoppingCartDatabaseAdapter.save(shoppingCart);

    assertNotNull(savedCart);
    assertEquals(USERNAME, savedCart.getLoggedInUser());
    assertEquals(CART_ID, savedCart.getId());
    verify(productRepository).save(any(ProductEntity.class));
    verify(shoppingCartRepository).save(any(ShoppingCartEntity.class));
  }

  @Test
  void save_ShouldHandleEmptyCartItems() {
    shoppingCart.setItems(new HashSet<>());
    when(shoppingCartRepository.save(any(ShoppingCartEntity.class)))
        .thenReturn(shoppingCartEntity);

    ShoppingCart savedCart = shoppingCartDatabaseAdapter.save(shoppingCart);

    assertNotNull(savedCart);
    verify(shoppingCartRepository).save(any(ShoppingCartEntity.class));
  }
} 