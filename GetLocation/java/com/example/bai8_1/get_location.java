package com.example.bai8_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class get_location extends AppCompatActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1;

    private LocationManager locationManager;

    private TextView longitude, latitude;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_loctation);

        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        checkPermission();


    }

    void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener());
    }

    void checkPermission() {

        String[] perm_array = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            List<String> permissions = new ArrayList<String>();

            for (int i=0;i<perm_array.length;i++)
            {
                if (checkSelfPermission(perm_array[i])!= PackageManager.PERMISSION_GRANTED)
                {
                    permissions.add(perm_array[i]);
                }
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 9999);
            } else {
                requestLocation();
            }
        } else {
            requestLocation();
        }
    }

    public void onClick(View view)
    {
        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null)
        {
            longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
        }
    }

    private class MyLocationListener
            implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle b) {}

        @Override
        public void onProviderDisabled(String s) {}

        @Override
        public void onProviderEnabled(String s) {}
    }
}