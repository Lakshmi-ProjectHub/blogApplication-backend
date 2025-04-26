package com.blog.service;

import com.blog.entities.Post;
//import com.blog.entities.PostInteraction;
import com.blog.entities.User;
//import com.blog.repository.PostInteractionRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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
    post.setId(null);
    
    return postRepository.save(post);
}
    
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByDateDesc();
    }

//    public Post getPostById(Long postId, Long userId) {
//        Post post = postRepository.findById(postId)
//            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        Optional<PostInteraction> interactionOpt = PostInteractionRepository.findByUserAndPost(user, post);
//
//        if (interactionOpt.isEmpty() || !interactionOpt.get().isViewed()) {
//            PostInteraction interaction = interactionOpt.orElse(new PostInteraction());
//            interaction.setUser(user);
//            interaction.setPost(post);
//            interaction.setViewed(true);
//            PostInteractionRepository.save(interaction);
//
//            post.setViewCount(post.getViewCount() + 1);
//            postRepository.save(post);
//        }
//
//        return post;
//    }

    
    public Post getPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
         if (optionalPost.isPresent()) {
             Post post = optionalPost.get();
             return postRepository.save(post);
         } else {
             throw new EntityNotFoundException("Post not found");
         }
     }
    
    public void viewPost(Long postId) {
    	Optional<Post> optionalPost=postRepository.findById(postId);
    if(optionalPost.isPresent())	{
    	Post post = optionalPost.get();
    	post.setViewCount(post.getViewCount() + 1);
    	postRepository.save(post);
    }
    else {
    	throw new EntityNotFoundException("Post not found with id: " + postId);
    }
    }
    
//    public void likePost(Long postId, Long userId) {
//        Post post = postRepository.findById(postId)
//            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        Optional<PostInteraction> interactionOpt = interactionRepository.findByUserAndPost(user, post);
//
//        PostInteraction interaction = interactionOpt.orElse(new PostInteraction());
//        interaction.setUser(user);
//        interaction.setPost(post);
//
//        if (interactionOpt.isPresent() && interaction.isLiked()) {
//            interaction.setLiked(false);
//            post.setLikeCount(post.getLikeCount() - 1);
//        } else {
//            interaction.setLiked(true);
//            post.setLikeCount(post.getLikeCount() + 1);
//        }
//
//        interactionRepository.save(interaction);
//        postRepository.save(post);
//    }
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
    
    public List<Post> searchByName(String name)
    {
    	return postRepository.findAllByNameContaining(name);
    }
    public List<Post> getPostByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        if (posts.isEmpty()) {
            throw new EntityNotFoundException("No posts found for this user");
        }

        return posts;
    }


	@Override
	public void deletePostById(Long id) {
	    postRepository.deleteById(id);
	}

    @Transactional
	@Override
	public Post updatePost(Long id, Post post) {
	    Post existingPost = postRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Post not found"));
	    
	    System.out.println("Updated Post: " + existingPost);

	    existingPost.setName(post.getName());
	    existingPost.setImg(post.getImg());
	    existingPost.setContent(post.getContent());
	    
	    System.out.println("Updated Post: " + existingPost);

	    return postRepository.save(existingPost);
	}

}
