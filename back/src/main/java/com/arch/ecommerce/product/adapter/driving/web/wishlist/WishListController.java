package com.arch.ecommerce.product.adapter.driving.web.wishlist;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListCommandDto;
import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListDto;
import com.arch.ecommerce.product.adapter.driving.web.wishlist.mapping.WishListDtoMapper;
import com.arch.ecommerce.product.application.port.in.wishlist.ExecuteWishListCommandUseCase;
import com.arch.ecommerce.product.application.port.in.wishlist.FindWishListUseCase;
import com.arch.ecommerce.product.domain.WishList;
import com.arch.ecommerce.product.domain.WishListAction;
import com.arch.ecommerce.product.domain.WishListCommand;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WishListController implements WishListApi {

  private static final WishListDtoMapper WISH_LIST_DTO_MAPPER =
      Mappers.getMapper(WishListDtoMapper.class);

  private final ExecuteWishListCommandUseCase executeWishListCommandUseCase;
  private final FindWishListUseCase findWishListUseCase;

  @Override
  public ResponseEntity<WishListDto> executeWishListCommand(
      @RequestBody WishListCommandDto wishlistCommandDto) throws Exception {
    WishListCommand wishListCommand =
        WishListCommand.builder()
            .productIds(wishlistCommandDto.productIds())
            .action(WishListAction.valueOf(wishlistCommandDto.action().name()))
            .build();
    WishList wishList = executeWishListCommandUseCase.execute(wishListCommand);
    return ResponseEntity.ok(WISH_LIST_DTO_MAPPER.toDto(wishList));
  }

  @Override
  public ResponseEntity<WishListDto> getUserWishList() throws EntityNotFoundException {
    WishList wishList = findWishListUseCase.findByCurrentUser();
    return ResponseEntity.ok(WISH_LIST_DTO_MAPPER.toDto(wishList));
  }
}
