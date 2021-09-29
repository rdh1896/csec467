package com.example.russellharveylab04_creator;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class intentwifi extends IntentService {

    LocationManager locationManager;

    public intentwifi(){
        super("my_intent_thread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronized (this) {
            try {
                wait(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(intentwifi.this, "Service Started", Toast.LENGTH_LONG).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        Intent i = new Intent();
        String lat = String.valueOf(loc.getLatitude());
        String lon = String.valueOf(loc.getLongitude());
        i.putExtra("latitude", lat);
        i.putExtra("longitude", lon);

        i.setComponent(new ComponentName("com.example.russellharveylab04_invoker", "com.example.russellharveylab04_invoker.reciever")); // receiver misspelled oops
        startService(i);
        return Service.START_NOT_STICKY; // sticky = persistence
    }

    @Override
    public void onDestroy(){
        Toast.makeText(intentwifi.this, "Service Torn Down", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}