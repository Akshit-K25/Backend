package com.example.eventsproj.controller;

import com.example.eventsproj.model.User;
import com.example.eventsproj.repository.UserRepository;
import com.example.eventsproj.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/validate")
    public ResponseEntity<?> validateRegistrationLink(
            @RequestParam("token") String token,
            @RequestParam("email") String email) {
        
        try {
            User existingUser = userRepository.findByEmail(email);
            
            Map<String, Object> response = new HashMap<>();
            
            // If no user exists, return an error
            if (existingUser == null) {
                logger.error("No user found for email: {}", email);
                response.put("status", "NO_USER");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Log detailed user information for debugging
            logger.info("Existing User Details:");
            logger.info("User ID: {}", existingUser.getId());
            logger.info("Email: {}", existingUser.getEmail());
            logger.info("Status: {}", existingUser.getStatus());
            logger.info("Registration Token: {}", existingUser.getRegistrationToken());

            // Check if user is already active
            if ("ACTIVE".equals(existingUser.getStatus())) {
                logger.info("Active user found, preparing redirect");
                response.put("status", "ALREADY_REGISTERED");
                response.put("email", email);
                response.put("name", existingUser.getName());
                return ResponseEntity.ok(response);
            }

            // Validate token for non-active users
            boolean isValid = tokenService.validateToken(token, email);
            if (isValid) {
                logger.info("Token validation successful for email: {}", email);
                response.put("status", "VALID_TOKEN");
                return ResponseEntity.ok(response);
            } else {
                logger.error("Invalid token for email: {}", email);
                response.put("status", "INVALID_TOKEN");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error in validateRegistrationLink", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Error validating registration link");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        logger.info("Processing registration for email: {}", request.getEmail());

        try {
            // Validate token
            if (!tokenService.validateToken(request.getToken(), request.getEmail())) {
                logger.error("Invalid token during registration for email: {}", request.getEmail());
                return ResponseEntity.badRequest().body("Invalid or expired token");
            }

            // Find or create user
            User user = userRepository.findByEmail(request.getEmail());
            if (user == null) {
                logger.error("User not found for email: {}", request.getEmail());
                return ResponseEntity.badRequest().body("User not found");
            }

            // Update user details
            user.setName(request.getName());
            user.setPhone(request.getPhone());
            user.setGender(request.getGender());
            user.setDesignation(request.getDesignation());
            user.setOrganization(request.getOrganisation());
            user.setAddress(request.getAddress());
            user.setRegistrationToken(null);  // Clear token after successful registration
            user.setTokenExpiry(null);
            user.setStatus("ACTIVE");  // Update status to indicate completed registration

            userRepository.save(user);
            logger.info("Registration successful for email: {}", request.getEmail());

            return ResponseEntity.ok("Registration successful!");
        } catch (Exception e) {
            logger.error("Error during registration", e);
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
}

class RegistrationRequest {
    private String email;
    private String token;
    private String name;
    private String phone;
    private String gender;
    private String designation;
    private String organisation;
    private String address;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}