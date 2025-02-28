package com.online_marketplace_api.awesomity.common.orderDTO;

import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemResponse;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private List<OrderItemResponse> items;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private String paymentId;
    private boolean paymentCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
