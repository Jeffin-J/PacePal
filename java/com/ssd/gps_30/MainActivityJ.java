package com.ssd.gps_30;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Ensure this import is added
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

// Your class implementation


public class MainActivityJ extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if location permissions have been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, start the LocationService directly
            startService();
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
