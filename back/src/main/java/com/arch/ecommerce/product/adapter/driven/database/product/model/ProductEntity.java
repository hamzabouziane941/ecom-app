package com.arch.ecommerce.product.adapter.driven.database.product.model;

import com.arch.ecommerce.product.adapter.driven.database.shoppingcart.model.CartItemEntity;
import com.arch.ecommerce.product.adapter.driven.database.wishlist.model.WishListEntity;
import com.arch.ecommerce.product.domain.InventoryStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Setter
@Getter
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = 500)
  private String description;

  private String imageUrl;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private BigDecimal price;

  private int quantity;

  @Column(nullable = false, unique = true)
  private String internalReference;

  private long ShellId;
  private InventoryStatus inventoryStatus;
  private BigDecimal rating;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CartItemEntity> cartItems;

  @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
  private Set<WishListEntity> wishLists = new HashSet<>();

  public void addWishList(WishListEntity wishList) {
    if (wishLists == null) {
      wishLists = new HashSet<>();
    }
    wishLists.add(wishList);
  }

  public void addCartItem(CartItemEntity cartItem) {
    if (cartItems == null) {
      cartItems = new HashSet<>();
    }
    cartItems.add(cartItem);
  }
}
