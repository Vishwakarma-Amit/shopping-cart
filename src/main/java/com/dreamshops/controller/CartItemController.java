package com.dreamshops.controller;

import com.dreamshops.entity.User;
import com.dreamshops.exception.ProductOutOfStockException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.request.CartItemRequest;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.cart.CartItemService;
import com.dreamshops.service.user.UserService;
import com.dreamshops.utility.Message;
import io.jsonwebtoken.JwtException;
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
    private final UserService userService;

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
    @Operation(summary = "Add cart item", description = "add product item into cart by product id and quantity" )
    public ResponseEntity<ApiResponse> createCartItem(@RequestBody CartItemRequest cartItemRequest) {
        try{
            User user = userService.getAuthenticatedUser();

            cartItemService.createCartItem(cartItemRequest, user);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item added successfully!" ), HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ProductOutOfStockException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        } catch (JwtException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UNAUTHORIZED,ex.getMessage()), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{cartId}/{productId}")
    @Operation(summary = "Delete cart item", description = "delete product item from cart by product id and cart id" )
    public ResponseEntity<ApiResponse> removeItemToCart(@PathVariable int cartId, @PathVariable int productId) {
        try{
            cartItemService.removeItemFromCart(cartId, productId);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, "Item deleted successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "update cart item", description = "update product item into cart by product id and cart id" )
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestBody CartItemRequest cartItemRequest) {
        try{
            cartItemService.updateCartItemQuantity(cartItemRequest.getCartId(), cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL, "Item quantity updated successfully!" ), HttpStatus.OK);
        }catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND,ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

