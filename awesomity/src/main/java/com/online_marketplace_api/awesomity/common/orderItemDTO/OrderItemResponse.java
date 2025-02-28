package com.online_marketplace_api.awesomity.common.orderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;



@Data
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal subtotal;
}

