package com.example.towntrek;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.towntrek.AviationDirectionHelpers.AviationRouteResponse;
import com.example.towntrek.AviationDirectionHelpers.AviationStackService;
import com.example.towntrek.MapDirectionHelpers.FetchURL;
import com.example.towntrek.MapDirectionHelpers.TaskLoadedCallback;
import com.example.towntrek.NearbyRestaurantHelpers.GetNearbyPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private HashMap<String, LatLng> locationCoordinates = new HashMap<>();
    SupportMapFragment smf;
    FusedLocationProviderClient client;
    SearchView searchView;
    GoogleMap myMap;
    Button buttonMapDirection;
    ImageButton button_nearby_hotel;
    LatLng currentLocation, searchedLocation, featuredLocationCoordinates;
    Polyline currentPolyline;
    double latitude, longitude;
    private Retrofit retrofit;
    private AviationStackService aviationService;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    int ProximityRadius = 1000;
    //AutocompleteSupportFragment autocompleteFragment;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        searchView = findViewById(R.id.google_search);
        buttonMapDirection = findViewById(R.id.btn_getDirection);
        button_nearby_hotel = findViewById(R.id.hotels_nearby);
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        smf.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.aviationstack.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aviationService = retrofit.create(AviationStackService.class);

        //setUpAutoCompleteSearchView();//

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            latitude = intent.getDoubleExtra("latitude", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setCompassEnabled(true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);

            client.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    myMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));

                    if (latitude != 0.0 && longitude != 0.0){
                        featuredLocationCoordinates = new LatLng(latitude, longitude);
                        myMap.addMarker(new MarkerOptions().position(featuredLocationCoordinates).title("Featured Location"));
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(featuredLocationCoordinates, 18));

                        MarkerOptions place1 = new MarkerOptions().position(currentLocation);
                        MarkerOptions place2 = new MarkerOptions().position(featuredLocationCoordinates);

                        buttonMapDirection.setVisibility(View.VISIBLE);
                        button_nearby_hotel.setVisibility(View.VISIBLE);
                        buttonMapDirection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isAviationRouteNeeded(currentLocation, featuredLocationCoordinates)){
                                    fetchAviationRouteForFeaturedLocation();
                                }else {
                                    String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                                    new FetchURL(MapActivity.this).execute(url, "driving");
                                }
                                /*PolylineOptions polylineOptions = new PolylineOptions()
                                        .add(currentLocation, featuredLocationCoordinates)
                                        .width(5) // Line width in pixels
                                        .color(Color.RED); // Line color
                                Polyline polyline = myMap.addPolyline(polylineOptions);
                                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(featuredLocationCoordinates, 12));*/
                            }
                        });
                        button_nearby_hotel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String hotel = "hotel";
                                Object transferData[] = new Object[2];
                                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

                                String url = getNearbyLocationUrl(latitude, longitude, hotel);
                                transferData[0] = myMap;
                                transferData[1] = url;
                                getNearbyPlaces.execute(transferData);
                                Toast.makeText(MapActivity.this, "Showing Nearby Local Hotels...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
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

    private String getNearbyLocationUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + getString(R.string.google_maps_key));

        Log.d("MapActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }

    private boolean isAviationRouteNeeded(LatLng origin, LatLng destination) {
        double distanceThreshold = 1000000;

        double distance = calculateDistance(origin, destination);

        return distance > distanceThreshold;
    }

    private double calculateDistance(LatLng origin, LatLng destination) {
        double lat1 = origin.latitude;
        double lon1 = origin.longitude;
        double lat2 = destination.latitude;
        double lon2 = destination.longitude;

        double earthRadius = 6371000;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }

    private void fetchAviationRouteForSearchedLocation() {

        Call<AviationRouteResponse> call = aviationService.getAviationRoute(
                "3204b5bd6eb1f053aa4f93a9fb161a9d",
                searchedLocation.latitude + "," + searchedLocation.longitude
        );

        call.enqueue(new Callback<AviationRouteResponse>() {
            @Override
            public void onResponse(Call<AviationRouteResponse> call, Response<AviationRouteResponse> response) {
                if (response.isSuccessful()) {
                    AviationRouteResponse aviationResponse = response.body();
                    if (aviationResponse != null) {
                        String aviationRoute = aviationResponse.getData().getFlightIata();
                    }
                } else {
                    Toast.makeText(MapActivity.this, "Failed to fetch aviation route", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AviationRouteResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAviationRouteForFeaturedLocation() {

        Call<AviationRouteResponse> call = aviationService.getAviationRoute(
                "3204b5bd6eb1f053aa4f93a9fb161a9d",
                featuredLocationCoordinates.latitude + "," + featuredLocationCoordinates.longitude
        );

        call.enqueue(new Callback<AviationRouteResponse>() {
            @Override
            public void onResponse(Call<AviationRouteResponse> call, Response<AviationRouteResponse> response) {
                if (response.isSuccessful()) {
                    AviationRouteResponse aviationResponse = response.body();
                    if (aviationResponse != null) {
                        String aviationRoute = aviationResponse.getData().getFlightIata();
                    }
                } else {
                    Toast.makeText(MapActivity.this, "Failed to fetch aviation route", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AviationRouteResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                searchedLocation = new LatLng(address.getLatitude(), address.getLongitude());
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(searchedLocation).title(locationName));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 18));

                MarkerOptions place1 = new MarkerOptions().position(currentLocation);
                MarkerOptions place3 = new MarkerOptions().position(searchedLocation);

                buttonMapDirection.setVisibility(View.VISIBLE);
                buttonMapDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isAviationRouteNeeded(currentLocation, searchedLocation)){
                            fetchAviationRouteForSearchedLocation();
                        }else {
                            String url = getUrl(place1.getPosition(), place3.getPosition(), "walking");
                            new FetchURL(MapActivity.this).execute(url, "walking");
                        }
                        /*PolylineOptions polylineOptions = new PolylineOptions()
                                .add(currentLocation, searchedLocation)
                                .width(5) // Line width in pixels
                                .color(Color.RED); // Line color
                        Polyline polyline = myMap.addPolyline(polylineOptions);
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 12));*/
                    }
                });
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void setUpAutoCompleteSearchView() {
         autocompleteFragment = AutocompleteSupportFragment.newInstance();
         getSupportFragmentManager().beginTransaction().replace(R.id.autocomplete_fragment_container, autocompleteFragment).commit();

        // Specify the types of place data to return (e.g., addresses)
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Set the type filter to addresses
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        // Set up the autocomplete click listener
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place (e.g., set it as the destination)
                // You can call your calculateAndDisplayRoute method here
                LatLng destinationLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle errors if any
            }
        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        smf.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        smf.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smf.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        smf.onLowMemory();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = myMap.addPolyline((PolylineOptions) values[0]);
    }
}
