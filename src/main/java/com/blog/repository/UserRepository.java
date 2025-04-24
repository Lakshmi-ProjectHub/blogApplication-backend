package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // Check if the email already exists
    User findByEmail(String email); // Find user by email

}
