package com.dreamshops.service.cart;

import com.dreamshops.dto.CartDto;
import com.dreamshops.entity.Cart;
import com.dreamshops.entity.User;

import java.math.BigDecimal;

public interface CartService {

    Cart initializeCart(User user);

    Cart getCart(int cartId);

    void clearCart(int cartId);

    BigDecimal getTotalPrice(int cartId);

    CartDto getCartById(int cartId);

    Cart getCartByUserId(int userId);

}
