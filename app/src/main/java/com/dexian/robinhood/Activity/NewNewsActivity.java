package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.YoutubeVideo;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewNewsActivity extends AppCompatActivity {

    String TAG = "XIAN";

    int CURRENT_POSTION = 0;

    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    RecyclerView.SmoothScroller smoothScroller;

    LinearLayout LL_loading;
    ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_news);
        //DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        LL_loading = findViewById(R.id.LL_loading);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("RESCUE_VIDEO");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                youtubeVideos.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    YoutubeVideo res = ds.getValue(YoutubeVideo.class);
                    youtubeVideos.add(res);
                    //Log.i(TAG,ds.toString()+" "+ds.getKey());
                }

                //Collections.reverse(youtubeVideos);

                recyclerViewAdapter = new YTRecyclerViewAdapterTiktok(getApplicationContext(), getLifecycle(), youtubeVideos, NewNewsActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);

                LL_loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        /*ArrayList<YoutubeVideoNew> yts = new ArrayList<YoutubeVideoNew>();
        yts.add(VDO);

        //recyclerViewAdapter = new YTRecyclerViewAdapter(yts, this.getLifecycle());
        recyclerViewAdapter = new YTRecyclerViewAdapterTiktok(getApplicationContext(), this.getLifecycle(), yts, YTRecyclerViewActivityNew.this);
        recyclerView.setAdapter(recyclerViewAdapter);*/

        //recyclerView.getLayoutManager().scrollToPosition(POS);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.i(TAG, "newState : "+newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    CURRENT_POSTION = position;
                    Log.i(TAG,"position : "+position);
                    recyclerView.getLayoutManager().scrollToPosition(position);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.i(TAG, "dx : "+dx+ " dy : "+dy);
            }


        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }


    private int getCurrentItem(){
        return ((LinearLayoutManager)recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.i(TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.i(TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.i(TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.i(TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.i(TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    public class YTRecyclerViewAdapterTiktok extends RecyclerView.Adapter<YTRecyclerViewAdapterTiktok.ViewHolder> {

        String TAG = "XIAN";

        ArrayList<YoutubeVideo> videosList;
        Context context;
        Lifecycle lifecycle;
        Activity activity;

        public YTRecyclerViewAdapterTiktok(Context context, Lifecycle lifecycle, ArrayList<YoutubeVideo> videosList, Activity activity) {
            super();
            this.context = context;
            this.videosList = videosList;
            this.lifecycle = lifecycle;
            this.activity = activity;

            //Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public YTRecyclerViewAdapterTiktok.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_item, viewGroup, false);

            YTRecyclerViewAdapterTiktok.ViewHolder viewHolder = new YTRecyclerViewAdapterTiktok.ViewHolder(v);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final YTRecyclerViewAdapterTiktok.ViewHolder viewHolder, final int i) {

            viewHolder.setIsRecyclable(false);

            lifecycle.addObserver(viewHolder.youtube_player_view);

            viewHolder.youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    //String videoId = "yabDCV4ccQs";
                    if(CURRENT_POSTION == i){
                        youTubePlayer.loadVideo(videosList.get(i).getId(), 0f);
                        CURRENT_POSTION += 99;
                    }

                }
            });


        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return videosList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            YouTubePlayerView youtube_player_view;
            Button btn_comment, btn_like, btn_share;
            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                youtube_player_view = itemView.findViewById(R.id.youtube_player_view);

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