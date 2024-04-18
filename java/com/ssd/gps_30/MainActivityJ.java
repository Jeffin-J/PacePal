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

public class MainActivityJ extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private BroadcastReceiver permissionReceiver;
    private BroadcastReceiver dynamicReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the receiver
        initReceiver();

        // Check if location permissions have been granted
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are already granted, do something or start service directly
            startService();
        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(MainActivityJ.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initReceiver() {
        permissionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ActivityCompat.requestPermissions(MainActivityJ.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Initialize and register receiver
        dynamicReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Handle received broadcast
            }
        };
        IntentFilter filter = new IntentFilter("YOUR_CUSTOM_ACTION");
        registerReceiver(dynamicReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister receiver
        if (dynamicReceiver != null) {
            unregisterReceiver(dynamicReceiver);
            dynamicReceiver = null;
        }
    }


    // Method to start the LocationService
    void startService() {
        Intent intent = new Intent(MainActivityJ.this, LocationServie.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, start the service or perform your operation
                startService();
            } else {
                // Permission was denied, show a message to the user
                Toast.makeText(this, "Location permission is required for this app to function.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
