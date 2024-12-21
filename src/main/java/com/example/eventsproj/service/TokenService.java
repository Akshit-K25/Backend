package com.example.eventsproj.service;

import com.example.eventsproj.model.User;
import com.example.eventsproj.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private UserRepository userRepository;

    public String generateAndSaveToken(String email) {
        logger.info("Generating token for email: {}", email);
        
        User existingUser = userRepository.findByEmail(email);
        
        // If user is already active, generate a home page access token
        if (existingUser != null && "ACTIVE".equals(existingUser.getStatus())) {
            String homePageToken = UUID.randomUUID().toString();
            existingUser.setRegistrationToken(homePageToken);
            existingUser.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
            userRepository.save(existingUser);
            return homePageToken;
        }

        if (existingUser != null && "PENDING".equals(existingUser.getStatus())) {
            // If token is still valid, return existing token
            if (existingUser.getTokenExpiry() != null && 
                existingUser.getTokenExpiry().isAfter(LocalDateTime.now())) {
                return existingUser.getRegistrationToken();
            }
        }

        // Existing token generation logic for pending users
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);
    
        if (existingUser == null) {
            logger.info("Creating new user for email: {}", email);
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName("Pending Registration");
            newUser.setRegistrationToken(token);
            newUser.setTokenExpiry(expiryTime);
            newUser.setStatus("PENDING");
            userRepository.save(newUser);
        } else {
            logger.info("Updating existing pending user with new token");
            existingUser.setRegistrationToken(token);
            existingUser.setTokenExpiry(expiryTime);
            existingUser.setStatus("PENDING");
            userRepository.save(existingUser);
        }
    
        return token;
    }

    public boolean validateToken(String token, String email) {
        logger.info("DETAILED: Validating token for email: {}", email);
        
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            logger.error("DETAILED: No user found for email: {}", email);
            return false;
        }
        
        // For active users, validate the home page access token
        if ("ACTIVE".equals(user.getStatus())) {
            if (!token.equals(user.getRegistrationToken())) {
                logger.error("Home page token mismatch");
                return false;
            }
            
            // Check token expiry
            if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
                logger.error("Home page token expired");
                return false;
            }
            
            return true;
        }
        
        // Existing registration token validation for pending users
        if (!token.equals(user.getRegistrationToken())) {
            logger.error("DETAILED: Token mismatch. Received: {}, Stored: {}", 
                         token, user.getRegistrationToken());
            return false;
        }
        
        // Check token expiry
        if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            logger.error("DETAILED: Token expired for email: {}", email);
            return false;
        }
        
        return true;
    }

    public boolean isTokenExpired(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getTokenExpiry() != null) {
            return user.getTokenExpiry().isBefore(LocalDateTime.now());
        }
        return true;
    }
}