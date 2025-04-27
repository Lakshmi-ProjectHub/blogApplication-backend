package com.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.service.CommentService;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("comments/create")
	public ResponseEntity<?> createComment(@RequestParam Long postId, @RequestParam String postedBy,
			@RequestBody String content) {
		try {
			return ResponseEntity.ok(commentService.createComment(postId, postedBy, content));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		}

	}

	@GetMapping("comments/{postId}")
	public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
		try {
			return ResponseEntity.ok(commentService.getCommentsByPostId(postId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");

		}
	}

    // 🔥 New API for Updating Comment
//    @PutMapping("comments/update/{commentId}")
//    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody String newContent) {
//        try {
//            return ResponseEntity.ok(commentService.updateComment(commentId, newContent));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
	
	
	@PutMapping("comments/update/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody Map<String, String> requestBody) {
	    try {
	        String newContent = requestBody.get("content");  // Extract the content from the request
	        return ResponseEntity.ok(commentService.updateComment(commentId, newContent));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}


    // 🔥 New API for Deleting Comment
//    @DeleteMapping("comments/delete/{commentId}")
//    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
//        try {
//            commentService.deleteCommentById(commentId);
//            return ResponseEntity.ok("Comment deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
    @DeleteMapping("comments/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteCommentById(commentId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


}
