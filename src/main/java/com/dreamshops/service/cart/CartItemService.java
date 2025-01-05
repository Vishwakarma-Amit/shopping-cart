package com.dreamshops.service.cart;

import com.dreamshops.dto.CartItemDto;
import com.dreamshops.request.CartItemRequest;

public interface CartItemService {

    void createCartItem(CartItemRequest cartItemRequest);

    void removeItemFromCart(int cartId, int productId);

    void updateCartItemQuantity(int cartId, int productId, int quantity);

    CartItemDto getCartItem(int cartId, int productId);
}
