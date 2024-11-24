package com.dreamshops.request;

import lombok.Data;

@Data
public class CartItemRequest {

    private int cartId;
    private int productId;
    private int quantity;
}
