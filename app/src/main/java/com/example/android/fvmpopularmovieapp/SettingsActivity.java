package com.example.android.fvmpopularmovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.fvmpopularmovieapp.fragment_settings.SettingsFragment;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SETTINGS_FRAGMENT_TAG;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment(), SETTINGS_FRAGMENT_TAG)
                .commit();
    }

}
