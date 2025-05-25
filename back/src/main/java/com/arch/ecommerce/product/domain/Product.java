package com.arch.ecommerce.product.domain;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Product {
  private Long id;
  private String code;
  private String name;
  private String description;
  private String imageUrl;
  private String category;
  private BigDecimal price;
  private int quantity;
  private String internalReference;
  private long ShellId;
  private InventoryStatus inventoryStatus;
  private BigDecimal rating;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public void decrementQuantity(int amount) {
    if (amount <= 0) {
      throw new BusinessException("Amount must be greater than zero");
    }
    if (quantity < amount) {
      throw new BusinessException("Insufficient quantity available");
    }
    quantity -= amount;
  }

  public void incrementQuantity(int amount) {
    if (amount <= 0) {
      throw new BusinessException("Amount must be greater than zero");
    }
    quantity += amount;
  }
}
