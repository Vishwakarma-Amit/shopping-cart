package com.dreamshops.service.order;

import com.dreamshops.dto.OrderDto;
import com.dreamshops.entity.Order;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder(int userId);

    OrderDto getOrder(int orderId);

    List<OrderDto> getUserOrders(int userId);
}
