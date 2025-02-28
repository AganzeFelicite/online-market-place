package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.Entity.User;
import com.online_marketplace_api.awesomity.Repository.IOrderRepository;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderListResponse;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderMessage;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderRequest;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderResponse;

import com.online_marketplace_api.awesomity.common.userDTO.UserResponseDTO;
import com.online_marketplace_api.awesomity.enums.OrderStatus;

import com.online_marketplace_api.awesomity.security.SecurityUtils;
import com.online_marketplace_api.awesomity.service.IOrderService;

import com.online_marketplace_api.awesomity.service.IUserService;
import com.online_marketplace_api.awesomity.util.order.OrderQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IUserService userService;
    private final OrderQueueProducer orderQueueProducer;

    private final SecurityUtils securityUtils;
    @Autowired
    public OrderService(IOrderRepository orderRepository, OrderQueueProducer orderQueueProducer , IUserService userService  ,  SecurityUtils securityUtils) {
        this.orderRepository = orderRepository;
        this.orderQueueProducer = orderQueueProducer;
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = securityUtils.getCurrentUser();


        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(request.getItems() != null ? (BigDecimal) request.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))): BigDecimal.ZERO);


        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentId("PAY-" + System.currentTimeMillis());
        order.setPaymentCompleted(false);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Save the order to get an ID
        Order savedOrder = orderRepository.save(order);

        // 2. Create an order message for the queue
        OrderMessage orderMessage = createOrderMessage(savedOrder);

        // 3. Send the order to the queue for async processing
        orderQueueProducer.sendOrderToQueue(orderMessage);

        // 4. Return response immediately (before processing is complete)
        return toOrderResponse(savedOrder);
    }

    /**
     * Creates an OrderMessage from the Order entity for queue processing
     */
    private OrderMessage createOrderMessage(Order order) {
        UserResponseDTO user =  userService.getUserById(order.getId());
        OrderMessage message = new OrderMessage();
        message.setOrderId(order.getId());
        message.setOrderNumber(order.getOrderNumber());
        message.setUserId(order.getUser().getId());
        message.setUserName(user.getFirstName());
        message.setTotalAmount(order.getTotalAmount());
        message.setShippingAddress(order.getShippingAddress());
        message.setPaymentMethod(order.getPaymentMethod());
        message.setPaymentId(order.getPaymentId());
        return message;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toOrderResponse(order);
    }

    @Override
    public OrderListResponse getUserOrders(Pageable pageable) {
        User user = SecurityUtils.getCurrentUser();
        Page<Order> ordersPage = orderRepository.findByUserId(user.getId(), pageable);
        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
        return new OrderListResponse(orderResponses, ordersPage.getTotalElements(),
                ordersPage.getTotalPages(), ordersPage.getNumber());
    }


    @Override
    public OrderListResponse getUserOrders(Long userId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, ordersPage.getTotalElements(),
                ordersPage.getTotalPages(), ordersPage.getNumber());
    }

    @Override
    public OrderListResponse getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, ordersPage.getTotalElements(),
                ordersPage.getTotalPages(), ordersPage.getNumber());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update the order status
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);

        // If status changes to SHIPPED or DELIVERED, we might want to notify the customer
        // This could also be done through the queue
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            OrderMessage orderMessage = createOrderMessage(updatedOrder);
            orderMessage.setAction("STATUS_UPDATE");
            orderQueueProducer.sendOrderToQueue(orderMessage);
        }

        return toOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);

        // Send cancellation to the queue for processing (refund, inventory update, etc.)
        OrderMessage orderMessage = createOrderMessage(updatedOrder);
        orderMessage.setAction("CANCELLED");
        orderQueueProducer.sendOrderToQueue(orderMessage);
    }



    @Override
    public OrderListResponse getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByStatus(status, pageable);
        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, ordersPage.getTotalElements(),
                ordersPage.getTotalPages(), ordersPage.getNumber());
    }

    private String generateOrderNumber() {
        // Simple example: generate a unique order number based on timestamp
        return "ORD-" + System.currentTimeMillis();
    }

    private OrderResponse toOrderResponse(Order order) {
        UserResponseDTO user =  userService.getUserById(order.getUser().getId());
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .userName(user.getFirstName() + " " + user.getLastName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .paymentId(order.getPaymentId())
                .paymentCompleted(order.isPaymentCompleted())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}