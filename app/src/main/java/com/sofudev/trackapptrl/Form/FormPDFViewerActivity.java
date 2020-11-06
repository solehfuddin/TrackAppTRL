package com.sofudev.trackapptrl.Form;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

import es.dmoral.toasty.Toasty;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class FormPDFViewerActivity extends AppCompatActivity{

    RippleView btn_back;
    UniversalFontTextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pdfviewer);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        Bundle bundle = getIntent().getExtras();
        String link = bundle.getString("data");
        String title= bundle.getString("title");

        btn_back = (RippleView) findViewById(R.id.form_pdfviewer_ripplebtnback);
        txt_title= (UniversalFontTextView) findViewById(R.id.form_pdfviewer_title);

        txt_title.setText(title);

        WebView webView = (WebView) findViewById(R.id.form_pdfviewer_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
