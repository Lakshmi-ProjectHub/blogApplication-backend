package com.blog.repository;

import com.blog.entities.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByNameContaining(String name);
	List<Post> findByUserId(Long userId);
	List<Post> findAllByOrderByDateDesc(); 
}

