package com.sofudev.trackapptrl.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_orderhistory_frame;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.CustomRecyclerOrderHistoryClick;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_orderhistory_optic;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormOrderHistoryPartaiActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    Config config = new Config();
    String SHOWALLHISTORY = config.Ip_address + config.orderpartai_show_allhistory;
    String SHOWRANGEHISTORY = config.Ip_address + config.orderpartai_show_rangehistory;
    String URLCHECKPAYORNOT = config.Ip_address + config.orderpartai_checkpayment_ornot;
    String URLSHOWPAYMENTQR = config.Ip_address + config.orderpartai_showpayment_qr;
    String URLSHOWPAYMENTVA = config.Ip_address + config.orderpartai_showpayment_va;
    String URLSHOWPAYMENTCC = config.Ip_address + config.orderpartai_showpayment_cc;
    String SHOWINFOPAYMENTLOAN  = config.Ip_address + config.orderpartai_showpayment_loan;
    String URLSHOWPAYMENTDEPO = config.Ip_address + config.orderpartai_showpayment_deposit;
    String CHECKSTATUS          = config.payment_check_status;
    String UPDATESTATUS         = config.Ip_address + config.payment_method_updateStatus;

    ImageView btnBack, btnOption, imgError;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CircleProgressBar progressBar;
    CustomLoading customLoading;

    MaterialEditText txt_startdate, txt_enddate;
    Button btn_filterdate;
    RippleView rp_filterdate;

    Adapter_orderhistory_frame adapter_orderhistory_frame;
    List<Data_orderhistory_optic> itemPartai = new ArrayList<>();

    String idParty, username, start_date, end_date, paymentInfo, level, sales;
    Integer day, month, year;
    String status;
    long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_history_partai);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

//        showLoader();
        customLoading = new CustomLoading(this);
        customLoading.showLoadingDialog();

        btnBack     = findViewById(R.id.activity_orderhistory_partai_btnBack);
        btnOption   = findViewById(R.id.activity_orderhistory_partai_btnDaterange);
        recyclerView = findViewById(R.id.activity_orderhistory_partai_recyclerview);
        progressBar = findViewById(R.id.activity_orderhistory_partai_progressBar);
        imgError    = findViewById(R.id.activity_orderhistory_partai_imgError);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter_orderhistory_frame = new Adapter_orderhistory_frame(getApplicationContext(), itemPartai, new CustomRecyclerOrderHistoryClick() {
            @Override
            public void onItemClick(View view, int pos, String id, String status, String info, String paymentType) {
                if (status.contentEquals("Pending") || status.equals("Pending") || status.contains("Pending"))
                {
                    checkPaymentMethod(id);
                }
                else if (status.contentEquals("Cancel") || status.equals("Cancel") || status.contains("Cancel"))
                {
                    Toasty.warning(getApplicationContext(), "This order has been cancelled", Toast.LENGTH_LONG).show();
                }
                else if (status.contentEquals("Failure") || status.equals("Failure") || status.contains("Failure"))
                {
                    Toasty.error(getApplicationContext(), "This order canceled by system", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormOrderDetailPartaiActivity.class);
                    intent.putExtra("key", id);

                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter_orderhistory_frame);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getInfo();
        showRangeHistory();
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            level = bundle.getString("level");
            assert level != null;

            if (level.equals("0"))
            {
                idParty = bundle.getString("user_info");
                username= bundle.getString("username");
                sales    = "";
            }
            else
            {
                sales   = bundle.getString("sales");
                idParty = "";
                username = "";
            }
        }
//        Toasty.info(getApplicationContext(), idParty, Toast.LENGTH_SHORT).show();

        getAllPartai(idParty);
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryPartaiActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String mon;
                String day;
                String mony;

                if (month == 1)
                {
                    mon = "JAN";
                }
                else if (month == 2)
                {
                    mon = "FEB";
                }
                else if (month == 3)
                {
                    mon = "MAR";
                }
                else if (month == 4)
                {
                    mon = "APR";
                }
                else if (month == 5)
                {
                    mon = "MAY";
                }
                else if (month == 6)
                {
                    mon = "JUN";
                }
                else if (month == 7)
                {
                    mon = "JUL";
                }
                else if (month == 8)
                {
                    mon = "AUG";
                }
                else if (month == 9)
                {
                    mon = "SEP";
                }
                else if (month == 10)
                {
                    mon = "OCT";
                }
                else if (month == 11)
                {
                    mon = "NOV";
                }
                else
                {
                    mon = "DEC";
                }

                if (dayOfMonth < 10)
                {
                    day = "0" + dayOfMonth;
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }

                if (month < 10)
                {
                    mony = "0" + month;
                }
                else
                {
                    mony = String.valueOf(month);
                }

                txt_startdate.setText(day + "-" + mon + "-" + year);
                start_date = year + "-" + mony + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        if (!isFinishing())
        {
            datePickerDialog.show();
        }
    }

    private void showEndDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryPartaiActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String day;
                String mon;
                String mony;

                if (month == 1)
                {
                    mon = "JAN";
                }
                else if (month == 2)
                {
                    mon = "FEB";
                }
                else if (month == 3)
                {
                    mon = "MAR";
                }
                else if (month == 4)
                {
                    mon = "APR";
                }
                else if (month == 5)
                {
                    mon = "MAY";
                }
                else if (month == 6)
                {
                    mon = "JUN";
                }
                else if (month == 7)
                {
                    mon = "JUL";
                }
                else if (month == 8)
                {
                    mon = "AUG";
                }
                else if (month == 9)
                {
                    mon = "SEP";
                }
                else if (month == 10)
                {
                    mon = "OCT";
                }
                else if (month == 11)
                {
                    mon = "NOV";
                }
                else
                {
                    mon = "DEC";
                }

                if (dayOfMonth < 10)
                {
                    day = "0" + dayOfMonth;
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }

                if (month < 10)
                {
                    mony = "0" + month;
                }
                else
                {
                    mony = String.valueOf(month);
                }

                txt_enddate.setText(day + "-" + mon + "-" + year);
                end_date = year + "-" + mony + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        if (!isFinishing())
        {
            datePickerDialog.show();
        }
    }

    private void checkPaymentMethod(final String transnumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKPAYORNOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    paymentInfo = jsonObject.getString("paymentType");
                    username    = jsonObject.getString("username");

//                    Toasty.info(getApplicationContext(), paymentInfo, Toast.LENGTH_SHORT).show();

                    if (paymentInfo.contentEquals("internetBanking") || paymentInfo.equals("internetBanking")
                            || paymentInfo.contains("internetBanking"))
                    {
                        showInfoPaymentQR(transnumber);
                    }
                    else if (paymentInfo.contentEquals("bankTransfer") || paymentInfo.equals("bankTransfer")
                            || paymentInfo.contains("bankTransfer"))
                    {
                        showInfoPaymentVA(transnumber);
                    }
                    else if (paymentInfo.contentEquals("creditCard") || paymentInfo.equals("creditCard")
                            || paymentInfo.contains("creditCard"))
                    {
                        showInfoPaymentCC(transnumber);
                    }
                    else if (paymentInfo.contentEquals("loanKS") || paymentInfo.equals("loanKS")
                            || paymentInfo.contains("loanKS"))
                    {
                        showInfoPaymentLoan(transnumber);
                    }
                    else if (paymentInfo.contentEquals("deposit") || paymentInfo.equals("deposit")
                            || paymentInfo.contains("deposit"))
                    {
                        showInfoPaymentDeposit(transnumber);
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
                hashMap.put("transnumber", transnumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showRangeHistory()
    {
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FormOrderHistoryPartaiActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_trackdate);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                UniversalFontTextView txtTitle = (UniversalFontTextView) dialog.findViewById(R.id.filter_track_txtTitle);
                txtTitle.setText("Filter Order History By Date");

                year  = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day   = calendar.get(Calendar.DATE);

                txt_startdate =  dialog.findViewById(R.id.filter_track_startdate);
                txt_enddate   =  dialog.findViewById(R.id.filter_track_enddate);
                btn_filterdate=  dialog.findViewById(R.id.filter_track_btnok);
                rp_filterdate =  dialog.findViewById(R.id.filter_track_ripple_btnok);

                txt_startdate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (SystemClock.elapsedRealtime() - lastClick < 2000)
                        {
                            return false;
                        }

                        lastClick = SystemClock.elapsedRealtime();

                        showStartDate();
                        return false;
                    }
                });

                txt_enddate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (SystemClock.elapsedRealtime() - lastClick < 2000)
                        {
                            return false;
                        }

                        lastClick = SystemClock.elapsedRealtime();

                        showEndDate();
                        return false;
                    }
                });

                rp_filterdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstdate, lastdate;

                        firstdate = txt_startdate.getText().toString();
                        lastdate  = txt_enddate.getText().toString();

                        if (firstdate.isEmpty())
                        {
                            txt_startdate.setError("Please choose start date");
                        }
                        else if (lastdate.isEmpty())
                        {
                            txt_enddate.setError("Please choose end date");
                        }
                        else
                        {
                            dialog.dismiss();

                            getAllFrameByRange(idParty, start_date, end_date);

                            Log.d("Info", "START : " + start_date + " END : " + end_date);

                        }
                    }
                });

                if (!isFinishing())
                {
                    dialog.show();
                }
            }
        });
    }

    private void getAllPartai(final String id)
    {
        itemPartai.clear();

        StringRequest request = new StringRequest(Request.Method.POST, SHOWALLHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    customLoading.dismissLoadingDialog();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
//                            loader.dismiss();
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
//                            loader.dismiss();

                            String transNumber = object.getString("transNumber");
                            String transStatus = object.getString("transStatus");

                            Log.d(FormBatchOrderActivity.class.getSimpleName(), "TransNumber = " + transNumber);
                            Log.d(FormBatchOrderActivity.class.getSimpleName(), "TransStatus = " + transStatus);

                            if (!transStatus.contains("Non Payment"))
                            {
                                checkStatusPayment(transNumber, transStatus);
                            }
                        }
                    }

                    getAllPartaiResult(id);
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
                hashMap.put("id_party", id);
                hashMap.put("salesname", sales);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllPartaiResult(final String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        itemPartai.clear();
        StringRequest request = new StringRequest(Request.Method.POST, SHOWALLHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            String transDate    = jsonObject.getString("transDate");
                            String transNumber  = jsonObject.getString("transNumber");
                            String totalPrice   = jsonObject.getString("transPrice");
                            String transStatus  = jsonObject.getString("transStatus");

                            Data_orderhistory_optic data = new Data_orderhistory_optic();
                            data.setTanggalOrder(transDate);
                            data.setNomorOrder(transNumber);
                            data.setTotalBiaya("Rp. " + CurencyFormat(totalPrice));
                            data.setStatusOrder(transStatus);

                            itemPartai.add(data);
                        }
                    }

                    customLoading.dismissLoadingDialog();
                    adapter_orderhistory_frame.notifyDataSetChanged();
//                    loader.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    imgError.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                loader.dismiss();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("salesname", sales);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllFrameByRange(final String id, final String dateFrom, final String dateUntil)
    {
        //progressBar.setVisibility(View.VISIBLE);
        itemPartai.clear();
//        showLoader();
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, SHOWRANGEHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            //progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
//                            loader.dismiss();
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            String transNumber  = jsonObject.getString("transNumber");
                            String transStatus  = jsonObject.getString("transStatus");

                            if (!transStatus.contains("Non Payment"))
                            {
                                checkStatusPayment(transNumber, transStatus);
                            }
                        }
                    }

                    getAllFrameByRangeResult(id, dateFrom, dateUntil);
                    customLoading.dismissLoadingDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("salesname", sales);
                hashMap.put("start_date", dateFrom);
                hashMap.put("until_date", dateUntil);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllFrameByRangeResult(final String id, final String dateFrom, final String dateUntil)
    {
        progressBar.setVisibility(View.VISIBLE);
        itemPartai.clear();

        StringRequest request = new StringRequest(Request.Method.POST, SHOWRANGEHISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

//                            loader.dismiss();
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            imgError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            String transDate    = jsonObject.getString("transDate");
                            String transNumber  = jsonObject.getString("transNumber");
                            String totalPrice   = jsonObject.getString("transPrice");
                            String transStatus  = jsonObject.getString("transStatus");

                            Data_orderhistory_optic data = new Data_orderhistory_optic();
                            data.setTanggalOrder(transDate);
                            data.setNomorOrder(transNumber);
                            data.setTotalBiaya("Rp. " + CurencyFormat(totalPrice));
                            data.setStatusOrder(transStatus);

                            itemPartai.add(data);
                        }
                    }

                    adapter_orderhistory_frame.notifyDataSetChanged();
                    customLoading.dismissLoadingDialog();
//                    loader.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("salesname", sales);
                hashMap.put("start_date", dateFrom);
                hashMap.put("until_date", dateUntil);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
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
//                        Toast.makeText(getApplicationContext(), "Cancel success", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Cannot cancel payment billing", Toast.LENGTH_LONG).show();
                    }

//                    finish();
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

    private void showInfoPaymentQR(final String transNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSHOWPAYMENTQR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String billing= jsonObject.getString("billingId");
                    String amount = jsonObject.getString("grandTotal");
                    String expDate= jsonObject.getString("expDate");
                    String duration = jsonObject.getString("duration");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(transNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormPaymentQR.class);
                        intent.putExtra("orderNumber", transNumber);
                        intent.putExtra("billingCode", billing);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", username);
                        startActivity(intent);
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
                hashMap.put("transnumber", transNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showInfoPaymentVA(final String transNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSHOWPAYMENTVA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String billing= jsonObject.getString("billingId");
                    String amount = jsonObject.getString("grandTotal");
                    String expDate= jsonObject.getString("expDate");
                    String duration = jsonObject.getString("duration");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(transNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormPaymentVA.class);
                        intent.putExtra("orderNumber", transNumber);
                        intent.putExtra("billingCode", billing);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", username);
                        startActivity(intent);
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
                hashMap.put("transnumber", transNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentCC(final String transNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSHOWPAYMENTCC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String expDate = jsonObject.getString("expDate");
                    String duration= jsonObject.getString("duration");
                    String amount  = jsonObject.getString("grandTotal");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(transNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormPaymentCC.class);
                        intent.putExtra("orderNumber", transNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("transnumber", transNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentLoan(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOWINFOPAYMENTLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String billing= jsonObject.getString("billingId");
                    String amount = jsonObject.getString("grandTotal");
                    String expDate= jsonObject.getString("expDate");
                    String duration = jsonObject.getString("duration");

//                    setTimer(Integer.parseInt(duration));

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(orderNumber);
                    }
                    else
                    {
                        Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormPaymentLoanActivity.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", billing);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        intent.putExtra("username", username);
                        startActivity(intent);
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
                hashMap.put("transnumber", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentDeposit(final String transNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSHOWPAYMENTDEPO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String expDate = jsonObject.getString("expDate");
                    String duration= jsonObject.getString("duration");
                    String amount  = jsonObject.getString("grandTotal");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(transNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryPartaiActivity.this, FormPaymentDeposit.class);
                        intent.putExtra("orderNumber", transNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("transnumber", transNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkStatusPayment(final String orderNumber, final String existingStatus)
    {
        StringRequest request = new StringRequest(Request.Method.POST, CHECKSTATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    status = object.getString("transactionStatus");

                    String lastStatus = "null";

                    if (existingStatus.equals("Paid"))
                    {
                        lastStatus = "SUCCESS";
                    }
                    else
                    {
                        lastStatus = existingStatus;
                    }

                    Log.d("info", "status payment " + status + " status sekarang ini : " + lastStatus);

                    if (status.equals("null"))
                    {
                        Log.d(FormOrderHistoryPartaiActivity.class.getSimpleName(), "* Jangan dieksekusi *");
                    }
                    else
                    {
                        Log.d(FormOrderHistoryPartaiActivity.class.getSimpleName(), "* Eksekusi *");

                        if (!status.equals(lastStatus))
                        {
                            updateStatus(orderNumber, status);
                        }
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateStatus(final String orderNumber, final String newStatus)
    {
        StringRequest request = new StringRequest(Request.Method.POST, UPDATESTATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//
//                    String output = object.getString("responseStatus");
//
//
//
//                    Log.d("hasil", "output update = " + output);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("orderId", orderNumber);
                hashmap.put("transactionStatus", newStatus);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
