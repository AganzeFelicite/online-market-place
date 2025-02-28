package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.Entity.OrderItem;
import com.online_marketplace_api.awesomity.Entity.Product;

import com.online_marketplace_api.awesomity.Repository.IOrderRepository;
import com.online_marketplace_api.awesomity.Repository.IProductRepository;
import com.online_marketplace_api.awesomity.Repository.OrderItemRepository;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemRequest;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemResponse;
import com.online_marketplace_api.awesomity.service.IOrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderItemServiceImpl implements IOrderItemService {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    private final OrderItemRepository orderItemRepository;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, IOrderRepository orderRepository, IProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        logger.info("Creating order item for order ID: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return new OrderItemResponse(
                savedOrderItem.getId(),
                savedOrderItem.getOrder().getId(),
                savedOrderItem.getProduct().getId(),
                savedOrderItem.getQuantity(),
                savedOrderItem.getSubtotal()
        );
    }



    @Override
    public List<OrderItemResponse> getOrderItemsByOrderId(Long orderId) {
        logger.info("Fetching order items for order ID: {}", orderId);

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getOrder().getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getSubtotal()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderItem(Long id) {
        logger.info("Deleting order item ID: {}", id);
        orderItemRepository.deleteById(id);
    }
}
