package com.example.cookfolio.Classes;

public class Recipe {
    private String username;
    private String description;
    private String profileImage;
    private String recipeImage;

    // Constructor, getters and setters

    public Recipe(String username, String timestamp, String description, String profileImage, String recipeImage) {
        this.username = username;
        this.description = description;
        this.profileImage = profileImage;
        this.recipeImage = recipeImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}

