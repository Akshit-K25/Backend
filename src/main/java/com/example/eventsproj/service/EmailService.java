package com.example.eventsproj.service;

import com.example.eventsproj.model.User;
import com.example.eventsproj.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(String recipientEmail, String name) {
        try {
            logger.info("Starting email send process for: {}", recipientEmail);

            User existingUser = userRepository.findByEmail(recipientEmail);
            
            String subject, body;
            String token;

            // Check if user exists and is already active
            if (existingUser != null && "ACTIVE".equals(existingUser.getStatus())) {
                // Generate a home page access token
                token = tokenService.generateAndSaveToken(recipientEmail);
                
                subject = "Access VITAP | Events Portal";
                body = String.format(
                    "Login to VITAP | Events\n\n" +
                    "Here is your link to access the VITAP | Events Portal\n\n" +
                    "The link will expire in 15 minutes. Use this link to access the home page:\n" +
                    "http://localhost:3000/home?token=%s&email=%s\n\n" +
                    "Contacts,\n" +
                    "events.vitap@vitap.ac.in",
                    token, recipientEmail
                );
            } else {
                // Existing registration token generation for pending users
                token = tokenService.generateAndSaveToken(recipientEmail);
                
                subject = "Register for VITAP-Events";
                body = String.format(
                    "Login to VITAP | Events\n\n" +
                    "Here is your link to securely register for VITAP | Events Portal\n\n" +
                    "The link will expire in 15 minutes. Use this link to complete your registration:\n" +
                    "http://localhost:3000/register?token=%s&email=%s\n\n" +
                    "Contacts,\n" +
                    "events.vitap@vitap.ac.in",
                    token, recipientEmail
                );
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("akshit.21bce8026@vitapstudent.ac.in");
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(body);

            logger.info("Attempting to send email");
            mailSender.send(message);
            logger.info("Email sent successfully");

        } catch (Exception e) {
            logger.error("Error in sendEmail method", e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}