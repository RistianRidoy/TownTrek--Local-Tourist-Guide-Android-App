package com.example.towntrek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int CAMERA_REQ_CODE = 100;
    static final float END_SCALE = 0.7f;

    CardView cardProfile, cardPlan, cardSearch, cardEnjoy, cardVisit, cardLogout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    ImageView imageView;
    Button upload_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        cardProfile = findViewById(R.id.cardProfile);
        cardPlan = findViewById(R.id.cardPlan);
        cardSearch = findViewById(R.id.cardSearch);
        cardEnjoy = findViewById(R.id.cardEnjoy);
        cardVisit = findViewById(R.id.cardVisit);
        cardLogout = findViewById(R.id.cardLogout);
        drawerLayout= findViewById(R.id.layout_Drawer);
        navigationView= findViewById(R.id.Navigation_View);
        menuIcon = findViewById(R.id.menu_icon);


        navigationDrawer();

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });
        cardPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlanningScreen.class));
            }
        });
        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        cardEnjoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NearbyRestaurantsActivity.class));
            }
        });
        cardVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TouristSpotsActivity.class));
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
            }
        });
    }

    private void navigationDrawer() {
        // navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundResource(R.color.primary);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // animateNavigationDrawer();
    }

    /*private void animateNavigationDrawer() {
    //Add any color or remove it to use the default one!
    //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
         });
    }*/

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch(item.getItemId())
        {
            case R.id.nav_all_categories:
                Intent intent = new Intent(getApplicationContext(),AllCategories.class);
                startActivity(intent);
                break;
        }
        switch(item.getItemId())
        {
            case R.id.nav_add_missing_place:
                Intent intent = new Intent(getApplicationContext(),BucketListActivity.class);
                startActivity(intent);
                break;
        }
        switch(item.getItemId())
        {
            case R.id.nav_share:
                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT,"application/vnd.android.package-archive");
                startActivity(Intent.createChooser(intent2,"ShareVia"));
                break;
        }
        switch(item.getItemId())
        {
            case R.id.nav_rate_us:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                            getPackageName())));

                } catch (Exception ex) {
                    startActivity(new
                            Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=" +
                            getPackageName())));
                }
                break;
        }

        switch(item.getItemId()) {
            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), RetailerStartupScreen.class);
                startActivity(intent);
                break;
        }
        switch(item.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_restaurants:
                Intent intent1 = new Intent(getApplicationContext(), NearbyRestaurantsActivity.class);
                intent1.putExtra("defaultOption", "restaurant");
                startActivity(intent1);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_hotels:
                Intent intent1 = new Intent(getApplicationContext(), NearbyRestaurantsActivity.class);
                intent1.putExtra("defaultOption", "restaurant");
                startActivity(intent1);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_banks:
                Intent intent1 = new Intent(getApplicationContext(), NearbyRestaurantsActivity.class);
                intent1.putExtra("defaultOption", "restaurant");
                startActivity(intent1);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_hospitals:
                Intent intent1 = new Intent(getApplicationContext(), NearbyRestaurantsActivity.class);
                intent1.putExtra("defaultOption", "restaurant");
                startActivity(intent1);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_search:
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}