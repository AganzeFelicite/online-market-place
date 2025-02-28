package com.online_marketplace_api.awesomity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of an order")
public enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED, FAILED
}