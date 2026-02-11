package com.example.eventcounter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class  MainActivity extends AppCompatActivity {

    private SharedPreferenceHelper prefs;

    private TextView tvTotal;
    private Button btnEvent1, btnEvent2, btnEvent3, btnShowCounts, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new SharedPreferenceHelper(this);

        tvTotal = findViewById(R.id.tvTotal);
        btnEvent1 = findViewById(R.id.btnEvent1);
        btnEvent2 = findViewById(R.id.btnEvent2);
        btnEvent3 = findViewById(R.id.btnEvent3);
        btnShowCounts = findViewById(R.id.btnShowCounts);
        btnSettings = findViewById(R.id.btnSettings);

        btnEvent1.setOnClickListener(v -> handleEventPress(1));
        btnEvent2.setOnClickListener(v -> handleEventPress(2));
        btnEvent3.setOnClickListener(v -> handleEventPress(3));

        btnShowCounts.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DataActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!prefs.hasSettings()) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return;
        }

        refreshUI();
    }

    private void refreshUI() {
        Settings s = prefs.getSettings();

        btnEvent1.setText(s.getButton1Name());
        btnEvent2.setText(s.getButton2Name());
        btnEvent3.setText(s.getButton3Name());

        tvTotal.setText("Total: " + prefs.getTotal());
    }

    private void handleEventPress(int eventNum) {

        if (!prefs.hasSettings()) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return;
        }


        if (!prefs.canAddEvent()) {
            Toast.makeText(this, "Max events reached", Toast.LENGTH_SHORT).show();
            return;
        }

        prefs.incrementCount(eventNum);
        prefs.incrementTotal();
        prefs.addHistory(eventNum);

        refreshUI();
    }
}
