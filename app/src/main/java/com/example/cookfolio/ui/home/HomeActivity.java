package com.example.cookfolio.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.AmplifyMain;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.example.cookfolio.AmplifyConfig.AmplifyCognito;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private String username;
    Button signOut;
    AmplifyCognito amplifyCognito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amplifyCognito = new AmplifyCognito(getApplicationContext());
        setContentView(R.layout.fragment_home);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amplifyCognito.loadLogin();
                amplifyCognito.logOut();
            }
        });
        LocalDateTime now = null;
        String formattedDateTime = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = now.format(formatter);
        }

        Recipe test = new Recipe(username, formattedDateTime, "This is a sample recipe", "ic_user_profile.xml", "ic_beef_goulash.jpg", "Test Farfale", "Fàcil", 15);
        Recipe anothertest = new Recipe(username, formattedDateTime, "This is the second sample recipe", "ic_user_profile.xml", "ic_beef_goulash.jpg", "Beef Goulash", "Difícil", 180);
        recipeList.add(test);
        recipeList.add(anothertest);

        // Populate recipeList with data
        recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
    }
}
