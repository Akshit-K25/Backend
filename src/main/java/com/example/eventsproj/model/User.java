package com.example.eventsproj.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")  // Explicitly name the table
public class User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id")
    )
    
    @Column(name = "role_name")
    private Set<String> roles = new HashSet<>();

    @Column(name = "role")
    private String role;

    public Set<String> getRoles() {
        return roles != null ? roles : new HashSet<>();
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles != null ? roles : new HashSet<>();
    }

    public void addRole(String roleName) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(roleName.toUpperCase());
    }

    // Method to check if user has a specific role
    public boolean hasRole(String roleName) {
        return roles.contains(roleName);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String organization;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String gender;
    private String zip;
    private String country;
    private String designation;

    @Column(length = 20)
    private String status;

    private String rememberToken;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String theme;
    private String themeColor;
    private String userType;
    private boolean isSgstApplicable;

    // New fields for registration token and expiry
    private String registrationToken;
    private LocalDateTime tokenExpiry;

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    // Comprehensive Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role != null ? role.toUpperCase() : null;
    }

    public void setRole(String role) {
        this.role = role != null ? role.toUpperCase() : null;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isSgstApplicable() {
        return isSgstApplicable;
    }

    public void setSgstApplicable(boolean sgstApplicable) {
        isSgstApplicable = sgstApplicable;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    // Utility methods
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && 
               Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    // ToString method for easy debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}