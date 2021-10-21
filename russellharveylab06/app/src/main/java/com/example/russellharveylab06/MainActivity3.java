package com.example.russellharveylab06;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity3 extends AppCompatActivity {
    
    public String keyAlias;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent i = getIntent();
        keyAlias = i.getStringExtra("keyAlias");
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void encrypt (View v) {
        try {
            EditText edtFilename = (EditText) findViewById(R.id.edtFilename);
            EditText edtData = (EditText) findViewById(R.id.edtData);

            String fn = edtFilename.getText().toString();
            String data = edtData.getText().toString();
            String path = getFilesDir().toString();

            byte[] iv;
            byte [] cipherText;

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            if(!ks.containsAlias(keyAlias)) {
                Toast.makeText(this, "Key Not Found", Toast.LENGTH_LONG).show();
                return;
            }
            
            SecretKey k = ((KeyStore.SecretKeyEntry) ks.getEntry(keyAlias, null)).getSecretKey();
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, k);
            iv = c.getIV();
            cipherText = c.doFinal(data.getBytes());
            FileOutputStream ctOut = new FileOutputStream((path + File.separator + fn));
            for (Byte B : cipherText) {
                ctOut.write(B);
            }
            ctOut.flush();
            ctOut.close();

            FileOutputStream ivOut = new FileOutputStream((path + File.separator + fn + "_iv"));
            for (Byte B : iv) {
                ivOut.write(B);
            }
            ivOut.flush();
            ivOut.close();
        } catch (UserNotAuthenticatedException e) {
            Executor ex = ContextCompat.getMainExecutor(this);
            PromptInfo details = new PromptInfo.Builder()
                    .setTitle("Lab06 Authenticator")
                    .setSubtitle("Please provide your PIN.")
                    .setNegativeButtonText("Cancel")
                    .setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_STRONG // Allow biometric
                            //BiometricManager.Authenticators.DEVICE_CREDENTIAL // Allow pin
                    ).build();
            BiometricPrompt prompt = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), "Auth Succ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                }
            });
            prompt.authenticate(details);
            encrypt(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        //encrypt(null);
    }

    public void decrypt (View v) {
        try {
            EditText edtFilename = (EditText) findViewById(R.id.edtFilename);
            EditText edtData = (EditText) findViewById(R.id.edtData);

            String fn = edtFilename.getText().toString();
            String data = edtData.getText().toString();
            String path = getFilesDir().toString();

            byte[] iv = new byte[0];
            byte [] cipherText;

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            if(!ks.containsAlias(keyAlias)) {
                Toast.makeText(this, "Key Not Found", Toast.LENGTH_LONG).show();
                return;
            }

            SecretKey k = ((KeyStore.SecretKeyEntry) ks.getEntry(keyAlias, null)).getSecretKey();
            
            File ivFile = new File(path + File.separator + fn + "_iv");
            if(ivFile.exists()) {
                Path ivP = Paths.get(path + File.separator + fn + "_iv");
                iv = Files.readAllBytes(ivP);
            } else {
                Toast.makeText(this, "IV file missing, cannot decrypt", Toast.LENGTH_LONG).show();
                return;
            }

            GCMParameterSpec params = new GCMParameterSpec(128, iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, k, params);

            File cipherFile = new File(path + File.separator + fn);
            if(cipherFile.exists()) {
                Path cipherP = Paths.get(path + File.separator + fn);
                cipherText = Files.readAllBytes(cipherP);
            } else {
                Toast.makeText(this, "IV file missing, cannot decrypt", Toast.LENGTH_LONG).show();
                return;
            }

            byte[] plainText = c.doFinal(cipherText);
            String readablePT = new String (plainText, "UTF-8");
            EditText display = (EditText)findViewById(R.id.edtData);
            display.setText(readablePT, TextView.BufferType.EDITABLE);
        } catch (UserNotAuthenticatedException e) {
            Executor ex = ContextCompat.getMainExecutor(this);
            PromptInfo details = new PromptInfo.Builder()
                    .setTitle("Lab06 Authenticator")
                    .setSubtitle("Please provide your PIN.")
                    .setNegativeButtonText("Cancel")
                    .setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_STRONG // Allow biometric
                            //BiometricManager.Authenticators.DEVICE_CREDENTIAL // Allow pin
                    ).build();
            BiometricPrompt prompt = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), "Auth Succ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                }
            });
            prompt.authenticate(details);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void newKey(View v) {
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
    }
}

/*

public void authenticate(View v) {
        Executor ex = ContextCompat.getMainExecutor(this);
        PromptInfo details = new PromptInfo.Builder()
                .setTitle("Lab06 Authenticator")
                .setSubtitle("Please provide your PIN.")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG // Allow biometric
                        // | BiometricManager.Authenticators.DEVICE_CREDENTIAL // Allow pin
                ).build();
        BiometricPrompt prompt = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Auth Succ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
            }
        });
        prompt.authenticate(details);

    }
        EditText edtFilename = (EditText)findViewById(R.id.edtFilename);

        String fn = edtFilename.getText().toString();
        String path = getFilesDir().toString();

        byte[] iv = new byte[0];
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        SecretKey k = null;
        try {
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, k, gcmSpec);
            Path p = Paths.get(path + File.separator + fn);
            byte[] cipherText = Files.readAllBytes(p);
            byte[] plainText = c.doFinal(cipherText);
            String readablePT = new String(plainText, "UTF-8");
            Toast.makeText(this, readablePT, Toast.LENGTH_LONG).show();
        } catch (UserNotAuthenticatedException e) {
            Executor ex = ContextCompat.getMainExecutor(this);
            PromptInfo details = new PromptInfo.Builder()
                    .setTitle("Lab06 Authenticator")
                    .setSubtitle("Please provide your PIN.")
                    //.setNegativeButtonText("Cancel")
                    .setAllowedAuthenticators(
                            //BiometricManager.Authenticators.BIOMETRIC_STRONG // Allow biometric
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL // Allow pin
                    ).build();
            BiometricPrompt prompt = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), "Auth Succ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                }
            });
            prompt.authenticate(details);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        */

/*
        // Get Cipher
        Cipher c = null;
        try {
            c = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // Encryption
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        try {
            c.init(Cipher.ENCRYPT_MODE, k, gcmSpec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] cipherText;
        try {
            cipherText = c.doFinal(data.getBytes());
            FileOutputStream out = new FileOutputStream(path + File.separator + fn);
            for(Byte B : cipherText) {
                out.write(B);
            }
            out.flush();
            out.close();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(path + File.separator + fn);
            SecretKeySpec spec = null;

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, spec);
            CipherOutputStream cout = new CipherOutputStream(out, c);

            byte[] plainText = data.getBytes(StandardCharsets.UTF_8);
            for(Byte B : plainText) {
                cout.write(B);
            }
            cout.flush();
            cout.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/