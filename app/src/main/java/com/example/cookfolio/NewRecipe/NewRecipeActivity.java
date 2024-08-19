package com.example.cookfolio.NewRecipe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.example.cookfolio.AmplifyConfig.AWSConfig;
import com.example.cookfolio.Classes.ApiCalls;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.Classes.Ingredient;
import com.example.cookfolio.Perfil_Despensa.ProfileActivity;
import com.example.cookfolio.R;
import com.example.cookfolio.Search.SearchActivity;
import com.example.cookfolio.ui.home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NewRecipeActivity extends AppCompatActivity {

    // Campos para la receta
    private EditText titleEditText, descriptionEditText, cookingTimeEditText;
    private Spinner difficultySpinner;
    private ImageView recipeImageView;
    private Button addRecipeButton;

    private Uri selectedImageUri;
    private String recipeImageUrl;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    AWSConfig awsConfig;
    private String username;

    // Campos para gestionar los ingredientes
    private Button addIngredientButton;
    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_creation);

        // Inicializamos las vistas
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipes);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_recipes){
                return true;
            }
            else if (item.getItemId() == R.id.bottom_search) {
                Intent navInt = new Intent(getApplicationContext(), SearchActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_home) {
                Intent navInt = new Intent(getApplicationContext(), HomeActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (item.getItemId() == R.id.bottom_profile) {
                Intent navInt = new Intent(getApplicationContext(), ProfileActivity.class);
                navInt.putExtra("username", username);
                startActivity(navInt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        awsConfig = new AWSConfig(getApplicationContext());
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        cookingTimeEditText = findViewById(R.id.cookingTimeEditText);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        recipeImageView = findViewById(R.id.recipeImageView);
        addRecipeButton = findViewById(R.id.addRecipeButton);

        // Inicializamos las vistas para los ingredientes
        addIngredientButton = findViewById(R.id.addIngredientButton);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);

        // Configuramos el RecyclerView para los ingredientes
        ingredientAdapter = new IngredientAdapter(ingredientList);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        // Configuramos el evento para añadir un ingrediente
        addIngredientButton.setOnClickListener(v -> showAddIngredientDialog());

        // Configuramos el evento para seleccionar una imagen
        recipeImageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        });

        // Watchers para verificar que todos los campos estén completos antes de habilitar el botón
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);
        cookingTimeEditText.addTextChangedListener(textWatcher);

        addRecipeButton.setOnClickListener(v -> uploadImageToS3AndSaveRecipe());
    }

    private void showAddIngredientDialog() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Ingrediente");
        builder.setView(dialogView);

        // Obtener referencias a los componentes del diálogo
        Spinner productSpinner = dialogView.findViewById(R.id.product_spinner);
        EditText quantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);

        // Llamar a la API para obtener los nombres de los productos
        ApiCalls.getAllProductNames().thenAccept(productNames -> {
            // Crear un ArrayAdapter con los nombres de los productos
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Establecer el adaptador en el Spinner
            runOnUiThread(() -> productSpinner.setAdapter(adapter));
        }).exceptionally(error -> {
            // Manejo de errores si la llamada a la API falla
            Log.e("API Error", "Error fetching product names", error);
            return null;
        });

        // Configurar los botones del diálogo
        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String selectedProduct = productSpinner.getSelectedItem().toString();
            String quantityStr = quantityEditText.getText().toString();

            if (!quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                Ingredient ingredient = new Ingredient(selectedProduct, quantity);

                // Asegurarnos de que la actualización del RecyclerView se realice en el hilo principal
                runOnUiThread(() -> {
                    ingredientList.add(ingredient);
                    ingredientAdapter.notifyDataSetChanged();
                });

                Log.d("AddIngredient", "Añadiendo ingrediente: " + selectedProduct + " Cantidad: " + quantity);
            }
        });


        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // Mostrar el diálogo
        builder.create().show();
    }



    private int generateRandomNum() {
        int randomNumber = (int) (Math.random() * 100000) + 1;
        return randomNumber;
    }

    private String generateRandomFileName(){
        int randomNumber = generateRandomNum();
        return randomNumber + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                recipeImageView.setImageURI(selectedImageUri);
                checkFieldsForEmptyValues();
            }
        }
    }

    private void uploadImageToS3AndSaveRecipe() {
        if (selectedImageUri != null) {
            String randomFileName = generateRandomFileName();
            String filePath = username + "/" + randomFileName;

            // Crea un archivo temporal para subirlo
            File tempFile;
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                tempFile = File.createTempFile("temp_image", ".jpg", getCacheDir());
                OutputStream outputStream = new FileOutputStream(tempFile);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    outputStream.write(inputStream.readAllBytes());
                } else {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                Toast.makeText(this, "Error al crear archivo temporal", Toast.LENGTH_SHORT).show();
                return;
            }

            // Subir la imagen a S3
            Amplify.Storage.uploadFile(
                    filePath,
                    tempFile,
                    result -> {
                        recipeImageUrl = result.getPath(); // Guarda la URL del archivo subido
                        saveRecipe(); // Guardar la receta con la URL de la imagen
                    },
                    error -> Toast.makeText(this, "Fallo al subir la imagen a S3", Toast.LENGTH_LONG).show()
            );
        }
    }

    private void checkFieldsForEmptyValues() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String cookingTime = cookingTimeEditText.getText().toString();

        if (!title.isEmpty() && !description.isEmpty() && !cookingTime.isEmpty() && selectedImageUri != null) {
            addRecipeButton.setEnabled(true);
            addRecipeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_700));
        } else {
            addRecipeButton.setEnabled(false);
            addRecipeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveRecipe() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaHoraActual.format(formato);

        CompletableFuture<Integer> userID = ApiCalls.getUserID(username);
        Amplify.Storage.getUrl(
                recipeImageUrl,
                result -> {
                    Recipe recipe = null;
                    try {
                        recipe = new Recipe(
                                generateRandomNum(),
                                userID.get(),
                                fechaFormateada,
                                descriptionEditText.getText().toString(),
                                recipeImageUrl,
                                titleEditText.getText().toString(),
                                difficultySpinner.getSelectedItem().toString(),
                                Integer.parseInt(cookingTimeEditText.getText().toString())
                        );
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Guardar la receta
                    ApiCalls.createRecipe(recipe).thenAccept(idRecepta -> {
                        // Después de guardar la receta, guardar los ingredientes en la base de datos
                        Log.i("DEBUG", "Received idRecepta: " + idRecepta);
                        Log.i("DEBUG", "Ingredient list size: " + ingredientList.size());
                        for (Ingredient ingredient : ingredientList) {
                            Log.i("DEBUG", "Adding ingredient: " + ingredient.getName());
                            ApiCalls.addIngredientToRecipe(idRecepta, ingredient)
                                    .thenAccept(aVoid -> Log.i("API Success", "Ingredient added to recipe"))
                                    .exceptionally(error -> {
                                        Log.e("API Error", "Failed to add ingredient", error);
                                        return null;
                                    });
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(this, "¡Receta creada!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent()); // Reinicia la actividad
                        });
                    });
                },
                error -> Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_LONG).show()
        );
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, NewRecipeActivity.class);
    }
}
