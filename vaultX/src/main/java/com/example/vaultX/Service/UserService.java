package com.example.vaultX.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
// Add these imports at the top of UserService.java

import com.example.vaultX.exception.UserAlreadyExistsException;
import com.example.vaultX.exception.UserNotFoundException;
import com.example.vaultX.exception.InvalidCredentialsException;
import com.example.vaultX.exception.UserInactiveException;
import com.example.vaultX.exception.ServiceException;

import com.example.vaultX.Repository.UserRepository;
import com.example.vaultX.dto.UserDetailsDto;
import com.example.vaultX.dto.UsersUpdateDto;
import com.example.vaultX.entity.UserDetails;
import com.example.vaultX.entity.Users;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Create a new user from UserDetailsDTO
     */
    @Transactional
    public Users createUser(UserDetailsDto userDetailsDTO) {
        log.info("Creating new user with email: {}", userDetailsDTO.getemail());
        
        // Check if user already exists
        if (userRepository.findByEmail(userDetailsDTO.getemail()).isPresent()) {
            log.warn("User creation failed - email already exists: {}", userDetailsDTO.getemail());
            throw new UserAlreadyExistsException("Email already registered");
        }
        
        // if (userRepository.findByUsername(userDetailsDTO.getusername()).isPresent()) {
        //     log.warn("User creation failed - username already exists: {}", userDetailsDTO.getusername());
        //     throw new UserAlreadyExistsException("Username already taken");
        // }
        
        try {
            // Create User entity from DTO
            Users user = new Users();
            user.setemail(userDetailsDTO.getemail());
            user.setusername(userDetailsDTO.getusername());
            
            // Encode password before storing
            String encodedPassword = passwordEncoder.encode(userDetailsDTO.getpassword());
            user.setpassword(encodedPassword);
            UserDetails userDetail = new UserDetails();
            userDetail.setfirstName(userDetailsDTO.getfirstName());
            userDetail.setlastName(userDetailsDTO.getlastName());
            userDetail.setphone(userDetailsDTO.getfirstName());
            userDetail.setusers(user);
            user.setuserDetails(userDetail);
            // Only set phoneNumber if it exists in DTO
            
            Users savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getuserId());
            return savedUser;
            
        } catch (Exception e) {
            log.error("Failed to create user with email: {}", userDetailsDTO.getemail(), e);
            throw new ServiceException("User creation failed", e);
        }
    }
}
//     /**
//      * Update user from UserUpdateDTO
//      */
//     @Transactional
//     public Users updateUser(Long userId, UsersUpdateDto userUpdateDTO) {
//         log.info("Updating user ID: {}", userId);
        
//         Users user = getUserById(userId);
//         boolean changesMade = false;
        
//         // Update email if provided
//         if (userUpdateDTO.getemail() != null && !userUpdateDTO.getemail().isBlank()) {
//             log.debug("Updating email for user ID: {}", userId);
            
//             // Check if new email already exists
//             Optional<Users> existingUser = userRepository.findByEmail(userUpdateDTO.getemail());
//             if (existingUser.isPresent() && !existingUser.get().getuserId().equals(userId)) {
//                 log.warn("Email update failed - email already in use: {}", userUpdateDTO.getemail());
//                 throw new UserAlreadyExistsException("Email already in use");
//             }
//             user.setemail(userUpdateDTO.getemail());
//             changesMade = true;
//         }
        
//         // Update phoneNumber if provided
        
//         // Update username if provided
//         if (userUpdateDTO.getusername() != null && !userUpdateDTO.getusername().isBlank()) {
//             log.debug("Updating username for user ID: {}", userId);
            
//             // Check if new username already exists
//             Optional<Users> existingUser = userRepository.findByUsername(userUpdateDTO.getusername());
//             if (existingUser.isPresent() && !existingUser.get().getuserId().equals(userId)) {
//                 log.warn("Username update failed - username already in use: {}", userUpdateDTO.getusername());
//                 throw new UserAlreadyExistsException("Username already taken");
//             }
//             user.setusername(userUpdateDTO.getusername());
//             changesMade = true;
//         }

//             if (userUpdateDTO.getpassword() != null && !userUpdateDTO.getpassword().isBlank()) {
//             String encodedPassword = passwordEncoder.encode(userUpdateDTO.getpassword());
//             user.setpassword(encodedPassword);
//             changesMade = true;
//         }
    
//     // Update UserDetails if any personal info provided
//         if (user.getuserDetails() == null) {
//             user.setuserDetails(new UserDetails());
//             user.getuserDetails().setusers(user);
//         }
        
//         UserDetails userDetails = user.getuserDetails();
        
//         if (userUpdateDTO.getfirstName() != null && !userUpdateDTO.getfirstName().isBlank()) {
//             userDetails.setfirstName(userUpdateDTO.getfirstName());
//             changesMade = true;
//         }
        
//         if (userUpdateDTO.getlastName() != null && !userUpdateDTO.getlastName().isBlank()) {
//             userDetails.setlastName(userUpdateDTO.getlastName());
//             changesMade = true;
//         }
        
//         if (userUpdateDTO.getphone() != null && !userUpdateDTO.getphone().isBlank()) {
//             userDetails.setphone(userUpdateDTO.getphone());
//             changesMade = true;
//         }
        
//         if (changesMade) {
//             user.setupdatedAt(LocalDateTime.now());
//             userDetails.setupdatedAt(LocalDateTime.now());
//             Users updatedUser = userRepository.save(user);
//             log.info("User ID: {} updated successfully", userId);
//             return updatedUser;
//         } else {
//             log.debug("No changes to update for user ID: {}", userId);
//             return user;
//         }
//     }
    
//     /**
//      * Get user by ID
//      */
//     public Users getUserById(Long userId) {
//         log.debug("Fetching user by ID: {}", userId);
        
//         Optional<Users> user = userRepository.findById(userId);
        
//         if (user.isEmpty()) {
//             log.warn("User not found with ID: {}", userId);
//             throw new UserNotFoundException("User not found");
//         }
        
//         log.trace("Found user: {}", user.get().getemail());
//         return user.get();
//     }
    
//     /**
//      * Authenticate user
//      */
//     public Users authenticate(String email, String password) {
//         log.debug("Authenticating user with email: {}", email);
        
//         Optional<Users> user = userRepository.findByEmail(email);
        
//         if (user.isEmpty()) {
//             log.warn("Authentication failed - user not found: {}", email);
//             throw new InvalidCredentialsException("Invalid credentials");
//         }
        
//         // Verify password using PasswordEncoder
//         if (!passwordEncoder.matches(password, user.get().getpassword())) {
//             log.warn("Authentication failed - incorrect password for user: {}", email);
//             throw new InvalidCredentialsException("Invalid credentials");
//         }
        
//         log.info("User authenticated successfully: {}", email);
//         return user.get();
//     }
    
//     /**
//      * Change password
//      */
//     @Transactional
//     public void changePassword(Long userId, String oldPassword, String newPassword) {
//         log.info("Changing password for user ID: {}", userId);
        
//         Users user = getUserById(userId);
        
//         // Verify old password using PasswordEncoder
//         if (!passwordEncoder.matches(oldPassword, user.getpassword())) {
//             log.warn("Password change failed - old password incorrect for user ID: {}", userId);
//             throw new InvalidCredentialsException("Old password is incorrect");
//         }
        
//         // Validate new password
//         if (newPassword == null || newPassword.length() < 8) {
//             log.warn("Password change failed - new password too short");
//             throw new IllegalArgumentException("Password must be at least 8 characters");
//         }
        
//         // Encode and set new password
//         String encodedPassword = passwordEncoder.encode(newPassword);
//         user.setpassword(encodedPassword);
//         userRepository.save(user);
        
//         log.info("Password changed successfully for user ID: {}", userId);
//     }
    
//     /**
//      * Get all users
//      */
//     public List<Users> getAllUsers() {
//         log.info("Fetching all users");
        
//         List<Users> users = userRepository.findAll();
//         log.debug("Found {} users", users.size());
//         return users;
//     }
    
//     /**
//      * Delete user
//      */
//     @Transactional
//     public void deleteUser(Long userId) {
//         log.warn("Deleting user ID: {}", userId);
        
//         Users user = getUserById(userId);
        
//         try {
//             userRepository.delete(user);
//             log.info("User ID: {} deleted successfully", userId);
//         } catch (Exception e) {
//             log.error("Failed to delete user ID: {}", userId, e);
//             throw new ServiceException("Failed to delete user", e);
//         }
//     }
    
    
//     /**
//      * Check if user exists by email
//      */
//     public boolean existsByEmail(String email) {
//         log.trace("Checking if user exists with email: {}", email);
//         boolean exists = userRepository.findByEmail(email).isPresent();
//         log.trace("User exists: {}", exists);
//         return exists;
//     }
    
//     /**
//      * Check if user exists by username
//      */
//     public boolean existsByUsername(String username) {
//         log.trace("Checking if user exists with username: {}", username);
//         boolean exists = userRepository.findByUsername(username).isPresent();
//         log.trace("User exists: {}", exists);
//         return exists;
//     }
    
//     /**
//      * Get user by email
//      */
//     public Users getUserByEmail(String email) {
//         log.debug("Getting user by email: {}", email);
        
//         Optional<Users> user = userRepository.findByEmail(email);
        
//         if (user.isEmpty()) {
//             log.warn("User not found with email: {}", email);
//             throw new UserNotFoundException("User not found");
//         }
        
//         return user.get();
//     }
    
//     /**
//      * Get user by username
//      */
//     public Users getUserByUsername(String username) {
//         log.debug("Getting user by username: {}", username);
        
//         Optional<Users> user = userRepository.findByUsername(username);
        
//         if (user.isEmpty()) {
//             log.warn("User not found with username: {}", username);
//             throw new UserNotFoundException("User not found");
//         }
        
//         return user.get();
//     }
// }