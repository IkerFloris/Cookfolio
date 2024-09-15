package com.example.cookfolio.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StoragePath;
import com.bumptech.glide.Glide;
import com.example.cookfolio.Classes.ApiCalls;
import com.example.cookfolio.Classes.Ingredient;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import android.os.Looper;
import android.os.Handler;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private static FragmentManager fragmentManager;

    public RecipeAdapter(List<Recipe> recipeList, FragmentManager fragmentManager) {
        this.recipeList = recipeList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // Aquí ya tienes el 'holder' específico de cada fila, no necesitas usar uno estático
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe); // Vincula el holder con la receta actual
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage, recipeImage, likeIcon, commentIcon;
        TextView username, recipeTimestamp, recipeName, description, temsCoccio, dificultat;
        Button veureIng;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTimestamp = itemView.findViewById(R.id.recipe_timestamp);
            likeIcon = itemView.findViewById(R.id.like_icon);
            commentIcon = itemView.findViewById(R.id.comment_icon);
            recipeName = itemView.findViewById(R.id.recipeName);
            description = itemView.findViewById(R.id.description);
            temsCoccio = itemView.findViewById(R.id.tempsCoccio);
            dificultat = itemView.findViewById(R.id.dificultat);
            veureIng = itemView.findViewById(R.id.buttonIngredient);
        }

        public void bind(Recipe recipe) {
            // Ahora ya no estás utilizando un holder estático, cada holder se manejará de manera independiente
            recipeImage.setImageDrawable(null); // Limpia la imagen anterior, si es necesario

            // Cargar username
            ApiCalls.getUsernameByUserID(recipe.getUserId()).thenAccept(finalUsername -> {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> username.setText(finalUsername)); // Actualiza el TextView en el hilo principal
            }).exceptionally(error -> {
                Log.e("AmplifyError", "Error fetching username", error);
                return null;
            });

            recipeTimestamp.setText(recipe.getTimestamp());
            recipeName.setText(recipe.getName());
            description.setText(recipe.getDescription());
            temsCoccio.setText(recipe.getCookingTime() + " m");
            dificultat.setText(recipe.getDifficulty());

            // Cargar imagen de la receta
            String uniqueFileName = "recipe_image" + recipe.getRecipeImage().hashCode();
            Amplify.Storage.downloadFile(
                    StoragePath.fromString(recipe.getRecipeImage()),
                    new File(itemView.getContext().getCacheDir(), uniqueFileName),
                    result -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Glide.with(itemView.getContext())
                                    .load(result.getFile())
                                    .placeholder(R.drawable.ic_background)
                                    .into(recipeImage);
                        });
                    },
                    error -> Log.e("AmplifyError", "Image download failed", error)
            );

            // Evento para el botón de ver ingredientes
            veureIng.setOnClickListener(v -> {
                try {
                    List<Ingredient> ingredientList = ApiCalls.getIngredientsForRecipe(recipe.getRecipeId()).get();
                    IngredientDialogFragment dialog = IngredientDialogFragment.newInstance(ingredientList);
                    dialog.show(fragmentManager, "IngredientDialogFragment");
                } catch (ExecutionException | InterruptedException e) {
                    Log.e("Error", "Failed to load ingredients", e);
                }
            });
        }
    }
}

