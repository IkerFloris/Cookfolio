package com.example.cookfolio.Classes;

import android.util.Log;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;

import org.json.JSONObject;

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

                        // Generate a random integer
                        int randomId = new Random().nextInt(1000000); // Adjust the range as needed

                        // Prepare the data for the PUT call
                        String putBody = "{\"nomUsuari\": \"" + username + "\"}";

                        RestOptions putOptions = RestOptions.builder()
                                .addPath("/Cookfolio/addUser")
                                .addBody(putBody.getBytes())
                                .build();

                        // Execute the PUT call
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

    public static void createRecipe(int idRecepta, int idUsuari, String nom, String descripcio, int tempsDeCoccio, String dificultat, String dataCreacio, String imatgeRecepta) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idRecepta", idRecepta);
            jsonBody.put("idUsuari", idUsuari);
            jsonBody.put("nom", nom);
            jsonBody.put("descripcio", descripcio);
            jsonBody.put("tempsDeCoccio", tempsDeCoccio);
            jsonBody.put("dificultat", dificultat);
            jsonBody.put("dataCreacio", dataCreacio);
            jsonBody.put("imatgeRecepta", imatgeRecepta);

            RestOptions options = RestOptions.builder()
                    .addPath("/Cookfolio/createRecipe")
                    .addBody(jsonBody.toString().getBytes())
                    .build();

            Amplify.API.put(options,
                    response -> {
                        Log.i("AmplifyAPI", "Recipe updated successfully: " + response.getData().asString());
                    },
                    error -> {
                        Log.e("AmplifyAPI", "Update failed", error);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
