package com.blog.service;

import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	public Comment createComment(Long postId, String postedBy, String content) {
		Optional<Post> optionalPost = postRepository.findById(postId);
		if (optionalPost.isPresent()) {
			Comment comment = new Comment();
			
			comment.setPost(optionalPost.get());
			comment.setContent(content);
			comment.setPostedBy(postedBy);
			comment.setCreatedAt(new Date());

			return commentRepository.save(comment);
		}
		throw new EntityNotFoundException("Post not Found");
	}
	
	public List<Comment> getCommentsByPostId(Long postId){
		return commentRepository.findByPostId(postId);
	}
}
