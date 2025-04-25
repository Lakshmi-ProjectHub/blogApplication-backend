package com.blog.service;

import com.blog.entities.Post;
import java.util.List;

public interface PostService {
    Post savePost(Post post);
    List<Post> getAllPosts();
    Post getPostById(Long postId);
    void likePost(Long postId);
   
	List<Post> searchByName(String name);
	
	List<Post> getPostByUserId(Long userId) ;
	void deletePostById(Long id);
	Post updatePost(Long id, Post post);
	 
    
	
	
}
