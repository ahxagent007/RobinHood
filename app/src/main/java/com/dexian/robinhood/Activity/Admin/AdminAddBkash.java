package com.dexian.robinhood.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dexian.robinhood.DB.bKashHistory;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminAddBkash extends AppCompatActivity {

    String TAG = "XIAN";

    EditText ET_mobile, ET_reff, ET_amount;
    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_bkash);

        ET_mobile = findViewById(R.id.ET_mobile);
        ET_reff = findViewById(R.id.ET_reff);
        ET_amount = findViewById(R.id.ET_amount);
        btn_done = findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = ET_mobile.getText().toString();
                String reff = ET_reff.getText().toString();
                int amount = 0;
                if(ET_amount.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    amount = Integer.parseInt(ET_amount.getText().toString());
                }

                if(mobile.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill up mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    addBkash(mobile, reff, amount);
                }
            }
        });


    }

    private void addBkash(final String mobile, final String refff, final int amount){
        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("BKASH_HISTORY");

        mDatabaseRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long id = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    id = ds.getValue(bKashHistory.class).getID();
                    id++;
                    Log.i(TAG, "ID == "+id);
                    Log.i(TAG, "DS == "+ds.toString());
                }

                mDatabaseRef.child(""+id).setValue(new bKashHistory(id, mobile, refff, amount));
                Toast.makeText(getApplicationContext(),"Donation added", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
