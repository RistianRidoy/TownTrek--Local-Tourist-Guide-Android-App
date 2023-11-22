package com.example.towntrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OTPValidation extends AppCompatActivity {

    Button account_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);

        account_verified = findViewById(R.id.verify_code);

        account_verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                Toast.makeText(OTPValidation.this, "Your account is verified. Now you can log in.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}