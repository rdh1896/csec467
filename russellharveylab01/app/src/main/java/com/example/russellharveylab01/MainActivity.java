/*
Name: Russell Harvey
Class: CSEC-467
Project: Lab 01
File: MainActivity.java
 */
package com.example.russellharveylab01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Defining SEC_ACT variable for use in intent
    public static final String SEC_ACT = "com.example.russellharveylab01.SecondActivity";

    // Default onCreate() function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        /*
        This function is used by the button with id:"@+id/button" as its "onClick" value.
        This makes it so that when the button is clicked, this function will execute.
         */
        // Gets Intent here for second activity, uses this to define context
        Intent intent = new Intent(this, SecondActivity.class);
        // Get EditText result
        EditText field = findViewById(R.id.plain_text_input);
        String result = field.getText().toString();
        // Puts the output of the EditText field into the Intent
        intent.putExtra(SEC_ACT, result);
        // Starts the new activity
        startActivity(intent);
    }
}