package com.dreamshops.service.cart;

import com.dreamshops.entity.Cart;
import com.dreamshops.entity.CartItem;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.CartRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.service.product.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemServiceImpl implements  CartItemService{

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public void AddItemToCart(int cartId, int productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+productId));
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
                .findFirst().orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+productId));
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
                .findFirst().orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+productId));
    }
}
