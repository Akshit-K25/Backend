package com.example.eventsproj.service;

import com.example.eventsproj.dto.MenuItemDTO;
import com.example.eventsproj.model.User;
import com.example.eventsproj.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private static final Logger log = LoggerFactory.getLogger(MenuService.class);
    
    @Autowired
    private UserRepository userRepository;

    public List<MenuItemDTO> getMenuItemsForUser(String email) {
        log.debug("Fetching menu items for email: {}", email);
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("No user found for email: {}", email);
            return new ArrayList<>();
        }

        // Get user's role and ensure it's normalized
        String role = user.getRole() != null ? user.getRole().toUpperCase().trim() : "USER";
        log.info("Processing menu items for role: {}", role);

        // Start with common menu items
        List<MenuItemDTO> menuItems = new ArrayList<>();
        
        // Add base menu items that all users should see
        menuItems.add(new MenuItemDTO("Home", "/", "home"));
        menuItems.add(new MenuItemDTO("Events", "/events", "calendar"));
        
        // Add services submenu
        MenuItemDTO servicesMenu = new MenuItemDTO("Services", "#", "services");
        servicesMenu.getSubItems().add(new MenuItemDTO("Gym", "/services/gym", "dumbbell"));
        servicesMenu.getSubItems().add(new MenuItemDTO("Guest House", "/services/guest-house", "bed"));
        menuItems.add(servicesMenu);

        // Add role-specific menu items
        switch (role) {
            case "ADMIN":
                menuItems.add(new MenuItemDTO("Admin Dashboard", "/admin/dashboard", "user-cog"));
                menuItems.add(new MenuItemDTO("Event Management", "/admin/events", "calendar-alt"));
                menuItems.add(new MenuItemDTO("User Management", "/admin/users", "chart-line"));
                break;
            
            case "USER":
                menuItems.add(new MenuItemDTO("My Dashboard", "/user/dashboard", "user-cog"));
                menuItems.add(new MenuItemDTO("My Events", "/user/events", "calendar-alt"));
                menuItems.add(new MenuItemDTO("Profile", "/profile", "user"));
                break;
            
            case "ORGANIZER":
                menuItems.add(new MenuItemDTO("Event Planning", "/organizer/events", "calendar-alt"));
                menuItems.add(new MenuItemDTO("Event Tasks", "/organizer/tasks", "clipboard-list"));
                break;
            
            case "FINANCER":
                menuItems.add(new MenuItemDTO("Financial Reports", "/finance/reports", "money-check-alt"));
                menuItems.add(new MenuItemDTO("Event Budgets", "/finance/budgets", "chart-line"));
                break;
            
            default:
                log.warn("Unknown role: {}, defaulting to USER menu items", role);
                menuItems.add(new MenuItemDTO("My Dashboard", "/user/dashboard", "user-cog"));
                menuItems.add(new MenuItemDTO("My Events", "/user/events", "calendar-alt"));
                menuItems.add(new MenuItemDTO("Profile", "/profile", "user"));
        }

        return menuItems;
    }
}