package com.blog.service;

import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
public Post savePost(Post post) {
    Long userId = post.getUser().getId(); // Assuming frontend sends { user: { id: 1 } }
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    post.setUser(user);
    post.setLikeCount(0);
    post.setViewCount(0);
    post.setDate(new Date());

    return postRepository.save(post);
}
    

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
       Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setViewCount(post.getViewCount() + 1);
            return postRepository.save(post);
        } else {
            throw new EntityNotFoundException("Post not found");
        }
    }
    public void likePost(Long postId) {
    	Optional<Post> optionalPost=postRepository.findById(postId);
    if(optionalPost.isPresent())	{
    	Post post = optionalPost.get();
    	
    	post.setLikeCount(post.getLikeCount()+1);
    	postRepository.save(post);
    }
    else {
    	throw new EntityNotFoundException("Post not found with id: " + postId);
    }
    }
}
