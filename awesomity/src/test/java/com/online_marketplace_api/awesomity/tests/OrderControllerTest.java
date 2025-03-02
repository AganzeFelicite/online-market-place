package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.common.orderDTO.OrderListResponse;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderRequest;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderResponse;
import com.online_marketplace_api.awesomity.controllers.OrderController;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import com.online_marketplace_api.awesomity.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderRequest request = new OrderRequest(1L, null, "address", "method");
        OrderResponse response = OrderResponse.builder().id(1L).build();

        when(orderService.createOrder(request)).thenReturn(response);

        ResponseEntity<OrderResponse> result = orderController.createOrder(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetOrderById() {
        Long id = 1L;
        OrderResponse response = OrderResponse.builder().id(id).build();

        when(orderService.getOrderById(id)).thenReturn(response);

        ResponseEntity<OrderResponse> result = orderController.getOrderById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetUserOrdersPageable() {
        OrderListResponse response = OrderListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(orderService.getUserOrders(pageable)).thenReturn(response);

        ResponseEntity<OrderListResponse> result = orderController.getUserOrders(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetUserOrdersByUserIdPageable() {
        Long userId = 1L;
        OrderListResponse response = OrderListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(orderService.getUserOrders(userId, pageable)).thenReturn(response);

        ResponseEntity<OrderListResponse> result = orderController.getUserOrders(userId, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetAllOrdersPageable() {
        OrderListResponse response = OrderListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(orderService.getAllOrders(pageable)).thenReturn(response);

        ResponseEntity<OrderListResponse> result = orderController.getAllOrders(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateOrderStatus() {
        Long id = 1L;
        OrderStatus status = OrderStatus.SHIPPED;
        OrderResponse response = OrderResponse.builder().id(id).status(status).build();

        when(orderService.updateOrderStatus(id, status)).thenReturn(response);

        ResponseEntity<OrderResponse> result = orderController.updateOrderStatus(id, status);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testCancelOrder() {
        Long id = 1L;

        ResponseEntity<Void> result = orderController.cancelOrder(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(orderService, times(1)).cancelOrder(id);
    }

    @Test
    public void testGetOrdersByStatus() {
        OrderStatus status = OrderStatus.PENDING;
        OrderListResponse response = OrderListResponse.builder().build();
        Pageable pageable = Pageable.ofSize(10);

        when(orderService.getOrdersByStatus(status, pageable)).thenReturn(response);

        ResponseEntity<OrderListResponse> result = orderController.getOrdersByStatus(status, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}