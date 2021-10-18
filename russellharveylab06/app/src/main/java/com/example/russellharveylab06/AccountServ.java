package com.example.russellharveylab06;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountServ extends Service {
    public AccountServ() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        AccountAuthenticator a = new AccountAuthenticator(this);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}