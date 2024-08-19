package com.example.cookfolio.Perfil_Despensa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookfolio.Classes.ApiCalls;
import com.example.cookfolio.Classes.Product;
import com.example.cookfolio.Classes.SwipeToDeleteCallback;
import com.example.cookfolio.NewRecipe.NewRecipeActivity;
import com.example.cookfolio.R;
import com.example.cookfolio.Search.SearchActivity;
import com.example.cookfolio.ui.home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DespensaActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    private List<String> products = new ArrayList<>();
    private DespensaProductAdapter productAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private String username;
    private CompletableFuture<Integer> idUsuariFuture;
    private int idUsuari;
    private int idRebost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despensa_fragment);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        idUsuariFuture = ApiCalls.getUserID(username);
        try {
            idUsuari = idUsuariFuture.get(); // Bloquea hasta que se complete el futuro
            // Usar idProducte aquí
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        fetchIdRebost(idUsuari);

        // Configuramos la lista inicial de productos

        // Configuramos el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.pantry_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new DespensaProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(productAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Configuramos el Spinner (inicialmente vacío)
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, products);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Llamamos a la API para obtener los productos
        fetchProductsFromApi();

        Button addProductButton = findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(v -> showAddProductDialog());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile) {
                return true;
            } else if (item.getItemId() == R.id.bottom_search) {
                Intent navInt = new Intent(getApplicationContext(), SearchActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                Intent navInt = new Intent(getApplicationContext(), HomeActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_recipes) {
                Intent navInt = new Intent(getApplicationContext(), NewRecipeActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        Button profileButton = findViewById(R.id.navigation_profile);
        Button pantryButton = findViewById(R.id.navigation_pantry);

        profileButton.setOnClickListener(v -> navigateToProfile());
        pantryButton.setOnClickListener(v -> navigateToPantry());
    }

    private void navigateToProfile() {
        Intent navInt = new Intent(getApplicationContext(), ProfileActivity.class);
        navInt.putExtra("username", username);
        startActivity(navInt);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void navigateToPantry() {
        Intent navInt = new Intent(getApplicationContext(), DespensaActivity.class);
        navInt.putExtra("username", username);
        startActivity(navInt);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void fetchProductsFromApi() {
        // Realizamos la llamada a la API para obtener la lista de productos
        CompletableFuture<List<String>> future = ApiCalls.getAllProductNames();

        // Cuando la respuesta esté lista, la procesamos
        future.thenAccept(productNames -> {
            // Limpiamos la lista actual
            products.clear();

            // Añadimos los productos recibidos a la lista
            products.addAll(productNames);

            // Notificamos al adaptador del Spinner que los datos han cambiado
            runOnUiThread(() -> spinnerAdapter.notifyDataSetChanged());
        }).exceptionally(error -> {
            Log.e("API Error", "Error fetching product names", error);
            return null;
        });
    }

    private void showAddProductDialog() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Product");
        builder.setView(dialogView);

        // Obtener referencias a los componentes del diálogo
        Spinner productSpinner = dialogView.findViewById(R.id.product_spinner);
        EditText quantityEditText = dialogView.findViewById(R.id.quantity_edittext);

        // Asignar el adaptador del Spinner con los productos
        productSpinner.setAdapter(spinnerAdapter);

        // Configurar los botones del diálogo
        builder.setPositiveButton("Add", (dialog, which) -> {
            String selectedProduct = productSpinner.getSelectedItem().toString();
            String quantityStr = quantityEditText.getText().toString();

            int quantity = 0;
            if (!quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
            }

            // Llamar a la API para obtener la UnitatMesura
            fetchUnitatMesuraAndAddProduct(selectedProduct, quantity);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Mostrar el diálogo
        builder.create().show();
    }

    private void addProductToList(String productName, int quantity, String unit) {
        // Crear un nuevo producto y agregarlo a la lista
        Product newProduct = new Product(productName, quantity, unit);
        productList.add(newProduct);

        // Notificar al adaptador que los datos han cambiado
        runOnUiThread(() -> productAdapter.notifyDataSetChanged());
    }


    private void fetchUnitatMesuraAndAddProduct(String productName, int quantity) {
        CompletableFuture<String> future = ApiCalls.getUnitatMesura(productName);

        future.thenAccept(unitatMesura -> {
            // Agregar el producto a la lista después de obtener la `unitatMesura`
            addProductToList(productName, quantity, unitatMesura);

            // Obtener el id del producto
            CompletableFuture<Integer> futureProductId = ApiCalls.getProductID(productName);
            futureProductId.thenAccept(idProducte -> {
                // Hacer la llamada PUT para añadir el producto al rebost
                ApiCalls.addProductToRebost(idRebost, idProducte, quantity);
            }).exceptionally(error -> {
                Log.e("API Error", "Error fetching product ID", error);
                return null;
            });

        }).exceptionally(error -> {
            Log.e("API Error", "Error fetching unitatMesura", error);
            return null;
        });
    }



    public static Intent newIntent(Context context) {
        return new Intent(context, DespensaActivity.class);
    }

    private void fetchIdRebost(int idUsuari) {
        CompletableFuture<Integer> futureIdRebost = ApiCalls.getIdRebost(idUsuari);

        futureIdRebost.thenAccept(idRebost -> {
            // Guardamos el idRebost para futuras operaciones
            this.idRebost = idRebost;


            // Obtener productos relacionados con este rebost
            fetchProductesRebost(idRebost);
        }).exceptionally(error -> {
            Log.e("API Error", "Error obteniendo idRebost", error);
            return null;
        });
    }

    // Obtener los productos del rebost
    private void fetchProductesRebost(int idRebost) {
        CompletableFuture<List<Product>> futureProductes = ApiCalls.getProductesRebost(idRebost);

        futureProductes.thenAccept(productes -> {
            // Añadir los productos obtenidos a productList
            productList.addAll(productes);

            // Actualizar el adaptador del RecyclerView
            runOnUiThread(() -> productAdapter.notifyDataSetChanged());
        }).exceptionally(error -> {
            Log.e("API Error", "Error obteniendo productos del rebost", error);
            return null;
        });
    }

    public void deleteItemDespensa(Product product) throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> productID = ApiCalls.getProductID(product.getName());
        CompletableFuture<Void> future = ApiCalls.deleteProductFromRebost(idRebost, productID.get());
        future.thenRun(() -> {
            runOnUiThread(() -> {
                productList.remove(product);
                productAdapter.notifyDataSetChanged();
            });
        }).exceptionally(error -> {
            Log.e("API Error", "Error deleting product from rebost: " + error.getMessage());
            return null;
        });

    }

}
