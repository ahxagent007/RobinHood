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
import com.dexian.robinhood.DB.NewsLink;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.RescueNewsDB;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.freesoulapps.preview.android.Preview;
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

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("NEWS_LINK");

        final ArrayList<NewsLink> newsLinks = new ArrayList<NewsLink>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsLinks.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    NewsLink res = ds.getValue(NewsLink.class);
                    newsLinks.add(res);

                }

                Collections.reverse(newsLinks);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_rescueNews.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForRescueNews(getApplicationContext(), newsLinks);
                RV_rescueNews.setAdapter(mRecycleAdapter);

                PB_rescueNews.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class RecycleViewAdapterForRescueNews extends RecyclerView.Adapter<RecycleViewAdapterForRescueNews.ViewHolder> {


        ArrayList<NewsLink> newsLinks;
        Context context;

        public RecycleViewAdapterForRescueNews(Context context, ArrayList<NewsLink> newsLinks) {
            super();
            this.context = context;
            this.newsLinks = newsLinks;

        }

        @Override
        public RecycleViewAdapterForRescueNews.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_rescue_news_link, viewGroup, false);

            RecycleViewAdapterForRescueNews.ViewHolder viewHolder = new RecycleViewAdapterForRescueNews.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForRescueNews.ViewHolder viewHolder, final int i) {


            /*RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.image_not_found);*/

            //Glide.with(getApplicationContext()).load(rescueDBS.get(i).getPictureLink()).apply(options).into(viewHolder.IV_RescueNews);
            //Glide.with(getApplicationContext()).load(rescueDBS.get(i).getPictureLink()).transform(new CenterInside(),new RoundedCorners(10)).dontAnimate().into(viewHolder.IV_RescueNews);


            if(newsLinks.get(i).getUrl() != null && newsLinks.get(i).getUrl() != null && !newsLinks.get(i).getUrl().equals("")){
                viewHolder.mPreview.setData(newsLinks.get(i).getUrl());
            }

            Log.i(TAG, newsLinks.get(i).getUrl());

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        //Open URL
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsLinks.get(position).getUrl()));
                        startActivity(browserIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsLinks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            //ImageView IV_RescueNews;
            Preview mPreview;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                mPreview = itemView.findViewById(R.id.LINK_preview);

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
