package com.example.cookfolio.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.ApiCalls;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.NewRecipe.NewRecipeActivity;
import com.example.cookfolio.Perfil_Despensa.ProfileActivity;
import com.example.cookfolio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.example.cookfolio.AmplifyConfig.AWSConfig;
import com.example.cookfolio.Search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private String username;
    private int idUsuari;
    Button signOut;
    AWSConfig AWSConfig;
    private ProgressBar progressBar;
    private LinearLayout mainContent; // Contenedor principal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        CompletableFuture<Integer> idUsuariFuture = ApiCalls.getUserID(username);
        try {
            idUsuari = idUsuariFuture.get(); // Bloquea hasta que se complete el futuro
            // Usar idProducte aquí
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_recipes) {
                Intent intent = new Intent(getApplicationContext(), NewRecipeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        AWSConfig = new AWSConfig(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = new ArrayList<>();
        signOut = findViewById(R.id.signOut);
        progressBar = findViewById(R.id.progress_bar);
        mainContent = findViewById(R.id.main_content); // Inicializamos el contenedor principal

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AWSConfig.loadLogin();
                AWSConfig.logOut();
            }
        });

        // Configurar el RecyclerView y el Adapter
        recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRecipes();  // Llamar a fetchRecipes cada vez que la actividad se reanuda
    }

    public void fetchRecipes() {
        Log.d("FETCH_RECIPES", "Starting fetchRecipes");

        // Mostrar el ProgressBar y ocultar el contenido
        progressBar.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);

        // Llamamos a ApiCalls para obtener todas las recetas excluyendo las del usuario actual
        CompletableFuture<List<Recipe>> futureRecipes = ApiCalls.getAllRecipesExceptUser(idUsuari);

        futureRecipes.thenAccept(recipes -> {
            Log.d("FETCH_RECIPES", "Recipes fetched successfully");
            // Actualizamos la lista y notificamos al adaptador en el hilo principal
            runOnUiThread(() -> {
                recipeList.clear();
                recipeList.addAll(recipes);
                recipeAdapter.notifyDataSetChanged();

                // Ocultar el ProgressBar y mostrar el contenido
                progressBar.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);

                Log.d("FETCH_RECIPES", "Recipes displayed in RecyclerView");
            });
        }).exceptionally(error -> {
            Log.e("API Error", "Error fetching recipes: ", error);
            return null;
        });

        Log.d("FETCH_RECIPES", "fetchRecipes function ended");
    }
}
