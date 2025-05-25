package com.arch.ecommerce.product.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CartItem {
  private Long id;
  private Product product;
  private Integer quantity;

  public CartItem(Product product, Integer quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public void addQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }
    this.quantity += quantity;
    this.product.decrementQuantity(quantity);
  }

  public void reduceQuantity(Integer quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }
    if (this.quantity < quantity) {
      this.quantity = 0;
    } else {
      this.quantity -= quantity;
      this.product.incrementQuantity(quantity);
    }
  }
}
