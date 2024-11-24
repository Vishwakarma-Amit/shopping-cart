package com.dreamshops.service.cart;

import com.dreamshops.dto.CartItemDto;

public interface CartItemService {

    void createCartItem(int cartId, int productId, int quantity);

    void removeItemFromCart(int cartId, int productId);

    void updateCartItemQuantity(int cartId, int productId, int quantity);

    CartItemDto getCartItem(int cartId, int productId);
}
