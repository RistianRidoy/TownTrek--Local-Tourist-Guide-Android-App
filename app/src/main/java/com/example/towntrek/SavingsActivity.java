package com.example.towntrek;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SavingsActivity extends AppCompatActivity {

    private ListView listView;
    private SavingsAdapter savingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_savings);

        listView = findViewById(R.id.listView);

        List<String> days = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            days.add("Day " + i);
        }

        savingsAdapter = new SavingsAdapter(this, days);
        listView.setAdapter(savingsAdapter);
    }
}