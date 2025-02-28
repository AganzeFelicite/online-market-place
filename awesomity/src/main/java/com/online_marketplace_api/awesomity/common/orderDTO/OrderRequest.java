package com.online_marketplace_api.awesomity.common.orderDTO;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemRequest;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private String shippingAddress;
    private String paymentMethod;
}
