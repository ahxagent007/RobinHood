package com.dexian.robinhood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String TAG = "XIAN";

    TouchyWebView WV_fbPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WV_fbPage = findViewById(R.id.WV_fbPage);

        WV_fbPage.getSettings().setJavaScriptEnabled(true);
        //WV_fbPage.getSettings().setLoadWithOverviewMode(true);
        //WV_fbPage.getSettings().setUseWideViewPort(true);
       // WV_fbPage.getSettings().setSupportZoom(true);
        WV_fbPage.setVerticalScrollBarEnabled(true);
        WV_fbPage.getSettings().setBuiltInZoomControls(true);


        WV_fbPage.loadUrl("https://m.facebook.com/junglesdad/");

        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.i(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    }
}
