package com.dreamshops.entity;

import com.dreamshops.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  int orderId;

    private LocalDateTime orderDateTime = LocalDateTime.now();
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItem = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(LocalDateTime orderDateTime, BigDecimal totalAmount, OrderStatus orderStatus, Set<OrderItem> orderItem) {
        this.orderDateTime = orderDateTime;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderItem = orderItem;
    }
}
