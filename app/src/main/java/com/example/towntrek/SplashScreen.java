package com.example.towntrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Map;
import java.util.Set;

public class SplashScreen extends AppCompatActivity {

    TextView slogan;
    LottieAnimationView lottie;
    SharedPreferences tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        slogan = findViewById(R.id.slogan);
        lottie = findViewById(R.id.lottie);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tutorial = getSharedPreferences("tutorial", MODE_PRIVATE);
                boolean isBeginner = tutorial.getBoolean("Beginner", true);

                if(isBeginner) {
                    SharedPreferences.Editor editor = tutorial.edit();
                    editor.putBoolean("Beginner", false);
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(), TutorialScreen.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getApplicationContext(), RetailerStartupScreen.class);
                    startActivity(i);
                }
                finish();
            }
        }, 3000);
    }
}