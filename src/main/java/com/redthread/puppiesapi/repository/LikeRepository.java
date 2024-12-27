package com.redthread.puppiesapi.repository;

import com.redthread.puppiesapi.model.Like;
import com.redthread.puppiesapi.model.Post;
import com.redthread.puppiesapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
