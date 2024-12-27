package com.redthread.puppiesapi.service;

import com.redthread.puppiesapi.dto.PostDTO;
import com.redthread.puppiesapi.model.Like;
import com.redthread.puppiesapi.model.Post;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.repository.LikeRepository;
import com.redthread.puppiesapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.hibernate.FetchNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public Post createPost(PostDTO postDTO, User user) {
        Post post = new Post();
        post.setImageUrl(postDTO.imageUrl());
        post.setContent(postDTO.content());
        post.setUser(user);
        return postRepository.save(post);
    }

    public void likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ObjectNotFoundException("Post", postId));

        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new RuntimeException("Post already liked");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }

    public List<Post> getFeed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
