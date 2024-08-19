package com.example.cookfolio.AmplifyConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.cookfolio.AccountManagement.*;
import com.example.cookfolio.ui.home.HomeActivity;
import com.example.cookfolio.Classes.ApiCalls;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


public class AWSConfig {

    private Context context;
    private Handler handler;

    public AWSConfig(Context context) {

        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void signUp(String email, String username, String password){

        AuthSignUpOptions signUpOptions = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(username, password, signUpOptions,
                result -> {
                    showToast("Sign Up Successful!");
                    loadConfirm(username);
                },
                error -> showToast("Sign Up Failed: " + error.getMessage()));
    }

    public void confirmAccount(String username, String code){
        Amplify.Auth.confirmSignUp(username, code,
                result -> {
                    showToast(result.isSignUpComplete() ? "Confirm Sign Up Succeeded" : "Confirm Sign Up did not succeed");
                    loadLogin();
                },
                error -> showToast("Confirm Sign Up Failed: " + error.getMessage()));
    }

    public void signIn(String username, String password){

        Amplify.Auth.signIn(username, password,
                result -> {
                    showToast(result.isSignedIn() ? "Sign In Succeeded" : "Sign In Not Complete");
                    loadHome(username);
                    ApiCalls.addFirstTimeLoggerBDD(username);
                },
                error -> showToast("Sign In Failed: " + error.getMessage()));
    }

    private void loadConfirm(String username){
        Intent intent = new Intent(context, ConfirmAccountActivity.class);
        intent.putExtra("username", username);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void loadLogin(){
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void loadSignUp(){
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void loadHome(String username){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("username", username.toLowerCase());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void logOut(){

        AuthSignOutOptions options = AuthSignOutOptions.builder()
                .globalSignOut(true)
                .build();

        Amplify.Auth.signOut(options, signOutResult -> {
            if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                // handle successful sign out
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                // handle partial sign out
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                // handle failed sign out
            }
        });

    }

    private void showToast(String message) {
        handler.post(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }

}
