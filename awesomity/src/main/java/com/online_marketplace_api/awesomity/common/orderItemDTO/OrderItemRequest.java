package com.online_marketplace_api.awesomity.common.orderItemDTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private Long orderId;
    private Integer quantity;
    private BigDecimal price;
}

