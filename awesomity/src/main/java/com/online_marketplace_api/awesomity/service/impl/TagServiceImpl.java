package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.Entity.Tag;
import com.online_marketplace_api.awesomity.Repository.ITagRepository;
import com.online_marketplace_api.awesomity.common.exceptions.GlobalExceptionHandler;
import com.online_marketplace_api.awesomity.common.tagDTO.TagListResponse;
import com.online_marketplace_api.awesomity.common.tagDTO.TagRequest;
import com.online_marketplace_api.awesomity.common.tagDTO.TagResponse;

import com.online_marketplace_api.awesomity.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

    private final ITagRepository tagRepository;

    @Override
    @Transactional
    public TagResponse createTag(TagRequest request) {
        // Check if tag with the same name already exists
        if (tagRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Tag with name '" + request.getName() + "' already exists");
        }

        Tag tag = new Tag();
        tag.setName(request.getName());

        Tag savedTag = tagRepository.save(tag);
        return mapToResponse(savedTag);
    }

    @Override
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Tag not found with id: " + id));

        return mapToResponse(tag);
    }

    @Override
    public TagListResponse getAllTags(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        List<TagResponse> tags = tagPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new TagListResponse(tags);
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Tag not found with id: " + id));

        // Check if another tag with the requested name exists
        if (!tag.getName().equalsIgnoreCase(request.getName()) &&
                tagRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Tag with name '" + request.getName() + "' already exists");
        }

        tag.setName(request.getName());


        Tag updatedTag = tagRepository.save(tag);
        return mapToResponse(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Tag not found with id: " + id);
        }

        tagRepository.deleteById(id);
    }

    private TagResponse mapToResponse(Tag tag) {
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());

        response.setCreatedAt(tag.getCreatedAt());
        if (tag.getUpdatedAt() != null) {
            response.setUpdatedAt(tag.getUpdatedAt());
        }
        return response;
    }
}