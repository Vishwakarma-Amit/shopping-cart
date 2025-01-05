package com.dreamshops.utility;

import com.dreamshops.dto.*;
import com.dreamshops.entity.*;
import com.dreamshops.repository.CartItemRepository;
import com.dreamshops.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Converter {

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CartItemRepository cartItemRepository;

    public  List<ProductDto> getConvertedProduct(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductProductId(product.getProductId());
        List<ImageDto> imageDtos = images.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    public CartItemDto convertToDto(CartItem cartItem){
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
        ProductDto productDto = modelMapper.map(cartItem.getProduct(), ProductDto.class);
        List<Image> images = imageRepository.findByProductProductId(productDto.getProductId());
        List<ImageDto> imageDtos = images.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        cartItemDto.setProduct(productDto);
        return cartItemDto;
    }

    public CartDto convertToDto(Cart cart){
        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CartItemDto convertToDto = convertToDto(cartItem);
            cartItemDtos.add(convertToDto);
        }
        cartDto.setItems(cartItemDtos);
        return cartDto;
    }

    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    public OrderDto convertToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        Set<OrderItemDto> orderItemDtos = new HashSet<>();
        for (OrderItem orderItem : order.getOrderItem()) {
            orderItemDtos.add(modelMapper.map(orderItem, OrderItemDto.class));
        }
        orderDto.setOrderItem(orderItemDtos);

        return orderDto;

    }

}
