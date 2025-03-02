package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.common.tagDTO.TagListResponse;
import com.online_marketplace_api.awesomity.common.tagDTO.TagRequest;
import com.online_marketplace_api.awesomity.common.tagDTO.TagResponse;
import com.online_marketplace_api.awesomity.controllers.TagController;
import com.online_marketplace_api.awesomity.service.ITagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TagControllerTest {

    @Mock
    private ITagService tagService;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTag() {
        TagRequest request = new TagRequest();
        request.setName("Test Tag");
        TagResponse response = new TagResponse();
        response.setId(1L);
        response.setName("Test Tag");

        when(tagService.createTag(request)).thenReturn(response);

        ResponseEntity<TagResponse> result = tagController.createTag(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetTagById() {
        Long id = 1L;
        TagResponse response = new TagResponse();
        response.setId(id);
        response.setName("Test Tag");

        when(tagService.getTagById(id)).thenReturn(response);

        ResponseEntity<TagResponse> result = tagController.getTagById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetAllTags() {
        TagListResponse response = new TagListResponse(null);
        Pageable pageable = Pageable.ofSize(10);

        when(tagService.getAllTags(pageable)).thenReturn(response);

        ResponseEntity<TagListResponse> result = tagController.getAllTags(pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateTag() {
        Long id = 1L;
        TagRequest request = new TagRequest();
        request.setName("Updated Tag");
        TagResponse response = new TagResponse();
        response.setId(id);
        response.setName("Updated Tag");

        when(tagService.updateTag(id, request)).thenReturn(response);

        ResponseEntity<TagResponse> result = tagController.updateTag(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testDeleteTag() {
        Long id = 1L;

        ResponseEntity<Void> result = tagController.deleteTag(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(tagService, times(1)).deleteTag(id);
    }
}