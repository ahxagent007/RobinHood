package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.bumptech.glide.request.RequestOptions;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.RescueNewsDB;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RescueNewsActivity extends AppCompatActivity {

    String TAG = "XIAN";

    DatabaseReference mDatabaseRef;

    RecyclerView RV_rescueNews;
    ProgressBar PB_rescueNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_news);

        RV_rescueNews = findViewById(R.id.RV_rescueNews);
        PB_rescueNews = findViewById(R.id.PB_rescueNews);

        PB_rescueNews.setVisibility(View.VISIBLE);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RESCUE_NEWS");

        final ArrayList<RescueNewsDB> rescueDBS = new ArrayList<RescueNewsDB>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rescueDBS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    RescueNewsDB res = ds.getValue(RescueNewsDB.class);
                    rescueDBS.add(res);
                    Log.i(TAG, res.getPictureLink()+"");
                }

                Collections.reverse(rescueDBS);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_rescueNews.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForRescueNews(getApplicationContext(), rescueDBS);
                RV_rescueNews.setAdapter(mRecycleAdapter);

                PB_rescueNews.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class RecycleViewAdapterForRescueNews extends RecyclerView.Adapter<RecycleViewAdapterForRescueNews.ViewHolder> {


        ArrayList<RescueNewsDB> rescueDBS;
        Context context;

        public RecycleViewAdapterForRescueNews(Context context, ArrayList<RescueNewsDB> rescueDBS) {
            super();
            this.context = context;
            this.rescueDBS = rescueDBS;

        }

        @Override
        public RecycleViewAdapterForRescueNews.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_rescue_news, viewGroup, false);

            RecycleViewAdapterForRescueNews.ViewHolder viewHolder = new RecycleViewAdapterForRescueNews.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForRescueNews.ViewHolder viewHolder, final int i) {


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.image_not_found);

            Glide.with(getApplicationContext()).load(rescueDBS.get(i).getPictureLink()).apply(options).into(viewHolder.IV_RescueNews);
            //Glide.with(getApplicationContext()).load(rescueDBS.get(i).getPictureLink()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(viewHolder.IV_RescueNews);


            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        //Open URL
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rescueDBS.get(i).getNewsLink()));
                        startActivity(browserIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return rescueDBS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView IV_RescueNews;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                IV_RescueNews = itemView.findViewById(R.id.IV_RescueNews);

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
