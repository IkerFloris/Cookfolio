package com.example.cookfolio.Classes;

import android.util.Log;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ApiCalls {

    public static void addFirstTimeLoggerBDD(String username) {
        RestOptions options = RestOptions.builder()
                .addPath("/Cookfolio/firstTimeLogger?nomUsuari=" + username)
                .build();

        Amplify.API.get(options,
                response -> {
                    byte[] rawBytes = response.getData().getRawBytes();
                    String responseData = new String(rawBytes);

                    if (responseData.isEmpty() || responseData.equals("[]")) {
                        Log.i("AmplifyCognito", "GET succeeded with no data. Generating random integer and executing PUT call.");

                        // Generar un número aleatorio
                        int randomId = new Random().nextInt(1000000); // Ajustar el rango si es necesario

                        // Preparar el cuerpo de la solicitud PUT
                        String putBody = "{\"idRebost\": " + randomId + ", \"nomUsuari\": \"" + username + "\"}";

                        RestOptions putOptions = RestOptions.builder()
                                .addPath("/Cookfolio/addUser")
                                .addBody(putBody.getBytes())
                                .build();

                        // Ejecutar la llamada PUT
                        Amplify.API.put(putOptions,
                                putResponse -> Log.i("AmplifyCognito", "PUT succeeded: " + putResponse.getData().asString()),
                                putError -> Log.e("AmplifyCognito", "PUT failed", putError)
                        );
                    } else {
                        Log.i("AmplifyCognito", "GET succeeded: " + responseData);
                    }
                },
                error -> Log.e("AmplifyCognito", "GET failed", error)
        );
    }


    private static void addNewRebost(int randomId, String username){

        try {
            // Crear el JSON con los valores idRebost y nom
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idRebost", randomId);
            jsonBody.put("nom", "Rebost " + username);

            // Crear las opciones REST para la llamada PUT
            RestOptions options = RestOptions.builder()
                    .addPath("/Cookfolio/addToRebost")
                    .addBody(jsonBody.toString().getBytes())
                    .build();

            // Realizar la llamada PUT a la API
            Amplify.API.put(options,
                    response -> {
                        // Manejar la respuesta exitosa
                        Log.i("AmplifyAPI", "Rebost added/updated successfully: " + response.getData().asString());
                    },
                    error -> {
                        // Manejar el error en caso de que falle la llamada
                        Log.e("AmplifyAPI", "Failed to add/update Rebost", error);
                    }
            );
        } catch (Exception e) {
            // Manejar cualquier excepción al construir la solicitud
            e.printStackTrace();
        }

    }

    public static CompletableFuture<List<String>> getAllProductNames() {

        // Creamos un CompletableFuture para manejar la operación asíncrona
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        String path = "/Cookfolio/getAllProductNames"; // Ruta de tu API

        // Construimos las opciones para la solicitud REST
        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        // Realizamos la llamada GET a la API
        Amplify.API.get(options,
                response -> {
                    // Manejar la respuesta en formato byte
                    byte[] rawBytes = response.getData().getRawBytes();
                    String responseData = new String(rawBytes);  // Convertimos la respuesta en String

                    try {
                        // Verificamos si la respuesta no está vacía o nula
                        if (responseData != null && !responseData.isEmpty() && !responseData.equals("[]")) {
                            JSONArray jsonArray = new JSONArray(responseData);  // Convertimos la respuesta en un JSONArray
                            List<String> productNames = new ArrayList<>();

                            // Iteramos sobre el array para extraer los nombres de productos
                            for (int i = 0; i < jsonArray.length(); i++) {
                                productNames.add(jsonArray.getString(i));  // Agregamos cada nombre a la lista
                            }

                            // Completar el future con la lista de nombres de productos
                            future.complete(productNames);
                        } else {
                            // Si no hay datos, completamos con una lista vacía
                            future.complete(new ArrayList<>());
                        }
                    } catch (Exception e) {
                        // En caso de una excepción, completamos el future excepcionalmente
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    // En caso de error en la solicitud, registramos el error y completamos el future excepcionalmente
                    Log.e("AmplifyAPI", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching product names: " + error.getMessage()));
                }
        );

        // Devolvemos el future, que eventualmente contendrá los nombres de los productos o un error
        return future;
    }

    public static CompletableFuture<String> getUnitatMesura(String nomProducte) {
        // Crear un CompletableFuture para manejar la operación asíncrona
        CompletableFuture<String> future = new CompletableFuture<>();

        // Ruta de la API con el parámetro `nomProducte`
        String path = "/Cookfolio/getUnitatMesura?nomProducte=" + nomProducte;

        // Crear las opciones REST para la solicitud GET
        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        // Realizar la llamada GET a la API
        Amplify.API.get(options,
                response -> {
                    try {
                        // Obtener la respuesta en formato String
                        String responseData = response.getData().asString();

                        // Procesar la respuesta JSON y extraer la `unitatMesura`
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String unitatMesura = jsonResponse.getString("unitatMesura");

                        // Completar el future con la `unitatMesura`
                        future.complete(unitatMesura);
                    } catch (Exception e) {
                        // Completar con una excepción si hay un error al procesar la respuesta
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    // Manejo del error si la llamada GET falla
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching unitatMesura: " + error.getMessage()));
                }
        );

        return future;
    }

    public static CompletableFuture<Integer> getUserID(String nomUsuari){

        CompletableFuture<Integer> future = new CompletableFuture<>();

        String path = "/Cookfolio/getUserID"; // Ruta de tu API

        // Construimos las opciones para la solicitud REST
        RestOptions options = RestOptions.builder()
                .addPath(path + "?nomUsuari=" + nomUsuari)
                .build();

        // Realizamos la llamada a la API
        Amplify.API.get(options,
                response -> {
                    byte[] rawBytes = response.getData().getRawBytes();
                    String responseData = new String(rawBytes);
                    try {
                        if (responseData != null && !responseData.isEmpty() && !responseData.equals("[]")) {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            int idUsuari = jsonResponse.getInt("idUsuari");
                            future.complete(idUsuari);  // Completa el futuro con el idUsuari
                        } else {
                            future.complete(-1);  // Devuelve -1 si la respuesta es vacía o no válida
                        }
                    } catch (Exception e) {
                        future.completeExceptionally(e);  // Manejo de excepciones
                    }
                },
                error -> {
                    Log.e("AmplifyAPI", "GET failed", error);
                    future.complete(-1);  // Devuelve -1 en caso de error
                }
        );

        return future;
    }

    public static CompletableFuture<Integer> createRecipe(Recipe recipe) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idRecepta", recipe.getRecipeId());
            jsonBody.put("idUsuari", recipe.getUserId());
            jsonBody.put("nom", recipe.getName());
            jsonBody.put("descripcio", recipe.getDescription());
            jsonBody.put("tempsDeCoccio", recipe.getCookingTime());
            jsonBody.put("dificultat", recipe.getDifficulty());
            jsonBody.put("dataCreacio", recipe.getTimestamp());
            jsonBody.put("imatgeRecepta", recipe.getRecipeImage());

            RestOptions options = RestOptions.builder()
                    .addPath("/Cookfolio/createRecipe")
                    .addBody(jsonBody.toString().getBytes())
                    .build();

            Amplify.API.put(options,
                    response -> {
                        Log.i("AmplifyAPI", "Recipe created successfully: " + response.getData().asString());
                        // Devuelve el id de la receta creada (lo obtenemos del propio recipe)
                        future.complete(recipe.getRecipeId());
                    },
                    error -> {
                        Log.e("AmplifyAPI", "Error creating recipe", error);
                        future.completeExceptionally(error);
                    }
            );
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }


    public static CompletableFuture<Integer> getIdRebost(int idUsuari) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        // Ruta de la API con el parámetro idUsuari
        String path = "/Cookfolio/getIdRebost?idUsuari=" + idUsuari;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        Amplify.API.get(options,
                response -> {
                    try {
                        // Procesar la respuesta JSON y extraer el idRebost
                        String responseData = response.getData().asString();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        int idRebost = jsonResponse.getInt("idRebost");

                        // Completar el future con el idRebost
                        future.complete(idRebost);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching idRebost: " + error.getMessage()));
                }
        );

        return future;
    }
    public static CompletableFuture<List<Product>> getProductesRebost(int idRebost) {
        CompletableFuture<List<Product>> future = new CompletableFuture<>();

        String path = "/Cookfolio/getProductesRebost?idRebost=" + idRebost;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        Amplify.API.get(options,
                response -> {
                    try {
                        // Procesar la respuesta JSON para obtener los productos
                        String responseData = response.getData().asString();
                        JSONArray jsonArray = new JSONArray(responseData);

                        List<Product> productList = new ArrayList<>();
                        List<CompletableFuture<Product>> futures = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonProducteRebost = jsonArray.getJSONObject(i);
                            int idProducte = jsonProducteRebost.getInt("idProducte");
                            int quantitat = jsonProducteRebost.getInt("quantitat");

                            // Obtener detalles del producto
                            CompletableFuture<Product> futureProduct = getProducteDetails(idProducte, quantitat);
                            futures.add(futureProduct);
                        }

                        // Esperar a que todos los futuros se completen y recopilar los resultados
                        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

                        allOfFutures.thenRun(() -> {
                            // Recopilar los resultados después de que todos los futuros se completen
                            futures.forEach(f -> {
                                try {
                                    productList.add(f.join()); // join() obtiene el resultado del futuro
                                } catch (Exception e) {
                                    future.completeExceptionally(e);
                                }
                            });
                            // Completar el future principal con la lista de productos
                            future.complete(productList);
                        }).exceptionally(ex -> {
                            future.completeExceptionally(ex);
                            return null;
                        });

                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching products: " + error.getMessage()));
                }
        );

        return future;
    }

    // Obtener detalles del producto dado su idProducte
    private static CompletableFuture<Product> getProducteDetails(int idProducte, int quantitat) {
        CompletableFuture<Product> future = new CompletableFuture<>();

        String path = "/Cookfolio/getProductDetails?idProducte=" + idProducte;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        Amplify.API.get(options,
                response -> {
                    try {
                        // Procesar la respuesta JSON para obtener detalles del producto
                        String responseData = response.getData().asString();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String nom = jsonResponse.getString("nom");
                        String unitatMesura = jsonResponse.getString("unitatMesura");

                        // Crear el objeto Product y completar el future
                        Product product = new Product(nom, quantitat, unitatMesura);
                        future.complete(product);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching product details: " + error.getMessage()));
                }
        );

        return future;
    }


    public static void addProductToRebost(int idRebost, int idProducte, int quantitat) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idRebost", idRebost);
            jsonBody.put("idProducte", idProducte);
            jsonBody.put("quantitat", quantitat);

            RestOptions options = RestOptions.builder()
                    .addPath("/Cookfolio/addProductToRebost")
                    .addBody(jsonBody.toString().getBytes())
                    .build();

            Amplify.API.put(options,
                    response -> Log.i("API Success", "Product added to rebost successfully"),
                    error -> Log.e("API Error", "Error adding product to rebost", error)
            );
        } catch (Exception e) {
            Log.e("API Error", "Error building PUT request", e);
        }
    }

    public static CompletableFuture<Integer> getProductID(String productName) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        // Ruta de la API con el parámetro `nomProducte`
        String path = "/Cookfolio/getProductID?nomProducte=" + productName;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        Amplify.API.get(options,
                response -> {
                    try {
                        // Capturar la respuesta completa en String
                        String responseData = response.getData().asString();
                        Log.d("API Response", "Response: " + responseData);

                        // Comprobar si la respuesta es JSON
                        if (responseData.startsWith("{")) {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            int idProducte = jsonResponse.getInt("idProducte");
                            future.complete(idProducte);
                        } else {
                            // Si no es un JSON válido, lanzar un error
                            future.completeExceptionally(new Exception("Unexpected response format: " + responseData));
                        }
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching product ID: " + error.getMessage()));
                }
        );

        return future;
    }

    public static CompletableFuture<Void> deleteProductFromRebost(int idRebost, int productId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Construir la ruta para eliminar el producto del rebost usando el nombre del producto
        String path = "/Cookfolio/deleteProductFromRebost?idRebost=" + idRebost + "&idProducte=" + productId;

        // Crear las opciones de la solicitud DELETE
        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        // Hacer la llamada a la API usando Amplify
        Amplify.API.delete(options,
                response -> {
                    // Completar el future exitosamente
                    future.complete(null);
                },
                error -> {
                    Log.e("API Error", "Error deleting product from rebost: " + error.getMessage());
                    future.completeExceptionally(new Exception("Error deleting product from rebost: " + error.getMessage()));
                }
        );

        return future;
    }

    public static CompletableFuture<List<Recipe>> getAllRecipesExceptUser(int idUsuari) {
        CompletableFuture<List<Recipe>> future = new CompletableFuture<>();

        // Construir la ruta para la llamada API
        String path = "/Cookfolio/getAllRecipesMainFeed?idUsuari=" + idUsuari;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        // Hacer la llamada API usando Amplify
        Amplify.API.get(options,
                response -> {
                    try {
                        List<Recipe> recipeList = new ArrayList<>();
                        String responseData = response.getData().asString();
                        Log.d("API Response", "Data received: " + responseData);
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Parsear los datos y construir la lista de recetas
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonRecipe = jsonArray.getJSONObject(i);
                            Recipe recipe = new Recipe(
                                    jsonRecipe.getInt("idRecepta"),
                                    jsonRecipe.getInt("idUsuari"),
                                    jsonRecipe.getString("dataCreacio"),
                                    jsonRecipe.getString("descripcio"),
                                    jsonRecipe.getString("imatgeRecepta"),
                                    jsonRecipe.getString("nom"),
                                    jsonRecipe.getString("dificultat"),
                                    jsonRecipe.getInt("tempsDeCoccio")
                            );
                            recipeList.add(recipe);
                        }

                        // Completar el future con la lista de recetas
                        future.complete(recipeList);

                    } catch (Exception e) {
                        Log.e("API Error", "Exception parsing response", e);
                        future.completeExceptionally(e);
                    }
                },
                error -> {
                    Log.e("API Error", "GET failed", error);
                    future.completeExceptionally(new Exception("Error fetching recipes: " + error.getMessage()));
                }
        );

        return future;
    }

    public static CompletableFuture<String> getUsernameByUserID(int userID) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Construir la ruta para la llamada API
        String path = "/Cookfolio/getUsername?idUsuari=" + userID;

        RestOptions options = RestOptions.builder()
                .addPath(path)
                .build();

        // Hacer la llamada API usando Amplify
        Amplify.API.get(options,
                response -> {
                    try {
                        String responseData = response.getData().asString();
                        JSONObject jsonResponse = new JSONObject(responseData);

                        // Verificar si hay un campo de error o si obtenemos el username
                        if (jsonResponse.has("nomUsuari")) {
                            String username = jsonResponse.getString("nomUsuari");
                            future.complete(username);
                        } else if (jsonResponse.has("message")) {
                            future.completeExceptionally(new Exception(jsonResponse.getString("message")));
                        }
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                },
                error -> future.completeExceptionally(new Exception("Error fetching username: " + error.getMessage()))
        );

         return future;
    }

    public static CompletableFuture<Void> addIngredientToRecipe(int idRecepta, Ingredient ingredient) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Primero necesitamos obtener el idProducte y la unitatMesura antes de hacer la llamada PUT
        CompletableFuture<Integer> futureProductId = getProductID(ingredient.getName());
        CompletableFuture<String> futureUnitatMesura = getUnitatMesura(ingredient.getName());

        CompletableFuture.allOf(futureProductId, futureUnitatMesura).thenAccept(voidResult -> {
            try {
                int idProducte = futureProductId.get();
                String unitatMesura = futureUnitatMesura.get();

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("idRecepta", idRecepta);
                jsonBody.put("idProducte", idProducte);
                jsonBody.put("quantitat", ingredient.getQuantity());
                jsonBody.put("unitats", unitatMesura);
                jsonBody.put("preu", 0);

                RestOptions options = RestOptions.builder()
                        .addPath("/Cookfolio/addIngredientToRecipe")
                        .addBody(jsonBody.toString().getBytes())
                        .build();

                Amplify.API.put(options,
                        response -> {
                            Log.i("API Success", "Ingredient added successfully");
                            future.complete(null);
                        },
                        error -> {
                            Log.e("API Error", "Error adding ingredient", error);
                            future.completeExceptionally(error);
                        }
                );

            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).exceptionally(error -> {
            future.completeExceptionally(error);
            return null;
        });

        return future;
    }

}
