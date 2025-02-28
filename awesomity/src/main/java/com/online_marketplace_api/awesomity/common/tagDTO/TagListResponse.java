package com.online_marketplace_api.awesomity.common.tagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TagListResponse {
    private List<TagResponse> tags;
}
