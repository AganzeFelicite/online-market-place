package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemRequest;
import com.online_marketplace_api.awesomity.common.orderItemDTO.OrderItemResponse;
import com.online_marketplace_api.awesomity.service.IOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);
    private final IOrderItemService orderItemService;

    public OrderItemController(IOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Operation(summary = "Create a new order item", description = "This endpoint allows users to create a new order item in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item successfully created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(@RequestBody OrderItemRequest request) {
        logger.info("Received request to create order item");
        OrderItemResponse orderItemResponse = orderItemService.createOrderItem(request);
        return ResponseEntity.ok(orderItemResponse);
    }

    @Operation(summary = "Get order items by order ID", description = "This endpoint allows users to fetch all order items for a given order ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        logger.info("Fetching order items for order ID: {}", orderId);
        List<OrderItemResponse> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @Operation(summary = "Delete an order item", description = "This endpoint allows users to delete an order item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        logger.info("Deleting order item ID: {}", id);
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
