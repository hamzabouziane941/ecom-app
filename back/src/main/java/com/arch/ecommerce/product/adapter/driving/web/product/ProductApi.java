package com.arch.ecommerce.product.adapter.driving.web.product;

import com.arch.ecommerce.errormanagement.exception.EntityNotFoundException;
import com.arch.ecommerce.errormanagement.model.ApiError;
import com.arch.ecommerce.product.adapter.driving.web.product.dto.ProductDto;
import com.arch.ecommerce.product.application.exception.ProductCreationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ProductApi {

  @Operation(
      operationId = "createProduct",
      description = "Creates a new product",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductDto.class),
                  examples = {@ExampleObject(value = ProductApiExample.CREATED_PRODUCT)})
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
      value = "/products",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<ProductDto> createProduct(
      @RequestBody(
              description = "product to create",
              required = true,
              content = {
                @Content(examples = {@ExampleObject(value = ProductApiExample.PRODUCT_TO_CREATE)})
              })
          @Valid
          ProductDto productDto)
      throws ProductCreationException;

  @Operation(
      operationId = "getAllProducts",
      description = "Get all products",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = List.class),
                  examples = {@ExampleObject(value = ProductApiExample.PRODUCT_LIST)})
            }),
      })
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/products",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<List<ProductDto>> getAllProducts();

  @Operation(
      operationId = "getProductById",
      description = "Get product by identifier",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = List.class),
                  examples = {@ExampleObject(value = ProductApiExample.FOUND_PRODUCT)})
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/products/{id}",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id)
      throws EntityNotFoundException;

  @Operation(
      operationId = "patchProduct",
      description = "Patch a product if exists",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductDto.class),
                  examples = {@ExampleObject(value = ProductApiExample.PATCHED_PRODUCT)})
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(
      method = RequestMethod.PATCH,
      value = "/products/{id}",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<ProductDto> patchProduct(
      @PathVariable("id") Long id,
      @RequestBody(
              description = "Product to patch",
              required = true,
              content = {
                @Content(examples = {@ExampleObject(value = ProductApiExample.PRODUCT_PATCH)})
              })
          @Valid
          ProductDto productDto)
      throws EntityNotFoundException;

  @Operation(
      operationId = "deleteProductById",
      description = "delete product with identifier",
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Successful response",
            content = {@Content(mediaType = "application/json", schema = @Schema())}),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  @RequestMapping(method = RequestMethod.DELETE, value = "/products/{id}")
  ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id)
      throws EntityNotFoundException;
}
