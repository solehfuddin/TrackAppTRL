package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class FormPaymentDeposit extends AppCompatActivity {

    Config config = new Config();
    private String URLCHECKDEPOSIT= config.Ip_address + config.depo_getsaldo;
    private String URLPAYDEPOSIT  = config.Ip_address + config.payment_method_deposit;

    CustomLoading customLoading;
    UniversalFontTextView txtOrderNumber, txtTimer, txtDate, txtAmount, txtDeposit;
    Button btnCancel, btnPay;
//    ImageView btnBack;

    String orderNumber, totalAmount, expDate, duration, tempAmount, emailaddress, opticname, username, nominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_deposit);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);

//        btnBack = findViewById(R.id.form_paymentcc_btn_back);
        txtOrderNumber = findViewById(R.id.form_paymentdepo_txtOrderNumber);
        txtTimer = findViewById(R.id.form_paymentdepo_txtTimer);
        txtDate = findViewById(R.id.form_paymentdepo_txtDate);
        txtAmount = findViewById(R.id.form_paymentdepo_txtAmountBill);
        txtDeposit = findViewById(R.id.form_paymentdepo_txtDeposit);
        btnCancel = findViewById(R.id.form_paymentdepo_btn_cancel);
        btnPay    = findViewById(R.id.form_paymentdepo_btn_pay);

        getData();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPayment(txtOrderNumber.getText().toString());
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showLoading();
                customLoading.showLoadingDialog();
                int hasil = Integer.valueOf(nominal) - Integer.valueOf(tempAmount);
//                Toasty.info(getApplicationContext(), "sisa saldo = " + String.valueOf(hasil), Toast.LENGTH_SHORT).show();
                if (hasil > 0)
                {
                    payDeposit(orderNumber, username, "-" + tempAmount, "Order pending #" + orderNumber);
                }
                else {
//                    loading.dismiss();
                        customLoading.dismissLoadingDialog();
                        information("Transaksi Gagal", "Mohon maaf sisa saldo depositmu tidak mencukupi", R.drawable.failed_outline, DefaultBootstrapBrand.WARNING);
//                    Toasty.error(getApplicationContext(), "Insufficient balance, please topup deposit", Toast.LENGTH_SHORT).show();
                }
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

//        getDetailAccount(username);

        getSaldoDepo(username);
        txtOrderNumber.setText(orderNumber);
        txtAmount.setText("IDR " + CurencyFormat(totalAmount));
        txtDate.setText(expDate);

        tempAmount = convertToInt(CurencyFormat(totalAmount));

        setTimer(txtTimer, Integer.parseInt(duration));
    }

    private String convertToInt(String amount)
    {
        String val = amount.replace(",00", "");
        val = val.replace(".", "");

        return val;
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
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

    private void getSaldoDepo(final String shipNumber){
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    nominal = jsonObject.getString("sisa_saldo");
                    String val = CurencyFormat(nominal);

                    txtDeposit.setText("IDR " + val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ship_number", shipNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void payDeposit(final String orderNumber, final String ship_number, final String saldo, final String keterangan)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLPAYDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
//                        Toasty.success(getApplicationContext(), "Transaction success", Toast.LENGTH_SHORT).show();
                        updatePaymentStatus(orderNumber, "SUCCESS");
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
                HashMap<String, String> map = new HashMap<>();
                map.put("order_number", orderNumber);
                map.put("ship_number", ship_number);
                map.put("jenis_pembayaran", "PENDING");
                map.put("saldo", saldo);
                map.put("keterangan", keterangan);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updatePaymentStatus(final String id, final String status) {
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

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormPaymentDeposit.this);
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
}
