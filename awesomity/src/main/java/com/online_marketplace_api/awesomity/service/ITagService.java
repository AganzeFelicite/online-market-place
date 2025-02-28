package com.online_marketplace_api.awesomity.service;

import com.online_marketplace_api.awesomity.common.tagDTO.TagListResponse;
import com.online_marketplace_api.awesomity.common.tagDTO.TagRequest;
import com.online_marketplace_api.awesomity.common.tagDTO.TagResponse;;
import org.springframework.data.domain.Pageable;

public interface ITagService {
    TagResponse createTag(TagRequest request);
    TagResponse getTagById(Long id);
    TagListResponse getAllTags(Pageable pageable);
    TagResponse updateTag(Long id, TagRequest request);
    void deleteTag(Long id);
}