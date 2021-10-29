package com.sofudev.trackapptrl.Form;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

//import com.liferay.portal.kernel.json.JSONFactoryUtil;
//import com.liferay.portal.kernel.json.JSONObject;
//import com.liferay.portal.kernel.json.JSONObject;
import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.FanpageActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class FormPaymentKreditpro extends AppCompatActivity {

    Config config = new Config();

    AlertDialog dialog3ds;
    ACProgressCustom loading;
    ImageView btnBack;
    UniversalFontTextView txtOrderNumber, txtTimer, txtDate, txtAmount;
    Button btnPay, btnCancel;
    Adapter_panduantransfer adapter_panduantransfer;
    ListView lvPanduan;
    List<String> list_panduantransfer = new ArrayList<>();
    String orderNumber, totalAmount, expDate, duration, tempAmount, ownerPhone;

    CustomWebView webView;
    private static final String TAG = FormPaymentKreditpro.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

    //select whether you want to upload multiple files (set 'true' for yes)
    private boolean multiple_files = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_kreditpro);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        showLoading();

        btnBack = findViewById(R.id.form_paymentkp_btn_back);
        txtOrderNumber = findViewById(R.id.form_paymentkp_txtOrderNumber);
        txtTimer = findViewById(R.id.form_paymentkp_txtTimer);
        txtDate = findViewById(R.id.form_paymentkp_txtDate);
        txtAmount = findViewById(R.id.form_paymentkp_txtAmountBill);
        btnPay = findViewById(R.id.form_paymentkp_btn_pay);
        btnCancel = findViewById(R.id.form_paymentkp_btn_cancel);
        lvPanduan = findViewById(R.id.form_paymentkp_listtransfer);

        getData();
        showPanduan();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvPanduan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                //intent.putExtra("data", "https://drive.google.com/open?id=1ot32BlP_Cqpka2P20qaoTxu5qgVTWmsw");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(FormPaymentKreditpro.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                dialog.setContentView(R.layout.form_dialog_cancelorder);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                //lwindow.copyFrom(dialog.getWindow().getAttributes());

                RippleView btnYes = (RippleView) dialog.findViewById(R.id.form_dialogcancelorder_btnYes);
                RippleView btnNo  = (RippleView) dialog.findViewById(R.id.form_dialogcancelorder_btnNo);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelPayment(txtOrderNumber.getText().toString());
                    }
                });

                if (!isFinishing()){
                    dialog.show();
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String redirectUrl = "https://wf.dev.neo-fusion.com/tdfp2p/request/loan/trl?param=z7w--VUpfxzg-ZMncbWVTMIZ4Wgdk_E8nuIGtpO0JeyRiSGlfa1C8lgfhYgXOJ8NdLWcAGNByOkR7q4TR8Hf9ynpc0XcaQNz9gq2yY0ooQCalEN3w8qTRAwkK4Z8F50pfRyAO78AlnTL02oQyPG9-nUqaukA6hzPC8Src2qrzzefYgJrUfdOEU3_xlgzoqSoPhwqOcLfymZ_ntlvZPtQbQ";
                postKreditPro(orderNumber, tempAmount, ownerPhone);
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();

        totalAmount = bundle.getString("amount");
        orderNumber = bundle.getString("orderNumber");
        expDate     = bundle.getString("expDate");
        duration    = bundle.getString("duration");
        ownerPhone  = bundle.getString("ownerPhone");

        Toasty.info(this, "No owner " + ownerPhone, Toast.LENGTH_SHORT).show();

        txtOrderNumber.setText(orderNumber);
        txtAmount.setText(totalAmount);
        txtDate.setText(expDate);

        tempAmount = convertToInt(totalAmount);

        setTimer(txtTimer, Integer.parseInt(duration));
    }

    private String convertToInt(String amount)
    {
        String val = amount.replace(",00", "");
        val = val.replace(".", "");

        return val;
    }

    private void postKreditPro(final String orderId, final String amount, final String msisdn)
    {
        String url = config.Ip_address + config.payment_method_kreditPro;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String encrypt = jsonObject.getString("encrypted");
                    String redirectUrl = "https://wf.dev.neo-fusion.com/tdfp2p/request/loan/trl?param=" + encrypt;

                    showDialogPayment(redirectUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderId", orderId);
                hashMap.put("amount", amount);
                hashMap.put("msisdn", msisdn);
                return hashMap;
            }
        };

//        AppController.getInstance().addToRequestQueue(stringRequest);
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void showDialogPayment(String url)
    {
        webView = new CustomWebView(FormPaymentKreditpro.this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= 21)
        {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new KrediproWebviewClient());
        webView.loadUrl(url);
        webView.setWebChromeClient(new KreditproChromeClient());

        AlertDialog.Builder builder = new AlertDialog.Builder(FormPaymentKreditpro.this);

        dialog3ds = builder.create();
        dialog3ds.setTitle("Kreditpro Payment");
        dialog3ds.setView(webView);
        webView.requestFocus(View.FOCUS_DOWN);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog3ds.dismiss();
            }
        });

        if (!isFinishing())
        {
            dialog3ds.show();
        }
    }

    private void cancelPayment(String id) {
        String url = config.Ip_address + config.payment_method_cancelBilling;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("responseCode");
                    //String respon = jsonObject.getString("responseDescription");

                    //Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

                    if (status.equals("200") || status.contentEquals("200") || status.contains("200")) {
                        Toast.makeText(getApplicationContext(), "Cancel success", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Cannot cancel payment billing", Toast.LENGTH_LONG).show();
                    }

                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cancel", error.getMessage());
            }
        });

//        AppController.getInstance().addToRequestQueue(stringRequest);
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void setTimer(final UniversalFontTextView lbl_timer, int duration)
    {
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timer = String.format(Locale.getDefault(), "%02d : %02d : %02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                lbl_timer.setText(timer);
            }

            @Override
            public void onFinish() {
                cancelPayment(txtOrderNumber.getText().toString());
                lbl_timer.setText("00 : 00 : 00");
            }
        }.start();

        loading.dismiss();
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormPaymentKreditpro.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();

        if(!isFinishing()){
            loading.show();
        }
    }

    private void showPanduan() {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allPanduan = Arrays.asList("Panduan Pembayaran dengan CC");
        list_panduantransfer.addAll(allPanduan);
        adapter_panduantransfer.notifyDataSetChanged();
        lvPanduan.setAdapter(adapter_panduantransfer);
    }

    private class CustomWebView extends WebView {
        public CustomWebView(Context context)
        {
            super(context);
        }

        // Note this!
        @Override
        public boolean onCheckIsTextEditor()
        {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!hasFocus())
                        requestFocus();
                    break;
            }

            return super.onTouchEvent(ev);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= 21)
        {
            Uri[] results = null;

            //Cek response
            if (resultCode == Activity.RESULT_OK)
            {
                if (requestCode == FCR)
                {
                    if (null == mUMA)
                    {
                        return;
                    }

                    if (data == null || data.getData() == null)
                    {
                        if (mCM != null)
                        {
                            results = new Uri[] {Uri.parse(mCM)};
                        }
                        else
                        {
                            String dataString = data.getDataString();
                            if (dataString != null)
                            {
                                results = new Uri[] {Uri.parse(dataString)};
                            }
                            else
                            {
                                if (multiple_files)
                                {
                                    if (data.getClipData() != null)
                                    {
                                        final int selectedFile = data.getClipData().getItemCount();
                                        results = new Uri[selectedFile];
                                        for (int i = 0; i < selectedFile; i++)
                                        {
                                            results[i] = data.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                mUMA.onReceiveValue(results);
                mUMA = null;
            }
            else
            {
                if (requestCode == FCR)
                {
                    if (null == mUM) return;
                    Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                    mUM.onReceiveValue(result);
                    mUM = null;
                }
            }
        }
    }

    private boolean file_permission(){
        if(Build.VERSION.SDK_INT >=23 && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(FormPaymentKreditpro.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    1);
            return false;
        }else{
            return true;
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    //back/down key handling
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(webView.canGoBack()){
                        webView.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class KrediproWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (Uri.parse(url).getHost().endsWith("180.250.96.154")) {
                dialog3ds.dismiss();

                finish();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    class KreditproChromeClient extends WebChromeClient {
        //handling input[type="file"] requests for android API 16+
        @SuppressLint("ObsoleteSdkInt")
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            if (multiple_files && Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
        }

        //handling input[type="file"] requests for android API 21+
        @SuppressLint("InlinedApi")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            if (file_permission()) {
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA};

                //checking for storage permission to write images for upload
                if (ContextCompat.checkSelfPermission(FormPaymentKreditpro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(FormPaymentKreditpro.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(FormPaymentKreditpro.this, perms, FCR);

                    //checking for WRITE_EXTERNAL_STORAGE permission
                } else if (ContextCompat.checkSelfPermission(FormPaymentKreditpro.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormPaymentKreditpro.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);

                    //checking for CAMERA permissions
                } else if (ContextCompat.checkSelfPermission(FormPaymentKreditpro.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormPaymentKreditpro.this,
                            new String[]{Manifest.permission.CAMERA}, FCR);
                }

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }

                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(FormPaymentKreditpro.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");

                if (multiple_files) {
                    contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }else{
                return false;
            }
        }
    }
}
