package com.example.bai8_1;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener_ implements LocationListener
{
    @Override
    public void onLocationChanged(Location location)
    {
        // Hiển thị vị trí lên Label Position
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}

    @Override
    public void onProviderDisabled(String s) {}

    @Override
    public void onProviderEnabled(String s) {}
}