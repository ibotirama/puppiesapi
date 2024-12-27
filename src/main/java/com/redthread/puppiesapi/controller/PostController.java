package com.redthread.puppiesapi.controller;

import com.redthread.puppiesapi.dto.PostDTO;
import com.redthread.puppiesapi.model.Post;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO,
                                           @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.createPost(postDTO, user));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        postService.likePost(postId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed(Pageable pageable) {
        List<Post> feed = postService.getFeed(pageable);
        return ResponseEntity.ok(feed);
    }
}