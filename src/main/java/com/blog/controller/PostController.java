package com.blog.controller;

import com.blog.service.PostService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import com.blog.controller.UserController.ResponseMessage;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post,HttpSession session){
    	    Long userid = (long) session.getAttribute("userid");

    	    if (userid == null) {
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
    	    }
    	 
        try {
            Post createdPost = postService.savePost(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    	}
    

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        try {
            
            return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId){
    	try {
    		postService.likePost(postId);
    		return ResponseEntity.ok(new String[]{"Post liked Succesfully."});
    } catch(EntityNotFoundException e) {
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    
    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name) {
        try {
        	 return ResponseEntity.status(HttpStatus.OK).body( postService.searchByName(name));
        } catch (EntityNotFoundException e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           
        }
    }
//    @GetMapping("/create-post")
//    @ResponseBody
//    public String showCreatePost(HttpSession session) {
//        Object userId = session.getAttribute("userId");
//
//        return "<script>" +
//                "alert('User ID from session: " + userId + "');" +
//               "</script>" +
//               "<h1>Create Post Page</h1>" +
//               "<p>If you see this, session is being read by backend.</p>";
//    }
}
