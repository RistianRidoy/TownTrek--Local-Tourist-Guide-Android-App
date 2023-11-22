package com.example.towntrek;

import static com.example.towntrek.MapActivity.LOCATION_PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.towntrek.MapDirectionHelpers.FetchURL;
import com.example.towntrek.MapDirectionHelpers.TaskLoadedCallback;
import com.example.towntrek.NearbyRestaurantHelpers.GetNearbyPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.squareup.picasso.Picasso;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TouristSpotsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private HashMap<String, LatLng> locationCoordinates = new HashMap<>();
    SupportMapFragment smf;
    FusedLocationProviderClient client;
    GoogleMap myMap;
    Button buttonNearbyTouristSpots;
    RelativeLayout infoWindow;
    LatLng currentLocation;
    Animation animation;
    ImageButton button_get_direction;
    Polyline currentPolyline;
    TextView nameTextView, categoryTextView, districtTextView, descTextView;
    ImageView backgroundImage;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tourist_spots);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        buttonNearbyTouristSpots = findViewById(R.id.tourist_spots_nearby);
        button_get_direction = findViewById(R.id.get_direction);
        infoWindow = findViewById(R.id.info_window);
        nameTextView = infoWindow.findViewById(R.id.name_of_place);
        categoryTextView = infoWindow.findViewById(R.id.category_type);
        districtTextView = infoWindow.findViewById(R.id.district_name);
        descTextView = infoWindow.findViewById(R.id.desc_of_place);
        backgroundImage = infoWindow.findViewById(R.id.background_image);
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        smf.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (isInsideInfoWindow(x, y)) {
            scaleGestureDetector.onTouchEvent(event); // Apply scaling to info_window
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private boolean isInsideInfoWindow(float x, float y) {
        int[] location = new int[2];
        infoWindow.getLocationOnScreen(location);
        int infoWindowLeft = location[0];
        int infoWindowTop = location[1];
        int infoWindowRight = infoWindowLeft + infoWindow.getWidth();
        int infoWindowBottom = infoWindowTop + infoWindow.getHeight();

        return x >= infoWindowLeft && x <= infoWindowRight && y >= infoWindowTop && y <= infoWindowBottom;
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 2.0f));

            infoWindow.setScaleX(scaleFactor);
            infoWindow.setScaleY(scaleFactor);
            nameTextView.setScaleX(scaleFactor);
            nameTextView.setScaleY(scaleFactor);
            categoryTextView.setScaleX(scaleFactor);
            categoryTextView.setScaleY(scaleFactor);
            districtTextView.setScaleX(scaleFactor);
            districtTextView.setScaleY(scaleFactor);
            descTextView.setScaleX(scaleFactor);
            descTextView.setScaleY(scaleFactor);
            backgroundImage.setScaleX(scaleFactor);
            backgroundImage.setScaleY(scaleFactor);
            return true;
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = myMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setCompassEnabled(true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);

            client.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    //currentLocation = new LatLng(33.738045, 73.084488);
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    myMap.addMarker(markerOptions.position(currentLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
                    List<TouristSpot> touristSpots = loadCSVData();

                    buttonNearbyTouristSpots.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("PotentialBehaviorOverride")
                        @Override
                        public void onClick(View v) {
                            double radius = 500000;
                            List<TouristSpot> nearbySpots = calculateNearbyTouristSpots(touristSpots, currentLocation, radius);
                            displayNearbyTouristSpots(nearbySpots);
                            buttonNearbyTouristSpots.setVisibility(View.INVISIBLE);

                            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    TouristSpot selectedSpot = findTouristSpotByTitle(marker.getTitle(), touristSpots);
                                    marker.showInfoWindow();
                                    if (selectedSpot != null) {
                                        showInfoWindow(selectedSpot);
                                    }
                                    else{
                                        infoWindow.setVisibility(View.INVISIBLE);
                                        button_get_direction.setVisibility(View.INVISIBLE);
                                    }
                                    return true;
                                }
                            });
                        }
                    });
                }
            });
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location layer
                if (myMap != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    myMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<TouristSpot> loadCSVData() {
        List<TouristSpot> touristSpots = new ArrayList<>();

        try {
            InputStream inputStream = getAssets().open("bd_tourist_spots");
            InputStreamReader reader = new InputStreamReader(inputStream);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            for (CSVRecord csvRecord : csvParser) {
                String placeName = csvRecord.get(0);
                String description = csvRecord.get(1);
                String category = csvRecord.get(2);
                String district = csvRecord.get(3);
                double latitude = Double.parseDouble(csvRecord.get(4));
                double longitude = Double.parseDouble(csvRecord.get(5));
                String imageUrl = csvRecord.get(6);

                TouristSpot touristSpot = new TouristSpot(placeName, description, category, district, latitude, longitude, imageUrl);
                touristSpots.add(touristSpot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return touristSpots;
    }

    private List<TouristSpot> calculateNearbyTouristSpots(List<TouristSpot> allTouristSpots, LatLng currentLocation, double radius) {
        List<TouristSpot> nearbyTouristSpots = new ArrayList<>();
        for (TouristSpot touristSpot : allTouristSpots) {
            LatLng spotLocation = new LatLng(touristSpot.getLatitude(), touristSpot.getLongitude());
            float[] distanceResults = new float[1];
            Location.distanceBetween(
                    currentLocation.latitude, currentLocation.longitude,
                    spotLocation.latitude, spotLocation.longitude,
                    distanceResults
            );
            double distance = distanceResults[0];
            if (distance <= radius) {
                nearbyTouristSpots.add(touristSpot);
            }
        }
        return nearbyTouristSpots;
    }

    private void displayNearbyTouristSpots(List<TouristSpot> nearbyTouristSpots) {
        //myMap.clear();
        for (TouristSpot touristSpot : nearbyTouristSpots) {
            LatLng spotLocation = new LatLng(touristSpot.getLatitude(), touristSpot.getLongitude());
            myMap.addMarker(new MarkerOptions().position(spotLocation).title(touristSpot.getPlaceName()));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
        }
    }

    private void showInfoWindow(TouristSpot touristSpot) {

        backgroundImage.setAlpha(0.7f);
        nameTextView.setAlpha(1.0f);
        categoryTextView.setAlpha(1.0f);
        districtTextView.setAlpha(1.0f);
        descTextView.setAlpha(1.0f);

        nameTextView.setText(touristSpot.getPlaceName());
        categoryTextView.setText("(" + touristSpot.getCategory() + ")");
        districtTextView.setText(touristSpot.getDistrict());
        descTextView.setText(touristSpot.getDescription());
        LatLng chosen_tourist_spot = new LatLng(touristSpot.getLatitude(), touristSpot.getLongitude());
        Picasso.with(this).load(touristSpot.getImageUrl()).into(backgroundImage);

        animation = AnimationUtils.loadAnimation(TouristSpotsActivity.this, R.anim.slide_down);
        infoWindow.setAnimation(animation);
        infoWindow.setVisibility(View.VISIBLE);
        button_get_direction.setVisibility(View.VISIBLE);
        button_get_direction.setPadding(20,13,20,13);

        button_get_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerOptions origin = new MarkerOptions().position(currentLocation);
                MarkerOptions destination = new MarkerOptions().position(chosen_tourist_spot);
                String url = getUrl(origin.getPosition(), destination.getPosition(), "walking");
                new FetchURL(TouristSpotsActivity.this).execute(url, "walking");
                //Toast.makeText(TouristSpotsActivity.this, "Showing route", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private TouristSpot findTouristSpotByTitle(String title, List<TouristSpot> touristSpots) {
        for (TouristSpot touristSpot : touristSpots) {
            if (touristSpot.getPlaceName().equals(title)) {
                return touristSpot;
            }
        }
        return null;
    }
}