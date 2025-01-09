package com.dreamshops.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {

    private int userId;
    @NotNull(message = "Product Id must Not be null")
    private int productId;
    @NotNull(message = "Quantity must Not be null")
    private int quantity;
    private int cartId;


}
