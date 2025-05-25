package com.arch.ecommerce.product.application;

import static java.lang.String.format;

import com.arch.ecommerce.errormanagement.exception.BusinessException;
import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.application.port.in.product.FindProductUseCase;
import com.arch.ecommerce.product.application.port.in.wishlist.ExecuteWishListCommandUseCase;
import com.arch.ecommerce.product.application.port.in.wishlist.FindWishListUseCase;
import com.arch.ecommerce.product.application.port.out.user.GetLoggedInUserPort;
import com.arch.ecommerce.product.application.port.out.wishlist.FindWishListPort;
import com.arch.ecommerce.product.application.port.out.wishlist.SaveWishListPort;
import com.arch.ecommerce.product.domain.Product;
import com.arch.ecommerce.product.domain.WishList;
import com.arch.ecommerce.product.domain.WishListAction;
import com.arch.ecommerce.product.domain.WishListCommand;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class WishListService implements ExecuteWishListCommandUseCase, FindWishListUseCase {

  private final GetLoggedInUserPort getLoggedInUserPort;
  private final FindWishListPort findWishListPort;
  private final SaveWishListPort saveWishListPort;
  private final FindProductUseCase findProductUseCase;

  @Transactional
  @Override
  public WishList execute(WishListCommand command) {
    List<Product> products = findProductsWithIds(command.getProductIds());
    WishList wishList = getWithListOrBuildNewOne();
    if (WishListAction.ADD_TO_WISHLIST.equals(command.getAction())) {
      wishList.addProducts(products);
    } else if (WishListAction.REMOVE_FROM_WISHLIST.equals(command.getAction())) {
      wishList.removeProducts(products);
    } else {
      throw new BusinessException(format("Unsupported action: %s", command.getAction()));
    }
    return saveWishList(wishList);
  }

  @Transactional
  @Override
  public WishList findByCurrentUser() throws EntityNotFoundException {
    return findWishListPort
        .findByUsername(getLoggedInUserPort.get())
        .orElseThrow(() -> new EntityNotFoundException("Wish list not found for the current user"));
  }

  private List<Product> findProductsWithIds(List<Long> productIds) {
    List<Product> products = findProductUseCase.findAllByIds(productIds);
    if (CollectionUtils.isEmpty(products)) {
      throw new BusinessException(format("No products found for the provided IDs: %s", productIds));
    }
    return products;
  }

  private WishList getWithListOrBuildNewOne() {
    String loggedInUsername = getLoggedInUserPort.get();
    Optional<WishList> wishList = findWishListPort.findByUsername(loggedInUsername);
    return wishList.orElseGet(() -> new WishList(loggedInUsername));
  }

  private WishList saveWishList(WishList wishList) {
    try {
      return saveWishListPort.save(wishList);
    } catch (Exception e) {
      throw new BusinessException("Failed to save wish list", e);
    }
  }
}
