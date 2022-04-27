package com.example.bluetoothfinder.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bluetoothfinder.R;
import com.example.bluetoothfinder.models.Device;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocationsHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    Realm realm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_history);
        realm = Realm.getDefaultInstance();
        setListeners();
        showBluetoothSensors();
        loadLocations();
    }

    private void setListeners() {
        ImageView iconBack = findViewById(R.id.iconBack);
        iconBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iconBack) {
            finish();
        }
    }

    private void showBluetoothSensors() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int deviceHeight = metrics.heightPixels;
        int deviceWidth = metrics.widthPixels;
        int deviceWidth1 = (deviceHeight - 400) / 2;
        int oneM = deviceWidth1 / 4;
        int roomWidth = deviceWidth1;
        int roomHeight = (int) (7.4 * oneM);
        int marginLeft = (deviceWidth - deviceWidth1) / 2;

        RelativeLayout container = findViewById(R.id.container);
        ImageView iv;
        RelativeLayout.LayoutParams params;

        iv = new ImageView(this);
        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bluetooth));
        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = marginLeft + (roomWidth / 2) - 50;
        params.topMargin = roomHeight - 100;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bluetooth));
        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = marginLeft + (roomWidth / 2) - 50;
        params.topMargin = 0;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bluetooth));
        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = marginLeft;
        params.topMargin = (roomHeight / 2) - 50;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bluetooth));
        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = marginLeft + roomWidth - 100;
        params.topMargin = (roomHeight / 2) - 50;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setBackgroundColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(2, roomHeight);
        params.leftMargin = marginLeft;
        params.topMargin = 0;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setBackgroundColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(2, roomHeight);
        params.leftMargin = marginLeft + roomWidth;
        params.topMargin = 0;
        container.addView(iv, params);

        iv = new ImageView(this);
        iv.setBackgroundColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(roomWidth, 2);
        params.leftMargin = marginLeft;
        params.topMargin = roomHeight;
        container.addView(iv, params);
    }

    private void loadLocations() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Device> results = realm.where(Device.class).findAll();
                for (Device device : results) {
                    showDeviceLocation(device);
                }
            }
        });
    }

    private void showDeviceLocation(Device device) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int deviceHeight = metrics.heightPixels;
        int deviceWidth = metrics.widthPixels;
        int deviceWidth1 = (deviceHeight - 400) / 2;
        int oneM = deviceWidth1 / 4;
        int roomHeight = (int) (7.4 * oneM);
        int marginLeft = (deviceWidth - deviceWidth1) / 2;

//        RelativeLayout container = findViewById(R.id.container);
//        RelativeLayout.LayoutParams params;
//        ImageView iv = new ImageView(this);
//        iv.setBackgroundColor(Color.BLACK);
//        params = new RelativeLayout.LayoutParams(30, 30);
//        params.leftMargin = (int) (marginLeft + (device.getxPosition() * oneM));
//        params.topMargin = (int) (roomHeight - (device.getyPosition() * oneM));
//        container.addView(iv, params);


        RelativeLayout container = findViewById(R.id.container);
        RelativeLayout.LayoutParams params;
        ImageView iv = new ImageView(this);

        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.locationmark));
        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = (int) (marginLeft + (device.getxPosition() * oneM));
        params.topMargin = (int) (roomHeight - (device.getyPosition() * oneM));
        container.addView(iv, params);
    }
}
