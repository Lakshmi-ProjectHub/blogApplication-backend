package com.blog.service;

import java.util.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.entities.User;

import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;

	public Comment createComment(Long postId, String postedBy, String content) {
		Optional<Post> optionalPost = postRepository.findById(postId);
		if (optionalPost.isPresent()) {
			Long userId = Long.parseLong(postedBy); // assuming postedBy is user ID
		    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			Comment comment = new Comment();
			
			comment.setPost(optionalPost.get());
			comment.setContent(content);
			comment.setUser(user);
			comment.setCreatedAt(new Date());

			return commentRepository.save(comment);
		}
		throw new EntityNotFoundException("Post not Found");
	}
	
	public List<Comment> getCommentsByPostId(Long postId){
		return commentRepository.findByPostId(postId);
	}
	
	@Transactional
	@Override
	public void deleteCommentsByPostId(Long postId) {
	    commentRepository.deleteByPostId(postId);
	}
	
	
}
