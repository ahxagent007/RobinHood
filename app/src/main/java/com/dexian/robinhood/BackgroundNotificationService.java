package com.dexian.robinhood;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dexian.robinhood.DB.RescueDB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundNotificationService extends Service {

    String TAG = "XIAN";

    public BackgroundNotificationService() {
    }

    public BackgroundNotificationService(Context context) {
        super();
        Log.i(TAG,"BackgroundNotificationService context ");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int NOTIFICATION_ID = (int) (System.currentTimeMillis()%10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
        }
    }

    DatabaseReference mDatabaseRefRescue;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        //startTimer();
        mDatabaseRefRescue = FirebaseDatabase.getInstance().getReference("RESCUE");
        mDatabaseRefRescue.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HashMap<String,String> details = new HashMap<String,String>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //res = ds.ge(RescueDB.class);
                    //Log.i(TAG,""+ds.getKey()+" : "+ds.getValue());
                    details.put(ds.getKey(),ds.getValue().toString());
                }
                RescueDB res = new RescueDB();
                res.setArea(details.get("area"));
                res.setDetails(details.get("details"));
                res.setID(Long.parseLong(details.get("id")));
                res.setIP(details.get("ip"));
                res.setLocation(details.get("location"));
                res.setName(details.get("name"));
                res.setPhone(details.get("phone"));
                res.setPictureName(details.get("pictureName"));
                res.setStatus(details.get("status"));
                res.setTime(details.get("time"));
                //Log.i(TAG, res.toString());

                if(new SharedPreffClass(getApplicationContext()).getLastRescueID().equalsIgnoreCase(""+res.getID())){
                    Log.i(TAG, "OLD RESCUE NOTIFICATION");
                }else{
                    Log.i(TAG, res.toString());
                    if(res.getStatus().equalsIgnoreCase("PENDING")){
                        String CHANNEL_ID = "RESCUE";
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.main_logo)
                                .setContentTitle("Rescue Needed")
                                .setContentText("Location : "+res.getArea())
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(res.getDetails()+"\n"+"Location : "+res.getArea()))
                                .setPriority(NotificationCompat.PRIORITY_MAX);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(1, builder.build());

                    }
                    new SharedPreffClass(getApplicationContext()).setLastRescueID(""+res.getID());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy! Service");
        Intent broadcastIntent = new Intent(getApplicationContext(), BackgroundNotificationBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        //stoptimertask();
    }

    /*private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public int counter=0;
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i(TAG, "in timer ++++  "+ (counter++));
            }
        };
    }


    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }*/



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
