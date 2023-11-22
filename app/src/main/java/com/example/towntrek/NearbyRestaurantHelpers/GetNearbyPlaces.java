package com.example.towntrek.NearbyRestaurantHelpers;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    private String google_place_data, url;
    private GoogleMap myMap;


    @Override
    protected String doInBackground(Object... objects) {

        myMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            google_place_data =downloadURL.readTheURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return google_place_data;
    }

    @Override
    protected void onPostExecute(String s){
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParserNR dataParserNR = new DataParserNR();
        nearbyPlacesList = dataParserNR.parse(s);

        displayNearbyPlaces(nearbyPlacesList);
    }

    private void displayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList)
    {
        for (int i = 0; i < nearbyPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearbyPlaces = nearbyPlacesList.get(i);
            String nameOfPlace = googleNearbyPlaces.get("place_name");
            String vicinity = googleNearbyPlaces.get("vicinity");
            double latitude = Double.parseDouble(googleNearbyPlaces.get("lat"));
            double longitude = Double.parseDouble(googleNearbyPlaces.get("lng"));

            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + ":" + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            myMap.addMarker(markerOptions);
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        }
    }

}
