package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blog.entities.User;
import com.blog.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:4200") // Allow CORS for this controller
    public ResponseEntity<Object> signUp(@RequestBody  @Valid User user) {
        boolean isUserCreated = userService.registerUser(user);
        if (isUserCreated) {
            return new ResponseEntity<>(new ResponseMessage("User registered successfully", 201), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Email already exists", 400), HttpStatus.BAD_REQUEST);
        }
    }

//    class ResponseMessage {
//        private String message;
//        private int status;
//
//        public ResponseMessage(String message, int status) {
//            this.message = message;
//            this.status = status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//    }


    // User Authentication (Sign In)
    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> signIn(@RequestBody @Valid User loginDetails,HttpSession session) {
        User user = userService.authenticateUser(loginDetails.getEmail(), loginDetails.getPassword());
        if (user != null) {
        	session.setAttribute("userid", user.getId());
            return new ResponseEntity<>(new ResponseMessage("User logged in successfully"+session.getAttribute("userid"), 200), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Invalid credentials", 401), HttpStatus.UNAUTHORIZED);
        }
    }

//    public static class ResponseMessagee {
//        private String message;
//        private int status;
//
//        public ResponseMessagee(String message, int status) {
//            this.message = message;
//            this.status = status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//    }
 // Put this outside any method in UserController
    public static class ResponseMessage {
        private String message;
        private int status;

        public ResponseMessage(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }


}
