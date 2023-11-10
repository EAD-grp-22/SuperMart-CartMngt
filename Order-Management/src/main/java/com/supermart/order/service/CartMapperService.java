package com.supermart.order.service;

import com.supermart.order.dto.*;
import com.supermart.order.model.Cart;
import com.supermart.order.model.CartItem;
import com.supermart.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartMapperService {

    private final CartRepository cartRepository;

    public CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> cartItems = cart.getCartItems().stream()
                .map(this::mapToCartItemResponse)
                .toList();
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .cartItems(cartItems)
                .build();
    }




    public CartItem mapRequestToCartItem(CartItemRequest cartItemRequest){
        CartItem cartItem=new CartItem();
        cartItem.setSkuCode(cartItemRequest.getSkuCode());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return cartItem;
    }

    public CartItem mapResponseToCartItem(CartItemResponse cartItemResponse){
        CartItem cartItem=new CartItem();
        cartItem.setSkuCode(cartItemResponse.getSkuCode());
        cartItem.setQuantity(cartItemResponse.getQuantity());
        return cartItem;
    }


    public CartItemResponse mapToCartItemResponse(CartItem cartItem){
        return CartItemResponse.builder()
                .quantity(cartItem.getQuantity())
                .skuCode(cartItem.getSkuCode())
                .build();
    }

    public CartItemRequest mapToCartItemRequest(CartItem cartItem){
        return CartItemRequest.builder()
                .quantity(cartItem.getQuantity())
                .skuCode(cartItem.getSkuCode())
                .build();
    }

    public OrderItemRequest mapToOrderItemRequest(CartItem cartItem){
        return OrderItemRequest.builder()
                .quantity(cartItem.getQuantity())
                .skuCode(cartItem.getSkuCode())
                .build();
    }
}
