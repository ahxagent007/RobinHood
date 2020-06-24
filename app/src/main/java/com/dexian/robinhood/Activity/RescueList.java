package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dexian.robinhood.DB.Area;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RescueList extends AppCompatActivity {

    String TAG = "XIAN";

    private DatabaseReference mDatabaseRef;
    ArrayList<RescueDB> rescueDBS;

    RecyclerView RV_rescueList;
    ProgressBar PB_loadingRescue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_list);

        RV_rescueList = findViewById(R.id.RV_rescueList);
        PB_loadingRescue = findViewById(R.id.PB_loadingRescue);

        PB_loadingRescue.setVisibility(View.VISIBLE);

        rescueDBS = new ArrayList<RescueDB>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RESCUE");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rescueDBS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    RescueDB res = ds.getValue(RescueDB.class);
                    rescueDBS.add(res);
                    //Log.i(TAG,ds.toString()+" "+ds.getKey());
                }

                Collections.reverse(rescueDBS);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_rescueList.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForRescueList(getApplicationContext(), rescueDBS);
                RV_rescueList.setAdapter(mRecycleAdapter);

                PB_loadingRescue.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class RecycleViewAdapterForRescueList extends RecyclerView.Adapter<RecycleViewAdapterForRescueList.ViewHolder> {


        ArrayList<RescueDB> rescueDBS;
        Context context;

        public RecycleViewAdapterForRescueList(Context context, ArrayList<RescueDB> rescueDBS) {
            super();
            this.context = context;
            this.rescueDBS = rescueDBS;

        }

        @Override
        public RecycleViewAdapterForRescueList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_rescue_item, viewGroup, false);

            RecycleViewAdapterForRescueList.ViewHolder viewHolder = new RecycleViewAdapterForRescueList.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForRescueList.ViewHolder viewHolder, final int i) {


            Glide.with(getApplicationContext()).load(rescueDBS.get(i).getPictureName()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(viewHolder.IV_pic);
            viewHolder.TV_details.setText(rescueDBS.get(i).getDetails());
            viewHolder.TV_location.setText("Location: "+rescueDBS.get(i).getArea());
            viewHolder.TV_time.setText(rescueDBS.get(i).getTime());

            viewHolder.TV_status.setText("Status: "+rescueDBS.get(i).getStatus());

            if(rescueDBS.get(i).getStatus().equalsIgnoreCase("PENDING")){
                viewHolder.btn_rescueOP.setVisibility(View.VISIBLE);

                viewHolder.btn_rescueOP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "RESCUE "+i);
                    }
                });
            }else{
                viewHolder.btn_rescueOP.setVisibility(View.GONE);
            }



            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return rescueDBS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView IV_pic;
            TextView TV_details, TV_location, TV_status, TV_time;
            Button btn_rescueOP;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                IV_pic = itemView.findViewById(R.id.IV_pic);
                TV_details = itemView.findViewById(R.id.TV_details);
                TV_location = itemView.findViewById(R.id.TV_location);
                TV_status = itemView.findViewById(R.id.TV_status);
                //btn_rescueOP = itemView.findViewById(R.id.btn_rescueOP);
                TV_time = itemView.findViewById(R.id.TV_time);

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
