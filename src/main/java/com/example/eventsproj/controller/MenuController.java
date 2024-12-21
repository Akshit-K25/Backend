package com.example.eventsproj.controller;

import com.example.eventsproj.dto.MenuItemDTO;
import com.example.eventsproj.model.User;
import com.example.eventsproj.repository.UserRepository;
import com.example.eventsproj.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getUserMenu(Principal principal) {
        logger.info("====== Menu Request Started ======");
        
        if (principal == null) {
            logger.warn("No principal found in request");
            return ResponseEntity.ok(List.of());
        }

        String email = principal.getName();
        logger.info("Raw principal: {}", principal);
        logger.info("Principal name (email): {}", email);

        User user = userRepository.findByEmail(email);
        if (user != null) {
            logger.info("User found with details:");
            logger.info("ID: {}", user.getId());
            logger.info("Name: {}", user.getName());
            logger.info("Email: {}", user.getEmail());
            logger.info("Role: {}", user.getRole());
            logger.info("Roles Set: {}", user.getRoles());
            logger.info("Status: {}", user.getStatus());
        } else {
            logger.warn("No user found for email: {}", email);
            return ResponseEntity.ok(List.of());
        }

        List<MenuItemDTO> menuItems = menuService.getMenuItemsForUser(email);
        logger.info("Generated menu items: {}", menuItems);
        logger.info("====== Menu Request Completed ======");

        return ResponseEntity.ok(menuItems);
    }
}