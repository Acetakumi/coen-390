package com.example.eventcounter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private SharedPreferenceHelper prefs;

    private TextView tvCount1, tvCount2, tvCount3, tvTotal;
    private ListView listHistory;

    private boolean showNames = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // Toolbar (prevents "no menu / no arrow")
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPreferenceHelper(this);

        tvCount1 = findViewById(R.id.tvCount1);
        tvCount2 = findViewById(R.id.tvCount2);
        tvCount3 = findViewById(R.id.tvCount3);
        tvTotal  = findViewById(R.id.tvTotal);
        listHistory = findViewById(R.id.listHistory);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshUI(); // ✅ THIS is what you were missing
    }

    private void refreshUI() {
        // If settings not saved yet, avoid crashes
        if (!prefs.hasSettings()) {
            tvCount1.setText("1: 0");
            tvCount2.setText("2: 0");
            tvCount3.setText("3: 0");
            tvTotal.setText("Total: 0");

            listHistory.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<>()
            ));
            return;
        }

        Settings s = prefs.getSettings();

        int c1 = prefs.getCount(1);
        int c2 = prefs.getCount(2);
        int c3 = prefs.getCount(3);
        int total = prefs.getTotal();

        if (showNames) {
            tvCount1.setText(s.getButton1Name() + ": " + c1);
            tvCount2.setText(s.getButton2Name() + ": " + c2);
            tvCount3.setText(s.getButton3Name() + ": " + c3);
        } else {
            tvCount1.setText("1: " + c1);
            tvCount2.setText("2: " + c2);
            tvCount3.setText("3: " + c3);
        }

        tvTotal.setText("Total: " + total);

        List<Integer> historyNums = prefs.getHistoryList();
        List<String> historyDisplay = buildHistoryDisplay(historyNums, s, showNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyDisplay
        );
        listHistory.setAdapter(adapter);
    }

    private List<String> buildHistoryDisplay(List<Integer> history, Settings s, boolean namesMode) {
        List<String> out = new ArrayList<>();
        for (int num : history) {
            if (!namesMode) {
                out.add(String.valueOf(num));
            } else {
                if (num == 1) out.add(s.getButton1Name());
                else if (num == 2) out.add(s.getButton2Name());
                else if (num == 3) out.add(s.getButton3Name());
                else out.add("?");
            }
        }
        return out;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_toggle) {
            showNames = !showNames;
            refreshUI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
