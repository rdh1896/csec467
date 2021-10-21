package com.example.russellharveylab06;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
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

    @RequiresApi(api = Build.VERSION_CODES.R)
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
        Create Key for Key Store
         */
        String keyAlias = u + "_key";

        KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationParameters(120, KeyProperties.AUTH_BIOMETRIC_STRONG)
                .setKeySize(128)
                .build();
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            kg.init(keySpec);
            kg.generateKey();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            /*
            if (!ks.containsAlias(keyAlias)){
                // if the alias does not exist, create a new key
                KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setUserAuthenticationRequired(true)
                        .setUserAuthenticationParameters(120, KeyProperties.AUTH_DEVICE_CREDENTIAL)
                        .setKeySize(128)
                        .build();
                KeyGenerator kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                kg.init(keySpec);
            }
             */

            if(!ks.containsAlias(keyAlias)) {
                Toast.makeText(this, "Key Gen Failed", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }


        Intent i = new Intent(this, MainActivity3.class);
        i.putExtra("keyAlias", keyAlias);
        startActivity(i);


        /*
        Create User Key

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