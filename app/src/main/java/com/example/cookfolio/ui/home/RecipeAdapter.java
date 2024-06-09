package com.example.cookfolio.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.R;

import java.util.List;

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
        holder.username.setText(recipe.getUsername());
        holder.recipeDescription.setText(recipe.getDescription());
        // Set image resources using Glide or Picasso
        // Glide.with(holder.itemView.getContext()).load(recipe.getProfileImage()).into(holder.profileImage);
        // Glide.with(holder.itemView.getContext()).load(recipe.getRecipeImage()).into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage, recipeImage, likeIcon, commentIcon;
        TextView username, recipeDescription;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeDescription = itemView.findViewById(R.id.recipe_description);
            likeIcon = itemView.findViewById(R.id.like_icon);
            commentIcon = itemView.findViewById(R.id.comment_icon);
        }
    }
}
