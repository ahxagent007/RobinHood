package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dexian.robinhood.R;

public class WebsiteView extends AppCompatActivity {

    WebView WV_webview;
    ProgressBar PB_loadingWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_view);

        WV_webview = findViewById(R.id.WV_webview);
        PB_loadingWeb = findViewById(R.id.PB_loadingWeb);

        String url = getIntent().getStringExtra("URL");

        WV_webview.getSettings().setJavaScriptEnabled(true);
        WV_webview.loadUrl(url);

        WV_webview.getSettings().setJavaScriptEnabled(true);

        String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
        WV_webview.getSettings().setUserAgentString(userAgent);

        WV_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return false;
            }
        });


        WV_webview.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                PB_loadingWeb.setProgress(progress);

                if(progress == 100){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PB_loadingWeb.setProgress(0);
                        }
                    }, 500);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (WV_webview.canGoBack()) {
                        WV_webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
