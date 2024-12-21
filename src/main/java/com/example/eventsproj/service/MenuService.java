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
import java.util.Set;

@Service
public class MenuService {
    
    // Use class-level logger
    private static final Logger log = LoggerFactory.getLogger(MenuService.class);
    
    @Autowired
    private UserRepository userRepository;

    public List<MenuItemDTO> getMenuItemsForUser(String email) {
        log.debug("Starting menu item retrieval for email: {}", email);
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("No user found for email: {}", email);
            return new ArrayList<>();
        }

        // Log user details for debugging
        log.debug("User found - ID: {}, Name: {}, Email: {}", 
                 user.getId(), user.getName(), user.getEmail());
        log.debug("User role details - Role: {}, Roles Set: {}", 
                 user.getRole(), user.getRoles());

        List<MenuItemDTO> menuItems = getCommonMenuItems();
        String effectiveRole = determineEffectiveRole(user);
        
        log.info("Determined effective role for user {}: {}", email, effectiveRole);
        addRoleSpecificMenuItems(menuItems, effectiveRole);
        
        log.debug("Generated {} menu items for user", menuItems.size());
        return menuItems;
    }
    
    private String determineEffectiveRole(User user) {
        // Check single role field first
        String singleRole = user.getRole();
        if (singleRole != null && !singleRole.trim().isEmpty()) {
            log.debug("Using single role field: {}", singleRole);
            return singleRole.toUpperCase().trim();
        }
        
        // Check roles set if single role is not available
        Set<String> roleSet = user.getRoles();
        if (roleSet != null && !roleSet.isEmpty()) {
            String primaryRole = roleSet.iterator().next().toUpperCase().trim();
            log.debug("Using primary role from roles set: {}", primaryRole);
            return primaryRole;
        }
        
        log.warn("No role found for user {}, defaulting to USER", user.getEmail());
        return "USER";
    }

    private List<MenuItemDTO> getCommonMenuItems() {
        List<MenuItemDTO> commonItems = new ArrayList<>();
        
        // Add basic menu items
        commonItems.add(new MenuItemDTO("Home", "/", "home"));
        commonItems.add(new MenuItemDTO("Events", "/events", "calendar"));
        
        // Add services submenu
        MenuItemDTO servicesMenu = new MenuItemDTO("Services", "#services", "services");
        servicesMenu.getSubItems().add(new MenuItemDTO("Gym", "/services/gym", "dumbbell"));
        servicesMenu.getSubItems().add(new MenuItemDTO("Guest House", "/services/guest-house", "bed"));
        commonItems.add(servicesMenu);
        
        log.debug("Generated {} common menu items", commonItems.size());
        return commonItems;
    }

    private List<MenuItemDTO> getAdminMenuItems() {
        List<MenuItemDTO> adminItems = new ArrayList<>();
        adminItems.add(new MenuItemDTO("Admin Dashboard", "/admin/dashboard", "user-cog"));
        adminItems.add(new MenuItemDTO("Event Management", "/admin/events", "calendar-alt"));
        adminItems.add(new MenuItemDTO("User Management", "/admin/users", "chart-line"));
        return adminItems;
    }

    private List<MenuItemDTO> getUserMenuItems() {
        List<MenuItemDTO> userItems = new ArrayList<>();
        userItems.add(new MenuItemDTO("My Dashboard", "/user/dashboard", "user-cog"));
        userItems.add(new MenuItemDTO("My Events", "/user/events", "calendar-alt"));
        userItems.add(new MenuItemDTO("Profile", "/profile", "user"));
        return userItems;
    }

    private List<MenuItemDTO> getOrganizerMenuItems() {
        List<MenuItemDTO> organizerItems = new ArrayList<>();
        organizerItems.add(new MenuItemDTO("Event Planning", "/organizer/events", "calendar-alt"));
        organizerItems.add(new MenuItemDTO("Event Tasks", "/organizer/tasks", "clipboard-list"));
        return organizerItems;
    }

    private List<MenuItemDTO> getFinancerMenuItems() {
        List<MenuItemDTO> financerItems = new ArrayList<>();
        financerItems.add(new MenuItemDTO("Financial Reports", "/finance/reports", "money-check-alt"));
        financerItems.add(new MenuItemDTO("Event Budgets", "/finance/budgets", "chart-line"));
        return financerItems;
    }

    private void addRoleSpecificMenuItems(List<MenuItemDTO> menuItems, String role) {
        log.debug("Adding role-specific menu items for role: {}", role);
        
        switch (role) {
            case "ADMIN":
                menuItems.addAll(getAdminMenuItems());
                break;
            case "USER":
                menuItems.addAll(getUserMenuItems());
                break;
            case "ORGANIZER":
                menuItems.addAll(getOrganizerMenuItems());
                break;
            case "FINANCER":
                menuItems.addAll(getFinancerMenuItems());
                break;
            default:
                log.warn("Unrecognized role: {}, defaulting to USER menu items", role);
                menuItems.addAll(getUserMenuItems());
        }
        
        log.debug("Added role-specific menu items. Total items: {}", menuItems.size());
    }
}