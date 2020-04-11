package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dexian.robinhood.DB.Post;
import com.dexian.robinhood.DB.bKash;
import com.dexian.robinhood.R;
import com.dexian.robinhood.TouchyWebView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String TAG = "XIAN";

    TouchyWebView WV_fbPage;

    Button btn_adminLogin, btn_rescue, btn_rescueList, btn_vet, btn_emergency, btn_members, btn_donate;
    TextView TV_news1, TV_news2, TV_news3;

    DatabaseReference mDatabaseRefPost;

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
        TV_news1 = findViewById(R.id.TV_news1);
        TV_news2 = findViewById(R.id.TV_news2);
        TV_news3 = findViewById(R.id.TV_news3);


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

        final ArrayList<Post> posts = new ArrayList<Post>();

        mDatabaseRefPost = FirebaseDatabase.getInstance().getReference("POST");

        mDatabaseRefPost.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post p = ds.getValue(Post.class);
                    posts.add(p);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(posts.size() > 2){
                            TV_news1.setText("# "+posts.get(posts.size()-1).getAdminName()+" : "+posts.get(posts.size()-1).getPost());
                            TV_news2.setText("# "+posts.get(posts.size()-2).getAdminName()+" : "+posts.get(posts.size()-2).getPost());
                            TV_news3.setText("# "+posts.get(posts.size()-3).getAdminName()+" : "+posts.get(posts.size()-3).getPost());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
