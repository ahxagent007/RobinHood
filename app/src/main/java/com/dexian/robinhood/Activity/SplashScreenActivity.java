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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

//import com.dexian.robinhood.BackgroundNotificationService;
import com.dexian.robinhood.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    String TAG = "XIAN";
    String CHANNEL_ID = "111";

    //BackgroundNotificationService backgroundNotificationService;
    //Intent backgroundServiceIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        /*backgroundNotificationService = new BackgroundNotificationService(getApplicationContext());

        backgroundServiceIntent = new Intent(getApplicationContext(), backgroundNotificationService.getClass());
        if (!isMyServiceRunning(backgroundNotificationService.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(backgroundServiceIntent);
            } else {
                startService(backgroundServiceIntent);
            }
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(checkAndRequestPermissions()){
                    if(isNetworkAvailable()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"OFFLINE MODE", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }

                }
            }
        }, 2000);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    String[] appPermissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    final int PERMISSION_REQUES_CODE = 101;

    public boolean checkAndRequestPermissions(){

        List<String> listPermissinsNeeded = new ArrayList<>();

        for(String perm : appPermissions){

            if (Build.VERSION.SDK_INT >= 23) {
                if(getApplicationContext().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                    listPermissinsNeeded.add(perm);
                }
            }else{
                if(PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED){
                    listPermissinsNeeded.add(perm);
                }
            }


        }

        if(!listPermissinsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(SplashScreenActivity.this,
                    listPermissinsNeeded.toArray(new String[listPermissinsNeeded.size()]),
                    PERMISSION_REQUES_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUES_CODE:{
                Log.i(TAG,"Permission 3 true" );
                if(checkPhonePermission()){
                    if(isNetworkAvailable()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                    }

                }
            }


        }
    }



    private boolean checkPhonePermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "NO SMS PERMISSION");

                new AlertDialog.Builder(this)
                        .setTitle("Call Permission")
                        .setMessage("We need your permission to run the app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                checkAndRequestPermissions();
                            }
                        })
                        .create()
                        .show();


            } else if (ContextCompat.checkSelfPermission(this,
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

                                checkAndRequestPermissions();
                            }
                        })
                        .create()
                        .show();


            } else if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {

                new AlertDialog.Builder(this)
                        .setTitle("Storage Permission")
                        .setMessage("We need your permission to run the app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                checkAndRequestPermissions();
                            }
                        })
                        .create()
                        .show();

            } else {
                Log.i(TAG, "ALL PERMISSION");
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //finish();
                return true;
            }

        } else {
            Log.i(TAG, "Build.VERSION.SDK_INT < 23");

            int permissionLocation = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCall = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
            int permissionStorage = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionStorage2 = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);


            if (permissionCall != PermissionChecker.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL);

            } else if (permissionStorage != PermissionChecker.PERMISSION_GRANTED && permissionStorage2 != PermissionChecker.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);

            } else if (permissionLocation != PermissionChecker.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOC);
            } else {
                Log.i(TAG, "ALL PERMISSION OK");

                return true;
            }
        }

        return false;
    }

    final int PERMISSIONS_REQUEST_CALL = 11;
    final int PERMISSIONS_REQUEST_STORAGE = 12;
    final int PERMISSIONS_REQUEST_LOC = 13;

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
        //getApplicationContext().stopService(backgroundServiceIntent);
        Log.i(TAG, "onDestroy!");
        super.onDestroy();
    }

    private static final int REQUEST_PHONE_CALL = 101;


}

