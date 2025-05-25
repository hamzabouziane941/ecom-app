package com.arch.ecommerce.product.adapter.driving.web.product;

public class ProductApiExample {

  public static final String CREATED_PRODUCT =
      "{\n"
          + "  \"id\": 1,\n"
          + "  \"code\": \"P001\",\n"
          + "  \"name\": \"Product 1\",\n"
          + "  \"description\": \"Description of Product 1\",\n"
          + "  \"imageUrl\": \"http://example.com/product1.jpg\",\n"
          + "  \"category\": \"Category 1\",\n"
          + "  \"price\": 100.0,\n"
          + "  \"quantity\": 10,\n"
          + "  \"internalReference\": \"REF001\",\n"
          + "  \"shellId\": 1,\n"
          + "  \"inventoryStatus\": \"IN_STOCK\",\n"
          + "  \"rating\": 4.5\n"
          + "}";

  public static final String PRODUCT_TO_CREATE =
      "{\n"
          + "  \"code\": \"P001\",\n"
          + "  \"name\": \"Product 1\",\n"
          + "  \"description\": \"Description of Product 1\",\n"
          + "  \"imageUrl\": \"http://example.com/product1.jpg\",\n"
          + "  \"category\": \"Category 1\",\n"
          + "  \"price\": 100.0,\n"
          + "  \"quantity\": 10,\n"
          + "  \"internalReference\": \"REF001\",\n"
          + "  \"shellId\": 1,\n"
          + "  \"inventoryStatus\": \"IN_STOCK\",\n"
          + "  \"rating\": 4.5\n"
          + "}";

  public static final String PRODUCT_LIST =
      "[{\n"
          + "  \"id\": 1,\n"
          + "  \"code\": \"P001\",\n"
          + "  \"name\": \"Product 1\",\n"
          + "  \"description\": \"Description of Product 1\",\n"
          + "  \"imageUrl\": \"http://example.com/product1.jpg\",\n"
          + "  \"category\": \"Category 1\",\n"
          + "  \"price\": 100.0,\n"
          + "  \"quantity\": 10,\n"
          + "  \"internalReference\": \"REF001\",\n"
          + "  \"shellId\": 1,\n"
          + "  \"inventoryStatus\": \"IN_STOCK\",\n"
          + "  \"rating\": 4.5\n"
          + "}]";

  public static final String FOUND_PRODUCT =
      "{\n"
          + "  \"id\": 1,\n"
          + "  \"code\": \"P001\",\n"
          + "  \"name\": \"Product 1\",\n"
          + "  \"description\": \"Description of Product 1\",\n"
          + "  \"imageUrl\": \"http://example.com/product1.jpg\",\n"
          + "  \"category\": \"Category 1\",\n"
          + "  \"price\": 100.0,\n"
          + "  \"quantity\": 10,\n"
          + "  \"internalReference\": \"REF001\",\n"
          + "  \"shellId\": 1,\n"
          + "  \"inventoryStatus\": \"IN_STOCK\",\n"
          + "  \"rating\": 4.5\n"
          + "}";

  public static final String PRODUCT_PATCH =
      "{\n" + "  \"description\": \"Updated Description of Product 1\",\n" + "}";

  public static final String PATCHED_PRODUCT =
      "{\n"
          + "  \"id\": 1,\n"
          + "  \"code\": \"P001\",\n"
          + "  \"name\": \"Product 1\",\n"
          + "  \"description\": \"Updated Description of Product 1\",\n"
          + "  \"imageUrl\": \"http://example.com/product1.jpg\",\n"
          + "  \"category\": \"Category 1\",\n"
          + "  \"price\": 100.0,\n"
          + "  \"quantity\": 10,\n"
          + "  \"internalReference\": \"REF001\",\n"
          + "  \"shellId\": 1,\n"
          + "  \"inventoryStatus\": \"IN_STOCK\",\n"
          + "  \"rating\": 4.5\n"
          + "}";
}
