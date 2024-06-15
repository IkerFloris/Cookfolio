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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookfolio.R;

public class DespensaFragment extends Fragment {

    public DespensaFragment() {
        // Required empty public constructor
    }

    public static DespensaFragment newInstance() {
        return new DespensaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.despensa_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.pantry_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configure your adapter and set it to the recyclerView

        Button profileButton = view.findViewById(R.id.navigation_profile);
        Button pantryButton = view.findViewById(R.id.navigation_pantry);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        profileButton.setOnClickListener(v -> navController.navigate(R.id.action_navigation_despensa_to_navigation_profile));
        pantryButton.setOnClickListener(v -> navController.navigate(R.id.navigation_pantry));

        return view;
    }
}
