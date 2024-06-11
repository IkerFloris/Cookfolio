package com.example.cookfolio.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.R;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeList = new ArrayList<>();
        LocalDateTime now = null;
        String formattedDateTime = new String();
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
        Recipe test = new Recipe("Cheff Bezos", formattedDateTime, "This is a sample recipe", R.drawable.ic_user_profile, R.drawable.ic_recipe_placeholder, "Test Farfale");
        Recipe anothertest = new Recipe("Iker Floris", formattedDateTime, "This is the second sample recipe", R.drawable.ic_user_profile, R.drawable.ic_beef_goulash, "Beef Goulash");
        recipeList.add(test);
        recipeList.add(anothertest);
        // Populate recipeList with data
        recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
        return root;
    }
}
