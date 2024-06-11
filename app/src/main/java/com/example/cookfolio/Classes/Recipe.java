package com.example.cookfolio.Classes;

import android.media.Image;

import java.util.Date;

public class Recipe {
    private String username;
    private int profileImage;

    private int recipeImage;

    private String name;

    private String timestamp;

    // Constructor, getters and setters

    public Recipe(String username, String timestamp, String description, int profileImage, int recipeImage, String name) {
        this.username = username;
        this.profileImage = profileImage;
        this.recipeImage = recipeImage;
        this.timestamp = timestamp;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName(){return name;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public int getProfileImage() {
        return profileImage;
    }

    public String getTimestamp() {return timestamp;}

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(int  recipeImage) {
        this.recipeImage = recipeImage;
    }
}

