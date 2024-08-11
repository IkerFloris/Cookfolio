package com.example.cookfolio.AccountManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookfolio.AmplifyConfig.AWSConfig;
import com.example.cookfolio.R;

public class ConfirmAccountActivity extends AppCompatActivity {

    EditText etCode;
    Button btnVerify;
    AWSConfig AWSConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_account);
        AWSConfig = new AWSConfig(getApplicationContext());
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        etCode = findViewById(R.id.etCode);
        btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString();
                AWSConfig.confirmAccount(username, code);
            }
        });
    }
}