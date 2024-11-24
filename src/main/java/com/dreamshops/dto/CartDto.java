package com.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private int cartId;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private List<CartItemDto> cartItems;
}
