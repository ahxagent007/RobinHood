package com.dexian.robinhood.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.dexian.robinhood.BackgroundNotificationService;
import com.dexian.robinhood.R;

public class SplashScreenActivity extends AppCompatActivity {

    String TAG = "XIAN";
    String CHANNEL_ID = "111";

    BackgroundNotificationService backgroundNotificationService;
    Intent backgroundServiceIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        backgroundNotificationService = new BackgroundNotificationService(getApplicationContext());

        backgroundServiceIntent = new Intent(getApplicationContext(), backgroundNotificationService.getClass());
        if (!isMyServiceRunning(backgroundNotificationService.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(backgroundServiceIntent);
            } else {
                startService(backgroundServiceIntent);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3000);

        /*
        URL = https://developer.android.com/training/notify-user/build-notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.main_logo)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());*/

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i (TAG, true+" serviceRunning");
                return true;
            }
        }
        Log.i (TAG, false+" serviceRunning");
        return false;
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().stopService(backgroundServiceIntent);
        Log.i(TAG, "onDestroy!");
        super.onDestroy();
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
