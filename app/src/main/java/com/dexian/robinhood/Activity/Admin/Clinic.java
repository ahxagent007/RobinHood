package com.dexian.robinhood.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dexian.robinhood.Activity.RescueList;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.Setting;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Clinic extends AppCompatActivity {

    TextView TV_title;
    ImageView IV_01, IV_02, IV_03, IV_04, IV_05;

    private DatabaseReference mDatabaseRef;
    ArrayList<Setting> setting = new ArrayList<Setting>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        TV_title = findViewById(R.id.TV_title);
        IV_01 = findViewById(R.id.IV_01);
        IV_02 = findViewById(R.id.IV_02);
        IV_03 = findViewById(R.id.IV_03);
        IV_04 = findViewById(R.id.IV_04);
        IV_05 = findViewById(R.id.IV_05);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SETTING");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setting.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Setting res = ds.getValue(Setting.class);
                    setting.add(res);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        TV_title.setText(setting.get(0).getTitle());
                        Glide.with(getApplicationContext()).load(setting.get(0).getIv_01()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(IV_01);
                        Glide.with(getApplicationContext()).load(setting.get(0).getIv_02()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(IV_02);
                        Glide.with(getApplicationContext()).load(setting.get(0).getIv_03()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(IV_03);
                        Glide.with(getApplicationContext()).load(setting.get(0).getIv_04()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(IV_04);
                        Glide.with(getApplicationContext()).load(setting.get(0).getIv_05()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(IV_05);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}