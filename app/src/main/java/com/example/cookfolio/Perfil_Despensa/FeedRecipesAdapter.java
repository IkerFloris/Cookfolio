package com.example.cookfolio.Perfil_Despensa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookfolio.R;
import java.util.List;

public class FeedRecipesAdapter extends RecyclerView.Adapter<FeedRecipesAdapter.ViewHolder> {

    private List<Integer> recipeImages;
    private Context context;

    public FeedRecipesAdapter(Context context, List<Integer> recipeImages) {
        this.context = context;
        this.recipeImages = recipeImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeImage.setImageResource(recipeImages.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
