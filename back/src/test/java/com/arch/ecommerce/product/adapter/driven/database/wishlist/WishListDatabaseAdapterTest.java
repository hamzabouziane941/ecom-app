package com.arch.ecommerce.product.adapter.driven.database.wishlist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arch.ecommerce.product.adapter.driven.database.wishlist.mapping.WishListEntityMapper;
import com.arch.ecommerce.product.adapter.driven.database.wishlist.model.WishListEntity;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.WishList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
class WishListDatabaseAdapterTest {

  private static final String USERNAME = "testUser";
  private static final Long WISHLIST_ID = 1L;
  private static final Long PRODUCT_ID = 1L;

  @InjectMocks private WishListDatabaseAdapter wishListDatabaseAdapter;

  @Mock private WishListRepository wishListRepository;

  private WishList wishList;
  private WishListEntity wishListEntity;

  @BeforeEach
  void setUp() {
    Product product = new Product();
    product.setId(PRODUCT_ID);

    List<Product> products = new ArrayList<>();
    products.add(product);

    wishList = new WishList(USERNAME);
    wishList.setId(WISHLIST_ID);
    wishList.setProducts(products);

    wishListEntity = new WishListEntity();
    wishListEntity.setId(WISHLIST_ID);
    wishListEntity.setUsername(USERNAME);
  }


  @Test
  void findByUsername_ShouldReturnWishList_WhenExists() {
    when(wishListRepository.findByUsername(USERNAME)).thenReturn(Optional.of(wishListEntity));

    Optional<WishList> foundWishList = wishListDatabaseAdapter.findByUsername(USERNAME);

    assertTrue(foundWishList.isPresent());
    assertEquals(USERNAME, foundWishList.get().getUsername());
    assertEquals(WISHLIST_ID, foundWishList.get().getId());
    verify(wishListRepository).findByUsername(USERNAME);
  }

  @Test
  void findByUsername_ShouldReturnEmpty_WhenWishListDoesNotExist() {
    when(wishListRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

    Optional<WishList> foundWishList = wishListDatabaseAdapter.findByUsername(USERNAME);

    assertTrue(foundWishList.isEmpty());
    verify(wishListRepository).findByUsername(USERNAME);
  }

  @Test
  void save_ShouldSaveWishList_WhenValidWishListProvided() {
    when(wishListRepository.save(any(WishListEntity.class))).thenReturn(wishListEntity);

    WishList savedWishList = wishListDatabaseAdapter.save(wishList);

    assertNotNull(savedWishList);
    assertEquals(USERNAME, savedWishList.getUsername());
    assertEquals(WISHLIST_ID, savedWishList.getId());
    verify(wishListRepository).save(any(WishListEntity.class));
  }

  @Test
  void save_ShouldHandleEmptyProductsList() {
    wishList.setProducts(new ArrayList<>());
    when(wishListRepository.save(any(WishListEntity.class))).thenReturn(wishListEntity);

    WishList savedWishList = wishListDatabaseAdapter.save(wishList);

    assertNotNull(savedWishList);
    assertTrue(savedWishList.getProducts().isEmpty());
    verify(wishListRepository).save(any(WishListEntity.class));
  }
}
