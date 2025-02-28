package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.tagDTO.TagListResponse;
import com.online_marketplace_api.awesomity.common.tagDTO.TagRequest;
import com.online_marketplace_api.awesomity.common.tagDTO.TagResponse;
import com.online_marketplace_api.awesomity.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tag Controller", description = "APIs for managing product tags.")
public class TagController {

    private final ITagService tagService;

    @PostMapping
    @Operation(summary = "Create a new tag", description = "Create a new product tag.")
    public ResponseEntity<TagResponse> createTag(
            @Parameter(description = "Tag details to be created") @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID", description = "Fetch a tag by its ID.")
    public ResponseEntity<TagResponse> getTagById(
            @Parameter(description = "ID of the tag to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping
    @Operation(summary = "Get all tags", description = "Fetch a list of all tags with pagination.")
    public ResponseEntity<TagListResponse> getAllTags(
            @Parameter(description = "Pagination details") Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTags(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tag", description = "Update an existing tag by its ID.")
    public ResponseEntity<TagResponse> updateTag(
            @Parameter(description = "ID of the tag to update") @PathVariable Long id,
            @Parameter(description = "Updated tag details") @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag", description = "Delete a tag by its ID.")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "ID of the tag to delete") @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
