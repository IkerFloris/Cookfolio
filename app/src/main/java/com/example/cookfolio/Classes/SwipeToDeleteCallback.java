package com.example.cookfolio.Classes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Perfil_Despensa.DespensaProductAdapter;

import java.util.concurrent.ExecutionException;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private DespensaProductAdapter mAdapter;

    public SwipeToDeleteCallback(DespensaProductAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT);  // Desliza a la derecha
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // No necesitamos mover elementos, así que devuelve false
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Llamar al método de eliminación en el adapter cuando se desliza
        int position = viewHolder.getAdapterPosition();
        try {
            mAdapter.deleteItem(position);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
