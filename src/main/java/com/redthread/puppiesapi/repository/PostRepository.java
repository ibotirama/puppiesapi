package com.redthread.puppiesapi.repository;

import com.redthread.puppiesapi.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findAllByUserIdAndLikesNotEmptyOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    void deleteAllByUserId(Long userId);

    List<Post> findAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE post_id IN (SELECT id FROM posts WHERE user_id = :userId)", nativeQuery = true)
    void deleteAllLikesByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM posts WHERE user_id = :userId", nativeQuery = true)
    void deleteAllPostsByUserId(Long userId);
}
