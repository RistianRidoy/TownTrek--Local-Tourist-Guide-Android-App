package com.example.towntrek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TouredListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TouredListAdapter adapter;
    private ArrayList<String> touredPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_toured_list);

        recyclerView = findViewById(R.id.recyclerView_toured);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        touredPlaces = new ArrayList<>();
        touredPlaces.add("Phuket");
        touredPlaces.add("Kolkata");

        adapter = new TouredListAdapter(touredPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Button addPlaceButton = findViewById(R.id.button_add_new_place);
    }
}