package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.orderDTO.OrderRequest;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderResponse;
import com.online_marketplace_api.awesomity.common.orderDTO.OrderListResponse;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import com.online_marketplace_api.awesomity.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order", description = "This endpoint allows users to create a new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid order request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "Get an order by its ID", description = "This endpoint allows users to fetch an order by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Get all orders for a user", description = "This endpoint allows users to retrieve a paginated list of their orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderListResponse.class))),
    })
    @GetMapping("/user")
    public ResponseEntity<OrderListResponse> getUserOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(pageable));
    }

    @Operation(summary = "Get orders for a specific user", description = "This endpoint allows admins or authorized users to fetch a user's orders by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderListResponse.class))),
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderListResponse> getUserOrders(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(userId, pageable));
    }

    @Operation(summary = "Get all orders", description = "This endpoint allows admins or authorized users to retrieve all orders in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderListResponse.class))),
    })
    @GetMapping
    public ResponseEntity<OrderListResponse> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @Operation(summary = "Update the status of an order", description = "This endpoint allows updating the status of an order (e.g., 'shipped', 'delivered', etc.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @Operation(summary = "Cancel an order", description = "This endpoint allows users to cancel an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order successfully cancelled"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get orders by status", description = "This endpoint allows filtering orders based on their status (e.g., 'pending', 'shipped').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderListResponse.class))),
    })
    @GetMapping("/status")
    public ResponseEntity<OrderListResponse> getOrdersByStatus(@RequestParam OrderStatus status, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageable));
    }
}
