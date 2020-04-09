package com.dexian.robinhood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String TAG = "XIAN";

    TouchyWebView WV_fbPage;

    Button btn_adminLogin, btn_rescue, btn_rescueList, btn_vet, btn_emergency, btn_members, btn_donate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WV_fbPage = findViewById(R.id.WV_fbPage);
        btn_adminLogin = findViewById(R.id.btn_adminLogin);
        btn_rescue = findViewById(R.id.btn_rescue);
        btn_vet = findViewById(R.id.btn_vet);
        btn_emergency = findViewById(R.id.btn_emergency);
        btn_rescueList = findViewById(R.id.btn_rescueList);
        btn_members = findViewById(R.id.btn_members);
        btn_donate = findViewById(R.id.btn_donate);

        WV_fbPage.getSettings().setJavaScriptEnabled(true);
        //WV_fbPage.getSettings().setLoadWithOverviewMode(true);
        //WV_fbPage.getSettings().setUseWideViewPort(true);
       // WV_fbPage.getSettings().setSupportZoom(true);
        WV_fbPage.setVerticalScrollBarEnabled(true);
        WV_fbPage.getSettings().setBuiltInZoomControls(true);


        WV_fbPage.loadUrl("https://m.facebook.com/junglesdad/");

        btn_adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btn_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddRescue.class));

            }
        });

        btn_vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Veterinarian.class));

            }
        });

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Emergency.class));

            }
        });

        btn_rescueList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RescueList.class));

            }
        });

        btn_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Members.class));

            }
        });

        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Donate .class));

            }
        });


    }
}
