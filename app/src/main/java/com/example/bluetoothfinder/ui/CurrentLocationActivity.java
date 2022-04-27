package com.example.bluetoothfinder.ui;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bluetoothfinder.R;
import com.example.bluetoothfinder.models.Device;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class CurrentLocationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST = 123;

    private TextView statusTextView;
    private Button searchButton;
    private ArrayList<String> bluetoothDevices = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private Device newDevice;
    private BluetoothAdapter bluetoothAdapter;
    private Realm realm = null;
    public double distance1;
    public double distance2;
    public double distance3;
    public double distance4;
    public double x1;
    public double x2;
    public double x3;
    public double y1;
    public double y2;
    public double y3;
    public double r1;
    public double r2;
    public double r3;
   // ImageView iv1 = new ImageView(this);
   ImageView devicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        realm = Realm.getDefaultInstance();
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);
        setListeners();
        showBluetoothSensors();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkLocationPermission();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(broadcastReceiver, intentFilter);
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

    private void checkLocationPermission() {
        int permissionLocation = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            permissionLocation = PackageManager.PERMISSION_GRANTED;
        }

        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            registerReceiver();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            checkLocationPermission();
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                statusTextView.setText("Finished");
                searchButton.setEnabled(true);
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Log.d("testfinal", "name=" + name + "\nrssi=" + rssi);





// RSSI=20log(d)+A
// izvedena formula za pretvaranje rssi u metre: d=10^((A-RSSI)/20), A iznosi vrijednost RSSI na udaljenosti 1m


                if (name.equals("BTM1")) {
                    double a1 = (-77 - (rssi));
                    double b1 = a1 / 20;


                    distance1=Math.pow(10, b1);
                    Log.d("test", "distance1=" + distance1 );

                }

                if (name.equals("BTM2")) {
                    double a2 = (-74 - (rssi));
                    double b2 = a2 / 20;

                  distance2= Math.pow(10, b2);
                    Log.d("test", "distance2=" + distance2 );

                }

                if (name.equals("BTM3")) {
                    double a3 = (-87 - (rssi));
                    double b3 = a3 / 20;

                  distance3= Math.pow(10, b3);
                    Log.d("test", "distance3=" + distance3 );

                }

                if (name.equals("BTM4")) {
                    double a4 = (-80 - (rssi));
                    double b4 = a4 / 20;
                  

                distance4= Math.pow(10, b4);
                    Log.d("test", "distance4=" + distance4 );

                }




                arrayAdapter.addAll(bluetoothDevices);
                arrayAdapter.notifyDataSetChanged();
                newDevice = new Device();
                newDevice.setDeviceId(getDeviceId());
            }
//dodjeljivanje vrijednosti tri najbliza senzora vrijednostima polumjera kruznice
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (distance1 < distance4 && distance2 < distance4 && distance3 < distance4) {
                    //pozicija BTM1
                    x1 = 2;
                    y1 = 0;
                    //pozicija BTM2
                    x2 = 0;
                    y2 = 3.7;
                    //pozicija BTM3
                    x3 = 2;
                    y3 = 7.4;
                    r1 = distance1;
                    r2 = distance2;
                    r3 = distance3;
                } else if (distance1 < distance3 && distance2 < distance3 && distance4 < distance3) {
                    //pozicija BTM1
                    x1 = 2;
                    y1 = 0;
                    //pozicija BTM2
                    x2 = 0;
                    y2 = 3.7;
                    //pozicija BTM4
                    x3 = 4;
                    y3 = 3.7;
                    r1 = distance1;
                    r2 = distance2;
                    r3 = distance4;
                } else if (distance1 < distance2 && distance3 < distance2 && distance4 < distance2) {
                    //pozicija BTM1
                    x1 = 2;
                    y1 = 0;
                    //pozicija BTM3
                    x2 = 2;
                    y2 = 7.4;
                    //pozicija BTM4
                    x3 = 4;
                    y3 = 3.7;
                    r1 = distance1;
                    r2 = distance3;
                    r3 = distance4;
                } else if (distance2 < distance1 && distance3 < distance1 && distance4 < distance1) {
                    //pozicija BTM2
                    x1 = 0;
                    y1 = 3.7;
                    //pozicija BTM3
                    x2 = 2;
                    y2 = 7.4;
                    //pozicija BTM4
                    x3 = 4;
                    y3 = 3.7;
                    r1 = distance2;
                    r2 = distance3;
                    r3 = distance4;
                }
                Log.d("trilateration", "r1=" + r1 + "\nr2=" + r2 + "\nr3=" + r3 );

                // trilateracija formula
                double A = 2 * x2 - 2 * x1;
                double B = 2 * y2 - 2 * y1;
                double C = Math.pow(r1, 2) - Math.pow(r2, 2) - Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(y1, 2) + Math.pow(y2, 2);
                double D = 2 * x3 - 2 * x2;
                double E = 2 * y3 - 2 * y2;
                double F = Math.pow(r2, 2) - Math.pow(r3, 2) - Math.pow(x2, 2) + Math.pow(x3, 2) - Math.pow(y2, 2) + Math.pow(y3, 2);
                double x = (C * E - F * B) / (E * A - B * D);
                double y = (C * D - A * F) / (B * D - A * E);
                Log.d("testfinal1", "r1=" + r1 );
                Log.d("testfinal1", "r2=" + r2 );
                Log.d("testfinal1", "r3=" + r3 );

                Log.d("testfinal1", "x=" + x + "\ny=" + y);

                newDevice.setxPosition(x);
                newDevice.setyPosition(y);
                addDeviceInDatabase(newDevice);
            }
        }
    };

    private int getDeviceId() {
        // increment index
        Realm realm = Realm.getDefaultInstance();
        Number currentIdNum = realm.where(Device.class).max("deviceId");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public void searchClicked(View view) {
        statusTextView.setText(getString(R.string.searching));
        searchButton.setEnabled(false);
        bluetoothAdapter.startDiscovery();
        if(devicePosition!=null) {
            devicePosition.setVisibility(View.GONE);
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
        RelativeLayout.LayoutParams params;
        ImageView iv = new ImageView(this);
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

    private void addDeviceInDatabase(final Device foundDevice) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        realm.copyToRealm(foundDevice);
                        showData(foundDevice);
                    } catch (RealmPrimaryKeyConstraintException e) {
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void showData(Device device) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int deviceHeight = metrics.heightPixels;
        int deviceWidth = metrics.widthPixels;
        int deviceWidth1 = (deviceHeight - 400) / 2;
        int oneM = deviceWidth1 / 4;
        int roomHeight = (int) (7.4 * oneM);
        int marginLeft = (deviceWidth - deviceWidth1) / 2;

        RelativeLayout container = findViewById(R.id.container);
        RelativeLayout.LayoutParams params;

//        iv1.setBackgroundColor(Color.BLACK);
        devicePosition = new ImageView(this);
//        devicePosition.setBackgroundColor(Color.BLACK);
//        ImageView iv = new ImageView(this);
        devicePosition.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.locationmark));

        params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = (int) (marginLeft + (device.getxPosition() * oneM));
        params.topMargin = (int) (roomHeight - (device.getyPosition() * oneM));
        container.addView(devicePosition, params);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}