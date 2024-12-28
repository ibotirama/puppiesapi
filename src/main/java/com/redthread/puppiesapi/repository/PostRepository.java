package com.redthread.puppiesapi.repository;

import com.redthread.puppiesapi.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.likes l WHERE l.user.id = :likedByUserId order by p.createdAt desc")
    List<Post> findAllByUserIdThatHasLike(Long likedByUserId);
}
