package com.sofudev.trackapptrl;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;

public class FanpageActivity extends AppCompatActivity {

    public WebView fan_view;
    BootstrapButton btn_back;
    ProgressBar progressBar;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanpage);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("data");
        }

        openFanpage();
        backToapp();
    }

    private void backToapp() {
        btn_back = (BootstrapButton) findViewById(R.id.sosmed_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FanpageActivity.this.finish();
            }
        });
    }

    private void openFanpage() {
        fan_view = (WebView) findViewById(R.id.sosmed_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fan_view.setWebViewClient(new MyWeb());
        fan_view.setWebChromeClient(new MyProgress());
        fan_view.getSettings().setJavaScriptEnabled(true);
        fan_view.loadUrl(url);
        fan_view.setHorizontalScrollBarEnabled(true);
    }


    private class MyWeb extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }

    private class MyProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
