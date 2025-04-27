package com.blog.service;

import java.util.List;

import com.blog.entities.Comment;

public interface CommentService {

	 Comment createComment(Long postId, String postedBy, String content) ;

	 
	 List<Comment> getCommentsByPostId(Long postId);
	 
	 void deleteCommentsByPostId(Long postId);

	 

	    // 🔥 New methods
	    Comment updateComment(Long commentId, String newContent);
	    
	    void deleteCommentById(Long commentId);
	 
	 
}
