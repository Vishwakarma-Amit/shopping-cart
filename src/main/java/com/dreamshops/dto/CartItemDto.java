package com.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private int cartItemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ProductDto product;
}
