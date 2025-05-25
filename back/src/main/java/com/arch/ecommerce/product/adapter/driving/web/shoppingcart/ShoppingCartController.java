package com.arch.ecommerce.product.adapter.driving.web.shoppingcart;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.ShoppingCartCommandDto;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.ShoppingCartDto;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.mapping.ShoppingCartDtoMapper;
import com.arch.ecommerce.product.application.port.in.shoppingcart.ExecuteShoppingCartCommandUseCase;
import com.arch.ecommerce.product.application.port.in.shoppingcart.FindShoppingCartUseCase;
import com.arch.ecommerce.product.domain.CartAction;
import com.arch.ecommerce.product.domain.ShoppingCart;
import com.arch.ecommerce.product.domain.ShoppingCartCommand;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartApi {

  public static final ShoppingCartDtoMapper shoppingCartDtoMapper =
      Mappers.getMapper(ShoppingCartDtoMapper.class);

  private final ExecuteShoppingCartCommandUseCase executeShoppingCartCommandUseCase;
  private final FindShoppingCartUseCase findShoppingCartUseCase;

  @Override
  public ResponseEntity<ShoppingCartDto> executeShoppingCardCommand(
      @RequestBody ShoppingCartCommandDto shoppingCartCommandDto) {
    ShoppingCartCommand shoppingCartCommand =
        ShoppingCartCommand.builder()
            .productId(shoppingCartCommandDto.productId())
            .quantity(shoppingCartCommandDto.quantity())
            .action(CartAction.valueOf(shoppingCartCommandDto.action().name()))
            .build();
    ShoppingCart shoppingCart = executeShoppingCartCommandUseCase.execute(shoppingCartCommand);
    return ResponseEntity.ok(shoppingCartDtoMapper.toDto(shoppingCart));
  }

  @Override
  public ResponseEntity<ShoppingCartDto> getUserShoppingCart() throws EntityNotFoundException {
    ShoppingCart shoppingCart = findShoppingCartUseCase.findUserShoppingCart();
    return ResponseEntity.ok(shoppingCartDtoMapper.toDto(shoppingCart));
  }
}
