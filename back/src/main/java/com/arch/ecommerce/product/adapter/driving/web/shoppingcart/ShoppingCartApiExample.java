package com.arch.ecommerce.product.adapter.driving.web.shoppingcart;

public class ShoppingCartApiExample {

  public static final String CART_ITEM_REQUEST =
      """
                  {  "productId": 1001,
                    "quantity": 2,
                    "actionType": "ADD_TO_CART"
                  }""";

  public static final String SHOPPING_CART =
      "{\n"
          + "  \"id\": 1,\n"
          + "  \"items\": [\n"
          + "    {\n"
          + "      \"id\": 101,\n"
          + "      \"product\": {\n"
          + "        \"id\": 1001,\n"
          + "        \"code\": \"PROD-1001\",\n"
          + "        \"name\": \"Wireless Headphones\",\n"
          + "        \"description\": \"Noise-cancelling wireless headphones with 30hr battery\",\n"
          + "        \"image\": \"headphones.jpg\",\n"
          + "        \"category\": \"Electronics\",\n"
          + "        \"price\": 199.99,\n"
          + "        \"quantity\": 50,\n"
          + "        \"internalReference\": \"WH-2023-X\",\n"
          + "        \"shellId\": 5001,\n"
          + "        \"inventoryStatus\": \"INSTOCK\",\n"
          + "        \"rating\": 4,\n"
          + "        \"createdAt\": \"2023-05-15T10:30:00\",\n"
          + "        \"updatedAt\": \"2023-05-20T14:15:00\"\n"
          + "      },\n"
          + "      \"quantity\": 2\n"
          + "    },\n"
          + "    {\n"
          + "      \"id\": 102,\n"
          + "      \"product\": {\n"
          + "        \"id\": 1002,\n"
          + "        \"code\": \"PROD-1002\",\n"
          + "        \"name\": \"Smart Watch\",\n"
          + "        \"description\": \"Fitness tracker with heart rate monitor\",\n"
          + "        \"image\": \"smartwatch.jpg\",\n"
          + "        \"category\": \"Electronics\",\n"
          + "        \"price\": 159.99,\n"
          + "        \"quantity\": 30,\n"
          + "        \"internalReference\": \"SW-2023-A\",\n"
          + "        \"shellId\": 5002,\n"
          + "        \"inventoryStatus\": \"LOWSTOCK\",\n"
          + "        \"rating\": 5,\n"
          + "        \"createdAt\": \"2023-04-10T09:15:00\",\n"
          + "        \"updatedAt\": \"2023-05-18T11:45:00\"\n"
          + "      },\n"
          + "      \"quantity\": 1\n"
          + "    }\n"
          + "  ],\n"
          + "  \"totalPrice\": 559.97,\n"
          + "  \"lastUpdated\": \"2023-05-25T16:20:30\"\n"
          + "}";
}
