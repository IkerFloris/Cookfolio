package com.example.cookfolio.Perfil_Despensa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookfolio.R;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FeedRecipesAdapter recipesAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_profile, container, false);

        recyclerView = view.findViewById(R.id.recipes_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Integer> recipeImages = Arrays.asList(
                R.drawable.ic_recipe_placeholder,
                R.drawable.ic_beef_goulash
        );

        recipesAdapter = new FeedRecipesAdapter(getContext(), recipeImages);
        recyclerView.setAdapter(recipesAdapter);

        Button profileButton = view.findViewById(R.id.navigation_profile);
        Button pantryButton = view.findViewById(R.id.navigation_pantry);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        profileButton.setOnClickListener(v -> navController.navigate(R.id.navigation_profile));
        pantryButton.setOnClickListener(v -> navController.navigate(R.id.action_navigation_profile_to_navigation_despensa));

        return view;
    }
}
