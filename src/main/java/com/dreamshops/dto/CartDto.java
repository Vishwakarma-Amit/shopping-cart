package com.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private int cartId;
    private int userId;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private List<CartItemDto> items;

}
