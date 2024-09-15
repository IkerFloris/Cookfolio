package com.example.cookfolio.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookfolio.Classes.Ingredient;
import com.example.cookfolio.NewRecipe.IngredientAdapter;
import com.example.cookfolio.R;
import java.io.Serializable;
import java.util.List;

public class IngredientDialogFragment extends DialogFragment {

    private static final String ARG_INGREDIENTS = "ingredients";
    private RecyclerView recyclerView;
    private IngredientAdapter ingredientAdapter; // Asumiendo que tienes un adapter para los ingredientes

    public static IngredientDialogFragment newInstance(List<Ingredient> ingredients) {
        IngredientDialogFragment fragment = new IngredientDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INGREDIENTS, (Serializable) ingredients); // Pasamos la lista de ingredientes
        fragment.setArguments(args);
        return fragment;
    }

    private List<Ingredient> ingredients;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.ingredients = (List<Ingredient>) getArguments().getSerializable(ARG_INGREDIENTS); // Recuperamos los ingredientes
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout que mencionaste (recipe_ingredient_viewer)
        View view = inflater.inflate(R.layout.recipe_ingredients_viewer, container, false);

        // Configuramos el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_ingredients); // Asegúrate de que este ID exista en tu layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Aquí debes configurar tu Adapter y cargar los ingredientes en el RecyclerView
        ingredientAdapter = new IngredientAdapter(getIngredients());
        recyclerView.setAdapter(ingredientAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Establecemos el ancho y limitamos la altura máxima para que el diálogo no sea demasiado alto
            int width = ViewGroup.LayoutParams.MATCH_PARENT; // Ocupa todo el ancho de la pantalla
            int height = ViewGroup.LayoutParams.WRAP_CONTENT; // Se ajusta al contenido dinámicamente

            getDialog().getWindow().setLayout(width, height);
            getDialog().getWindow().setGravity(Gravity.CENTER);

            // Fondo blanco para asegurarse de que el diálogo no es transparente
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }



    // Método que se ejecuta cuando se cierra el dialogo
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    // Método que devuelve la lista de ingredientes (simulada en este ejemplo)
    private List<Ingredient> getIngredients() {
        return this.ingredients;  // Devuelve la lista de ingredientes recuperada
    }
}
