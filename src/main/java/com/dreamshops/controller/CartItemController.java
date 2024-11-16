package com.dreamshops.controller;

import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.cart.CartItemService;
import com.dreamshops.utility.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> getCartItem(@PathVariable int cartId, @PathVariable int productId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, cartItemService.getCartItem(cartId, productId)), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> AddItemToCart(@RequestParam(required = false) int cartId, @RequestParam int productId, @RequestParam int quantity) {
        try{
            cartItemService.AddItemToCart(cartId, productId, quantity);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item added successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> removeItemToCart(@RequestParam(required = false) int cartId, @RequestParam int productId) {
        try{
            cartItemService.removeItemFromCart(cartId, productId);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item deleted successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam(required = false) int cartId, @RequestParam int productId,  @RequestParam int quantity) {
        try{
            cartItemService.updateCartItemQuantity(cartId, productId, quantity);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item quantity updated successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

