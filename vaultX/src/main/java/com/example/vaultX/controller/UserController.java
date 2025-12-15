package com.example.vaultX.controller;

import com.example.vaultX.Service.UserService;
import com.example.vaultX.dto.*;
import com.example.vaultX.entity.Users;
import com.example.vaultX.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    // CREATE USER
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDetailsDto userDetailsDto) {
        log.info("POST /api/users - Creating new user with email: {}", userDetailsDto.getemail());
        
        try {
            Users createdUser = userService.createUser(userDetailsDto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            response.put("userId", createdUser.getuserId());
            response.put("email", createdUser.getemail());
            response.put("username", createdUser.getusername());
            ApiResponseDto<Object> responseData = ApiResponseDto.success("User created successfully", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
            
        } catch (UserAlreadyExistsException e) {
            log.warn("User creation failed: {}", e.getMessage());
            ApiResponseDto<Object> ErrorResponse = ApiResponseDto.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            ApiResponseDto<Object> errorResponse = ApiResponseDto.error("Registration failed. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}

    // // GET USER BY ID
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getUserById(@PathVariable Long id) {
    //     log.info("GET /api/users/{} - Fetching user", id);
        
    //     try {
    //         Users user = userService.getUserById(id);
    //         return ResponseEntity.ok(convertToResponse(user));
    //     } catch (UserNotFoundException e) {
    //         log.warn("User not found with ID: {}", id);
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //                 .body(createErrorResponse("User not found", e.getMessage()));
    //     } catch (Exception e) {
    //         log.error("Error fetching user with ID: {}", id, e);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(createErrorResponse("Error fetching user", "Internal server error"));
    //     }
    // }

    // // GET ALL USERS
    // @GetMapping
    // public ResponseEntity<?> getAllUsers() {
    //     log.info("GET /api/users - Fetching all users");
        
    //     try {
    //         List<Users> users = userService.getAllUsers();
    //         List<Map<String, Object>> response = users.stream()
    //                 .map(this::convertToResponse)
    //                 .toList();
    //         return ResponseEntity.ok(response);
    //     } catch (Exception e) {
    //         log.error("Error fetching all users", e);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(createErrorResponse("Error fetching users", "Internal server error"));
    //     }
    // }

    // // UPDATE USER
    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateUser(
    //         @PathVariable Long id,
    //         @Valid @RequestBody UsersUpdateDto usersUpdateDto) {
    //     log.info("PUT /api/users/{} - Updating user", id);
        
    //     try {
    //         Users updatedUser = userService.updateUser(id, usersUpdateDto);
    //         return ResponseEntity.ok(convertToResponse(updatedUser));
    //     } catch (UserNotFoundException e) {
    //         log.warn("User not found for update with ID: {}", id);
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //                 .body(createErrorResponse("User not found", e.getMessage()));
    //     } catch (UserAlreadyExistsException e) {
    //         log.warn("Update failed: {}", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.CONFLICT)
    //                 .body(createErrorResponse("Update failed", e.getMessage()));
    //     } catch (Exception e) {
    //         log.error("Error updating user with ID: {}", id, e);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(createErrorResponse("Error updating user", "Internal server error"));
    //     }
    // }

    // DELETE USER
   