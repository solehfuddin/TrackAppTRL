package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class FormPaymentLoanOtp extends AppCompatActivity {

    Config config = new Config();
    String URLPOSTLOAN = config.payment_method_loanOtp;
    String URLUPDATESALDOLOAN = config.Ip_address + config.payment_update_loanbprks;

    Button btnNext, btnPrev;
    UniversalFontTextView txtBillingId, txtTotalBilling;
    BootstrapEditText edOtp;
    ACProgressCustom loading;

    String billingId, totalBilling, nomorHp, orderNumber, username, sisaSaldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_loan_otp);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnNext = findViewById(R.id.form_paymentloan_otp_btnNext);
        btnPrev = findViewById(R.id.form_paymentloan_otp_btnPrev);

        txtBillingId = findViewById(R.id.form_paymentloan_otp_txtBillingId);
        txtTotalBilling = findViewById(R.id.form_paymentloan_otp_txtTotalBilling);
        edOtp = findViewById(R.id.form_paymentloan_otp_edOtp);

        getInfo();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            finish();
        }
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();

        billingId       = bundle.getString("billingId");
        totalBilling    = bundle.getString("totalBill");
        nomorHp         = bundle.getString("nomorHp");
        orderNumber     = bundle.getString("orderNumber");
        username        = bundle.getString("username");
        sisaSaldo       = bundle.getString("sisaSaldo");

        txtBillingId.setText(billingId);
        txtTotalBilling.setText(totalBilling);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent());
                postLoanPayment(billingId, nomorHp, edOtp.getText().toString(), totalBilling);

//                Intent intent = new Intent(FormPaymentLoanOtp.this, FormPaymentLoanSuccess.class);
//                intent.putExtra("billingId", billingId);
//                intent.putExtra("totalBill", totalBilling);
//                startActivityForResult(intent, 1102);
            }
        });
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormPaymentLoanOtp.this)
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

    private void postLoanPayment(final String billingId, final String nomorHandphone, final String otp, final String nominal)
    {
        showLoading();
        StringRequest request = new StringRequest(Request.Method.POST, URLPOSTLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response OTP : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

//                    String billingId    = jsonObject.getString("billingId");
//                    String grossAmount  = jsonObject.getString("grossAmount");
                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        updatePaymentStatus(orderNumber, description.toUpperCase());
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(),
                            "Request Time out !!",
                            Toast.LENGTH_LONG).show();
                }
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("billingId", billingId);
                hashMap.put("nomorHandphone", nomorHandphone);
                hashMap.put("otp", otp);
                hashMap.put("nominal", nominal);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updatePaymentStatus(final String id, final String status)
    {
        //String url = config.Ip_address + config.payment_method_autoMoveOrder;
        String url = config.Ip_address + config.payment_method_updateStatus;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormPaymentLoanOtp.class.getSimpleName(), "Update status : " + response);

                    String output = jsonObject.getString("responseStatus");

                    if (output.equals("Success"))
                    {
                        int sisa = Integer.parseInt(sisaSaldo) - Integer.parseInt(totalBilling);
                        updateSisaSaldoLoan(username, sisa);
                        //success
//                        information("INFORMATION", "Pembayaran telah berhasil", R.drawable.success_outline, DefaultBootstrapBrand.SUCCESS);
//                        Intent intent = new Intent(FormPaymentLoanOtp.this, FormPaymentLoanSuccess.class);
//                        intent.putExtra("billingId", billingId);
//                        intent.putExtra("totalBill", totalBilling);
//                        startActivityForResult(intent, 1102);
                    }
                    else
                    {
                        //failed
                        information("ERROR", "Transaksi gagal, coba lagi !", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
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
                hashMap.put("orderId", id);
                hashMap.put("transactionStatus", status);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateSisaSaldoLoan(final String username, final int sisasaldo)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLUPDATESALDOLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Intent intent = new Intent(FormPaymentLoanOtp.this, FormPaymentLoanSuccess.class);
                        intent.putExtra("billingId", billingId);
                        intent.putExtra("totalBill", totalBilling);
                        startActivityForResult(intent, 1102);
                    }
                    else
                    {
                        information("ERROR", "Transaksi gagal, coba lagi !", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                    }
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
                hashMap.put("username", username);
                hashMap.put("saldoLoan", String.valueOf(sisasaldo));
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormPaymentLoanOtp.this);
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

        img_status      = dialog.findViewById(R.id.info_status_imageview);
        txt_information = dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     = dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          = dialog.findViewById(R.id.info_status_btnOk);

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
}
