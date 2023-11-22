package com.example.towntrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {

    Button update_image;
    CardView savings_cardview, bucketlist_cardview, touredplaces_cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        update_image = findViewById(R.id.update_image);
        savings_cardview = findViewById(R.id.savings_cardview);
        bucketlist_cardview = findViewById(R.id.bucketlist_cardview);
        touredplaces_cardview = findViewById(R.id.touredplaces_cardview);

        update_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
            }
        });
        savings_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SavingsActivity.class));
            }
        });
        bucketlist_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BucketListActivity.class));
            }
        });
        touredplaces_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TouredListActivity.class));
            }
        });
    }
}