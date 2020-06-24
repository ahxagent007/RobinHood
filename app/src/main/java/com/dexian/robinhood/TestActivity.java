package com.dexian.robinhood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.freesoulapps.preview.android.Preview;


public class TestActivity extends AppCompatActivity implements Preview.PreviewListener{

    String url = "https://play.google.com/store/apps/details?id=com.secretdevbd.dexian.bangladeshpigeonhub";

    Preview mPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        mPreview = findViewById(R.id.preview);
        mPreview.setListener(this);

        try{
            mPreview.setData(url);
        }catch (Exception e){
            Log.i("XIAN", "EX : "+e);
        }

        //"https://play.google.com/store/apps/details?id=com.secretdevbd.dexian.bangladeshpigeonhub"

        //https://github.com/PonnamKarthik/RichLinkPreview
    }

    @Override
    public void onDataReady(Preview preview) {
        mPreview.setMessage(preview.getLink());
    }
}
