package com.arch.ecommerce.product.adapter.driving.web.wishlist;

public class WishListApiExample {

  public static final String WISHLIST_COMMAND =
      "{\n" + "  \"productIds\": [101, 205, 308],\n" + "  \"action\": \"ADD\"\n" + "}";

  public static final String WISHLIST_RESPONSE =
      "{\n"
          + "  \"id\": 123,\n"
          + "  \"products\": [\n"
          + "    {\n"
          + "      \"id\": 101,\n"
          + "      \"name\": \"Wireless Headphones\",\n"
          + "      \"description\": \"Noise-cancelling wireless headphones\",\n"
          + "      \"image\": \"headphones.jpg\",\n"
          + "      \"category\": \"Electronics\",\n"
          + "      \"price\": 199.99,\n"
          + "      \"quantity\": 50,\n"
          + "      \"inventoryStatus\": \"INSTOCK\",\n"
          + "      \"rating\": 4\n"
          + "    },\n"
          + "    {\n"
          + "      \"id\": 205,\n"
          + "      \"name\": \"Smart Watch\",\n"
          + "      \"description\": \"Fitness tracker with heart rate monitor\",\n"
          + "      \"image\": \"smartwatch.jpg\",\n"
          + "      \"category\": \"Electronics\",\n"
          + "      \"price\": 159.99,\n"
          + "      \"quantity\": 30,\n"
          + "      \"inventoryStatus\": \"LOWSTOCK\",\n"
          + "      \"rating\": 5\n"
          + "    }\n"
          + "  ]\n"
          + "}";

}
