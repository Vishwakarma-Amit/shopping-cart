package com.dreamshops.service.cart;

import com.dreamshops.entity.Cart;
import com.dreamshops.entity.CartItem;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.CartRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.utility.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements  CartItemService{

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final CartRepository cartRepository;

    private final CartService cartService;

    @Override
    @Transactional
    public void createCartItem(int cartId, int productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getProductId()==productId)
                .findFirst().orElse(new CartItem());
        if (cartItem.getCartItemId() == 0) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCart(int cartId, int productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(int cartId, int productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(int cartId, int productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream()
                .filter(item-> item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND+productId));
    }
}
