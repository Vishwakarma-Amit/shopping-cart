package com.dreamshops.controller;

import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.cart.CartItemService;
import com.dreamshops.utility.Message;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{cartId}/{productId}")
    @Operation(description = "Get cart item by cart id and product id", summary = "Get cart item" )
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
    public ResponseEntity<ApiResponse> createCartItem(@RequestParam int cartId, @RequestParam int productId, @RequestParam int quantity) {
        try{
            cartItemService.createCartItem(cartId, productId, quantity);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item added successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> removeItemToCart(@RequestParam int cartId, @RequestParam int productId) {
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
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam int cartId, @RequestParam int productId,  @RequestParam int quantity) {
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

