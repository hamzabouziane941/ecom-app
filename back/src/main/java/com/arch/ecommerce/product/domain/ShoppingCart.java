package com.arch.ecommerce.product.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingCart {
  private Long id;
  private BigDecimal totalPrice;
  private String loggedInUser;
  private LocalDateTime lastUpdated;
  @Builder.Default private Set<CartItem> items = new HashSet<>();

  public void addItem(CartCommandDetails cartCommandDetails) {
    Product product = cartCommandDetails.product();
    int quantity = cartCommandDetails.quantity();
    items.stream()
        .filter(item -> item.getProduct().getId().equals(product.getId()))
        .findFirst()
        .ifPresentOrElse(
            item -> item.addQuantity(quantity), () -> items.add(new CartItem(product, quantity)));
  }

  public void removeItem(CartCommandDetails cartCommandDetails) {
    items.stream()
        .filter(item -> item.getProduct().getId().equals(cartCommandDetails.product().getId()))
        .findFirst()
        .ifPresent(
            item -> {
              if (item.getQuantity() > cartCommandDetails.quantity()) {
                item.reduceQuantity(cartCommandDetails.quantity());
              } else {
                item.reduceQuantity(item.getQuantity());
                items.removeIf(
                    cartItem -> cartItem.getProduct().getId().equals(item.getProduct().getId()));
              }
            });
  }

  public void calculateTotalPrice() {
    totalPrice =
        items.stream()
            .map(
                item ->
                    item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
