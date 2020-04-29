package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.dexian.robinhood.R;

public class WebsiteView extends AppCompatActivity {

    WebView WV_website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_view);

        WV_website = findViewById(R.id.WV_website);

        WV_website.getSettings().setJavaScriptEnabled(true);
        WV_website.loadUrl("http://animalcaretrustbangladesh.org/");

    }
}
