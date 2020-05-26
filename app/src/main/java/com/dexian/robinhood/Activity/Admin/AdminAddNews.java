package com.dexian.robinhood.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dexian.robinhood.DB.NewsLink;
import com.dexian.robinhood.DB.bKashHistory;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminAddNews extends AppCompatActivity {

    EditText ET_link;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_news);

        ET_link = findViewById(R.id.ET_link);
        btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = ET_link.getText().toString();

                if(link.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter URL", Toast.LENGTH_SHORT).show();
                }else{
                    addNewsLink(link);
                }
            }
        });


    }

    private void addNewsLink(final String url){
        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("NEWS_LINK");

        mDatabaseRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long id = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    id = ds.getValue(bKashHistory.class).getID();
                    id++;
                }
                mDatabaseRef.child(""+id).setValue(new NewsLink(id, url));

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ET_link.setText("");
                        Toast.makeText(getApplicationContext(),"News Added", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
