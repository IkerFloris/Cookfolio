package com.example.cookfolio;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class AmplifyMain extends Application {

    public void onCreate(){
        super.onCreate();
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Log.i("CookfolioCognito", "Cognito plugin added correctly");
        } catch (AmplifyException e) {
            e.printStackTrace();
            Log.e("CookfolioCognito", "Could not initialize amplify", e);
        }

        try {
            Amplify.configure(getApplicationContext());
            Log.i("Cookfolio", "Initialized amplify correctly");
        } catch (AmplifyException error){
            Log.e("Cookfolio", "Could not initialize amplify", error);
        }
    }

}
