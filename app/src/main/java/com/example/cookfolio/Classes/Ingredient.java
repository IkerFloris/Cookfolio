package com.example.cookfolio.Classes;

import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Ingredient {
    private String name;
    private int quantity;
    private String unitat;

    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public Ingredient(String name, int quantity, String unitat) {
        this.name = name;
        this.quantity = quantity;
        this.unitat = unitat;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnitat(){
        return this.unitat;
    }

    public CompletableFuture<String> generateUnitatMesura() {
        return ApiCalls.getUnitatMesura(this.name)
                .thenApply(unitatMesura -> {
                    this.unitat = unitatMesura;  // Actualizar la unidad internamente
                    return unitatMesura;  // Retornar la unidad
                })
                .exceptionally(error -> {
                    Log.e("API Error", "Error fetching unitatMesura", error);
                    return "";  // Retorna una cadena vacía o valor predeterminado en caso de error
                });
    }

}
