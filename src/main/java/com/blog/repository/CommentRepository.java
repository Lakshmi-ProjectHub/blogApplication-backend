package com.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blog.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

	List<Comment> findByPostId(Long postId);
	
	@Modifying
	@Query("DELETE FROM Comment c WHERE c.post.id = :postId")
	void deleteByPostId(Long postId);
	    
	}

