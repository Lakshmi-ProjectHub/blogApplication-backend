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
			Long userId = Long.parseLong(postedBy);
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
	    List<Comment> comments = commentRepository.findByPostIdByOrderByDateDesc(postId);
	    comments.forEach(comment -> {
	        System.out.println("Comment content: " + comment.getContent());
	    });
	    return comments;
	}

	@Transactional
	@Override
	public void deleteCommentsByPostId(Long postId) {
	    commentRepository.deleteByPostId(postId);
	}
	

    // 🔥 New Code for Update
    @Override
    public Comment updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
        
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    // 🔥 New Code for Delete
    @Override
    public void deleteCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }


}
