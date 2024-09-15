package com.example.cookfolio.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StoragePath;
import com.bumptech.glide.Glide;
import com.example.cookfolio.Classes.ApiCalls;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.R;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.os.Looper;
import android.os.Handler;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeImage.setImageDrawable(null);
        // Realiza la llamada a la API de manera asíncrona
        ApiCalls.getUsernameByUserID(recipe.getUserId()).thenAccept(finalUsername -> {
            // Crear un handler para ejecutar en el hilo principal
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> holder.username.setText(finalUsername)); // Actualiza el TextView en el hilo principal
            Log.i("SucessUser", "Successfully added username " + finalUsername);
        }).exceptionally(error -> {
            Log.e("AmplifyError", "Error fetching username", error);
            return null;
        });

        holder.recipeTimestamp.setText(recipe.getTimestamp());
        holder.recipeName.setText(recipe.getName());
        holder.description.setText(recipe.getDescription());
        holder.temsCoccio.setText(recipe.getCookingTime() + " m");
        holder.dificultat.setText(recipe.getDifficulty());


        String uniqueFileName = "recipe_image" + recipe.getRecipeImage().hashCode();

        Log.d("S3Path", "Attempting to download file from S3 at path: " + recipe.getRecipeImage());
        Amplify.Storage.downloadFile(
                StoragePath.fromString(recipe.getRecipeImage()),
                new File(holder.itemView.getContext().getCacheDir(), uniqueFileName),
                result -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Glide.with(holder.itemView.getContext())
                                .load(result.getFile())
                                .placeholder(R.drawable.ic_background)
                                .into(holder.recipeImage);
                    });
                },
                error -> Log.e("AmplifyError", "Image download failed", error)
        );
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage, recipeImage, likeIcon, commentIcon;
        TextView username, recipeTimestamp, recipeName, description, temsCoccio, dificultat;

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

        }
    }
}
