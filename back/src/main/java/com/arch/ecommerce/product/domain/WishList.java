package com.arch.ecommerce.product.domain;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Getter
@Setter
@EqualsAndHashCode
public class WishList {

  private Long id;

  private String username;

  private List<Product> products;

  public WishList(String username) {
    this.username = username;
  }

  public void addProducts(List<Product> products) {
    if (CollectionUtils.isEmpty(this.products)) {
      this.products = products;
    } else {
      this.products.addAll(products);
    }
  }

  public void removeProducts(List<Product> products) {
    if (!CollectionUtils.isEmpty(this.products)) {
      this.products.removeAll(products);
    }
  }
}
