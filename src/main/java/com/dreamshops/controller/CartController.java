package com.dreamshops.controller;

import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.cart.CartService;
import com.dreamshops.utility.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable int cartId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, cartService.getCart(cartId)), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable int cartId){
        try{
            cartService.clearCart(cartId);
            return new ResponseEntity<>(new ApiResponse(Message.CLEAR_CART, null), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable int cartId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, cartService.getTotalPrice(cartId)), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
