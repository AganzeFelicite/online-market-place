package com.online_marketplace_api.awesomity.tests;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemRequest;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemResponse;
import com.online_marketplace_api.awesomity.controllers.OrderItemController;
import com.online_marketplace_api.awesomity.service.IOrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderItemControllerTest {

    @Mock
    private IOrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderItem() {
        OrderItemRequest request = new OrderItemRequest();
        request.setOrderId(1L);
        request.setQuantity(2);
        request.setPrice(BigDecimal.TEN);

        OrderItemResponse response = new OrderItemResponse(1L, 1L, 1L, 2, BigDecimal.valueOf(20));

        when(orderItemService.createOrderItem(request)).thenReturn(response);

        ResponseEntity<OrderItemResponse> result = orderItemController.createOrderItem(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetOrderItemsByOrderId() {
        Long orderId = 1L;
        OrderItemResponse response1 = new OrderItemResponse(1L, orderId, 1L, 2, BigDecimal.valueOf(20));
        OrderItemResponse response2 = new OrderItemResponse(2L, orderId, 2L, 1, BigDecimal.TEN);
        List<OrderItemResponse> responses = Arrays.asList(response1, response2);

        when(orderItemService.getOrderItemsByOrderId(orderId)).thenReturn(responses);

        ResponseEntity<List<OrderItemResponse>> result = orderItemController.getOrderItemsByOrderId(orderId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
    }

    @Test
    public void testDeleteOrderItem() {
        Long id = 1L;

        ResponseEntity<Void> result = orderItemController.deleteOrderItem(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(orderItemService, times(1)).deleteOrderItem(id);
    }
}