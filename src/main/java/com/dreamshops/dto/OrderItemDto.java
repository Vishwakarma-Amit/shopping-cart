package com.dreamshops.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private int productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
