package com.example.cookfolio.Perfil_Despensa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.NewRecipe.NewRecipeActivity;
import com.example.cookfolio.R;
import com.example.cookfolio.Search.SearchActivity;
import com.example.cookfolio.ui.home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedRecipesAdapter recipesAdapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_navigation_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile){
                return true;
            }
            else if (item.getItemId() == R.id.bottom_search) {
                Intent navInt = new Intent(getApplicationContext(), SearchActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_home) {
                Intent navInt = new Intent(getApplicationContext(), HomeActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_recipes) {
                Intent navInt = new Intent(getApplicationContext(), NewRecipeActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.recipes_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Integer> recipeImages = Arrays.asList(
                R.drawable.ic_recipe_placeholder,
                R.drawable.ic_beef_goulash
        );

        recipesAdapter = new FeedRecipesAdapter(this, recipeImages);
        recyclerView.setAdapter(recipesAdapter);

        Button profileButton = findViewById(R.id.navigation_profile);
        Button pantryButton = findViewById(R.id.navigation_pantry);
        TextView etUsername = findViewById(R.id.profile_name);
        etUsername.setText(username);

        profileButton.setOnClickListener(v -> navigateToProfile());
        pantryButton.setOnClickListener(v -> navigateToPantry());
    }

    private void navigateToProfile() {
        Intent navInt = new Intent(getApplicationContext(), ProfileActivity.class);
        navInt.putExtra("username", username);
        startActivity(navInt);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void navigateToPantry() {
        Intent navInt = new Intent(getApplicationContext(), DespensaActivity.class);
        navInt.putExtra("username", username);
        startActivity(navInt);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }
}
