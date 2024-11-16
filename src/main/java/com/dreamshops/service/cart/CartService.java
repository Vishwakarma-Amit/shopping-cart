package com.dreamshops.service.cart;

import com.dreamshops.entity.Cart;

import java.math.BigDecimal;

public interface CartService {

    Cart getCart(int cartId);

    void clearCart(int cartId);

    BigDecimal getTotalPrice(int cartId);
}
