package com.dreamshops.controller;

import com.dreamshops.dto.OrderDto;
import com.dreamshops.entity.Order;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.order.OrderService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable int userId) {
        try {
            OrderDto order = orderService.placeOrder(userId);
            if (order != null && order.getOrderId() != 0) {
                return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, order), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ApiResponse(Message.SOMETHING_WENT_WRONG, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (ResourceNotFoundException rex) {
            return new ResponseEntity<>(new ApiResponse(rex.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable int orderId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,orderService.getOrder(orderId)), HttpStatus.OK);
        } catch(ResourceNotFoundException rex){
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, rex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getOrderByUserId(@PathVariable int userId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,orderService.getUserOrders(userId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
