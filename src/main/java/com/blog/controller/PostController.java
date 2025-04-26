package com.blog.controller;

import com.blog.service.PostService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import com.blog.controller.UserController.ResponseMessage;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.repository.UserRepository;
import com.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CommentService commentService;
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
        @RequestParam("name") String name,
        @RequestParam("content") String content,
        @RequestParam("userId") Long userId,
        @RequestParam("img") MultipartFile imageFile,
        HttpSession session) {
    	 
    	 Post post=new Post();
        try {     	
        	Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            User user = optionalUser.get();
            
            post.setName(name);
           
            post.setContent(content);
            post.setUser(user); 
            
                    // 1. Save uploaded image
                    String uploadDir = "uploads/";
                    String randomFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path path = Paths.get(uploadDir + randomFileName);
                    Files.createDirectories(path.getParent());
                    Files.write(path, imageFile.getBytes());
                    post.setImg(randomFileName);

            Post createdPost = postService.savePost(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    	}
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(value="/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam("img") MultipartFile imageFile ) {
	try {
//    	
//    	Optional<User> optionalUser = userRepository.findById(userId);
//    	User user = optionalUser.get();
    	
    	Post post=new Post();
    	
    	post.setName(name);
        
        post.setContent(content);
       // post.setUser(user); 
        
	    String uploadDir = "uploads/";
	    String randomFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	    Path path = Paths.get(uploadDir + randomFileName);
	    Files.createDirectories(path.getParent());
	    Files.write(path, imageFile.getBytes());
	    post.setImg(randomFileName);
    	
        Post updatedPost = postService.updatePost(id, post);
        return ResponseEntity.ok(updatedPost);
    	}
    	catch (Exception e){
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
    
    @GetMapping("/{postId}/view")
    public ResponseEntity<?> viewPost(@PathVariable Long postId){
    	try {
    		postService.viewPost(postId);
    		return ResponseEntity.ok(new String[]{"Post viewed Succesfully."});
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
    
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> getPostByUserId(@PathVariable Long userId) {
//        try {
//            Post post = postService.getPostById(userId);
//            return ResponseEntity.ok(post);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostByUserId(@PathVariable Long userId) {
        try {
            List<Post> posts = postService.getPostByUserId(userId);
            
            // If no posts are found, return an empty list
            if (posts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            
            // Return posts as a List
            return ResponseEntity.ok(posts);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
    	commentService.deleteCommentsByPostId(id);
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
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
