package com.online_marketplace_api.awesomity.common.orderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@AllArgsConstructor
@Builder

public class OrderListResponse {
    private List<OrderResponse> orders;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
