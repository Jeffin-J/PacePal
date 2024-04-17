package com.ssd.gps_30;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

public class MainActivityJ extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // BroadcastReceiver to handle permission requests from LocationService
    private BroadcastReceiver permissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             {
                // Request location permission
                ActivityCompat.requestPermissions(MainActivityJ.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register the broadcast receiver
        IntentFilter filter = new IntentFilter(LocationServie.ACTION_REQUEST_PERMISSIONS);
        registerReceiver(permissionReceiver, filter);

        // Check if location permissions have been granted
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request Location
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Request Location Permission
            startService();
        }
    }

    // Method to start the LocationService
    void startService() {
        Intent intent = new Intent(MainActivityJ.this, LocationServie.class);
        startService(intent);
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied, start the service
                startService();
            } else {
                // Permission granted, show a toast message
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
