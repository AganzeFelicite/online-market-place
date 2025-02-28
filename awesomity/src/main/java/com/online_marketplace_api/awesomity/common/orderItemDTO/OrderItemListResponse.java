package com.online_marketplace_api.awesomity.common.orderItemDTO;

import lombok.Data;

import java.util.List;

@Data

public class OrderItemListResponse {
    private List<OrderItemResponse> items;
}