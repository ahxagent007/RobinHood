package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dexian.robinhood.DB.EmergencyDB;
import com.dexian.robinhood.DB.bKashHistory;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Emergency extends AppCompatActivity {

    String TAG = "XIAN";

    DatabaseReference mDatabaseRefEmergency;
    RecyclerView RV_emergency;
    ProgressBar PB_loadingEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        RV_emergency = findViewById(R.id.RV_emergency);
        PB_loadingEmergency = findViewById(R.id.PB_loadingEmergency);

        mDatabaseRefEmergency = FirebaseDatabase.getInstance().getReference("EMERGENCY");

        final ArrayList<EmergencyDB> emergencyDBS = new ArrayList<EmergencyDB>();

        mDatabaseRefEmergency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emergencyDBS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    EmergencyDB res = ds.getValue(EmergencyDB.class);
                    emergencyDBS.add(res);
                    Log.i(TAG, ds.getKey());
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_emergency.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForEmergency(getApplicationContext(), emergencyDBS);
                RV_emergency.setAdapter(mRecycleAdapter);

                PB_loadingEmergency.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class RecycleViewAdapterForEmergency extends RecyclerView.Adapter<RecycleViewAdapterForEmergency.ViewHolder> {


        ArrayList<EmergencyDB> emergencyDBS;
        Context context;

        public RecycleViewAdapterForEmergency(Context context, ArrayList<EmergencyDB> bKashHistories) {
            super();
            this.context = context;
            this.emergencyDBS = bKashHistories;

        }

        @Override
        public RecycleViewAdapterForEmergency.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_emergency, viewGroup, false);

            RecycleViewAdapterForEmergency.ViewHolder viewHolder = new RecycleViewAdapterForEmergency.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForEmergency.ViewHolder viewHolder, final int i) {


            viewHolder.TV_number.setText(emergencyDBS.get(i).getNUMBER());
            viewHolder.TV_name.setText(emergencyDBS.get(i).getNAME());


            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        //Call that number
                        makeCall(emergencyDBS.get(i).getNUMBER());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return emergencyDBS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView TV_number, TV_name;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                TV_number = itemView.findViewById(R.id.TV_number);
                TV_name = itemView.findViewById(R.id.TV_name);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);
            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }

    }


    private void makeCall(final String number) {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + number));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                /*
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                }else{

                }*/
                //startActivity(intent);

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        // Set other dialog properties
        builder.setMessage("Do you want to make call?");
        builder.setTitle("Call "+number);

        // Create the AlertDialog
        dialog = builder.create();
        dialog.show();



    }
}
