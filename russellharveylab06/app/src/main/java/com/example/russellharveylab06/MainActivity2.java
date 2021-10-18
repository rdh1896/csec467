package com.example.russellharveylab06;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity2 extends AppCompatActivity {

    public static final String TER_ACT = "com.example.russellharveylab06.MainActivity3";
    public byte[] salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void genSalt() {
        SecureRandom generator = null;

        try {
            generator = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        salt = new byte[16];
        generator.nextBytes(salt);
    }

    public void register(View v) {
        /*
        Register User with Android Account Manager
        */

        EditText edtUsername = (EditText) findViewById(R.id.user_act2);
        EditText edtPassword = (EditText) findViewById(R.id.pass_act2);

        String u = edtUsername.getText().toString();
        String p = edtPassword.getText().toString();

        AccountManager am = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account toBeAdded = new Account(edtUsername.getEditableText().toString(), "com.example.russellharveylab06.user");

        am.addAccountExplicitly(toBeAdded, edtPassword.getEditableText().toString(), null); // last param is for a bundle, we need this to make and store the key alias?

        /*
        Create User Key


        // Generate Salt
        genSalt();

        // Generate Key
        PBEKeySpec keySpec = new PBEKeySpec(p.toCharArray(), salt, 1000, 128);
        SecretKey k = null;
        try {
            k = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        */
    }
}