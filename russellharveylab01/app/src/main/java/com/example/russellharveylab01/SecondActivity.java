/*
Name: Russell Harvey
Class: CSEC-467
Project: Lab 01
File: SecondActivity.java
 */

package com.example.russellharveylab01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default onCreate() to load the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        // Defines the message variable to get the extra content
        String message = intent.getStringExtra(MainActivity.SEC_ACT);
        // Defines the toast button as a variable
        Button b = (Button)findViewById(R.id.btntoast);
        // Creates an OnClickListener and defines the onClick() function
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FRESH, HOT-BUTTERED TOAST
                Toast.makeText(SecondActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
        // Sets "o" as the OnClickListener for "b"
        b.setOnClickListener(o);
    }

    public void returnToMain(View view) {
        /*
        Not necessary, but similar to the "sendMessage()" method in MainActivity.java
        this is tied to a button in activity_second.xml. This just allows you to return
        to the main activity.
         */
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}