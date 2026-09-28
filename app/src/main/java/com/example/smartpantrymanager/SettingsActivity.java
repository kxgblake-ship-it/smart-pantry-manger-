package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

// Settings screen: one on/off switch, saved in SharedPreferences so it survives app restarts.
public class SettingsActivity extends AppCompatActivity {

    // Shared constants so other screens read the setting with the same key
    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        SwitchMaterial switchExpiry = findViewById(R.id.switchExpiryAlerts);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Show the saved value (off the first time)
        switchExpiry.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, false));

        // Save the new value every time the switch is flipped
        switchExpiry.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        BottomNavHelper.setup(this, R.id.nav_settings);
    }
}