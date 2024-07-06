package com.example.cookfolio.Classes;

import android.media.Image;

import java.util.Date;

public class Recipe {
    private int recipeId;
    private int userId;
    private String username;
    private String profileImage;
    private String recipeImage;
    private String name;
    private String timestamp;
    private int cookingTime;
    private String description;
    private String difficulty;

    // Constructor, getters and setters

    public Recipe(String username, String timestamp, String description, String profileImage, String recipeImage, String name, String difficulty, int cookingTime) {
        this.username = username;
        this.profileImage = profileImage;
        this.recipeImage = recipeImage;
        this.timestamp = timestamp;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.cookingTime = cookingTime;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName(){return name;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public String getProfileImage() {
        return profileImage;
    }

    public String getTimestamp() {return timestamp;}

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String  recipeImage) {
        this.recipeImage = recipeImage;
    }
}

