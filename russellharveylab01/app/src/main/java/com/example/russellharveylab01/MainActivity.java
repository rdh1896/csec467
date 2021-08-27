package com.example.russellharveylab01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String SEC_ACT = "com.example.myfirstapp.SEC_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        // EL BUTTON
        Intent intent = new Intent(this, SecondActivity.class);
        EditText field = findViewById(R.id.plain_text_input);
        String result = field.getText().toString();
        intent.putExtra(SEC_ACT, result);
        startActivity(intent);
    }
}