package com.blog.service;

import java.util.List;

import com.blog.entities.Comment;

public interface CommentService {

	 Comment createComment(Long postId, String postedBy, String content) ;

	 
	 List<Comment> getCommentsByPostId(Long postId);
}
