package com.example.cookfolio.Perfil_Despensa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.NewRecipe.NewRecipeActivity;
import com.example.cookfolio.R;
import com.example.cookfolio.Search.SearchActivity;
import com.example.cookfolio.ui.home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DespensaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despensa_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile){
                return true;
            }
            else if (item.getItemId() == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_recipes) {
                startActivity(new Intent(getApplicationContext(), NewRecipeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        RecyclerView recyclerView = findViewById(R.id.pantry_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configure your adapter and set it to the recyclerView

        Button profileButton = findViewById(R.id.navigation_profile);
        Button pantryButton = findViewById(R.id.navigation_pantry);

        profileButton.setOnClickListener(v -> navigateToProfile());
        pantryButton.setOnClickListener(v -> navigateToPantry());
    }

    private void navigateToProfile() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void navigateToPantry() {
        startActivity(new Intent(getApplicationContext(), DespensaActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, DespensaActivity.class);
    }
}
