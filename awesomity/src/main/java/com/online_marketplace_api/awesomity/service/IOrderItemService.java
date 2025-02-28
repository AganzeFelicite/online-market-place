package com.online_marketplace_api.awesomity.service;



import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemRequest;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemResponse;

import java.util.List;

public interface IOrderItemService {
    OrderItemResponse createOrderItem(OrderItemRequest request);
    List<OrderItemResponse> getOrderItemsByOrderId(Long orderId);
    void deleteOrderItem(Long id);
}
