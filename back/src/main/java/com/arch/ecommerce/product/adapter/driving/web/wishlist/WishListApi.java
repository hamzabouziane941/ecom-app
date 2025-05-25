package com.arch.ecommerce.product.adapter.driving.web.wishlist;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.errormanagement.model.ApiError;
import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListCommandDto;
import com.arch.ecommerce.product.adapter.driving.web.wishlist.dto.WishListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface WishListApi {

  @Operation(
      operationId = "executeWithListCommand",
      description = "Execute wish list command to add or remove items from it",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = WishListDto.class),
                  examples = {@ExampleObject(value = WishListApiExample.WISHLIST_RESPONSE)})
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/wishLists/command",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<WishListDto> executeWishListCommand(
      @RequestBody(
              description = "Cart item request",
              required = true,
              content = {
                @Content(examples = {@ExampleObject(value = WishListApiExample.WISHLIST_COMMAND)})
              })
          @Valid
          WishListCommandDto wishlistCommandDto)
      throws Exception;

  @Operation(
      operationId = "getUserWishList",
      description = "Get user's wish list",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = List.class),
                  examples = {@ExampleObject(value = WishListApiExample.WISHLIST_RESPONSE)})
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Wish list not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/wishLists/user",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<WishListDto> getUserWishList() throws EntityNotFoundException;
}
