package com.example.bai8_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

public class layvitri extends AppCompatActivity {

    LocationManager myLM;
    TextView lblPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layvitri);

        myLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lblPosition = findViewById(R.id.lblPosition);

        checkPermission();
    }

    void checkPermission() {

        String[] perm_array = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            List<String> permissions = new ArrayList<String>();

            for (int i = 0; i < perm_array.length; i++) {
                if (checkSelfPermission(perm_array[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(perm_array[i]);
                }
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 9999);
            } else {
                //requestLocation();
            }
        } else {
            //requestLocation();
        }
    }

    public void refreshLocation(View view) {
        // 1. Lấy vị trí của máy để hiển thị lên màn hình ở label Postion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myLM.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0, new MyLocationListener());

    }

    private class MyLocationListener
            implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            // Hiển thị vị trí lên Label Position
            String displayContent = "Long: " +
                                    location.getLongitude() +
                                    ", Lat:" +
                                    location.getLatitude();
            lblPosition.setText(displayContent);


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle b) {}

        @Override
        public void onProviderDisabled(String s) {}

        @Override
        public void onProviderEnabled(String s) {}
    }


}