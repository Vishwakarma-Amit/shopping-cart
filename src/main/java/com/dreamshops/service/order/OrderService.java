package com.dreamshops.service.order;

import com.dreamshops.entity.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(int userId);

    Order getOrder(int orderId);

    List<Order> getUserOrders(int userId);
}
