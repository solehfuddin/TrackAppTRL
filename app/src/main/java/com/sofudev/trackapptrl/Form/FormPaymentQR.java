package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
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
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.zxing.WriterException;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class FormPaymentQR extends AppCompatActivity {

    Config config = new Config();
    String URLGETDETAILACCOUNT = config.Ip_address + config.orderlens_get_detailaccount;
    String URLSENDREMINDER     = config.Ip_address + config.orderlens_send_reminder;
    String URLCHECKREMINDER    = config.Ip_address + config.orderlens_check_reminder;

    Adapter_panduantransfer adapter_panduantransfer;
    ACProgressCustom loading;
    ImageView imgQrcode, btnBack;
    Button btnCancel;
    UniversalFontTextView txtOrderNumber, txtAmount, txtTimer, txtDate, txtKodeBilling, txtCopyKode;
    QRGEncoder qrgEncoder;
    List<String> list_panduantransfer = new ArrayList<>();
    ListView listView_panduanTransfer;

    String username, emailaddress, opticname, orderNumber, billingCode, amount, expDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_qr);
        showLoading();

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        btnBack   = (ImageView) findViewById(R.id.form_paymentqa_btnBack);
        btnCancel = (Button) findViewById(R.id.form_paymentqa_btnCancel);
        imgQrcode = (ImageView) findViewById(R.id.form_paymentqa_imgQrcode);
        txtOrderNumber = (UniversalFontTextView) findViewById(R.id.form_paymentqa_txtOrderNumber);
        txtAmount = (UniversalFontTextView) findViewById(R.id.form_paymentqa_txtTotalAmount);
        txtTimer  = (UniversalFontTextView) findViewById(R.id.form_paymentqa_txtTimer);
        txtDate   = (UniversalFontTextView) findViewById(R.id.form_paymentqa_txtDate);
        txtKodeBilling = findViewById(R.id.form_paymentqa_txtKodeQr);
        txtCopyKode = findViewById(R.id.form_paymentqa_txtCopyQr);
        listView_panduanTransfer = (ListView) findViewById(R.id.form_paymentqa_listtransfer);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //setTimer(txtTimer);
        getData();
        showBankTransfer();
        //addTimer();

        txtCopyKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kodeQr  = txtKodeBilling.getText().toString();

                ClipData clipData = ClipData.newPlainText("Kode Billing ", kodeQr);
                clipboardManager.setPrimaryClip(clipData);

                Snackbar snackbar = Snackbar.make(view, "Kode Billing telah disalin", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancelPayment(txtOrderNumber.getText().toString());
                //finish();
                final Dialog dialog = new Dialog(FormPaymentQR.this);
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


        listView_panduanTransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String atmType = list_panduantransfer.get(i);

                final Dialog dialog = new Dialog(FormPaymentQR.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                if (atmType.contentEquals("Panduan Pembayaran") || atmType.equals("Panduan Pembayaran") || atmType.contains("Panduan Pembayaran"))
                {
                    dialog.setContentView(R.layout.dialog_qr_panduan);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    lwindow.copyFrom(dialog.getWindow().getAttributes());
                    lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lwindow);
                }
                else if (atmType.contentEquals("Mobile Banking Yang Tersedia") || atmType.equals("Mobile Banking Yang Tersedia") || atmType.contains("Mobile Banking Yang Tersedia"))
                {
                    dialog.setContentView(R.layout.dialog_qr_mobile);
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
        loading = new ACProgressCustom.Builder(FormPaymentQR.this)
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

    private void showBankTransfer()
    {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allAtm = Arrays.asList("Panduan Pembayaran", "Mobile Banking Yang Tersedia");
        list_panduantransfer.addAll(allAtm);

        adapter_panduantransfer.notifyDataSetChanged();
        listView_panduanTransfer.setAdapter(adapter_panduantransfer);
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        orderNumber = bundle.getString("orderNumber");
        billingCode = bundle.getString("billingCode");
        amount      = bundle.getString("amount");
        expDate     = bundle.getString("expDate");
        String duration    = bundle.getString("duration");

        username    = bundle.getString("username");

        getDetailAccount(username);

        qrgEncoder = new QRGEncoder(billingCode, null, QRGContents.Type.TEXT, 450);

        txtOrderNumber.setText(orderNumber);
        txtDate.setText(expDate);
        txtAmount.setText("Total Bill = " + amount);
        txtKodeBilling.setText(billingCode);

        Toasty.info(this, "Amount : " + amount, Toast.LENGTH_LONG).show();

        Bitmap bitmap = null;
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        setTimer(txtTimer, Integer.parseInt(duration));
        imgQrcode.setImageBitmap(bitmap);
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
}
