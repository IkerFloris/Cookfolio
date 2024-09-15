package com.example.cookfolio.ui.home;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.Ingredient;
import com.example.cookfolio.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;

    public IngredientAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientName;
        private TextView ingredientQuantity;
        private TextView ingredientUnity;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientNameTextView); // Asegúrate de que este ID exista en tu layout item_ingredient.xml
            ingredientQuantity = itemView.findViewById(R.id.ingredientQuantityTextView); // Asegúrate de que este ID exista en tu layout item_ingredient.xml
            ingredientUnity = itemView.findViewById(R.id.ingredientUnitatTextview); // Asegúrate de que este ID exista en tu layout item_ingredient.xml
        }

        public void bind(Ingredient ingredient) {
            ingredientName.setText(ingredient.getName());
            ingredientQuantity.setText(ingredient.getQuantity());
            ingredientQuantity.setText(ingredient.getUnitat());
        }
    }
}

