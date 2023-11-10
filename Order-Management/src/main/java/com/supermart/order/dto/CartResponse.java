package com.supermart.order.dto;

import com.supermart.order.model.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Integer id;
    private Integer customerId;
    private List<CartItemResponse> cartItems;
}
