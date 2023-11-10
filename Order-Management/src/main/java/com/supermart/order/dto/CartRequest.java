package com.supermart.order.dto;

import com.supermart.order.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Integer customerId;
    private List<CartItemRequest> cartItems;
}
