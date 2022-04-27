package com.example.bluetoothfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bluetoothfinder.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setListeners();
    }

    private void setListeners() {
        CardView currentLocationCard = findViewById(R.id.currentLocationCard);
        CardView locationsHistoryCard = findViewById(R.id.locationsHistoryCard);
        currentLocationCard.setOnClickListener(this);
        locationsHistoryCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentLocationCard:
                goToCurrentLocationActivity();
                break;
            case R.id.locationsHistoryCard:
                goToLocationsHistoryActivity();
                break;
        }
    }

    private void goToLocationsHistoryActivity() {
        startActivity(new Intent(this, LocationsHistoryActivity.class));
    }

    private void goToCurrentLocationActivity() {
        startActivity(new Intent(this, CurrentLocationActivity.class));
    }
}
