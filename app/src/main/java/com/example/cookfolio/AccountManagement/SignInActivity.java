package com.example.cookfolio.AccountManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookfolio.AmplifyConfig.AWSConfig;
import com.example.cookfolio.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class SignInActivity extends AppCompatActivity {

    AWSConfig AWSConfig;
    EditText etUsername;
    EditText etPassword;
    Button btnSignUp;
    Button btnSignIn;
    AtomicBoolean isOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        AWSConfig = new AWSConfig(getApplicationContext());
        AWSConfig.logOut();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                AWSConfig.signIn(username,password);
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AWSConfig.loadSignUp();
            }
        });

    }
}