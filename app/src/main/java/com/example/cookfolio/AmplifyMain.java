package com.example.cookfolio;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class AmplifyMain extends Application {

    public void onCreate(){
        super.onCreate();
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("CookfolioConfig", "All plugin added correctly");
        } catch (AmplifyException e) {
            e.printStackTrace();
            Log.e("CookfolioConfig", "Could not initialize amplify", e);
        }

    }

}
