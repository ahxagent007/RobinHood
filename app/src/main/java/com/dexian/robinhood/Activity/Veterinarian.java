package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dexian.robinhood.DB.EmergencyDB;
import com.dexian.robinhood.DB.VetDB;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Veterinarian extends AppCompatActivity {

    String TAG = "XIAN";

    RecyclerView RV_vet;
    ProgressBar PB_loadingVet;

    DatabaseReference mDatabaseRefVet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinarian);

        RV_vet = findViewById(R.id.RV_vet);
        PB_loadingVet = findViewById(R.id.PB_loadingVet);

        PB_loadingVet.setVisibility(View.VISIBLE);

        mDatabaseRefVet = FirebaseDatabase.getInstance().getReference("VET");

        final ArrayList<VetDB> vetDBS = new ArrayList<VetDB>();

        mDatabaseRefVet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vetDBS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    VetDB res = ds.getValue(VetDB.class);
                    vetDBS.add(res);
                    //Log.i(TAG, ds.getKey());
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_vet.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForVet(getApplicationContext(), vetDBS);
                RV_vet.setAdapter(mRecycleAdapter);

                PB_loadingVet.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class RecycleViewAdapterForVet extends RecyclerView.Adapter<RecycleViewAdapterForVet.ViewHolder> {


        ArrayList<VetDB> vetDBS;
        Context context;

        public RecycleViewAdapterForVet(Context context, ArrayList<VetDB> vetDBS) {
            super();
            this.context = context;
            this.vetDBS = vetDBS;

        }

        @Override
        public RecycleViewAdapterForVet.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_vet, viewGroup, false);

            RecycleViewAdapterForVet.ViewHolder viewHolder = new RecycleViewAdapterForVet.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForVet.ViewHolder viewHolder, final int i) {


            viewHolder.TV_number.setText(vetDBS.get(i).getNUMBER());
            viewHolder.TV_name.setText(vetDBS.get(i).getNAME());
            viewHolder.TV_location.setText("Location: "+vetDBS.get(i).getLOCATION());


            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        //Call that number
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return vetDBS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView TV_number, TV_name, TV_location;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                TV_number = itemView.findViewById(R.id.TV_number);
                TV_name = itemView.findViewById(R.id.TV_name);
                TV_location = itemView.findViewById(R.id.TV_location);

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
}
