package com.online_marketplace_api.awesomity.service;


import com.online_marketplace_api.awesomity.common.orderDTO.OrderListResponse;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderRequest;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderResponse;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import org.springframework.data.domain.Pageable;


public interface IOrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    OrderListResponse getUserOrders(Pageable pageable);
    OrderListResponse getAllOrders(Pageable pageable);

    OrderListResponse getUserOrders(Long userId, Pageable pageable);



    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void cancelOrder(Long id);


    OrderListResponse getOrdersByStatus(OrderStatus status,Pageable pageable);
}