package com.example.russellharveylab06;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String SEC_ACT = "com.example.russellharveylab06.MainActivity2";
    public static final String TER_ACT = "com.example.russellharveylab06.MainActivity3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v){
        EditText edtUsername = (EditText)findViewById(R.id.user_act1);
        EditText edtPassword = (EditText)findViewById(R.id.pass_act1);

        String u = edtUsername.getText().toString();
        String p = edtPassword.getText().toString();

        AccountManager am = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = am.getAccountsByType("com.example.russellharveylab06.user");
        boolean thePhantomBean = false;
        for (Account A : accounts) {
            if (A.name.compareTo(u) == 0) {
                thePhantomBean = true;
                if (am.getPassword(A).compareTo(p) == 0) {
                    Intent i = new Intent(this, MainActivity3.class);
                    i.putExtra("keyAlias", u + "_key");
                    startActivity(i);
                } else {
                    String error = "LOGIN FAILED: PASSWORD DOES NOT MATCH STORED VALUE";
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
            }
        }
        if (thePhantomBean == false){
            String nouser = "LOGIN FAILED: NO USER " + u + " EXISTS";
            Toast.makeText(this, nouser, Toast.LENGTH_LONG).show();
        } else {
            ;
        }
    }

    public void register(View v){
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }
}