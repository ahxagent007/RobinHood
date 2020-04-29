package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.bKash;
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

public class Donate extends AppCompatActivity {

    String TAG = "XIAN";

    private DatabaseReference mDatabaseRefDonateHistory, mDatabaseRefbKashNo;
    RecyclerView RV_donateList;
    ProgressBar PB_donateHistory;
    TextView TV_bkash01, TV_bkash02, TV_bkash03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        RV_donateList = findViewById(R.id.RV_donateList);
        PB_donateHistory = findViewById(R.id.PB_donateHistory);
        TV_bkash01 = findViewById(R.id.TV_bkash01);
        TV_bkash02 = findViewById(R.id.TV_bkash02);
        TV_bkash03 = findViewById(R.id.TV_bkash03);

        mDatabaseRefDonateHistory = FirebaseDatabase.getInstance().getReference("BKASH_HISTORY");
        mDatabaseRefbKashNo = FirebaseDatabase.getInstance().getReference("BKASH");

        final ArrayList<bKashHistory> bKashHistories = new ArrayList<bKashHistory>();
        final ArrayList<bKash> bKashs = new ArrayList<bKash>();

        mDatabaseRefbKashNo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bKashs.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    bKash kash = ds.getValue(bKash.class);
                    bKashs.add(kash);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(bKashs.size() > 1){
                            TV_bkash01.setText(bKashs.get(0).getNUMBER());
                            TV_bkash02.setText(bKashs.get(1).getNUMBER());
                            TV_bkash03.setText(bKashs.get(2).getNUMBER());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseRefDonateHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bKashHistories.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    bKashHistory res = ds.getValue(bKashHistory.class);
                    bKashHistories.add(res);
                }

                Collections.reverse(bKashHistories);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_donateList.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForDonateHistory(getApplicationContext(), bKashHistories);
                RV_donateList.setAdapter(mRecycleAdapter);

                PB_donateHistory.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class RecycleViewAdapterForDonateHistory extends RecyclerView.Adapter<RecycleViewAdapterForDonateHistory.ViewHolder> {


        ArrayList<bKashHistory> bKashHistories;
        Context context;

        public RecycleViewAdapterForDonateHistory(Context context, ArrayList<bKashHistory> bKashHistories) {
            super();
            this.context = context;
            this.bKashHistories = bKashHistories;

        }

        @Override
        public RecycleViewAdapterForDonateHistory.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_donate_history, viewGroup, false);

            RecycleViewAdapterForDonateHistory.ViewHolder viewHolder = new RecycleViewAdapterForDonateHistory.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForDonateHistory.ViewHolder viewHolder, final int i) {


            String number = bKashHistories.get(i).getMOBILE();
            String hiddenNumber = number.substring(0,3);
            for(int z=3; z<number.length()-3; z++){
                hiddenNumber += "X";
            }
            hiddenNumber += number.substring(number.length()-3,number.length());

            viewHolder.TV_mobile.setText(hiddenNumber);
            viewHolder.TV_ref.setText("Ref: "+bKashHistories.get(i).getREF());
            viewHolder.TV_amount.setText("TK. "+bKashHistories.get(i).getAMOUNT());


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
            return bKashHistories.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView TV_mobile, TV_ref, TV_amount;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                TV_mobile = itemView.findViewById(R.id.TV_mobile);
                TV_ref = itemView.findViewById(R.id.TV_ref);
                TV_amount = itemView.findViewById(R.id.TV_amount);

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
