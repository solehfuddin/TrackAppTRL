package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressCustom;

public class FormPaymentVA extends AppCompatActivity {

    ACProgressCustom loading;
    Adapter_panduantransfer adapter_panduantransfer;
    ImageView btnBack;
    Button btnCancel;
    UniversalFontTextView txtOrderNumber, txtKodeVA, txtAmount, txtCopyVa, txtTimer, txtDate;
    List<String> list_panduantransfer = new ArrayList<>();
    ListView listView_panduanTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_va);
        showLoading();

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        btnBack                  = (ImageView) findViewById(R.id.form_paymentva_btn_back);
        btnCancel                = (Button) findViewById(R.id.form_paymentva_btn_cancel);
        txtOrderNumber           = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtOrderNumber);
        txtKodeVA                = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtKodeVa);
        txtAmount                = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtAmountBill);
        txtCopyVa                = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtCopyVa);
        txtTimer                 = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtTimer);
        txtDate                  = (UniversalFontTextView) findViewById(R.id.form_paymentva_txtDate);
        listView_panduanTransfer = (ListView) findViewById(R.id.form_paymentva_listtransfer);

        //addTimer();
        //setTimer(txtTimer);
        getData();
        showBankTransfer();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancelPayment(txtOrderNumber.getText().toString());
                //finish();
                final Dialog dialog = new Dialog(FormPaymentVA.this);
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

                dialog.show();
                //dialog.getWindow().setAttributes(lwindow);
            }
        });

        txtCopyVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kodeVa  = txtKodeVA.getText().toString();

                ClipData clipData = ClipData.newPlainText("Nomor VA", kodeVa);
                clipboardManager.setPrimaryClip(clipData);

                Snackbar snackbar = Snackbar.make(view, "Nomor virtual akun telah disalin", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        listView_panduanTransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String atmType = list_panduantransfer.get(i);

                final Dialog dialog = new Dialog(FormPaymentVA.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                if (atmType.contentEquals("ATM Bersama") || atmType.equals("ATM Bersama") || atmType.contains("ATM Bersama"))
                {
                    dialog.setContentView(R.layout.dialog_atm_bersama);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    lwindow.copyFrom(dialog.getWindow().getAttributes());
                    lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lwindow);
                }
                else if (atmType.contentEquals("ATM ALTO") || atmType.equals("ATM ALTO") || atmType.contains("ATM ALTO"))
                {
                    dialog.setContentView(R.layout.dialog_atm_alto);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    lwindow.copyFrom(dialog.getWindow().getAttributes());
                    lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lwindow);
                }
            }
        });
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormPaymentVA.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        loading.show();
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

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        String orderNumber = bundle.getString("orderNumber");
        String billingCode = bundle.getString("billingCode");
        String amount      = bundle.getString("amount");
        String expDate     = bundle.getString("expDate");
        String duration    = bundle.getString("duration");

        txtOrderNumber.setText(orderNumber);
        txtKodeVA.setText(billingCode);
        txtAmount.setText(amount);
        txtDate.setText(expDate);
        setTimer(txtTimer, Integer.parseInt(duration));
    }

    private void showBankTransfer()
    {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allAtm = Arrays.asList("ATM Bersama", "ATM ALTO");
        list_panduantransfer.addAll(allAtm);

        adapter_panduantransfer.notifyDataSetChanged();
        listView_panduanTransfer.setAdapter(adapter_panduantransfer);
    }

    private void addTimer()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        String currentDate   = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24);

        txtDate.setText(calendar.getTime().toString());
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
}
