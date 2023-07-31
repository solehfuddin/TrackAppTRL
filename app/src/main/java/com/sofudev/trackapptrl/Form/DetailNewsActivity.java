package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

public class DetailNewsActivity extends AppCompatActivity {
    ImageView imageView;
    UniversalFontTextView txtTitle, txtSubtitle;
    WebView webView;
    FloatingActionButton fabPrev;

    String title, subtitle, image, description;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        imageView = findViewById(R.id.detail_news_imgnews);
        txtTitle = findViewById(R.id.detail_news_txttitle);
        txtSubtitle = findViewById(R.id.detail_news_txtsubtitle);
        webView = findViewById(R.id.detail_news_webview);
        fabPrev = findViewById(R.id.detail_news_fabPrev);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData();
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            subtitle = bundle.getString("subtitle");
            image = bundle.getString("image");
            description = bundle.getString("descr");

            txtTitle.setText(title);
            txtSubtitle.setText(subtitle);
            webView.loadData(description, "text/html", "UTF-8");

            Picasso.with(getApplicationContext()).load(image).fit().into(imageView);
        }
    }
}