package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_dpodk;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CourierHistoryActivity extends AppCompatActivity {

    Config config = new Config();
    String GETHISTORYBYDATERANGE = config.Ip_address + config.dpodk_historybydaterange;
    String FINDHISTORYBYNOTRX    = config.Ip_address + config.dpodk_historybysearchidtrx;
    String GETPROCESSBYDATERANGE = config.Ip_address + config.dpodk_processbydaterange;
    String FINDPROCESSBYKEYWORD  = config.Ip_address + config.dpodk_processbysearch;

    String partySiteId, username, todayDate, start_date, end_date, seachKeyword;
    SimpleDateFormat dateFormat;

    Calendar calendar;
    int day, month, year;
    int offset = 0;
    int start = 1;
    int until = 0;
    int limit = 5;
    int totalData = 0;
    long lastClick = 0;
    boolean isAdmin;

    BootstrapButton btn_prev, btn_next;
    Button btn_filterdate;
    ImageButton btn_back, btn_search, btn_filter;
    ImageView imgNotFound;
    MaterialEditText txt_search, txt_startdate, txt_enddate;
    RippleView rp_search, rp_filterdate;
    UniversalFontTextView txt_counter, txtTitle;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;
    CircleProgressBar progressBar;

    List<Data_courier> listData = new ArrayList<>();
    Adapter_courier_dpodk adapter_courier_dpodk;
    CustomLoading customLoading;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_history);
        customLoading = new CustomLoading(this);

        btn_back        = (ImageButton) findViewById(R.id.courier_history_btnback);
        btn_search      = (ImageButton) findViewById(R.id.courier_history_btnsearch);
        btn_filter      = (ImageButton) findViewById(R.id.courier_history_btnfilter);
        btn_prev        = (BootstrapButton) findViewById(R.id.courier_history_btnprev);
        btn_next        = (BootstrapButton) findViewById(R.id.courier_history_btnnext);
        rp_search       = (RippleView) findViewById(R.id.courier_history_rpsearch);
        txt_counter     = (UniversalFontTextView) findViewById(R.id.courier_history_txtCounter);
        txt_search      = (MaterialEditText) findViewById(R.id.courier_history_txtSearch);
        txtTitle        = (UniversalFontTextView) findViewById(R.id.courier_history_txtTitle);
        imgNotFound     = (ImageView) findViewById(R.id.courier_history_imgnotfound);
        recyclerView    = (RecyclerView) findViewById(R.id.courier_history_recycleview);
        progressBar     = (CircleProgressBar) findViewById(R.id.courier_history_progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = dateFormat.format(Calendar.getInstance().getTime());

        backToDashboard();
//        getUsernameData();
        ClickNext();
        ClickPrev();
        ClickFilter();
        handleSearch();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        until = 0;
    }

    private void backToDashboard(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            partySiteId = bundle.getString("idparty");
            username = bundle.getString("username");
            isAdmin = bundle.getBoolean("isadmin");

            Log.d(CourierHistoryActivity.class.getSimpleName(), "Isadmin : " + isAdmin);

            if (isAdmin)
            {
                start_date = todayDate;
                txt_search.setHint("Cari berdasarkan nama kurir");
                txtTitle.setText("Monitoring Kurir");
            }
            else
            {
                start_date = getCalculatedDate(todayDate, "yyyy-MM-dd", -3);
                txt_search.setHint("Cari berdasarkan nomor dpodk");
                txtTitle.setText("Riwayat Pengiriman");
            }

            end_date = todayDate;

            Log.d("Id party : ", partySiteId);
            Log.d("Today date : ", todayDate);

            Log.d("Start date : ", start_date);
            Log.d("End date : ", end_date);

            until += limit;
            if (isAdmin)
            {
                getProcessCourier(start_date, end_date);
            }
            else
            {
                getHistoryCourier(partySiteId, start_date, end_date);
            }

            adapter_courier_dpodk = new Adapter_courier_dpodk(getApplicationContext(), listData, 2, isAdmin, new RecyclerViewOnClickListener() {
                @Override
                public void onItemClick(View view, int pos, String id) {
                    Log.d(CourierHistoryActivity.class.getSimpleName(), "History Dpodk : " + id);
                    Intent intent = new Intent(getApplicationContext(), CourierDpodkActivity.class);
                    intent.putExtra("idparty", partySiteId);
                    intent.putExtra("username", username);
                    intent.putExtra("iddpodk", id);
                    intent.putExtra("status", 1);
                    intent.putExtra("isadmin", isAdmin);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(adapter_courier_dpodk);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAdmin)
        {
            getUsernameData();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCalculatedDate(String date,String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        if (!date.isEmpty()) {
            try {
                cal.setTime(s.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    private void handleSearch(){
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                seachKeyword = txt_search.getText().toString().trim();
                if (seachKeyword.isEmpty())
                {
                    start = 1;
                    offset = 0;
                    until = 0;
                    until += limit;

                    if (isAdmin)
                    {
                        getProcessCourier(start_date, end_date);
                    }
                    else
                    {
                        getHistoryCourier(partySiteId, start_date, end_date);
                    }
                }
                else
                {
                    start = 1;
                    offset = 0;
                    until = 0;
                    until += limit;

                    if (isAdmin)
                    {
                        findProcessCourier(seachKeyword, start_date, end_date);
                    }
                    else
                    {
                        findHistoryCourier(partySiteId, seachKeyword);
                    }
                }
            }
        });

        txt_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    seachKeyword = txt_search.getText().toString().trim();
                    if (seachKeyword.isEmpty())
                    {
                        start = 1;
                        offset = 0;
                        until = 0;
                        until += limit;

                        if (isAdmin)
                        {
                            getProcessCourier(start_date, end_date);
                        }
                        else
                        {
                            getHistoryCourier(partySiteId, start_date, end_date);
                        }
                    }
                    else
                    {
                        start = 1;
                        offset = 0;
                        until = 0;
                        until += limit;

                        if (isAdmin)
                        {
                            findProcessCourier(seachKeyword, start_date, end_date);
                        }
                        else
                        {
                            findHistoryCourier(partySiteId, seachKeyword);
                        }
                    }
                }

                return false;
            }
        });
    }

    private void ClickFilter(){
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CourierHistoryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_trackdate);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                calendar = Calendar.getInstance();

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
                            start = 1;
                            offset = 0;
                            until = 0;
                            until += limit;
//                            getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));

                            if (isAdmin)
                            {
                                getProcessCourier(start_date, end_date);
                            }
                            else
                            {
                                getHistoryCourier(partySiteId, start_date, end_date);
                            }
                        }
                    }
                });
            }
        });
    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CourierHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String mon;
                String day;

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

                txt_startdate.setText(day + "-" + mon + "-" + year);
                start_date = year + "-" + month + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showEndDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CourierHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String day;
                String mon;

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

                txt_enddate.setText(day + "-" + mon + "-" + year);
                end_date = year + "-" + month + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void ClickNext()
    {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset += limit;
                start += limit;
                until += limit;

                if (txt_search.getText().length() == 0)
                {
                    if (isAdmin)
                    {
                        getProcessCourier(start_date, end_date);
                    }
                    else
                    {
                        getHistoryCourier(partySiteId, start_date, end_date);
                    }
                }
                else
                {
                    if (isAdmin)
                    {
                        findProcessCourier(seachKeyword, start_date, end_date);
                    }
                    else
                    {
                        findHistoryCourier(partySiteId, seachKeyword);
                    }
                }

                if (totalData < until)
                {
                    btn_next.setEnabled(false);
                }
            }
        });
    }

    private void ClickPrev()
    {
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset -= limit;
                start -= limit;
                until -= limit;

                if (txt_search.getText().length() == 0)
                {
                    if (isAdmin)
                    {
                        getProcessCourier(start_date, end_date);
                    }
                    else
                    {
                        getHistoryCourier(partySiteId, start_date, end_date);
                    }
                }
                else
                {
                    if (isAdmin)
                    {
                        findProcessCourier(seachKeyword, start_date, end_date);
                    }
                    else
                    {
                        findHistoryCourier(partySiteId, seachKeyword);
                    }
                }

                if (start == 1)
                {
                    btn_prev.setEnabled(false);
                }
                else
                {
                    btn_prev.setEnabled(true);
                }
            }
        });
    }

    private void getHistoryCourier(final String idCourier, final String stDate, final String edDate)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        Log.d(CourierHistoryActivity.class.getSimpleName(), "Url : " + GETHISTORYBYDATERANGE + "/" + limit + "/" + offset);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param1 : " + stDate);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param2 : " + edDate);

        StringRequest request = new StringRequest(Request.Method.POST, GETHISTORYBYDATERANGE + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txt_counter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);
                map.put("id_kurir", isAdmin ? "" :  idCourier);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getProcessCourier(final String stDate, final String edDate)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        Log.d(CourierHistoryActivity.class.getSimpleName(), "Url : " + GETPROCESSBYDATERANGE + "/" + limit + "/" + offset);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param1 : " + stDate);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param2 : " + edDate);

        StringRequest request = new StringRequest(Request.Method.POST, GETPROCESSBYDATERANGE + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txt_counter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void findHistoryCourier(final String idCourier, final String keyword)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        Log.d(CourierHistoryActivity.class.getSimpleName(), "Url : " + FINDHISTORYBYNOTRX);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param1 : " + idCourier);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param2 : " + keyword);

        StringRequest request = new StringRequest(Request.Method.POST, FINDHISTORYBYNOTRX + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txt_counter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("id_kurir", idCourier);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void findProcessCourier(final String keyword, final String dateSt, final String dateEd)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        Log.d(CourierHistoryActivity.class.getSimpleName(), "Url : " + FINDPROCESSBYKEYWORD);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param1 : " + dateSt);
        Log.d(CourierHistoryActivity.class.getSimpleName(), "Param2 : " + keyword);

        StringRequest request = new StringRequest(Request.Method.POST, FINDPROCESSBYKEYWORD + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txt_counter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("dateSt", dateSt);
                map.put("dateEd", dateEd);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}