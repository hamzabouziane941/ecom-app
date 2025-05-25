package com.arch.ecommerce.product.adapter.driving.web.shoppingcart;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.errormanagement.model.ApiError;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.ShoppingCartCommandDto;
import com.arch.ecommerce.product.adapter.driving.web.shoppingcart.dto.ShoppingCartDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ShoppingCartApi {

  @Operation(
      operationId = "executeItemRequestToShoppingCart",
      description = "Execute an item request to the shopping cart",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ShoppingCartDto.class),
                  examples = {@ExampleObject(value = ShoppingCartApiExample.SHOPPING_CART)})
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
      value = "/shoppingCarts/command",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<ShoppingCartDto> executeShoppingCardCommand(
      @RequestBody(
              description = "Cart item request",
              required = true,
              content = {
                @Content(
                    examples = {@ExampleObject(value = ShoppingCartApiExample.CART_ITEM_REQUEST)})
              })
          @Valid
          ShoppingCartCommandDto shoppingCartCommandDto);

  @Operation(
      operationId = "getUserShoppingCart",
      description = "Get user's shopping cart",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ShoppingCartDto.class),
                  examples = {@ExampleObject(value = ShoppingCartApiExample.SHOPPING_CART)})
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Shopping cart not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/shoppingCarts/user",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<ShoppingCartDto> getUserShoppingCart() throws EntityNotFoundException;
}
