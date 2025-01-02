package com.example.eventsproj.controller;

import com.example.eventsproj.dto.MenuItemDTO;
import com.example.eventsproj.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getMenu(Authentication authentication) {
        logger.info("Menu request received for user: {}", 
                   authentication != null ? authentication.getName() : "anonymous");
        
        if (authentication == null) {
            logger.warn("No authentication found");
            return ResponseEntity.ok(List.of());
        }

        String email = authentication.getName();
        List<MenuItemDTO> menuItems = menuService.getMenuItemsForUser(email);
        
        logger.info("Returning {} menu items", menuItems.size());
        return ResponseEntity.ok(menuItems);
    }
}