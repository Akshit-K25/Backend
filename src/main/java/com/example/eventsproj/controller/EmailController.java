package com.example.eventsproj.controller;

import com.example.eventsproj.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:3000") // Add this if not already present
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        logger.info("Received email request for: {}", emailRequest.getEmail());
        
        try {
            if (emailRequest.getEmail() == null || emailRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email address is required");
            }

            String name = emailRequest.getName();
            if (name == null || name.trim().isEmpty()) {
                name = "User";
            }

            emailService.sendEmail(emailRequest.getEmail(), name);
            logger.info("Email sent successfully to: {}", emailRequest.getEmail());
            return ResponseEntity.ok("Email sent successfully!");
            
        } catch (Exception e) {
            logger.error("Error sending email", e);
            return ResponseEntity.status(500)
                .body("Error sending email: " + e.getMessage());
        }
    }
}

class EmailRequest {
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}