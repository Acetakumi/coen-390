package com.example.eventcounter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferenceHelper prefs;
    private EditText etBtn1, etBtn2, etBtn3, etMaxEvents;
    private Button btnSave;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar tb = findViewById(R.id.toolbarSettings);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPreferenceHelper(this);

        etBtn1 = findViewById(R.id.etBtn1);
        etBtn2 = findViewById(R.id.etBtn2);
        etBtn3 = findViewById(R.id.etBtn3);
        etMaxEvents = findViewById(R.id.etMaxEvents);
        btnSave = findViewById(R.id.btnSave);

        loadAndDisplay();

        btnSave.setOnClickListener(v -> attemptSave());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!prefs.hasSettings()) {
            setEditMode(true);
        }
    }
    private void loadAndDisplay() {
        Settings s = prefs.getSettings();

        etBtn1.setText(s.getButton1Name() == null ? "" : s.getButton1Name());
        etBtn2.setText(s.getButton2Name() == null ? "" : s.getButton2Name());
        etBtn3.setText(s.getButton3Name() == null ? "" : s.getButton3Name());

        if (s.getMaxEvents() == 0) {
            etMaxEvents.setText("");
        } else {
            etMaxEvents.setText(String.valueOf(s.getMaxEvents()));
        }

        setEditMode(false);

        if (!prefs.hasSettings()) {
            setEditMode(true);
        }
    }
    private void setEditMode(boolean enabled) {
        isEditMode = enabled;

        etBtn1.setEnabled(enabled);
        etBtn2.setEnabled(enabled);
        etBtn3.setEnabled(enabled);
        etMaxEvents.setEnabled(enabled);

        btnSave.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }
    private void attemptSave() {
        String b1 = etBtn1.getText().toString().trim();
        String b2 = etBtn2.getText().toString().trim();
        String b3 = etBtn3.getText().toString().trim();
        String maxStr = etMaxEvents.getText().toString().trim();

        if (!isValidName(b1) || !isValidName(b2) || !isValidName(b3)) {
            Toast.makeText(this, "Names must be letters/spaces only (max 20).", Toast.LENGTH_SHORT).show();
            return;
        }

        int max;
        try {
            max = Integer.parseInt(maxStr);
        } catch (Exception e) {
            Toast.makeText(this, "Max events must be a number (5–200).", Toast.LENGTH_SHORT).show();
            return;
        }

        if (max < 5 || max > 200) {
            Toast.makeText(this, "Max events must be between 5 and 200.", Toast.LENGTH_SHORT).show();
            return;
        }

        Settings s = new Settings(b1, b2, b3, max);
        prefs.saveSettings(s);

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        setEditMode(false);

        finish(); // go back to MainActivity
    }
    private boolean isValidName(String s) {
        if (s == null || s.isEmpty() || s.length() > 20) return false;
        return s.matches("[A-Za-z ]+");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_edit) {
            setEditMode(true);
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
