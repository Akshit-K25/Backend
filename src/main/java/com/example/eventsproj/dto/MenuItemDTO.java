package com.example.eventsproj.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuItemDTO {
    private String label;
    private String path;
    private String icon;
    private List<MenuItemDTO> subItems = new ArrayList<>();

    public MenuItemDTO() {}

    public MenuItemDTO(String label, String path, String icon) {
        this.label = label;
        this.path = path;
        this.icon = icon;
    }

    public List<MenuItemDTO> getSubItems() {
        return subItems;
    }

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}