package com.dexian.robinhood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.freesoulapps.preview.android.Preview;

public class TestActivity extends AppCompatActivity implements Preview.PreviewListener{
    Preview f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        f = findViewById(R.id.LINK_preview);
        f.setData("https://play.google.com/store/apps/details?id=com.secretdevbd.dexian.bangladeshpigeonhub");

        //https://github.com/PonnamKarthik/RichLinkPreview
    }

    @Override
    public void onDataReady(Preview preview) {
        f.setMessage(preview.getLink());
    }
}
