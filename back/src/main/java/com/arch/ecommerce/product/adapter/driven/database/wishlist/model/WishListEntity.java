package com.arch.ecommerce.product.adapter.driven.database.wishlist.model;

import com.arch.ecommerce.product.adapter.driven.database.product.model.ProductEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wish_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @ManyToMany
  @JoinTable(
      name = "wish_list_products",
      joinColumns = @JoinColumn(name = "wish_list_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private Set<ProductEntity> products = new HashSet<>();
}
