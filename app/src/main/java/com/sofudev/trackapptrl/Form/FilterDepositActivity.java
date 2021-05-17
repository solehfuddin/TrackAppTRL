package com.sofudev.trackapptrl.Form;

import android.app.DatePickerDialog;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_deposit;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_detail_deposit;
import com.sofudev.trackapptrl.R;

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

public class FilterDepositActivity extends AppCompatActivity {

    Config config = new Config();
    DateFormat formatTgl = new DateFormat();
    String URLFILTERDEPOSIT = config.Ip_address + config.depo_getfiltersaldo;
    String URLFILTERPENDING = config.Ip_address + config.depo_getfilterpending;
    String URLFILTERTOTAL   = config.Ip_address + config.depo_getfiltertotal;

    RippleView rippleBack;
    UniversalFontTextView txtTglAwal, txtTglAkhir, txtDeposit, txtKredit, txtTitle;
    ImageView btnTglAwal, btnTglAkhir, imgNotFound;
    RecyclerView recyclerDeposit;
    ProgressBar loading;
    LinearLayout linear4;
    View view;

    Adapter_detail_deposit adapter_detail_deposit;
    List<Data_detail_deposit> list_deposit = new ArrayList<>();

    String username, saldo, idparty, todayDate;
    SimpleDateFormat dateFormat;
    Calendar calendar;
    DateFormat customDateFormat;
    int totalDeposit, totalKredit, day, month, year;
    String start_date, end_date, status, filter_type;
    long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_deposit);

        rippleBack = findViewById(R.id.form_filterdeposit_ripplebtnback);
        txtTitle   = findViewById(R.id.form_filterdeposit_txtfilter);
        txtTglAwal = findViewById(R.id.form_filterdeposit_txttglawal);
        txtTglAkhir= findViewById(R.id.form_filterdeposit_txttglakhir);
        txtDeposit = findViewById(R.id.form_filterdeposit_txtsaldodebit);
        txtKredit  = findViewById(R.id.form_filterdeposit_txtsaldokredit);
        btnTglAwal = findViewById(R.id.form_filterdeposit_btntglawal);
        btnTglAkhir= findViewById(R.id.form_filterdeposit_btntglakhir);
        imgNotFound= findViewById(R.id.form_filterdeposit_imgLensnotfound);
        recyclerDeposit = findViewById(R.id.form_filterdeposit_recyclerview);
        loading    = findViewById(R.id.form_filterdeposit_progress);
        linear4    = findViewById(R.id.linearLayout4);
        view       = findViewById(R.id.view1);

        calendar = Calendar.getInstance();
        customDateFormat = new DateFormat();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = dateFormat.format(calendar.getTime());

        adapter_detail_deposit = new Adapter_detail_deposit(getApplicationContext(), list_deposit);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerDeposit.setLayoutManager(manager);
        recyclerDeposit.setAdapter(adapter_detail_deposit);

        String split [] = todayDate.split("-");
        txtTglAwal.setText(customDateFormat.ValueUserDate(Integer.valueOf(split[2]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[0])));
        txtTglAkhir.setText(customDateFormat.ValueUserDate(Integer.valueOf(split[2]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[0])));
        start_date = customDateFormat.ValueDbDate(Integer.valueOf(split[2]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[0]));
        end_date   = customDateFormat.ValueDbDate(Integer.valueOf(split[2]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[0]));

        year  = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day   = calendar.get(Calendar.DATE);

        rippleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTglAwal.setOnTouchListener(new View.OnTouchListener() {
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

        btnTglAkhir.setOnTouchListener(new View.OnTouchListener() {
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

        getUserInfo();

        showFilterTotalDeposit(username, start_date, end_date);
        showFilterTotalKredit(username, start_date, end_date);
    }

    private void getUserInfo()
    {
        Bundle bundle = getIntent().getExtras();
        filter_type = bundle.getString("filter_type");
        username = bundle.getString("username");
        saldo    = bundle.getString("nominal");
        idparty  = bundle.getString("user_info");

        switch (filter_type){
            case "PENDING":
                hideCounter();
                txtTitle.setText("Pending History");

                showPendingDeposit(username, start_date, end_date);
                break;
            case "DEPO":
                showCounter();
                txtTitle.setText("Transaction History");
                showRecentDeposit(username, start_date, end_date);
                break;
        }
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

    private void showCounter() {
        linear4.setVisibility(View.VISIBLE);
    }

    private void hideCounter() {
        linear4.setVisibility(View.GONE);
    }

    private void showStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FilterDepositActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

//                txtTglAwal.setText(day + "-" + mon + "-" + year);
//                start_date = year + "-" + month + "-" + day;

                txtTglAwal.setText(customDateFormat.ValueUserDate(dayOfMonth, month, year));
                start_date = customDateFormat.ValueDbDate(dayOfMonth, month, year);

                switch (filter_type){
                    case "PENDING":
                        showPendingDeposit(username, start_date, end_date);
                        break;
                    case "DEPO":
                        showRecentDeposit(username, start_date, end_date);
                        break;
                }

                //showRecentDeposit(username, start_date, end_date);
                showFilterTotalDeposit(username, start_date, end_date);
                showFilterTotalKredit(username, start_date, end_date);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FilterDepositActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

//                txtTglAkhir.setText(day + "-" + mon + "-" + year);
//                end_date = year + "-" + month + "-" + day;

                txtTglAkhir.setText(customDateFormat.ValueUserDate(dayOfMonth, month, year));
                end_date = customDateFormat.ValueDbDate(dayOfMonth, month, year);

                switch (filter_type){
                    case "PENDING":
                        showPendingDeposit(username, start_date, end_date);
                        break;
                    case "DEPO":
                        showRecentDeposit(username, start_date, end_date);
                        break;
                }

                //showRecentDeposit(username, start_date, end_date);
                showFilterTotalDeposit(username, start_date, end_date);
                showFilterTotalKredit(username, start_date, end_date);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showRecentDeposit(final String username, final String tglAwal, final String tglAkhir) {
        list_deposit.clear();
        loading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, URLFILTERDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(DetailDepositActivity.class.getSimpleName(), response);
                    loading.setVisibility(View.GONE);

                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgNotFound.setVisibility(View.VISIBLE);
                        recyclerDeposit.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("insert_date");
                            String deskripsi= jsonObject.getString("keterangan");
                            String jenis    = jsonObject.getString("jenis_pembayaran");
                            String bank     = jsonObject.getString("bank_name");
                            int nominal     = jsonObject.getInt("saldo");

                            Data_detail_deposit item = new Data_detail_deposit();
                            item.setJenis_pembayaran(jenis);
                            item.setInsert_date(formatTgl.Indotime(tanggal));
                            item.setBank_name(bank);
                            item.setKeterangan(deskripsi);
                            item.setSaldo(nominal);

                            list_deposit.add(item);
                        }

                        imgNotFound.setVisibility(View.GONE);
                        recyclerDeposit.setVisibility(View.VISIBLE);

                        adapter_detail_deposit.notifyDataSetChanged();
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
                hashMap.put("ship_number", username);
                hashMap.put("start", tglAwal);
                hashMap.put("end", tglAkhir);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showPendingDeposit(final String username, final String tglAwal, final String tglAkhir) {
        list_deposit.clear();
        loading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, URLFILTERPENDING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(DetailDepositActivity.class.getSimpleName(), response);
                    loading.setVisibility(View.GONE);

                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgNotFound.setVisibility(View.VISIBLE);
                        recyclerDeposit.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("insert_date");
                            String deskripsi= jsonObject.getString("keterangan");
                            String jenis    = jsonObject.getString("jenis_pembayaran");
                            String bank     = jsonObject.getString("bank_name");
                            int nominal     = jsonObject.getInt("saldo");

                            Data_detail_deposit item = new Data_detail_deposit();
                            item.setJenis_pembayaran(jenis);
                            item.setInsert_date(formatTgl.Indotime(tanggal));
                            item.setBank_name(bank);
                            item.setKeterangan(deskripsi);
                            item.setSaldo(nominal);

                            list_deposit.add(item);
                        }

                        imgNotFound.setVisibility(View.GONE);
                        recyclerDeposit.setVisibility(View.VISIBLE);

                        adapter_detail_deposit.notifyDataSetChanged();
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
                hashMap.put("ship_number", username);
                hashMap.put("start", tglAwal);
                hashMap.put("end", tglAkhir);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showFilterTotalDeposit(final String username, final String tglAwal, final String tglAkhir) {
        StringRequest request = new StringRequest(Request.Method.POST, URLFILTERTOTAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    totalDeposit = object.getInt("total");
                    txtDeposit.setText(CurencyFormat(String.valueOf(totalDeposit)));
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
                hashMap.put("ship_number", username);
                hashMap.put("jenis_pembayaran", "DEPOSIT");
                hashMap.put("start", tglAwal);
                hashMap.put("end", tglAkhir);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showFilterTotalKredit(final String username, final String tglAwal, final String tglAkhir) {
        StringRequest request = new StringRequest(Request.Method.POST, URLFILTERTOTAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    totalKredit = object.getInt("total");
                    txtKredit.setText(CurencyFormat(String.valueOf(totalKredit).replace('-', ' ')));
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
                hashMap.put("ship_number", username);
                hashMap.put("jenis_pembayaran", "KREDIT");
                hashMap.put("start", tglAwal);
                hashMap.put("end", tglAkhir);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
