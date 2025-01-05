package com.dreamshops.service.order;

import com.dreamshops.dto.OrderDto;
import com.dreamshops.entity.Cart;
import com.dreamshops.entity.Order;
import com.dreamshops.entity.OrderItem;
import com.dreamshops.entity.Product;
import com.dreamshops.enums.OrderStatus;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.OrderRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.service.cart.CartService;
import com.dreamshops.utility.Converter;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final Converter orderConverter;

    @Transactional
    @Override
    public OrderDto placeOrder(int userId) {
        final String methodName = "placeOrder";
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItem(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(order));

        Order savedOrder = orderRepository.save(order);
        log.info("{} - order processed for user with id: {}", methodName, userId);

        cartService.clearCart(cart.getCartId());
        return orderConverter.convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        final String methodName = "createOrderItems";
        return cart.getCartItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory()-cartItem.getQuantity());
                    productRepository.save(product);
                    log.info("{} -Product inventory updated successfully!", methodName);
                    return new OrderItem(cartItem.getQuantity(), cartItem.getUnitPrice(), product, order);
                }).toList();
    }

    private BigDecimal calculateTotalAmount(Order order){
        final String methodName = "calculateTotalAmount";
        log.info("{} - invoked!", methodName);
        return order.getOrderItem()
                .stream()
                .map(item->item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(int orderId) {
        final String methodName= "getOrder";
        log.info("{} - Order fetched with id: {}", methodName, orderId);

        return orderRepository.findById(orderId).map(orderConverter::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException(Message.ORDER_NOT_FOUND +orderId));

    }

    @Override
    public List<OrderDto> getUserOrders(int userId){
        final String methodName= "getUserOrders";
        log.info("{} - Order list fetched for user id: {}", methodName, userId);
        return orderRepository.findByUserUserId(userId)
                .stream()
                .map(orderConverter::convertToDto)
                .toList();
    }

}
