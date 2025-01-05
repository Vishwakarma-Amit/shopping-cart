package com.dreamshops.dto;

import com.dreamshops.entity.User;
import com.dreamshops.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private int orderId;
    private int userId;
    private LocalDateTime orderDateTime;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Set<OrderItemDto> orderItem = new HashSet<>();
    @JsonIgnore
    private User user;
}
