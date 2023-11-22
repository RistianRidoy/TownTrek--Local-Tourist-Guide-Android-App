package com.example.towntrek;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BucketListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BucketListAdapter adapter;
    private ArrayList<String> bucketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bucket_list);

        recyclerView = findViewById(R.id.recyclerView);
        bucketList = new ArrayList<>();

        // Add some dummy data
        bucketList.add("1. Alaska");
        bucketList.add("2. Mt. Fuji");

        adapter = new BucketListAdapter(bucketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
