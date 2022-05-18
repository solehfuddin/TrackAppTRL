package com.sofudev.trackapptrl.Form;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.CustomSpinner;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.FanpageActivity;
import com.sofudev.trackapptrl.R;
import com.ssomai.android.scalablelayout.ScalableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class FormPaymentCC extends AppCompatActivity {

    private static final String TAG = FormPaymentCC.class.getSimpleName();
    Config config = new Config();
    String URLGETDETAILACCOUNT = config.Ip_address + config.orderlens_get_detailaccount;
    String URLSENDREMINDER     = config.Ip_address + config.orderlens_send_reminder;
    String URLCHECKREMINDER    = config.Ip_address + config.orderlens_check_reminder;

    Dialog dialogCC;
    AlertDialog dialog3ds;
    CustomLoading customLoading;
    ImageView btnBack;
    UniversalFontTextView txtOrderNumber, txtTimer, txtDate, txtAmount;
    Button btnPay, btnCancel;
    Adapter_panduantransfer adapter_panduantransfer;
    ListView lvPanduan;
    List<String> list_panduantransfer = new ArrayList<>();
    String orderNumber, totalAmount, expDate, duration, tempAmount, emailaddress, opticname, username;
    String BASE_URL;
    String CLIENT_KEY;
    String CARD_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_cc);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);

        BASE_URL    = "http://www.timurrayalab.com/";
//        CLIENT_KEY  = "SB-Mid-client-WeC58FxcSR3DiAlh";
        CLIENT_KEY  = "9de85895-8bde-4e82-a369-1f574d330106";

//        showLoading();

        btnBack = findViewById(R.id.form_paymentcc_btn_back);
        txtOrderNumber = findViewById(R.id.form_paymentcc_txtOrderNumber);
        txtTimer = findViewById(R.id.form_paymentcc_txtTimer);
        txtDate = findViewById(R.id.form_paymentcc_txtDate);
        txtAmount = findViewById(R.id.form_paymentcc_txtAmountBill);
        btnPay = findViewById(R.id.form_paymentcc_btn_pay);
        btnCancel = findViewById(R.id.form_paymentcc_btn_cancel);
        lvPanduan = findViewById(R.id.form_paymentcc_listtransfer);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData();
        showPanduan();

        SdkCoreFlowBuilder.init(this, CLIENT_KEY, BASE_URL)
                .enableLog(true)
                .buildSDK();

        lvPanduan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                intent.putExtra("data", "https://drive.google.com/open?id=1SPOypCIQuIHq8byfA895QYMWx-fOL1b-");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPayment(txtOrderNumber.getText().toString());
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCC = new Dialog(FormPaymentCC.this);
                dialogCC.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                dialogCC.setContentView(R.layout.dialog_payment_cc);
                dialogCC.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogCC.setCancelable(true);
                lwindow.copyFrom(dialogCC.getWindow().getAttributes());
                lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                //Binding UI
                ScalableLayout slMain             = dialogCC.findViewById(R.id.dialog_paymentcc_slmain);
                LinearLayout llResult             = dialogCC.findViewById(R.id.dialog_paymentcc_llresult);
                TextView tvAmount                 = dialogCC.findViewById(R.id.dialog_paymentcc_tvAmount);
                TextView tvResult                 = dialogCC.findViewById(R.id.dialog_paymentcc_tvResult);
                final BootstrapEditText edtName   = dialogCC.findViewById(R.id.dialog_paymentcc_edtName);
                final BootstrapEditText edtNumber = dialogCC.findViewById(R.id.dialog_paymentcc_edtCCNumber);
                final BootstrapEditText edtCvv    = dialogCC.findViewById(R.id.dialog_paymentcc_edtCvv);
                final Spinner spinMonth           = dialogCC.findViewById(R.id.dialog_paymentcc_spinMonth);
                final Spinner spinYear            = dialogCC.findViewById(R.id.dialog_paymentcc_spinYear);
                Button btnPay                     = dialogCC.findViewById(R.id.dialog_paymentcc_btnPay);
                Button btnBack                    = dialogCC.findViewById(R.id.dialog_paymentcc_btnBack);

                //Inject View
                //tvAmount.setText(convertToInt(tempAmount));
                tvAmount.setText("Rp " + totalAmount);
                //tvAmount.setText(tempAmount);

                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, new CustomSpinner[]{
                        new CustomSpinner(1, "01", "JAN"),
                        new CustomSpinner(2, "02", "FEB"),
                        new CustomSpinner(3, "03", "MAR"),
                        new CustomSpinner(4, "04", "APR"),
                        new CustomSpinner(5, "05", "MEI"),
                        new CustomSpinner(6, "06", "JUN"),
                        new CustomSpinner(7, "07", "JUL"),
                        new CustomSpinner(8, "08", "AGT"),
                        new CustomSpinner(9, "09", "SEP"),
                        new CustomSpinner(10, "10", "OKT"),
                        new CustomSpinner(11, "11", "NOP"),
                        new CustomSpinner(12, "12", "DES")
                });
                spinMonth.setAdapter(spinnerArrayAdapter);

                ArrayList<String> years = new ArrayList<>();
                int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                for (int i = thisYear+1; i <= (thisYear+20); i++) {
                    years.add(Integer.toString(i));
                }
                ArrayAdapter<String> yearadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, years);
                spinYear.setAdapter(yearadapter);

                //Enabling Btn
                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name     = edtName.getText().toString().trim();
                        final String number   = edtNumber.getText().toString();
                        final String vcc      = edtCvv.getText().toString();

                        if (name.isEmpty())
                        {
                            edtName.setError("Please insert name here");
                        }
                        else if (number.isEmpty())
                        {
                            edtNumber.setError("Please check cc number here");
                        }
                        else if (vcc.isEmpty())
                        {
                            edtCvv.setError("Please check your cvv code");
                        }
                        else if (!name.isEmpty() && !number.isEmpty() && !vcc.isEmpty())
                        {
                            //Midtrans here
                            CustomSpinner customSpinner = (CustomSpinner) spinMonth.getSelectedItem();
                            String expMonth = String.valueOf(customSpinner.id);
                            String expYear  = (String) spinYear.getSelectedItem();

                            midtransVerify(number, vcc, expMonth, expYear, CLIENT_KEY);
                        }
                    }
                });

                dialogCC.getWindow().setAttributes(lwindow);
                dialogCC.show();
            }
        });
    }

    private String convertToInt(String amount)
    {
        String val = amount.replace(",00", "");
        val = val.replace(".", "");

        return val;
    }

    private void midtransVerify(String cardNumber, String cvvNumber, String expMonth, String expYear, String key)
    {
        CardTokenRequest tokenRequest = new CardTokenRequest(cardNumber, cvvNumber, expMonth, expYear, key);
        tokenRequest.setSecure(true);
        tokenRequest.setGrossAmount(Long.parseLong(tempAmount));

        MidtransSDK.getInstance().getCardToken(tokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {
                CARD_TOKEN = tokenDetailsResponse.getTokenId();
                Log.d(TAG, "Tokennya : " + CARD_TOKEN);

                String redirectUrl = tokenDetailsResponse.getRedirectUrl();

                CustomWebView webView = new CustomWebView(FormPaymentCC.this);
                webView.setWebViewClient(new PACWebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(redirectUrl);

                AlertDialog.Builder builder = new AlertDialog.Builder(FormPaymentCC.this);

                dialog3ds = builder.create();
                dialog3ds.setTitle("3D Secure Form");
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

            @Override
            public void onFailure(TokenDetailsResponse tokenDetailsResponse, String s) {
                Log.d(TAG, tokenDetailsResponse.getStatusMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();

        totalAmount = bundle.getString("amount");
        orderNumber = bundle.getString("orderNumber");
        expDate     = bundle.getString("expDate");
        duration    = bundle.getString("duration");

        username    = bundle.getString("username");

        getDetailAccount(username);

        txtOrderNumber.setText(orderNumber);
        txtAmount.setText(totalAmount);
        txtDate.setText(expDate);

        tempAmount = convertToInt(totalAmount);

        setTimer(txtTimer, Integer.parseInt(duration));
    }

    private void showPanduan() {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allPanduan = Arrays.asList("Panduan Pembayaran CC");
        list_panduantransfer.addAll(allPanduan);
        adapter_panduantransfer.notifyDataSetChanged();
        lvPanduan.setAdapter(adapter_panduantransfer);
    }

    private void ViewPdfByFilter(final String title, final String header)
    {
        Intent intent = new Intent(FormPaymentCC.this, FormPDFViewerActivity.class);
        intent.putExtra("data", title);
        intent.putExtra("title", header);
        startActivity(intent);
    }

    private void payWithCC(final String amount, final String orderNumber, final String token)
    {
        String url = config.payment_method_creditCard;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                //Toasty.info(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("transactionStatus");
                    String id     = jsonObject.getString("orderId");
                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        if (status.equals("SUCCESS"))
                        {
                            updatePaymentStatus(id, status);
                        }
                        else
                        {
                            //Transaksi gagal
                            information("ERROR", "Pembayaran gagal, coba lagi", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                        }

                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }

                    //Toasty.info(getApplicationContext(), status, Toast.LENGTH_SHORT).show();


                    Log.d(TAG, response);
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
                hashMap.put("paymentType", "creditCard");
                hashMap.put("grossAmount", amount);
                hashMap.put("orderId", orderNumber);
                hashMap.put("token", token);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updatePaymentStatus(final String id, final String status)
    {
        String url = config.Ip_address + config.payment_method_autoMoveOrder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    String output = jsonObject.getString("responseStatus");

                    if (output.equals("Success"))
                    {
                        //success
                        information("INFORMATION", "Pembayaran telah berhasil", R.drawable.success_outline, DefaultBootstrapBrand.SUCCESS);
                    }
                    else
                    {
                        //failed
                        information("ERROR", "Transaksi gagal, coba lagi !", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderId", id);
                hashMap.put("transactionStatus", status);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
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

        AppController.getInstance().addToRequestQueue(stringRequest);
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

//        loading.dismiss();
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormPaymentCC.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ||
                        keyCode == KeyEvent.KEYCODE_APP_SWITCH)
                {
                    //donothing
                }
                return false;
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        img_status      = (ImageView) dialog.findViewById(R.id.info_status_imageview);
        txt_information = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          = (BootstrapButton) dialog.findViewById(R.id.info_status_btnOk);

        img_status.setImageResource(resource);
        txt_information.setText(info);
        txt_message.setText(message);
        btn_ok.setBootstrapBrand(defaultcolorbtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
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

    class PACWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Check if redirect URL has "/token/callback/" to ensure authorization was completed
            if (url.contains("/token/callback/")) {
//                showLoading();
                customLoading.showLoadingDialog();
                dialogCC.dismiss();
                dialog3ds.dismiss();

                Toasty.success(getApplicationContext(), "Success sent 3ds to midtrans", Toast.LENGTH_LONG).show();

                //SendChargeAsync sendChargeAsync = new SendChargeAsync();
                //sendChargeAsync.execute();

                payWithCC(tempAmount, orderNumber, CARD_TOKEN);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    private void getDetailAccount(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDETAILACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    emailaddress = object.getString("emailaddress");
                    opticname    = object.getString("opticname");

//                    Toasty.info(getApplicationContext(), "Email " + emailaddress, Toast.LENGTH_SHORT).show();

                    checkReminder(orderNumber);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("ERROR GET DETAIL", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void checkReminder(final String orderNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKREMINDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        reminderPayment(orderNumber, emailaddress, username, opticname, totalAmount, "Credit Card", expDate);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("CHECK REMINDER", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderNumber", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void reminderPayment(final String orderNumber, final String email_address, final String username,
                                 final String optic_name,
                                 final String amount, final String paymentMethod, final String expDate)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSENDREMINDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), "Reminder has been success", Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                //hideLoading();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderNumber", orderNumber);
                hashMap.put("email_address", email_address);
                hashMap.put("username", username);
                hashMap.put("optic_name", optic_name);
                hashMap.put("amount", amount);
                hashMap.put("paymentMethod", paymentMethod);
                hashMap.put("expDate", expDate);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
