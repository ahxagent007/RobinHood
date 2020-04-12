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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dexian.robinhood.DB.EmergencyDB;
import com.dexian.robinhood.DB.MemberDB;
import com.dexian.robinhood.ItemClickListener;
import com.dexian.robinhood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Members extends AppCompatActivity {

    String TAG = "XIAN";

    RecyclerView RV_members;
    ProgressBar PB_members;

    DatabaseReference mDatabaseRefMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        RV_members = findViewById(R.id.RV_members);
        PB_members = findViewById(R.id.PB_members);

        PB_members.setVisibility(View.VISIBLE);

        mDatabaseRefMembers = FirebaseDatabase.getInstance().getReference("MEMBERS");

        final ArrayList<MemberDB> members = new ArrayList<MemberDB>();

        mDatabaseRefMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                members.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    MemberDB res = ds.getValue(MemberDB.class);
                    members.add(res);
                    Log.i(TAG, ds.getKey());
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_members.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForMembers(getApplicationContext(), members);
                RV_members.setAdapter(mRecycleAdapter);

                PB_members.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class RecycleViewAdapterForMembers extends RecyclerView.Adapter<RecycleViewAdapterForMembers.ViewHolder> {


        ArrayList<MemberDB> memberDBS;
        Context context;

        public RecycleViewAdapterForMembers(Context context, ArrayList<MemberDB> memberDBS) {
            super();
            this.context = context;
            this.memberDBS = memberDBS;

        }

        @Override
        public RecycleViewAdapterForMembers.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_member, viewGroup, false);

            RecycleViewAdapterForMembers.ViewHolder viewHolder = new RecycleViewAdapterForMembers.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecycleViewAdapterForMembers.ViewHolder viewHolder, final int i) {

            Glide.with(getApplicationContext()).load(memberDBS.get(i).getPIC()).transform(new CenterInside(),new RoundedCorners(100)).dontAnimate().into(viewHolder.IV_memberPic);
            viewHolder.TV_memberName.setText(memberDBS.get(i).getNAME().toUpperCase());
            viewHolder.TV_memberType.setText(memberDBS.get(i).getTYPE());


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
            return memberDBS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView IV_memberPic;
            TextView TV_memberName, TV_memberType;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                IV_memberPic = itemView.findViewById(R.id.IV_memberPic);
                TV_memberName = itemView.findViewById(R.id.TV_memberName);
                TV_memberType = itemView.findViewById(R.id.TV_memberType);

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
