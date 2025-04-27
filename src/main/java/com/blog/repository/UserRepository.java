package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    User findByEmail(String email);

}
