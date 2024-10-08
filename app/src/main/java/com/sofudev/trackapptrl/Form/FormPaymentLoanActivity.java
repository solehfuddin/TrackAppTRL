package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.FanpageActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class FormPaymentLoanActivity extends AppCompatActivity {

    Config config = new Config();
    String URLPHONE = config.Ip_address + config.order_history_getPhoneNumber;
    String URLUPDATEPHONE = config.Ip_address + config.order_history_updatePhoneNumber;
    String URLGETSALDO = config.payment_method_loanSaldo;
    String URLGETDETAILACCOUNT = config.Ip_address + config.orderlens_get_detailaccount;
    String URLSENDREMINDER     = config.Ip_address + config.orderlens_send_reminder;
    String URLCHECKREMINDER    = config.Ip_address + config.orderlens_check_reminder;

    ImageView btnBack;
    UniversalFontTextView txtOrderNumber, txtTimer, txtDate, txtBillingId, txtAmount;
    Button btnPay, btnCancel;
    ListView listTransfer;
    CustomLoading customLoading;

    Adapter_panduantransfer adapter_panduantransfer;
    List<String> list_panduantransfer = new ArrayList<>();
    String phone, orderNumber, username, emailaddress, opticname, billingCode, amount, expDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_loan);
//        showLoading();

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);
        customLoading.showLoadingDialog();

        btnBack         = findViewById(R.id.form_paymentloan_btn_back);
        txtOrderNumber  = findViewById(R.id.form_paymentloan_txtOrderNumber);
        txtTimer        = findViewById(R.id.form_paymentloan_txtTimer);
        txtDate         = findViewById(R.id.form_paymentloan_txtDate);
        txtBillingId    = findViewById(R.id.form_paymentloan_txtKodeBilling);
        txtAmount       = findViewById(R.id.form_paymentloan_txtAmountBill);
        btnPay          = findViewById(R.id.form_paymentloan_btn_pay);
        btnCancel       = findViewById(R.id.form_paymentloan_btn_cancel);
        listTransfer    = findViewById(R.id.form_paymentloan_listtransfer);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listTransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                intent.putExtra("data", "https://drive.google.com/open?id=1kLhwebwgFQHyZT4Zowr4e0bBxklI3eA7");
                startActivity(intent);
            }
        });

        getData();

        showBankTransfer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            finish();
        }
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        orderNumber        = bundle.getString("orderNumber");
        billingCode = bundle.getString("billingCode");
        amount      = bundle.getString("amount");
        expDate     = bundle.getString("expDate");
        String duration    = bundle.getString("duration");
        username    = bundle.getString("username");

        txtOrderNumber.setText(orderNumber);
        txtDate.setText(expDate);
        txtAmount.setText(amount);
        txtBillingId.setText(billingCode);

//        Toasty.info(getApplicationContext(), username, Toast.LENGTH_SHORT).show();

        getPhoneNumber(username);
        getDetailAccount(username);

        setTimer(txtTimer, Integer.parseInt(duration));
    }

    private void getPhoneNumber(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLPHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    phone = jsonObject.getString("phone");

//                    Toasty.info(getApplicationContext(), "Phone : " + phone, Toast.LENGTH_LONG).show();

                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!phone.equals("null"))
                            {
                                getSaldoLoan(txtBillingId.getText().toString(), phone);
                            }
                            else
                            {
                                //Toasty.warning(getApplicationContext(), "Harap masukkan nomor hp loan", Toast.LENGTH_LONG).show();

                                updatePhone();
                            }
//                            Intent intent = new Intent(FormPaymentLoanActivity.this, FormPaymentLoanSaldo.class);
//                            intent.putExtra("billingId", txtBillingId.getText().toString());
//                            intent.putExtra("sisaSaldo", 1000000);
//                            intent.putExtra("totalBill", 500000);
//                            intent.putExtra("nomorHp", phone);
//                            intent.putExtra("orderNumber", orderNumber);
//                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException e)
                {
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
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showBankTransfer()
    {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allAtm = Arrays.asList("Panduan Pembayaran Loan");
        list_panduantransfer.addAll(allAtm);

        adapter_panduantransfer.notifyDataSetChanged();
        listTransfer.setAdapter(adapter_panduantransfer);
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
        customLoading.dismissLoadingDialog();
    }

    private void cancelPayment(String id) {
        Config config = new Config();
        String url = config.Ip_address + config.payment_method_cancelBilling;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url + id, new Response.Listener<String>() {
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

    private void getSaldoLoan(final String billingId, final String nomorHp)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSALDO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Loan step1 : " + response);

                    String status    = jsonObject.getString("responseCode");
                    String description = jsonObject.getString("responseDescription");
                    String sisaSaldo = jsonObject.getString("Balance");
                    String totalBill = jsonObject.getString("grossAmount");

//                    Toasty.info(getApplicationContext(), sisaSaldo, Toast.LENGTH_SHORT).show();

                    if (status.equals("00") || sisaSaldo.contains("00") || sisaSaldo.contentEquals("00"))
                    {
                        Intent intent = new Intent(FormPaymentLoanActivity.this, FormPaymentLoanSaldo.class);
                        intent.putExtra("billingId", billingId);
                        intent.putExtra("sisaSaldo", sisaSaldo);
                        intent.putExtra("totalBill", totalBill);
                        intent.putExtra("nomorHp", phone);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("username", username);
//                        startActivity(intent);
                        startActivityForResult(intent, 1100);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + status + ")", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                loading.dismiss();
                customLoading.dismissLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                loading.dismiss();
                customLoading.dismissLoadingDialog();

                Log.d(FormLensSummaryActivity.class.getSimpleName(), "Loan step1 : " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("billingId", billingId);
                hashMap.put("nomorHandphone", nomorHp);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updatePhone()
    {
        final Dialog dialog = new Dialog(FormPaymentLoanActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_phone);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        final MaterialEditText editPhone= dialog.findViewById(R.id.dialog_updatephone_edtPhone);
        UniversalFontTextView txtInfo   = dialog.findViewById(R.id.dialog_updatephone_txtInfo);
        RippleView btnUpdate            = dialog.findViewById(R.id.dialog_updatephone_btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = editPhone.getText().toString();
                final String prefix = phone.substring(0, 2);

                if (phone.length() > 0)
                {
                    if (prefix.contains("+"))
                    {
                        int len = phone.length();
                        String end = phone.substring(3, len);
                        String nomorHp = "62" + end;

                        //Toasty.info(getApplicationContext(), "Nomor hp = " + nomorHp, Toast.LENGTH_SHORT).show();
                        actionUpdate(username, nomorHp);
                        dialog.dismiss();

                        finish();
                    }
                    else if (prefix.equals("08"))
                    {
                        int len = phone.length();
                        String end = phone.substring(1, len);
                        String nomorHp = "62" + end;

                        //Toasty.info(getApplicationContext(), "Nomor hp = " + nomorHp, Toast.LENGTH_SHORT).show();
                        actionUpdate(username, nomorHp);
                        dialog.dismiss();

                        finish();
                    }
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Mohon masukkan nomor hp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
    }

    private void actionUpdate(final String key, final String phone)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLUPDATEPHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("Success"))
                    {
                        Toasty.success(getApplicationContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
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
                hashMap.put("username", key);
                hashMap.put("phone", phone);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
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
                        reminderPayment(orderNumber, emailaddress, username, opticname, billingCode, amount, "QR Code", expDate);
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
                                 final String optic_name, final String billingCode,
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
                hashMap.put("billingCode", billingCode);
                hashMap.put("amount", amount);
                hashMap.put("paymentMethod", paymentMethod);
                hashMap.put("expDate", expDate);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
