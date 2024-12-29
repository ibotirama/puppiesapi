package com.redthread.puppiesapi.service;

import com.redthread.puppiesapi.dto.PostDTO;
import com.redthread.puppiesapi.model.Like;
import com.redthread.puppiesapi.model.Post;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.repository.LikeRepository;
import com.redthread.puppiesapi.repository.PostRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostServiceTest {
    private static final Long POST_ID = 123L;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePost() {
        PostDTO postDTO = new PostDTO("Title", "imageUrl", "Content");
        User user = new User();
        user.setName("John Doe");
        user.setUsername("jhondoe");
        user.setPassword("password");
        Post post = new Post();
        post.setContent(postDTO.content());
        post.setImageUrl(postDTO.imageUrl());
        post.setUser(user);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(postDTO, user);

        assertEquals(post, createdPost);
    }

    @Test
    void shouldLikePost() {
        User user = new User();
        Post post = new Post();
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> postService.likePost(POST_ID, user));
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        User user = new User();
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            postService.likePost(POST_ID, user);
        });

        assertEquals("No row with the given identifier exists: [Post#123]", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPostAlreadyLiked() {
        User user = new User();
        Post post = new Post();
        Like like = new Like();
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(like));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.likePost(POST_ID, user);
        });

        assertEquals("Post already liked", exception.getMessage());
    }

    @Test
    void shouldGetFeed() {
        List<Post> posts = List.of(new Post(), new Post());
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        Page<Post> page = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page.getContent());

        List<Post> feed = postService.getFeed(pageable);

        assertEquals(posts, feed);
    }

    @Test
    void shouldGetPostsFromASpecificUser() {
        List<Post> posts = List.of(new Post(), new Post());
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        Page<Post> page = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAllByUserIdOrderByCreatedAtDesc(1L, pageable)).thenReturn(page.getContent());

        List<Post> feed = postService.getFeed(1L, null, PageRequest.of(0, 10));

        assertEquals(posts, feed);
    }

    @Test
    void shouldGetPostsLikedByASpecificUser() {
        List<Post> posts = List.of(new Post(), new Post());
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        Page<Post> page = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAllByUserIdAndLikesNotEmptyOrderByCreatedAtDesc(1L, pageable)).thenReturn(page.getContent());

        List<Post> feed = postService.getFeed(1L, true, PageRequest.of(0, 10));

        assertEquals(posts, feed);
    }

    @Test
    void shouldGetASpecificPost() {
        Post post1 = new Post();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        Optional<Post> post = postService.getPost(1L);

        assertEquals(post1, post.get());
    }
}