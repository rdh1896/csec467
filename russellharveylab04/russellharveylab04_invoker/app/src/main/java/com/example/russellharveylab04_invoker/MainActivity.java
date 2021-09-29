package com.example.russellharveylab04_invoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();

        i.setComponent(new ComponentName("com.example.russellharveylab04_creator", "com.example.russellharveylab04_creator.intentwifi"));
        startService(i);
    }
}