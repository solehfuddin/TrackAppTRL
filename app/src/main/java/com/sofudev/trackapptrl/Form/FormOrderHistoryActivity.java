package com.sofudev.trackapptrl.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontCheckBox;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_orderhistory_optic;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.CustomRecyclerOrderHistoryClick;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_orderhistory_optic;
import com.sofudev.trackapptrl.FanpageActivity;
import com.sofudev.trackapptrl.R;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormOrderHistoryActivity extends AppCompatActivity implements CustomRecyclerOrderHistoryClick {
    Config config = new Config();
    Calendar calendar = Calendar.getInstance();

    String GETBYDATE           = config.Ip_address + config.order_history_getOrderToday;
    String SEARCHBYORDERNUMBER = config.Ip_address + config.order_history_searchByOrderNumber;
    String SEARCHBYRANGEDATE   = config.Ip_address + config.order_history_searchByDateRange;
    String SHOWINFOPAYMENTQR    = config.Ip_address + config.order_history_showInfoPaymentQR;
    String SHOWINFOPAYMENTVA    = config.Ip_address + config.order_history_showInfoPaymentVA;
    String SHOWINFOPAYMENTCC    = config.Ip_address + config.order_history_showInfoPaymentCC;
    String SHOWINFOPAYMENTLOAN  = config.Ip_address + config.order_history_showInfoPaymentLoan;
    String URLSHOWPAYMENTDEPO   = config.Ip_address + config.order_history_showInfoPaymentDepo;
    String CHECKSTATUS          = config.payment_check_status;
    String UPDATESTATUS         = config.Ip_address + config.payment_method_updateStatus;
    String URLCOMPLETEINFO      = config.Ip_address + config.orderlens_complete_info;

    //Area Integrasi Web
    String URL_CONFIRMNONPAY   = config.Ip_address + config.orderlens_confirm_entrynonpay;
    String HISTORYBYDATE        = config.Ip_address + config.lenshistory_getOrderTodayWeb;
    String HISTORYBYPATIENT     = config.Ip_address + config.lenshistory_getOrderByPatient;
    String HISTORYBYRANGEDATE   = config.Ip_address + config.lenshistory_getOrderByRange;
    String HISTORYPAYMENTORNOT  = config.Ip_address + config.lenshistory_checkpayment_ornot;

    ImageView img_back, img_daterange, img_error;
    Button btn_search, btn_filterdate;
    RippleView rp_filterdate;
    EditText txt_search;
    MaterialEditText txt_startdate, txt_enddate;
    CircleProgressBar loading;
    CustomLoading customLoading;
    LovelyStandardDialog dialogComplete;

    RecyclerView recycler_data;
    RecyclerView.LayoutManager recyclerViewManager;
    Adapter_orderhistory_optic adapteOrderHistory;
    List<Data_orderhistory_optic> itemOrderHistory = new ArrayList<>();
    String username, start_date, end_date, status, idparty, paymentInfo, level, sales, orderNumber;
    Integer day, month, year;
    long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_history);
//        showLoader();

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);
        customLoading.showLoadingDialog();

        img_back        = (ImageView) findViewById(R.id.activity_orderhistory_btn_back);
        img_daterange   = (ImageView) findViewById(R.id.activity_orderhistory_btn_daterange);
        img_error       = (ImageView) findViewById(R.id.activity_orderhistory_imgError);
        loading         = (CircleProgressBar) findViewById(R.id.activity_orderhistory_progressBar);
        btn_search      = (Button) findViewById(R.id.activity_orderhistory_btn_search);
        txt_search      = (EditText) findViewById(R.id.activity_orderhistory_txt_search);
        recycler_data   = (RecyclerView) findViewById(R.id.activity_orderhistory_recyclerview);
        recycler_data.setHasFixedSize(true);

        recyclerViewManager = new LinearLayoutManager(this);
        recycler_data.setLayoutManager(recyclerViewManager);

        adapteOrderHistory = new Adapter_orderhistory_optic(getApplicationContext(), itemOrderHistory, this);
        recycler_data.setAdapter(adapteOrderHistory);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUserInfo();
//        showDataByDate();
        historyLensByDate();
        searchHistoryByName();
        searchByRangeDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        historyLensByDate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        showDataByDate();
//        historyLensByDate();
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.#";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void searchByRangeDate()
    {
        img_daterange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FormOrderHistoryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_trackdate);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                UniversalFontTextView txtTitle = (UniversalFontTextView) dialog.findViewById(R.id.filter_track_txtTitle);
                txtTitle.setText("Filter Order History By Date");

                year  = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day   = calendar.get(Calendar.DATE);

                txt_startdate = (MaterialEditText) dialog.findViewById(R.id.filter_track_startdate);
                txt_enddate   = (MaterialEditText) dialog.findViewById(R.id.filter_track_enddate);
                btn_filterdate= (Button) dialog.findViewById(R.id.filter_track_btnok);
                rp_filterdate = (RippleView) dialog.findViewById(R.id.filter_track_ripple_btnok);

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
//                            showLoader();
                            customLoading.showLoadingDialog();
                            //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
//                            showDataByDateRange(start_date, end_date);
//                            historyLensByRangeDate(start_date, end_date);

//                            Toasty.info(getApplicationContext(), "Start Date : " + start_date + " End Date: " + end_date,
//                                    Toast.LENGTH_SHORT).show();

                            Log.d("Error History Range", "start date : " + start_date + " end date : " + end_date);

                            historyLensByRangeDate(start_date, end_date);
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

    private void searchHistoryByName()
    {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showLoader();
                customLoading.showLoadingDialog();
//                showDataByOrderNumber(txt_search.getText().toString());
                historyLensByPatient(txt_search.getText().toString());
            }
        });

        txt_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    String pasien = txt_search.getText().toString();
                    if (pasien.isEmpty())
                    {
//                        showLoader();
                        customLoading.showLoadingDialog();
//                        showDataByDate();
                        historyLensByDate();
                    }
                    else
                    {
//                        showLoader();
                        customLoading.showLoadingDialog();
//                        showDataByOrderNumber(txt_search.getText().toString());
                        historyLensByPatient(txt_search.getText().toString());
                    }
                }

                return false;
            }
        });
    }

    private void getUserInfo()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            level    = bundle.getString("level");
            orderNumber = bundle.getString("ordernumber");

            assert level != null;
            if (level.equals("0") || level.equals("3"))
            {
                username = bundle.getString("user_info");
                idparty  = bundle.getString("idparty");
                sales    = "";
            }
            else
            {
                sales   = bundle.getString("sales");
                idparty = "";
                username = "";
            }

            if (orderNumber != null) {
                customLoading.showLoadingDialog();
                txt_search.setText(orderNumber);
//                showDataByOrderNumber(txt_search.getText().toString());
                historyLensByPatient(txt_search.getText().toString());
            }
        }
    }

//    private void showStartDate()
//    {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month = (month + 1);
//                String mon;
//                String day;
//
//                if (month == 1)
//                {
//                    mon = "JAN";
//                }
//                else if (month == 2)
//                {
//                    mon = "FEB";
//                }
//                else if (month == 3)
//                {
//                    mon = "MAR";
//                }
//                else if (month == 4)
//                {
//                    mon = "APR";
//                }
//                else if (month == 5)
//                {
//                    mon = "MAY";
//                }
//                else if (month == 6)
//                {
//                    mon = "JUN";
//                }
//                else if (month == 7)
//                {
//                    mon = "JUL";
//                }
//                else if (month == 8)
//                {
//                    mon = "AUG";
//                }
//                else if (month == 9)
//                {
//                    mon = "SEP";
//                }
//                else if (month == 10)
//                {
//                    mon = "OCT";
//                }
//                else if (month == 11)
//                {
//                    mon = "NOV";
//                }
//                else
//                {
//                    mon = "DEC";
//                }
//
//                if (dayOfMonth < 10)
//                {
//                    day = "0" + dayOfMonth;
//                }
//                else
//                {
//                    day = String.valueOf(dayOfMonth);
//                }
//
//                txt_startdate.setText(day + "-" + mon + "-" + year);
//                start_date = year + "-" + month + "-" + day;
//            }
//        }, year, month, day);
//
//        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        datePickerDialog.show();
//    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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
//                txt_startdate.setText(start_date);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        if (!isFinishing())
        {
            datePickerDialog.show();
        }
    }

//    private void showEndDate()
//    {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month = (month + 1);
//                String day;
//                String mon;
//
//                if (month == 1)
//                {
//                    mon = "JAN";
//                }
//                else if (month == 2)
//                {
//                    mon = "FEB";
//                }
//                else if (month == 3)
//                {
//                    mon = "MAR";
//                }
//                else if (month == 4)
//                {
//                    mon = "APR";
//                }
//                else if (month == 5)
//                {
//                    mon = "MAY";
//                }
//                else if (month == 6)
//                {
//                    mon = "JUN";
//                }
//                else if (month == 7)
//                {
//                    mon = "JUL";
//                }
//                else if (month == 8)
//                {
//                    mon = "AUG";
//                }
//                else if (month == 9)
//                {
//                    mon = "SEP";
//                }
//                else if (month == 10)
//                {
//                    mon = "OCT";
//                }
//                else if (month == 11)
//                {
//                    mon = "NOV";
//                }
//                else
//                {
//                    mon = "DEC";
//                }
//
//                if (dayOfMonth < 10)
//                {
//                    day = "0" + dayOfMonth;
//                }
//                else
//                {
//                    day = String.valueOf(dayOfMonth);
//                }
//
//                txt_enddate.setText(day + "-" + mon + "-" + year);
//                end_date = year + "-" + month + "-" + day;
//            }
//        }, year, month, day);
//
//        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        datePickerDialog.show();
//    }

    private void showEndDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormOrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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
//                txt_enddate.setText(end_date);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        if (!isFinishing())
        {
            datePickerDialog.show();
        }
    }

    private void historyLensByDate()
    {
        loading.setVisibility(View.VISIBLE);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        itemOrderHistory.clear();

        final String today_date = sdf1.format(calendar.getTime());

        StringRequest request = new StringRequest(Request.Method.POST, HISTORYBYDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
//                            loader.dismiss();
                            customLoading.dismissLoadingDialog();
                        }
                        else
                        {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String noOrder  = jsonObject.getString("nomor_order");
                            String status     = jsonObject.getString("status");

//                            checkStatusPayment(noOrder, status);
                            customLoading.dismissLoadingDialog();
                        }
                    }

                    historyLensByDateResult();
                    customLoading.dismissLoadingDialog();
                    //loader.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();

                    img_error.setVisibility(View.VISIBLE);
                    recycler_data.setVisibility(View.GONE);
//                    loader.dismiss();
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
                hashMap.put("salesname", sales);
                hashMap.put("id_party", idparty);
                hashMap.put("today_date", today_date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void historyLensByDateResult()
    {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        itemOrderHistory.clear();

        final String today_date = sdf1.format(calendar.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HISTORYBYDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
                        }
                        else
                        {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String tglOrder = jsonObject.getString("tanggal_order");
                            String noOrder  = jsonObject.getString("nomor_order");
                            String pasien   = jsonObject.getString("nama_pasien");
                            String totalBiaya = jsonObject.getString("total_biaya");
                            String status     = jsonObject.getString("status");
//                            String paymentType= jsonObject.getString("payment_type");
                            String icon     = jsonObject.getString("icon");

                            Data_orderhistory_optic dataOrder = new Data_orderhistory_optic();
                            dataOrder.setTanggalOrder(tglOrder);
                            dataOrder.setNomorOrder(noOrder);
                            dataOrder.setNamaPasien(pasien);
                            dataOrder.setTotalBiaya("Rp. " + CurencyFormat(totalBiaya));
                            dataOrder.setStatusOrder(status);
                            dataOrder.setIconOrder(icon);
                            dataOrder.setUserLevel(Integer.parseInt(level));

//                            dataOrder.setPaymentType(paymentType);

                            itemOrderHistory.add(dataOrder);
                        }
                    }

                    adapteOrderHistory.notifyDataSetChanged();
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                } catch (JSONException e) {
                    e.printStackTrace();

                    img_error.setVisibility(View.VISIBLE);
                    recycler_data.setVisibility(View.GONE);
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                error.printStackTrace();
//                loader.dismiss();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("salesname", sales);
                hashMap.put("id_party", idparty);
                hashMap.put("today_date", today_date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void historyLensByPatient(final String key)
    {
        loading.setVisibility(View.VISIBLE);
        itemOrderHistory.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HISTORYBYPATIENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
//                            loader.dismiss();
                            customLoading.dismissLoadingDialog();
                        }
                        else
                        {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String noOrder  = jsonObject.getString("nomor_order");
                            String status   = jsonObject.getString("status");

//                            checkStatusPayment(noOrder, status);
                            customLoading.dismissLoadingDialog();
                        }
                    }

                    historyLensByPatientResult(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                error.printStackTrace();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idparty);
                hashMap.put("salesname", sales);
                hashMap.put("patient_name", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void historyLensByPatientResult(final String key)
    {
        loading.setVisibility(View.VISIBLE);
        itemOrderHistory.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HISTORYBYPATIENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
                        }
                        else
                        {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String tglOrder = jsonObject.getString("tanggal_order");
                            String noOrder  = jsonObject.getString("nomor_order");
                            String pasien   = jsonObject.getString("nama_pasien");
                            String totalBiaya = jsonObject.getString("total_biaya");
                            String status   = jsonObject.getString("status");
//                            String paymentType= jsonObject.getString("payment_type");
                            String icon     = jsonObject.getString("icon");

                            Data_orderhistory_optic dataOrder = new Data_orderhistory_optic();
                            dataOrder.setTanggalOrder(tglOrder);
                            dataOrder.setNomorOrder(noOrder);
                            dataOrder.setNamaPasien(pasien);
                            dataOrder.setTotalBiaya("Rp. " + CurencyFormat(totalBiaya));
                            dataOrder.setStatusOrder(status);
                            dataOrder.setIconOrder(icon);
                            dataOrder.setUserLevel(Integer.parseInt(level));
//                            dataOrder.setPaymentType(paymentType);

                            itemOrderHistory.add(dataOrder);
                        }
                    }

//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                    adapteOrderHistory.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                    img_error.setVisibility(View.VISIBLE);
                    recycler_data.setVisibility(View.GONE);
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                error.printStackTrace();
//                loader.dismiss();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idparty);
                hashMap.put("salesname", sales);
                hashMap.put("patient_name", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void historyLensByRangeDate(final String startDate, final String endDate)
    {
        loading.setVisibility(View.VISIBLE);
        itemOrderHistory.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HISTORYBYRANGEDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
//                            loader.dismiss();
                            customLoading.dismissLoadingDialog();
                        }
                        else {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String noOrder = jsonObject.getString("nomor_order");
                            String status = jsonObject.getString("status");

//                            checkStatusPayment(noOrder, status);
                        }
                    }

                    historyLensByRangeDateResult(startDate, endDate);
                } catch (JSONException e) {
                    e.printStackTrace();

                    img_error.setVisibility(View.VISIBLE);
                    recycler_data.setVisibility(View.GONE);
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loader.dismiss();
                customLoading.dismissLoadingDialog();
                loading.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idparty);
                hashMap.put("salesname", sales);
                hashMap.put("date_from", startDate);
                hashMap.put("date_to", endDate);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void historyLensByRangeDateResult(final String startDate, final String endDate)
    {
        loading.setVisibility(View.VISIBLE);
        itemOrderHistory.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HISTORYBYRANGEDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            img_error.setVisibility(View.VISIBLE);
                            recycler_data.setVisibility(View.GONE);
                        }
                        else {
                            img_error.setVisibility(View.GONE);
                            recycler_data.setVisibility(View.VISIBLE);

                            String tglOrder = jsonObject.getString("tanggal_order");
                            String noOrder = jsonObject.getString("nomor_order");
                            String pasien = jsonObject.getString("nama_pasien");
                            String totalBiaya = jsonObject.getString("total_biaya");
                            String status = jsonObject.getString("status");
//                            String paymentType= jsonObject.getString("payment_type");
                            String icon = jsonObject.getString("icon");

                            Data_orderhistory_optic dataOrder = new Data_orderhistory_optic();
                            dataOrder.setTanggalOrder(tglOrder);
                            dataOrder.setNomorOrder(noOrder);
                            dataOrder.setNamaPasien(pasien);
                            dataOrder.setTotalBiaya("Rp. " + CurencyFormat(totalBiaya));
                            dataOrder.setStatusOrder(status);
                            dataOrder.setIconOrder(icon);
                            dataOrder.setUserLevel(Integer.parseInt(level));
//                            dataOrder.setPaymentType(paymentType);

                            itemOrderHistory.add(dataOrder);
                        }
                    }

                    adapteOrderHistory.notifyDataSetChanged();
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                } catch (JSONException e) {
                    e.printStackTrace();

                    img_error.setVisibility(View.VISIBLE);
                    recycler_data.setVisibility(View.GONE);
//                    loader.dismiss();
                    customLoading.dismissLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loader.dismiss();
                customLoading.dismissLoadingDialog();
                loading.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idparty);
                hashMap.put("salesname", sales);
                hashMap.put("date_from", startDate);
                hashMap.put("date_to", endDate);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentQR(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOWINFOPAYMENTQR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String billing= jsonObject.getString("billing_id");
                    String amount = jsonObject.getString("price_total");
                    String expDate= jsonObject.getString("exp_date");
                    String duration = jsonObject.getString("duration");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(orderNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryActivity.this, FormPaymentQR.class);
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentVA(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOWINFOPAYMENTVA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String billing= jsonObject.getString("billing_id");
                    String amount = jsonObject.getString("price_total");
                    String expDate= jsonObject.getString("exp_date");
                    String duration = jsonObject.getString("duration");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(orderNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryActivity.this, FormPaymentVA.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", billing);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showInfoPaymentCC(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOWINFOPAYMENTCC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String expDate = jsonObject.getString("exp_date");
                    String duration= jsonObject.getString("duration");
                    String amount  = jsonObject.getString("price_total");

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(orderNumber);
                    }
                    else {
                        Intent intent = new Intent(FormOrderHistoryActivity.this, FormPaymentCC.class);
                        intent.putExtra("orderNumber", orderNumber);
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
                hashMap.put("order_number", orderNumber);
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

                    String billing= jsonObject.getString("billing_id");
                    String amount = jsonObject.getString("price_total");
                    String expDate= jsonObject.getString("exp_date");
                    String duration = jsonObject.getString("duration");

//                    setTimer(Integer.parseInt(duration));

                    if (Integer.parseInt(duration) < 0)
                    {
                        Toasty.error(getApplicationContext(), "Payment session has expired", Toast.LENGTH_SHORT).show();

                        cancelPayment(orderNumber);
                    }
                    else
                    {
                        Intent intent = new Intent(FormOrderHistoryActivity.this, FormPaymentLoanActivity.class);
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
                hashMap.put("order_number", orderNumber);
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
                        Intent intent = new Intent(FormOrderHistoryActivity.this, FormPaymentDeposit.class);
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

    @Override
    public void onItemClick(View view, int pos, String id, String status, String info, String paymentType) {
        if (status.contentEquals("Pending") || status.equals("Pending") || status.contains("Pending") ||
            status.contentEquals("PENDING") || status.equals("PENDING") || status.contains("PENDING"))
        {
            checkPaymentMethod(id);
        }
        else if (status.contentEquals("Cancel") || status.equals("Cancel") || status.contains("Cancel"))
        {
            Toasty.warning(getApplicationContext(), "This order has been cancelled", Toast.LENGTH_LONG).show();
        }
        else if (status.contentEquals("FAILURE") || status.equals("FAILURE") || status.contains("FAILURE"))
        {
            Toasty.error(getApplicationContext(), "This order canceled by system", Toast.LENGTH_LONG).show();
        }
        else if (status.contentEquals("Waiting Order to Complete") || status.equals("Waiting Order to Complete") || status.contains("Waiting Order to Complete"))
        {
            Log.d("Incomplete", "Complete first");
            String msg = id + " - " + info;
            dialogKonfirmasiNonPay(id, msg);
        }
        else if (status.contentEquals("Waiting Payment Method") || status.equals("Waiting Payment Method") || status.contains("Waiting Payment Method"))
        {
            Log.d("Incomplete", "Wait payment method");
            completeInfo(id);
        }
        else
        {
            Intent intent = new Intent(FormOrderHistoryActivity.this, FormOrderDetailActivity.class);
            intent.putExtra("key", id);
            intent.putExtra("level", level);
            startActivity(intent);
        }
    }

    private void dialogKonfirmasiNonPay(final String id, String pesan)
    {
        dialogComplete = new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL);
        dialogComplete.setTopColorRes(R.color.colorPrimary);
        dialogComplete.setTopTitle("Konfirmasi data");
        dialogComplete.setMessage("Apakah anda setuju dengan order " + pesan + " ?");
        dialogComplete.setTopTitleColor(Color.WHITE);
        dialogComplete.setPositiveButtonColor(Color.parseColor("#0275d8"));
        dialogComplete.setPositiveButton("Konfirmasi", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aksi", "Konfirmasi");
                dialogComplete.dismiss();
                confirmEntryNonPayment(id);
            }
        });
        dialogComplete.setNegativeButtonColor(Color.parseColor("#f90606"));
        dialogComplete.setNegativeButton("Keluar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aksi", "Keluar");
                dialogComplete.dismiss();
            }
        });

        dialogComplete.show();
    }

    private void showAccessDenied(String msg)
    {
        UniversalFontTextView txt_caption;
        BootstrapButton btn_close;

        final Dialog dialog = new Dialog(FormOrderHistoryActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_access_denied);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        btn_close  = dialog.findViewById(R.id.dialog_accessdenied_btnsave);
        txt_caption = dialog.findViewById(R.id.dialog_accessdenied_txtcaption);

        txt_caption.setText(msg);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void confirmEntryNonPayment(final String orderNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CONFIRMNONPAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("Informasi", "success");
                        customLoading.dismissLoadingDialog();

                        historyLensByDate();
                    }
                    else
                    {
                        Log.d("Informasi", "error");
                        customLoading.dismissLoadingDialog();

                        showAccessDenied("Gagal menyimpan data, silahkan coba kembali");
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
                    Log.d("ERROR ENTRY STATUS", error.getMessage());
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

    private void completeInfo(final String orderNumber) {
        StringRequest request = new StringRequest(Request.Method.POST, URLCOMPLETEINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Log.d("Info : ", object.toString());

                        if (object.names().get(0).equals("error"))
                        {
                            Log.d("Informasi", "error");

                            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                            customLoading.dismissLoadingDialog();
                        }
                        else
                        {
                            Log.d("Informasi", "success");
                            customLoading.dismissLoadingDialog();

                            Intent intent = new Intent(FormOrderHistoryActivity.this, FormLensSummaryActivity.class);
                            intent.putExtra("id_party", object.getString("id_party"));
                            intent.putExtra("city_info", object.getString("city_info"));
                            intent.putExtra("province", object.getString("province"));
                            intent.putExtra("price_lens", Integer.parseInt(object.getString("price_lens")));
                            intent.putExtra("description_lens", object.getString("description_lens"));
                            intent.putExtra("discount_lens", Double.parseDouble(object.getString("discount_lens")));
                            intent.putExtra("facet_lens", Integer.parseInt(object.getString("facet_lens")));
                            intent.putExtra("tinting_lens", Integer.parseInt(object.getString("tinting_lens")));
                            intent.putExtra("item_weight", Integer.parseInt(object.getString("item_weight")));
                            intent.putExtra("flag_pasang", object.getString("flag_pasang"));
                            intent.putExtra("username_info", object.getString("username_info"));
                            intent.putExtra("optic_flag", object.getString("optic_flag"));
                            intent.putExtra("flag_shipping", object.getString("flag_shipping"));
                            intent.putExtra("order_number", object.getString("order_number"));
                            intent.putExtra("patient_name", object.getString("patient_name"));
                            intent.putExtra("phone_number", object.getString("phone_number"));
                            intent.putExtra("note", object.getString("note"));
                            intent.putExtra("prodDivType", object.getString("prodDivType"));
                            intent.putExtra("lenstype", object.getString("lenstype"));
                            intent.putExtra("lensdesc", object.getString("lensdesc"));
                            intent.putExtra("categoryLens", object.getString("categoryLens"));
                            intent.putExtra("isSp", object.getString("isSp"));
                            intent.putExtra("sales", object.getString("sales"));
                            intent.putExtra("level", object.getString("level"));
                            intent.putExtra("opticname", object.getString("opticname"));

                            /* Input SP Header */
                            intent.putExtra("headerNoSp", object.getString("headerNoSp"));
                            intent.putExtra("headerTipeSp", object.getString("headerTipeSp"));
                            intent.putExtra("headerSales", object.getString("headerSales"));
                            intent.putExtra("headerShipNumber", object.getString("headerShipNumber"));
                            intent.putExtra("headerCustName", object.getString("headerCustName"));
                            intent.putExtra("headerAddress", object.getString("headerAddress"));
                            intent.putExtra("headerCity", object.getString("headerCity"));
                            intent.putExtra("headerOrderVia", object.getString("headerOrderVia"));
                            intent.putExtra("headerDp", Integer.parseInt(object.getString("headerDp")));
                            intent.putExtra("headerDisc", object.getString("headerDisc"));
                            intent.putExtra("headerCondition", object.getString("headerCondition"));
                            intent.putExtra("headerInstallment", object.getString("headerInstallment"));
                            intent.putExtra("headerStartInstallment", object.getString("headerStartInstallment"));
                            intent.putExtra("headerShippingAddress", object.getString("headerShippingAddress"));
                            intent.putExtra("headerStatus", object.getString("headerStatus"));
                            intent.putExtra("headerImage", object.getString("headerImage"));
                            intent.putExtra("headerSignedPath", object.getString("headerSignedPath"));

                            intent.putExtra("sphR", object.getString("sphR"));
                            intent.putExtra("sphL", object.getString("sphL"));
                            intent.putExtra("cylR", object.getString("cylR"));
                            intent.putExtra("cylL", object.getString("cylL"));
                            intent.putExtra("axsR", object.getString("axsR"));
                            intent.putExtra("axsL", object.getString("axsL"));
                            intent.putExtra("addR", object.getString("addR"));
                            intent.putExtra("addL", object.getString("addL"));
                            intent.putExtra("coatCode", object.getString("coatCode"));
                            intent.putExtra("coatDesc", object.getString("coatDesc"));
                            intent.putExtra("tintCode", object.getString("tintCode"));
                            intent.putExtra("tintDesc", object.getString("tintDesc"));
                            intent.putExtra("corridor", object.getString("corridor"));
                            intent.putExtra("mpdR", object.getString("mpdR"));
                            intent.putExtra("mpdL", object.getString("mpdL"));
                            intent.putExtra("pv", object.getString("pv"));
                            intent.putExtra("wrap", object.getString("wrap"));
                            intent.putExtra("panto", object.getString("panto"));
                            intent.putExtra("vd", object.getString("vd"));
                            intent.putExtra("facetInfo", object.getString("facetInfo"));
                            intent.putExtra("frameModel", object.getString("frameModel"));
                            intent.putExtra("dbl", object.getString("dbl"));
                            intent.putExtra("hor", object.getString("hor"));
                            intent.putExtra("ver", object.getString("ver"));
                            intent.putExtra("frameCode", object.getString("frameCode"));

                            intent.putExtra("margin_lens", object.getString("margin_lens"));
                            intent.putExtra("extramargin_lens", object.getString("extramargin_lens"));

                            //Area Lens R
                            intent.putExtra("itemid_R", object.getString("itemid_R"));
                            intent.putExtra("itemcode_R", object.getString("itemcode_R"));
                            intent.putExtra("description_R", object.getString("description_R"));
                            intent.putExtra("power_R", object.getString("power_R"));
                            intent.putExtra("qty_R", object.getString("qty_R"));
                            intent.putExtra("itemprice_R", Integer.parseInt(object.getString("itemprice_R")));
                            intent.putExtra("itemtotal_R", Integer.parseInt(object.getString("itemtotal_R")));

                            //Area Lens L
                            intent.putExtra("itemid_L", object.getString("itemid_L"));
                            intent.putExtra("itemcode_L", object.getString("itemcode_L"));
                            intent.putExtra("description_L", object.getString("description_L"));
                            intent.putExtra("power_L", object.getString("power_L"));
                            intent.putExtra("qty_L", object.getString("qty_L"));
                            intent.putExtra("itemprice_L", Integer.parseInt(object.getString("itemprice_L")));
                            intent.putExtra("itemtotal_L", Integer.parseInt(object.getString("itemtotal_L")));

                            //Area Diskon
                            intent.putExtra("description_diskon", object.getString("description_diskon"));
                            intent.putExtra("discount_r", Double.parseDouble(object.getString("discount_r")));
                            intent.putExtra("discount_l", Double.parseDouble(object.getString("discount_l")));
                            intent.putExtra("extra_margin_discount", object.getString("extra_margin_discount"));
                            intent.putExtra("prod_attr_valR", object.getString("prod_attr_valR"));
                            intent.putExtra("prod_attr_valL", object.getString("prod_attr_valL"));

                            //Area Facet
                            intent.putExtra("itemcode_facet", object.getString("itemcode_facet"));
                            intent.putExtra("description_facet", object.getString("description_facet"));
                            intent.putExtra("qty_facet", object.getString("qty_facet"));
                            intent.putExtra("price_facet", Integer.parseInt(object.getString("price_facet")));
                            intent.putExtra("total_facet", Integer.parseInt(object.getString("total_facet")));
                            intent.putExtra("margin_facet", object.getString("margin_facet"));
                            intent.putExtra("extra_margin_facet", object.getString("extra_margin_facet"));

                            //Area Tinting
                            intent.putExtra("itemcode_tinting", object.getString("itemcode_tinting"));
                            intent.putExtra("description_tinting", object.getString("description_tinting"));
                            intent.putExtra("qty_tinting", object.getString("qty_tinting"));
                            intent.putExtra("price_tinting", Integer.parseInt(object.getString("price_tinting")));
                            intent.putExtra("total_tinting", Integer.parseInt(object.getString("total_tinting")));
                            intent.putExtra("margin_tinting", object.getString("margin_tinting"));
                            intent.putExtra("extra_margin_tinting", object.getString("extra_margin_tinting"));

                            intent.putExtra("total_price", Double.parseDouble(object.getString("total_price")));
                            intent.putExtra("isHasInsert", 1);

                            startActivity(intent);
                        }
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
                    Log.d("ERROR ENTRY STATUS", error.getMessage());
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

                   // Log.d("info", "status payment " + status + " status sekarang ini : " + existingStatus);
                      Log.d("info", "status payment " + orderNumber + " : " + status + " status sekarang ini : " + lastStatus);

                    if (status.equals("null"))
                    {
                        Log.d(FormOrderHistoryActivity.class.getSimpleName(), "* Jangan dieksekusi *");
                    }
                    else {
                        if (!status.equals(lastStatus)) {
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
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("info", "output = " + error);
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
                Log.d(FormOrderHistoryActivity.class.getSimpleName(), "Status Update = " + response);
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
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("hasil", "output update = " + error);
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

    private void checkPaymentMethod(final String transnumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, HISTORYPAYMENTORNOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    paymentInfo = jsonObject.getString("paymentType");
                    username    = jsonObject.getString("username");

                    if (paymentInfo.contentEquals("internetBanking") || paymentInfo.equals("internetBanking")
                            || paymentInfo.contains("internetBanking"))
                    {
                        //Toasty.info(getApplicationContext(), paymentType, Toast.LENGTH_SHORT).show();

                        showInfoPaymentQR(transnumber);
                    }
                    else if (paymentInfo.contentEquals("bankTransfer") || paymentInfo.equals("bankTransfer")
                            || paymentInfo.contains("bankTransfer"))
                    {
                        //Toasty.info(getApplicationContext(), paymentType, Toast.LENGTH_SHORT).show();

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
}
