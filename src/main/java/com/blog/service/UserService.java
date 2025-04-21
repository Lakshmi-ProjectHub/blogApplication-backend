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
    private UserRepository userRepository; // Assuming you have a UserRepository for DB interaction

    // Register User (sign-up)
    public boolean registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false; // Email already exists
        }
        
        // Encode the user's password before saving to DB
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // Save user to DB
        userRepository.save(user);
        return true;
    }

    // Authenticate User (sign-in)
    public User authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user; // Return user object if authentication is successful
        }
        return null; // Return null if authentication fails
    }

}
