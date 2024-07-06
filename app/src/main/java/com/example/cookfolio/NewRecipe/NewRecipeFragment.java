package com.example.cookfolio.NewRecipe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cookfolio.R;
import com.example.cookfolio.Classes.Recipe;
import com.example.cookfolio.Containers.RecipeContainer;

public class NewRecipeFragment extends Fragment {

    private EditText titleEditText, descriptionEditText, cookingTimeEditText;
    private Spinner difficultySpinner;
    private ImageView recipeImageView;
    private Button addRecipeButton;

    private String recipeImageUrl;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_creation, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        cookingTimeEditText = view.findViewById(R.id.cookingTimeEditText);
        difficultySpinner = view.findViewById(R.id.difficultySpinner);
        recipeImageView = view.findViewById(R.id.recipeImageView);
        addRecipeButton = view.findViewById(R.id.addRecipeButton);

        recipeImageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        });


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

        addRecipeButton.setOnClickListener(v -> saveRecipe());

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                recipeImageView.setImageURI(selectedImageUri);
                recipeImageUrl = selectedImageUri.toString();
                checkFieldsForEmptyValues();
            }
        }
    }


    private void checkFieldsForEmptyValues() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String cookingTime = cookingTimeEditText.getText().toString();

        if (!title.isEmpty() && !description.isEmpty() && !cookingTime.isEmpty() && recipeImageUrl != null) {
            addRecipeButton.setEnabled(true);
            addRecipeButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.teal_700));
        } else {
            addRecipeButton.setEnabled(false);
            addRecipeButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), android.R.color.darker_gray));
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveRecipe() {
        Recipe recipe = new Recipe(
                "Chef Bezos",
                "01/01/01",
                descriptionEditText.getText().toString(),
                "profileimg.xml",
                recipeImageUrl,
                titleEditText.getText().toString(),
                difficultySpinner.getSelectedItem().toString(),
                Integer.parseInt(cookingTimeEditText.getText().toString())


        );

        RecipeContainer.addRecipe(recipe);

        Toast.makeText(getContext(), "¡Receta creada!", Toast.LENGTH_SHORT).show();

        // Reiniciar el fragmento
        getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
