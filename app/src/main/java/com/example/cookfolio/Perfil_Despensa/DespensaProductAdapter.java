package com.example.cookfolio.Perfil_Despensa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.Product;
import com.example.cookfolio.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DespensaProductAdapter extends RecyclerView.Adapter<DespensaProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private DespensaActivity despensaActivity;

    public DespensaProductAdapter(List<Product> productList, DespensaActivity despensaActivity) {
        this.productList = productList;
        this.despensaActivity = despensaActivity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.despensa_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productUnit.setText(product.getUnit());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productUnit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productUnit = itemView.findViewById(R.id.product_unit);
        }
    }

    public void deleteItem(int position) throws ExecutionException, InterruptedException {
        despensaActivity.deleteItemDespensa(productList.get(position));
    }
}
