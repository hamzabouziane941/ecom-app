package com.arch.ecommerce.product.application;

import static java.lang.String.format;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.in.product.PatchProductUseCase;
import com.arch.ecommerce.product.application.port.in.shoppingcart.ExecuteShoppingCartCommandUseCase;
import com.arch.ecommerce.product.application.port.in.shoppingcart.FindShoppingCartUseCase;
import com.arch.ecommerce.product.application.port.out.shoppingcart.FindShoppingCartPort;
import com.arch.ecommerce.product.application.port.out.shoppingcart.SavingShoppingCartPort;
import com.arch.ecommerce.product.application.port.out.user.GetLoggedInUserPort;
import com.arch.ecommerce.product.domain.CartAction;
import com.arch.ecommerce.product.domain.CartCommandDetails;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.ShoppingCart;
import com.arch.ecommerce.product.domain.ShoppingCartCommand;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingShoppingCartService
    implements ExecuteShoppingCartCommandUseCase, FindShoppingCartUseCase {

  private final SavingShoppingCartPort savingShoppingCartPort;
  private final FindShoppingCartPort findShoppingCartPort;
  private final GetLoggedInUserPort getLoggedInUserPort;
  private final FindProductUseCase findProductUseCase;
  private final PatchProductUseCase patchProductUseCase;

  @Transactional
  @Override
  public ShoppingCart execute(ShoppingCartCommand shoppingCartCommand) {
    Product product = findProductUseCase.findById(shoppingCartCommand.getProductId());

    if (CartAction.ADD_TO_CART.equals(shoppingCartCommand.getAction())) {
      Optional<ShoppingCart> shoppingCart =
          findShoppingCartPort.findByUsername(getLoggedInUserPort.get());
      product.decrementQuantity(shoppingCartCommand.getQuantity());
      patchProductUseCase.patch(product.getId(), product);
      return shoppingCart
          .map(cart -> addCartItemToShoppingCart(shoppingCartCommand, cart, product))
          .orElseGet(() -> createNewShoppingCardWithItem(shoppingCartCommand, product));
    }

    if (CartAction.REMOVE_FROM_CART.equals(shoppingCartCommand.getAction())) {
      Optional<ShoppingCart> shoppingCart =
          findShoppingCartPort.findByUsername(getLoggedInUserPort.get());
      if (shoppingCart.isPresent()) {
        return removeItemFromShoppingCart(shoppingCartCommand, shoppingCart.get(), product);
      } else {
        throw new BusinessException(
            format("Shopping cart for user '%s' not found", getLoggedInUserPort.get()));
      }
    }
    throw new BusinessException(
        format("No action defined for product with code '%s'", product.getCode()));
  }

  @Transactional
  @Override
  public ShoppingCart findUserShoppingCart() throws EntityNotFoundException {
    return findShoppingCartPort
        .findByUsername(getLoggedInUserPort.get())
        .map(
            shoppingCart -> {
              shoppingCart.calculateTotalPrice();
              return shoppingCart;
            })
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    format("Shopping cart for user '%s' not found", getLoggedInUserPort.get())));
  }

  private ShoppingCart createNewShoppingCardWithItem(
      ShoppingCartCommand shoppingCartCommand, Product product) {
    ShoppingCart newShoppingCart =
        ShoppingCart.builder().loggedInUser(getLoggedInUserPort.get()).build();
    CartCommandDetails cartCommandDetails =
        new CartCommandDetails(product, shoppingCartCommand.getQuantity());
    newShoppingCart.addItem(cartCommandDetails);
    return saveShoppingCart(newShoppingCart);
  }

  private ShoppingCart addCartItemToShoppingCart(
      ShoppingCartCommand shoppingCartCommand, ShoppingCart shoppingCart, Product product) {
    CartCommandDetails cartCommandDetails =
        new CartCommandDetails(product, shoppingCartCommand.getQuantity());
    shoppingCart.addItem(cartCommandDetails);
    return saveShoppingCart(shoppingCart);
  }

  private ShoppingCart removeItemFromShoppingCart(
      ShoppingCartCommand shoppingCartCommand, ShoppingCart shoppingCart, Product product) {
    CartCommandDetails cartCommandDetails =
        new CartCommandDetails(product, shoppingCartCommand.getQuantity());
    shoppingCart.removeItem(cartCommandDetails);
    return saveShoppingCart(shoppingCart);
  }

  private ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
    try {
      ShoppingCart savedShoppingCart = savingShoppingCartPort.save(shoppingCart);
      savedShoppingCart.calculateTotalPrice();
      return savedShoppingCart;
    } catch (Exception e) {
      throw new BusinessException("Shopping card update failed", e);
    }
  }
}
