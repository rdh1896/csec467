package com.example.russellharveylab04_creator;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class wifi extends Service {
    public wifi() {
    }

    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 5000, 5, (LocationListener) this);
        //WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //WifiInfo wi = wm.getConnectionInfo();
        //String ssid = wi.getSSID();

        Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}