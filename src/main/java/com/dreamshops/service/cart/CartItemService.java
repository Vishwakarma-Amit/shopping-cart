package com.dreamshops.service.cart;

import com.dreamshops.entity.CartItem;

public interface CartItemService {

    void createCartItem(int cartId, int productId, int quantity);

    void removeItemFromCart(int cartId, int productId);

    void updateCartItemQuantity(int cartId, int productId, int quantity);

    CartItem getCartItem(int cartId, int productId);
}
