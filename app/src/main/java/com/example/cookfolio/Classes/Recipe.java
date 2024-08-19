package com.example.cookfolio.Classes;

import android.media.Image;

import java.util.Date;

public class Recipe {
    private int recipeId;
    private int userId;
    private String recipeImage;
    private String name;
    private String timestamp;
    private int cookingTime;
    private String description;
    private String difficulty;

    // Constructor, getters and setters




    public Recipe(int recipeId, int userId, String timestamp, String description, String recipeImage, String name, String difficulty, int cookingTime) {

        this.recipeId = recipeId;
        this.userId = userId;
        this.recipeImage = recipeImage;
        this.timestamp = timestamp;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.cookingTime = cookingTime;

    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }
    public String getName(){return name;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public String getTimestamp() {

        String formattedDate = timestamp.substring(0,10);
        return formattedDate;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String  recipeImage) {
        this.recipeImage = recipeImage;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }
}

