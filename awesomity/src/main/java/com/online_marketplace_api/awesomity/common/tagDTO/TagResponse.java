package com.online_marketplace_api.awesomity.common.tagDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
