package com.redthread.puppiesapi.controller;

import com.redthread.puppiesapi.dto.PostDTO;
import com.redthread.puppiesapi.model.Post;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class PostControllerTest {
    private static final Long POST_ID = 123L;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePost() {
        PostDTO postDTO = new PostDTO("Title", "ImageUrl", "Content");
        User user = new User();
        Post post = new Post();
        when(postService.createPost(eq(postDTO), any(User.class))).thenReturn(post);

        ResponseEntity<Post> response = postController.createPost(postDTO, user);

        assertEquals(ResponseEntity.ok(post), response);
    }

    @Test
    void shouldLikePost() {
        User user = new User();
        ResponseEntity<Void> response = postController.likePost(POST_ID, user);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void shouldFailLikeIfThePostDoesNotExists() {
        User user = new User();

        doThrow(new RuntimeException("Post not found")).when(postService).likePost(POST_ID, user);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postController.likePost(POST_ID, user);
        });

        assertEquals("Post not found", exception.getMessage());    }

    @Test
    void shouldGetFeed() {
        List<Post> posts = List.of(new Post(), new Post());
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        Page<Post> page = new PageImpl<>(posts, pageable, posts.size());
        when(postService.getFeed(pageable)).thenReturn(page.getContent());

        ResponseEntity<List<Post>> response = postController.getFeed(pageable);

        assertEquals(ResponseEntity.ok(posts), response);
    }
}