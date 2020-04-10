package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.dexian.robinhood.R;

public class SplashScreenActivity extends AppCompatActivity {

    String TAG = "XIAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    private boolean checkPhonePermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "NO LOCATION PERMISSION");

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("We need your permission to run the app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                askingLocationPermission();
                            }
                        })
                        .create()
                        .show();


            }

        } else {
            Log.i(TAG, "Build.VERSION.SDK_INT < 23");

            int permissionLocation = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionLocation != PermissionChecker.PERMISSION_GRANTED) {
                askingLocationPermission();
            } else {
                Log.i(TAG, "ALL PERMISSION OK");

                return true;
            }
        }

        return false;
    }

    static final int PERMISSIONS_REQUEST_LOCATION = 99;

    // Location Permission
    private void askingLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }
}
