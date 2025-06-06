package com.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public boolean registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        userRepository.save(user);
        return true;
    }

    public User authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
        	System.out.println("User found: " + user.getEmail());
            System.out.println("Raw Password: " + rawPassword);
            System.out.println("Encoded Password in DB: " + user.getPassword());
            return user;
        }
        return null;
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
