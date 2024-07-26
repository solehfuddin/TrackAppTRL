package com.sofudev.trackapptrl.Form;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class FormPDFViewerActivity extends AppCompatActivity{

    RippleView btn_back;
    UniversalFontTextView txt_title;
    ProgressBar progressBar;
    PDFView pdfView;
    WebView webView;

    CustomLoading customLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pdfviewer);

        customLoading = new CustomLoading(this);
        customLoading.showLoadingDialog();

        btn_back = findViewById(R.id.form_pdfviewer_ripplebtnback);
        txt_title= findViewById(R.id.form_pdfviewer_title);
        pdfView  = findViewById(R.id.form_pdfviewer_pdf);
        progressBar = findViewById(R.id.form_pdfviewer_progressbar);
        webView  = findViewById(R.id.form_pdfviewer_webview);

        webView.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);

        getData();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class RetrievePDFfromUrl extends AsyncTask<String, Integer, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
//            pdfView.fromStream(inputStream).load();

            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    customLoading.dismissLoadingDialog();
                }
            }).load();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getData()
    {
        String source;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String link = bundle.getString("data");
            String title= bundle.getString("title");
            source      = bundle.getString("source");

            txt_title.setText(title);

//            webView.getSettings().setLightTouchEnabled(true);
//            webView.getSettings().setBuiltInZoomControls(true);
//            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + link);
//            webView.setWebChromeClient(new WebChromeClient(){
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    super.onProgressChanged(view, newProgress);
//                    if (newProgress < 100) {
//                        customLoading.showLoadingDialog();
//                    }
//                    if (newProgress == 100) {
//                        customLoading.dismissLoadingDialog();
//                    }
//                }
//            });

//            new RetrievePDFfromUrl().execute(link);

            assert source != null;
            if (source.equals("url"))
            {
                getPdf(link);
            }
            else
            {
                pdfView.fromAsset(link).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        customLoading.dismissLoadingDialog();
                    }
                }).onError(new OnErrorListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onError(Throwable t) {
                        customLoading.dismissLoadingDialog();

                        final Dialog dialog = new Dialog(FormPDFViewerActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                        dialog.setContentView(R.layout.dialog_warning);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        lwindow.copyFrom(dialog.getWindow().getAttributes());
                        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                        UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                        imgClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                        dialog.getWindow().setAttributes(lwindow);
                        txtTitle.setText("Gagal memuat file pdf (404)");
                        if (!isFinishing()){
                            dialog.show();
                        }
                    }
                }).load();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getPdf(final String completeUrl) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    InputStream input = new URL(completeUrl).openStream();
                    pdfView.fromStream(input).onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            customLoading.dismissLoadingDialog();
                        }
                    }).onError(new OnErrorListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onError(Throwable t) {
                            Log.d(FormPDFViewerActivity.class.getSimpleName(), "Error pdf : " + t.getMessage());
                            customLoading.dismissLoadingDialog();

                            final Dialog dialog = new Dialog(FormPDFViewerActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                            dialog.setContentView(R.layout.dialog_warning);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                            UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                            imgClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                            dialog.getWindow().setAttributes(lwindow);
                            txtTitle.setText("Gagal memuat file pdf (404)");
                            if (!isFinishing()){
                                dialog.show();
                            }
                        }
                    }).load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
