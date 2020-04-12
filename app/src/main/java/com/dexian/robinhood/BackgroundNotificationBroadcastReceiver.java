package com.dexian.robinhood;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class BackgroundNotificationBroadcastReceiver extends BroadcastReceiver {
    String TAG = "XIAN";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Service Stops! Oooooooooooooppppssssss!!!!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, BackgroundNotificationService.class));
        } else {
            context.startService(new Intent(context, BackgroundNotificationService.class));
        }

    }
}
