package com.example.cookfolio.NewRecipe;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.Ingredient;
import com.example.cookfolio.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        // Obtener el ingrediente actual
        Ingredient ingredient = ingredients.get(position);

        // Establecer temporalmente los valores
        holder.ingredientNameTextView.setText(ingredient.getName());
        holder.ingredientQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));

        // Limpiar el campo de la unidad de medida mientras se obtiene
        holder.ingredientUnitatTextView.setText("");
        holder.ingredientUnitatTextView.setTag(ingredient.getName());

        // Llamada asíncrona para obtener la unidad de medida
        ingredient.generateUnitatMesura().thenAccept(unitat -> {
            // Verificamos que la respuesta corresponde al ingrediente actual basado en el tag
            if (holder.ingredientUnitatTextView.getTag().equals(ingredient.getName())) {
                holder.itemView.post(() -> {
                    holder.ingredientUnitatTextView.setText(unitat);
                });
            }
        }).exceptionally(error -> {
            holder.itemView.post(() -> {
                if (holder.ingredientUnitatTextView.getTag().equals(ingredient.getName())) {
                    holder.ingredientUnitatTextView.setText("Error");
                }
            });
            return null;
        });
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameTextView;
        TextView ingredientQuantityTextView;
        TextView ingredientUnitatTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            ingredientQuantityTextView = itemView.findViewById(R.id.ingredientQuantityTextView);
            ingredientUnitatTextView = itemView.findViewById(R.id.ingredientUnitatTextview);
        }
    }
}

