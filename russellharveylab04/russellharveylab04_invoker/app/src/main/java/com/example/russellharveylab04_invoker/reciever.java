package com.example.russellharveylab04_invoker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class reciever extends Service {
    public reciever() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        String lat = intent.getStringExtra("latitude");
        String lon = intent.getStringExtra("longitude");
        String loc = "Latitude: " + lat + " Longitude: " + lon;
        Toast.makeText(this, loc, Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY; // sticky = persistence
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}