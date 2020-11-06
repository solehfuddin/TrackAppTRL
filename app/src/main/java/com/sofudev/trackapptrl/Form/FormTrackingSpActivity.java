package com.sofudev.trackapptrl.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.sofudev.trackapptrl.Adapter.Adapter_track_orderview;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_detail_sp;
import com.sofudev.trackapptrl.Data.Data_track_order;
import com.sofudev.trackapptrl.Info.InfoOrderHistoryActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ayar.oktay.advancedtextview.AdvancedTextView;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class FormTrackingSpActivity extends AppCompatActivity implements View.OnClickListener {

    Config config = new Config();
    DateFormat tglFormat = new DateFormat();
    String URL_GETSP = config.Ip_address + config.ordersp_get_listSp;
    String URL_GETSPOPTIC  = config.Ip_address + config.ordersp_get_listSpByOptic;
    String URL_GETSEARCHSP = config.Ip_address + config.ordersp_get_searchSp;
    String URL_GETSEARCHSPOPTIC = config.Ip_address + config.ordersp_get_searchSpByOptic;
    String URL_GETRANGESP  = config.Ip_address + config.ordersp_get_rangeSp;
    String URL_GETRANGESPOPTIC  = config.Ip_address + config.ordersp_get_rangeSpByOptic;
    String URL_GETSTATUSSP = config.Ip_address + config.ordersp_get_statusSp;
    String URL_GETDETAILSP = config.Ip_address + config.ordersp_get_detailSp;
    String URL_GETINV      = config.Ip_address + config.ordersp_get_inv;

    Calendar calendar;

    Button btn_filterdate, btn_detail;
    RippleView btnBack, btnFilter, btnSearch, rp_filterdate, ripple_btnDetail;
    BootstrapButton btnPrev, btnNext;
    UniversalFontTextView txtCounter, lbl_duration;
    AdvancedTextView  txt_status, txt_statusnull, txt_datestatus, txt_duration, txt_invoice;
    MaterialEditText txtSearch, txt_startdate, txt_enddate;
    RecyclerView recyclerView;
    RelativeLayout rl_track;
    ImageView img_track;
    LinearLayout linear_invoice;
    RecyclerView.LayoutManager recyclerLayoutManager;
//    ImageView imgNotFound;
    CircleProgressBar loader;
    ACProgressFlower loading;

    Adapter_track_orderview adapter_track_orderview;
    List<Data_track_order> list = new ArrayList<>();
    List<Data_detail_sp> listSp = new ArrayList<>();

    long lastClick = 0;
    int counter = 0, totalData, total_item, total_range, day, month, year, flagCon, level;
    String sales, start_date, end_date, status, date_in, totalDurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_tracking_sp);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack     = findViewById(R.id.form_tracksp_ripplebtnback);
        btnFilter   = findViewById(R.id.form_tracksp_ripplebtnfilter);
        btnPrev     = findViewById(R.id.form_tracksp_btnprev);
        btnNext     = findViewById(R.id.form_tracksp_btnnext);
        txtSearch   = findViewById(R.id.form_tracksp_txtJobNumber);
        txtCounter  = findViewById(R.id.form_tracksp_txtCounter);
        btnSearch   = findViewById(R.id.form_tracksp_rpsearch);
        recyclerView= findViewById(R.id.form_tracksp_recycleview);
        rl_track    = findViewById(R.id.form_tracksp_relativelayout);
        loader      = findViewById(R.id.form_tracksp_progressBar);

        btnFilter.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        getData();
        adapter_track_orderview = new Adapter_track_orderview(getApplicationContext(), list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                showDialogTrack(pos);
            }
        });

        recyclerView.setAdapter(adapter_track_orderview);
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            sales  = bundle.getString("username");
            level  = bundle.getInt("level", 0);

            Log.d("NAMA SALES", sales);

            showLoading();
            if (level == 1)
            {
                getListSp(sales, "0");
            }
            else
            {
                getListSpByOptic(sales, "0");
            }
        }
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormTrackingSpActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showErrorImage()
    {
        img_track = new ImageView(getApplicationContext());
        img_track.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_track.setLayoutParams(lp);
        rl_track.addView(img_track);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.form_tracksp_ripplebtnback:
                finish();
                break;

            case R.id.form_tracksp_rpsearch:
                String key = String.valueOf(txtSearch.getText());
                showLoading();

                if (key.length() > 0)
                {
                    if (level == 1)
                    {
                        getSearchSp(sales, key, String.valueOf(counter));
                    }
                    else
                    {
                        getSearchByOptic(sales, key, String.valueOf(counter));
                    }
                }
                else
                {
                    if (level == 1)
                    {
                        getListSp(sales, String.valueOf(counter));
                    }
                    else
                    {
                        getListSpByOptic(sales, String.valueOf(counter));
                    }
                }

                break;

            case R.id.form_tracksp_ripplebtnfilter:
                handlerDialog();
                break;

            case R.id.form_tracksp_btnprev:
                switch (flagCon) {
                    case 0:
                        showLoading();
                        counter = counter - 8;
//                        getListSp(sales, String.valueOf(counter));
                        if (level == 1)
                        {
                            getListSp(sales, String.valueOf(counter));
                        }
                        else
                        {
                            getListSpByOptic(sales, String.valueOf(counter));
                        }
                        break;

                    case 1:
                        list.clear();
                        listSp.clear();
                        showLoading();
                        counter = counter - 8;

                        if (counter <= total_item) {
//                            getSearchSp(sales, txtSearch.getText().toString(), String.valueOf(counter));

                            if (level == 1)
                            {
                                getSearchSp(sales, txtSearch.getText().toString(), String.valueOf(counter));
                            }
                            else
                            {
                                getSearchByOptic(sales, txtSearch.getText().toString(), String.valueOf(counter));
                            }
                        }
                        break;

                    case 2:
                        list.clear();
                        listSp.clear();
                        showLoading();
                        counter = counter - 8;

                        if (counter <= total_range) {
//                            getRangeSp(sales, start_date, end_date, String.valueOf(counter));

                            if (level == 1)
                            {
                                getRangeSp(sales, start_date, end_date, String.valueOf(counter));
                            }
                            else
                            {
                                getRangeSpByOptic(sales, start_date, end_date, String.valueOf(counter));
                            }
                        }
                        break;
                }
                break;

            case R.id.form_tracksp_btnnext:
                switch (flagCon) {
                    case 0:
                        counter = counter + 8;
                        showLoading();
//                        getListSp(sales, String.valueOf(counter));
                        if (level == 1)
                        {
                            getListSp(sales, String.valueOf(counter));
                        }
                        else
                        {
                            getListSpByOptic(sales, String.valueOf(counter));
                        }
                        break;

                    case 1:
                        list.clear();
                        listSp.clear();
                        showLoading();
                        counter = counter + 8;

                        if (counter <= total_item) {
//                            getSearchSp(sales, txtSearch.getText().toString(), String.valueOf(counter));

                            if (level == 1)
                            {
                                getSearchSp(sales, txtSearch.getText().toString(), String.valueOf(counter));
                            }
                            else
                            {
                                getSearchByOptic(sales, txtSearch.getText().toString(), String.valueOf(counter));
                            }
                        }
                        break;

                    case 2:
                        list.clear();
                        listSp.clear();
                        showLoading();
                        counter = counter + 8;

                        if (counter <= total_range) {
//                            getRangeSp(sales, start_date, end_date, String.valueOf(counter));

                            if (level == 1)
                            {
                                getRangeSp(sales, start_date, end_date, String.valueOf(counter));
                            }
                            else
                            {
                                getRangeSpByOptic(sales, start_date, end_date, String.valueOf(counter));
                            }
                        }
                        break;
                }
                break;
        }
    }

    private void showDialogTrack(int pos) {
        final UniversalFontTextView txt_jobNumber;
        final AdvancedTextView txt_tipe, txt_nama, txt_kondisi, txt_via;
        final RippleView ripple_btndownload;

//        final BootstrapLabel btn_lblfacet;

        final Dialog dialog = new Dialog(FormTrackingSpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dialog_tracksp);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int) (width * 0.98f);
        int dialogHeight= (int) (height * 0.65f);
        layoutParams.width = dialogWidth;
        layoutParams.height= dialogHeight;
        dialog.getWindow().setAttributes(layoutParams);

        linear_invoice  = dialog.findViewById(R.id.form_dialogtracksp_linearinvoice);
        txt_invoice     = dialog.findViewById(R.id.form_dialogtracksp_txtinvoice);
        txt_jobNumber   = dialog.findViewById(R.id.form_dialogtracksp_txtspnumber);
        txt_tipe        = dialog.findViewById(R.id.form_dialogtracksp_txttipe);
        txt_nama        = dialog.findViewById(R.id.form_dialogtracksp_txtnama);
        txt_kondisi     = dialog.findViewById(R.id.form_dialogtracksp_txtkondisi);
        txt_via         = dialog.findViewById(R.id.form_dialogtracksp_txtvia);
        txt_status      = dialog.findViewById(R.id.form_dialogtracksp_txtorderstatus);
        txt_statusnull  = dialog.findViewById(R.id.form_dialogtracksp_txtorderstatusnull);
        txt_datestatus  = dialog.findViewById(R.id.form_dialogtracksp_txtdatestatus);
        lbl_duration    = dialog.findViewById(R.id.form_dialogtracksp_lblduration);
        txt_duration    = dialog.findViewById(R.id.form_dialogtracksp_txtduration);

        btn_detail      = dialog.findViewById(R.id.form_dialogtracksp_btnDetail);
        ripple_btnDetail= dialog.findViewById(R.id.form_dialogtracksp_rippleBtnDetail);
        ripple_btndownload = dialog.findViewById(R.id.form_dialogtracksp_rippleBtnDownload);
        final String noSp = listSp.get(pos).getNoSp();
        final String tipe = listSp.get(pos).getTipeSp();

        getStatusSp(noSp);

        txt_jobNumber.setText("Job Number #" + noSp);
        txt_tipe.setText(tipe);
        txt_nama.setText(listSp.get(pos).getNamaOptik());
        txt_kondisi.setText(listSp.get(pos).getKondisi());
        txt_via.setText(listSp.get(pos).getOrderVia());

//        getInvNumber(noSp);
        getInv(noSp);

        ripple_btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showLoading();

                Intent intent = new Intent(FormTrackingSpActivity.this, InfoOrderHistoryActivity.class);
                intent.putExtra("order_number", noSp);
                intent.putExtra("is_sp", 1);
                startActivity(intent);

//                dialog.dismiss();
            }
        });

        ripple_btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipe.equals("FRAME") || tipe.equals("Frame"))
                {
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://timurrayalab.com/trl-dev/index.php/PrintReceipt/spFrame/" + noSp));
                    startActivity(openBrowser);
                }
                else
                {
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://timurrayalab.com/trl-dev/index.php/PrintReceipt/spLensa/" + noSp));
                    startActivity(openBrowser);
                }
            }
        });
        dialog.show();
    }

    private void handlerDialog() {
        final Dialog dialog = new Dialog(FormTrackingSpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_trackdate);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        calendar = Calendar.getInstance();

        year  = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day   = calendar.get(Calendar.DATE);

        txt_startdate = dialog.findViewById(R.id.filter_track_startdate);
        txt_enddate   = dialog.findViewById(R.id.filter_track_enddate);
        btn_filterdate= dialog.findViewById(R.id.filter_track_btnok);
        rp_filterdate = dialog.findViewById(R.id.filter_track_ripple_btnok);

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
                    //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                    counter = 0;
                    list.clear();
                    showLoading();

                    if (level == 1)
                    {
                        getRangeSp(sales, start_date, end_date, String.valueOf(counter));
                    }
                    else
                    {
                        getRangeSpByOptic(sales, start_date, end_date, String.valueOf(counter));
                    }

//                    showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);
                }
            }
        });
    }

    private void showStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormTrackingSpActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormTrackingSpActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void getListSp(final String sales, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSP + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 0;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            totalData   = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + totalData + " data";
                            txtCounter.setText(counter);

                            if (totalData == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("sales", sales);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getListSpByOptic(final String optic, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSPOPTIC + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 0;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            totalData   = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + totalData + " data";
                            txtCounter.setText(counter);

                            if (totalData == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("optic", optic);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSearchSp(final String sales, final String key, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSEARCHSP + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 1;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            total_item  = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + total_item + " data";
                            txtCounter.setText(counter);

                            if (total_item == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("sales", sales);
                hashMap.put("key", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSearchByOptic(final String optic, final String key, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSEARCHSPOPTIC + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 1;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            total_item  = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + total_item + " data";
                            txtCounter.setText(counter);

                            if (total_item == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("optic", optic);
                hashMap.put("key", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getRangeSp(final String sales, final String dateStart, final String dateUntil, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETRANGESP + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 2;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            total_range  = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + total_range + " data";
                            txtCounter.setText(counter);

                            if (total_range == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("sales", sales);
                hashMap.put("date_start", dateStart);
                hashMap.put("date_until", dateUntil);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getRangeSpByOptic(final String optic, final String dateStart, final String dateUntil, final String start) {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        listSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETRANGESPOPTIC + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 2;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
//                            String noInv= object.getString("no_inv");
                            String tgl  = tglFormat.indo(object.getString("date"));
                            String type_sp     = object.getString("type_sp");
                            String customer_name = object.getString("customer_name");
                            String conditions  = object.getString("conditions");
                            String order_via   = object.getString("order_via");
                            total_range  = object.getInt("totalrow");

                            Data_track_order data = new Data_track_order();
                            data.setOrder_custname(noSp);
                            data.setOrder_entrydate(tgl);

                            list.add(data);

                            Data_detail_sp sp = new Data_detail_sp();
                            sp.setNoSp(noSp);
//                            sp.setNoInv(noInv);
                            sp.setTipeSp(type_sp);
                            sp.setNamaOptik(customer_name);
                            sp.setKondisi(conditions);
                            sp.setOrderVia(order_via);

                            listSp.add(sp);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + total_range + " data";
                            txtCounter.setText(counter);

                            if (total_range == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_track_orderview.notifyDataSetChanged();
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
                hashMap.put("optic", optic);
                hashMap.put("date_start", dateStart);
                hashMap.put("date_until", dateUntil);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getStatusSp(final String noSp)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSTATUSSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    status = object.getString("status");
                    date_in= object.getString("date_in");
                    totalDurr = object.getString("total_durr");

                    if (date_in.equals("null") || totalDurr.equals("Dalam proses"))
                    {
                        txt_statusnull.setText(status);

                        txt_statusnull.setVisibility(View.VISIBLE);
                        txt_status.setVisibility(View.GONE);
                        txt_datestatus.setVisibility(View.GONE);
                        lbl_duration.setVisibility(View.GONE);
                        txt_duration.setVisibility(View.GONE);
                        ripple_btnDetail.setEnabled(false);
                        btn_detail.setBackgroundColor(Color.parseColor("#58595e"));
                    }
                    else {
                        if (status.equals("AR")){
                            String approval = object.getString("approve");
                            if (!approval.equals("null"))
                            {
                                status = approval + " BY " + status;

                                if(approval.contains("APPROVE"))
                                {
                                    txt_status.setBackgroundColor(Color.parseColor("#45ac2d"));
                                    txt_status.setTextColor(Color.WHITE);
//                                    txt_status.setHintTextColor(Color.parseColor("#fff"));
                                }
                                else
                                {
                                    txt_status.setBackgroundColor(Color.parseColor("#f90606"));
                                    txt_status.setTextColor(Color.WHITE);
//                                    txt_status.setHintTextColor(Color.parseColor("#fff"));
                                }
                            }
                        }

                        txt_status.setText(status);
                        txt_datestatus.setText(tglFormat.Indotime(date_in));
                        txt_duration.setText(totalDurr);

                        txt_status.setVisibility(View.VISIBLE);
                        txt_statusnull.setVisibility(View.GONE);
                        txt_datestatus.setVisibility(View.VISIBLE);
                        lbl_duration.setVisibility(View.VISIBLE);
                        txt_duration.setVisibility(View.VISIBLE);
                        ripple_btnDetail.setEnabled(true);
                        btn_detail.setBackgroundColor(Color.parseColor("#ff9100"));
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
                hashMap.put("no_sp", noSp);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getInvNumber(final String noSp) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETDETAILSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

//                        if (object.getString("status").equals("CS"))
//                        {
                            linear_invoice.setVisibility(View.VISIBLE);
                            txt_invoice.setText(object.getString("no_inv"));
//                        }
//                        else
//                        {
//                            linear_invoice.setVisibility(View.GONE);
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    linear_invoice.setVisibility(View.GONE);
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
                hashMap.put("id_sp", noSp);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getInv(final String noSp)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETINV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error")){
                        linear_invoice.setVisibility(View.GONE);
                    }
                    else
                    {
                        linear_invoice.setVisibility(View.VISIBLE);
                        txt_invoice.setText(jsonObject.getString("no_inv"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    linear_invoice.setVisibility(View.GONE);
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
                HashMap<String, String> map = new HashMap<>();
                map.put("id_sp", noSp);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
