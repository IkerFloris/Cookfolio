package com.example.cookfolio.Containers;

import com.example.cookfolio.Classes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeContainer {
    private static List<Recipe> recipes = new ArrayList<>();

    public static void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public static List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }
}

