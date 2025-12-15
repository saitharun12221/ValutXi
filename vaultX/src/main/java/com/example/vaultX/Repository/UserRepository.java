package com.example.vaultX.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vaultX.entity.Users;

public interface UserRepository extends JpaRepository<Users,Long> {
    
    // Find user by email
    Optional<Users> findByEmail(String email);
    
    // Find user by username
    // Optional<Users> findByUsername(String username);
    
    // // Check if email exists
    // boolean existsByEmail(String email);
    
    // // Check if username exists
    // boolean existsByUsername(String username);
}

