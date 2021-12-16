package com.sofudev.trackapptrl.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_track_orderview;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_track_order;
import com.sofudev.trackapptrl.Info.InfoOrderHistoryActivity;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;

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

public class FormTrackOrderActivity extends AppCompatActivity {
    Config config = new Config();

    private String CHECK_ORDER      = config.Ip_address + config.track_order_optic;
    private String CHECK_CUSTNAME   = config.Ip_address + config.track_order_custname;
    private String CHECK_REFERENCE  = config.Ip_address + config.track_order_reference;
    private String CHECK_BYDATE     = config.Ip_address + config.track_order_daterange;
    private String GET_FRAME_BRAND  = config.Ip_address + config.track_order_getFrame;
    private String GET_FRAME_TYPE   = config.Ip_address + config.track_order_getType;

    Button btn_filterdate;
    ImageButton btn_back, btn_search, btn_filter;
    BootstrapButton btn_prev, btn_next;
    UniversalFontTextView txt_counter;
    MaterialEditText txt_customer, txt_startdate, txt_enddate;
    RippleView rp_search, rp_filterdate;
    CircleProgressBar progress;
    MCrypt mCrypt;
    Calendar calendar;
    ACProgressFlower loading;
    LinearLayout linear_frameBrand;
    AdvancedTextView txt_frameBrand;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;
    RelativeLayout rl_track;
    ImageView img_track;
    //Adapter_track_order adapter_track_order;
    Adapter_track_orderview adapter_track_orderview;
    List<Data_track_order> list = new ArrayList<>();

    long lastClick = 0;
    Integer req_start = 0, total_row, total_item, total_filter, day, month, year;
    String id_data, customer_name, start_date, end_date, frame_brand_dt, frame_type_dt;

    String order_number, order_lensname, order_sphr, order_cylr, order_addr, order_sphl, order_cyll, order_addl,
            order_reference, order_tinting, order_facettrl, order_status, order_statusdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_track_order);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btn_back        = (ImageButton) findViewById(R.id.form_trackorder_btnback);
        btn_filter      = (ImageButton) findViewById(R.id.form_trackorder_btnfilter);
        txt_customer    = (MaterialEditText) findViewById(R.id.form_trackorder_txtJobNumber);
        btn_search      = (ImageButton) findViewById(R.id.form_trackorder_btnsearch);
        rp_search       = (RippleView) findViewById(R.id.form_trackorder_rpsearch);
        progress        = (CircleProgressBar) findViewById(R.id.form_trackorder_progressBar);
        rl_track        = (RelativeLayout) findViewById(R.id.form_trackorder_relativelayout);
        btn_prev        = (BootstrapButton) findViewById(R.id.form_trackorder_btnprev);
        btn_next        = (BootstrapButton) findViewById(R.id.form_trackorder_btnnext);
        txt_counter     = (UniversalFontTextView) findViewById(R.id.form_trackorder_txtCounter);

        recyclerView = (RecyclerView) findViewById(R.id.form_trackorder_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        /*adapter_track_order = new Adapter_track_order(recyclerView, list_lens, new CustomOnClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String order_number = list_lens.get(position).getOrder_number();
                Toast.makeText(getApplicationContext(), order_number, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(FormTrackOrderActivity.this, InfoOrderHistoryActivity.class);
                intent.putExtra("order_number", order_number);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter_track_order);*/

        adapter_track_orderview = new Adapter_track_orderview(getApplicationContext(), list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                order_number        = list.get(pos).getOrder_number();
                order_lensname      = list.get(pos).getOrder_lenscode();
                order_sphr          = list.get(pos).getOrder_sphr();
                order_cylr          = list.get(pos).getOrder_cylr();
                order_addr          = list.get(pos).getOrder_addr();
                order_sphl          = list.get(pos).getOrder_sphl();
                order_cyll          = list.get(pos).getOrder_cyll();
                order_addl          = list.get(pos).getOrder_addl();
                order_facettrl      = list.get(pos).getOrder_facet();
                order_reference     = list.get(pos).getOrder_reference();
                order_tinting       = list.get(pos).getOrder_tint_descr();
                order_status        = list.get(pos).getOrder_status();
                order_statusdate    = list.get(pos).getOrder_statusdate() + " " + list.get(pos).getOrder_statustime();

                if (order_facettrl.equals("G"))
                {
                    getFrameBrand(order_number);

                    try {
                        Thread.sleep(150);
                        getFrameType(order_number);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(400);
                    showDialogTrack();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), id + " " + order_number, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter_track_orderview);

        mCrypt = new MCrypt();
        BackToDashboard();
        OpenFilter();
        getUsernameData();

        progress.setVisibility(View.GONE);

        searchTrack();
        showTrackOrder(id_data, req_start);
        //scrollRecycler();

        btn_next.setEnabled(false);
        btn_prev.setEnabled(false);
        show_next();
        show_prev();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void BackToDashboard()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDialogTrack() {
        final UniversalFontTextView txt_jobNumber,  txt_sphR, txt_cylR, txt_addR, txt_sphL, txt_cylL, txt_addL;
        final AdvancedTextView txt_lensName, txt_reference, txt_tinting, txt_orderStatus, txt_dateStatus;
        final BootstrapLabel btn_lblfacet;
        final RippleView ripple_btnDetail;

        final Dialog dialog = new Dialog(FormTrackOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dialog_trackorder);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_jobNumber   = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtjobnumber);
        txt_lensName    = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txtlensname);
        txt_sphR        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtsphr);
        txt_cylR        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtcylr);
        txt_addR        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtaddr);
        txt_sphL        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtsphl);
        txt_cylL        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtcyll);
        txt_addL        = (UniversalFontTextView) dialog.findViewById(R.id.form_dialogtrack_txtaddl);
        txt_reference   = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txtreference);
        txt_tinting     = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txttinting);
        btn_lblfacet    = (BootstrapLabel) dialog.findViewById(R.id.form_dialogtrack_btnfacet);
        txt_orderStatus = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txtorderstatus);
        txt_dateStatus  = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txtdatestatus);
        txt_frameBrand  = (AdvancedTextView) dialog.findViewById(R.id.form_dialogtrack_txtframebrand);
        linear_frameBrand = (LinearLayout) dialog.findViewById(R.id.form_dialogtrack_linearframebrand);

        ripple_btnDetail= (RippleView) dialog.findViewById(R.id.form_dialogtrack_rippleBtnDetail);
        Button btn_Detail      = (Button) dialog.findViewById(R.id.form_dialogtrack_btnDetail);

        txt_jobNumber.setText("Job Number #" + order_number);
        txt_lensName.setText(order_lensname);
        txt_sphR.setText(order_sphr);
        txt_cylR.setText(order_cylr);
        txt_addR.setText(order_addr);
        txt_sphL.setText(order_sphl);
        txt_cylL.setText(order_cyll);
        txt_addL.setText(order_addl);
        txt_reference.setText(order_reference);
        linear_frameBrand.setVisibility(View.GONE);
//        getFrameBrand(order_number);

        txt_orderStatus.setText(order_status);
        txt_dateStatus.setText(order_statusdate);

        if (order_tinting != null | !order_tinting.isEmpty())
        {
            txt_tinting.setText(order_tinting);
        }
        else
        {
            txt_tinting.setText("");
        }

        if (order_facettrl.equals("G"))
        {
            btn_lblfacet.setText("FACET TIMUR RAYA");
            btn_lblfacet.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
        }
        else
        {
            btn_lblfacet.setText("NON FACET");
            btn_lblfacet.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        }

        if (order_status.equals("IMPT"))
        {
            ripple_btnDetail.setEnabled(false);
            btn_Detail.setBackgroundColor(Color.parseColor("#dedede"));
            btn_Detail.setTextColor(Color.parseColor("#58595e"));
        }
//        else
//        {
//            ripple_btnDetail.setEnabled(true);
//            btn_Detail.setBackgroundColor(Color.parseColor("#ff33b5e5"));
//            btn_Detail.setTextColor(Color.parseColor("#fff"));
//        }

        ripple_btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showLoading();

                Intent intent = new Intent(FormTrackOrderActivity.this, InfoOrderHistoryActivity.class);
                intent.putExtra("order_number", order_number);
                intent.putExtra("is_sp", 0);
                startActivity(intent);
            }
        });

        if(!isFinishing())
        {
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.dismiss();
    }

    private void getFrameBrand(final String jobNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, GET_FRAME_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("invalid"))
                    {
                        frame_brand_dt = "";
                    }
                    else
                    {
                        String receive_by = object.getString("receive_by");
                        String receive_at = object.getString("receive_at");
                        String merk_frame = object.getString("merk_frame");

                        frame_brand_dt = merk_frame + " (Diterima " + receive_by + ")";
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
                map.put("job", jobNumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getFrameType(final String jobNumber){
        StringRequest request = new StringRequest(Request.Method.POST, GET_FRAME_TYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("invalid"))
                    {
                        frame_type_dt = "";
                    }
                    else
                    {
                        String edgeType = object.getString("edge_type");

                        switch (edgeType){
                            case "1":
                                frame_type_dt = "FULL";
                                break;
                            case "2":
                                frame_type_dt = "BOR";
                                break;
                            case "3":
                                frame_type_dt = "NYLON";
                                break;
                            default:
                                frame_type_dt = "-";
                                break;
                        }

                        linear_frameBrand.setVisibility(View.VISIBLE);
                        if (frame_brand_dt != null)
                        {
                            if (!frame_brand_dt.isEmpty())
                            {
                                if (frame_type_dt.equals("-"))
                                {
                                    frame_type_dt = frame_brand_dt;
                                }
                                else
                                {
                                    frame_type_dt = frame_type_dt + " - " + frame_brand_dt;
                                }
                            }
                        }
                        else
                        {
                            if (frame_type_dt.equals("-"))
                            {
                                frame_type_dt = "-";
                            }
                        }

                        txt_frameBrand.setText(frame_type_dt);
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
                map.put("job", jobNumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void OpenFilter()
    {
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FormTrackOrderActivity.this);
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
                            //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                            req_start = 0;
                            list.clear();
                            showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);
                        }
                    }
                });
            }
        });
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormTrackOrderActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();

        if(!isFinishing()){
            loading.show();
        }
    }

    private void informationTrackOrder(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormTrackOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

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
                showTrackOrder(id_data, req_start);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void informationTrackOrderByCustomer(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormTrackOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

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
                showTrackOrderyByCustomer(id_data, req_start, txt_customer.getText().toString());

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void informationTrackOrderByDate(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormTrackOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

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
                showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormTrackOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormTrackOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            id_data = bundle.getString("idparty");
        }
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

    private void searchTrack()
    {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                customer_name = txt_customer.getText().toString();
                if (customer_name.isEmpty())
                {
                    list.clear();
                    req_start = 0;
                    showTrackOrder(id_data, req_start);
                }
                else
                {
                    list.clear();
                    req_start = 0;
                    showTrackOrderyByCustomer(id_data, req_start, customer_name);
                    //showTrackOrderyByReference(id_data, req_start, txt_customer.getText().toString());
                }
            }
        });

        txt_customer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    customer_name = txt_customer.getText().toString();
                    if (customer_name.isEmpty())
                    {
                        list.clear();
                        req_start = 0;
                        showTrackOrder(id_data, req_start);
                    }
                    else
                    {
                        list.clear();
                        req_start = 0;
                        showTrackOrderyByCustomer(id_data, req_start, customer_name);
                        //showTrackOrderyByReference(id_data, req_start, txt_customer.getText().toString());
                    }
                }

                return false;
            }
        });
    }

    /*private boolean isLastItemRecycler(RecyclerView recyclerView)
    {
        if (recyclerView.getAdapter().getItemCount() != 0)
        {
            int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastItem != RecyclerView.NO_POSITION  && lastItem == recyclerView.getAdapter().getItemCount() - 1)
            {
                return true;
            }
        }
        return false;
    }*/

    private void show_prev()
    {
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 2000)
                {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                if (txt_customer.getText().length() == 0)
                {
                    if (start_date != null && end_date != null)
                    {
                        list.clear();

                        req_start = req_start - 8;

                        if (req_start == 0)
                        {
                            btn_prev.setEnabled(false);
                        }

                        showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);
                    }
                    else
                    {
                        list.clear();

                        req_start = req_start - 8;

                        if (req_start == 0)
                        {
                            btn_prev.setEnabled(false);
                        }

                        showTrackOrder(id_data, req_start);
                    }
                }
                else
                {
                    list.clear();

                    req_start = req_start - 8;

                    showTrackOrderyByCustomer(id_data, req_start, customer_name);
                    //showTrackOrderyByReference(id_data, req_start, txt_customer.getText().toString());
                }
            }
        });
    }

    private void show_next()
    {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 2000)
                {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                if (txt_customer.getText().length() == 0)
                {
                    if (start_date != null && end_date != null)
                    {
                        list.clear();

                        req_start = req_start + 8;

                        if (req_start <= total_filter)
                        {
                            showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);
                        }
                        //Toast.makeText(getApplicationContext(), "Start date = " + start_date , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        list.clear();

                        req_start = req_start + 8;

                        if (req_start <= total_item)
                        {
                            showTrackOrder(id_data, req_start);
                        }
                    }
                }
                else
                {
                    list.clear();

                    req_start = req_start + 8;

                    showTrackOrderyByCustomer(id_data, req_start, customer_name);
                    //showTrackOrderyByReference(id_data, req_start, txt_customer.getText().toString());
                }

            }
        });
    }

    /*private void scrollRecycler()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SystemClock.elapsedRealtime() - lastClick < 2000)
                {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                if (isLastItemRecycler(recyclerView))
                {
                    if (txt_customer.getText().length() == 0)
                    {
                        req_start = req_start + 8;

                        if (total_item.equals(adapter_track_order.getItemCount()) || req_start > total_item)
                        {
                            progress.setVisibility(View.VISIBLE);
                            Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                        }
                        else if (req_start < total_item)
                        {
                            showTrackOrder(id_data, req_start);
                        }
                    }
                    else
                    {
                        req_start = req_start + 8;

                        if (total_row.equals(adapter_track_order.getItemCount()) || req_start > total_row)
                        {
                            timer = new CountDownTimer(3000 , 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    progress.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFinish() {
                                    progress.setVisibility(View.GONE);
                                    Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                                }
                            };

                            timer.start();
                        }
                        else if (req_start < total_item)
                        {
                            list_lens.clear();
                            showTrackOrderyByCustomer(id_data, req_start, customer_name);
                        }
                    }
                }
            }
        });
    }*/

    private void showTrackOrder(final String key, int record)
    {
        progress.setVisibility(View.VISIBLE);
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_ORDER + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                progress.setVisibility(View.GONE);
                rl_track.removeView(img_track);
                btn_next.setEnabled(true);
                String data1 = "order_number";
                String data2 = "order_entrydate";
                String data3 = "order_custname";
                String data4 = "order_reference";
                String data5 = "order_tinting_descr";
                String data6 = "order_status";
                String data7 = "order_statusdt";
                String data8 = "order_statustm";
                String data9 = "order_lensname";
                String data10= "total_row";
                String data11= "invalid";

                String data12= "order_sphr";
                String data13= "order_cylr";
                String data14= "order_addr";
                String data15= "order_sphl";
                String data16= "order_cyll";
                String data17= "order_addl";

                String data18= "order_facet";

                int start, until;

                try {
                    String detail_data1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String detail_data2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    String detail_data3 = MCrypt.bytesToHex(mCrypt.encrypt(data3));
                    String detail_data4 = MCrypt.bytesToHex(mCrypt.encrypt(data4));
                    String detail_data5 = MCrypt.bytesToHex(mCrypt.encrypt(data5));
                    String detail_data6 = MCrypt.bytesToHex(mCrypt.encrypt(data6));
                    String detail_data7 = MCrypt.bytesToHex(mCrypt.encrypt(data7));
                    String detail_data8 = MCrypt.bytesToHex(mCrypt.encrypt(data8));
                    String detail_data9 = MCrypt.bytesToHex(mCrypt.encrypt(data9));
                    String detail_data10= MCrypt.bytesToHex(mCrypt.encrypt(data10));
                    String detail_data11= MCrypt.bytesToHex(mCrypt.encrypt(data11));

                    String detail_data12= MCrypt.bytesToHex(mCrypt.encrypt(data12));
                    String detail_data13= MCrypt.bytesToHex(mCrypt.encrypt(data13));
                    String detail_data14= MCrypt.bytesToHex(mCrypt.encrypt(data14));
                    String detail_data15= MCrypt.bytesToHex(mCrypt.encrypt(data15));
                    String detail_data16= MCrypt.bytesToHex(mCrypt.encrypt(data16));
                    String detail_data17= MCrypt.bytesToHex(mCrypt.encrypt(data17));

                    String detail_data18= MCrypt.bytesToHex(mCrypt.encrypt(data18));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(detail_data11))
                            {
                                showErrorImage();
                                btn_next.setEnabled(false);
                                btn_prev.setEnabled(false);
                                String error = "0 record";
                                txt_counter.setText(error);
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                            }
                            else
                            {
                                String dt_jobnumber = new String(mCrypt.decrypt(jsonObject.getString(detail_data1)));
                                String dt_entrydate = new String(mCrypt.decrypt(jsonObject.getString(detail_data2)));
                                String dt_custname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data3)));
                                String dt_reference = new String(mCrypt.decrypt(jsonObject.getString(detail_data4)));
                                String dt_tinting   = new String(mCrypt.decrypt(jsonObject.getString(detail_data5)));
                                String dt_status    = new String(mCrypt.decrypt(jsonObject.getString(detail_data6)));
                                String dt_datestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data7)));
                                String dt_timestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data8)));
                                String dt_lensname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data9)));
                                String dt_totalrow  = new String(mCrypt.decrypt(jsonObject.getString(detail_data10)));

                                String dt_sphr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data12)));
                                String dt_cylr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data13)));
                                String dt_addr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data14)));
                                String dt_sphl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data15)));
                                String dt_cyll      = new String(mCrypt.decrypt(jsonObject.getString(detail_data16)));
                                String dt_addl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data17)));

                                String dt_facet     = new String(mCrypt.decrypt(jsonObject.getString(detail_data18)));

                                total_item = Integer.parseInt(dt_totalrow);

                                Data_track_order item = new Data_track_order();
                                item.setOrder_number(dt_jobnumber);
                                item.setOrder_entrydate(dt_entrydate);
                                item.setOrder_custname(dt_custname);
                                item.setOrder_reference(dt_reference);
                                item.setOrder_tint_descr(dt_tinting);
                                item.setOrder_status(dt_status);
                                item.setOrder_statusdate(dt_datestatus);
                                item.setOrder_statustime(dt_timestatus);
                                item.setOrder_lenscode(dt_lensname);

                                item.setOrder_sphr(dt_sphr);
                                item.setOrder_cylr(dt_cylr);
                                item.setOrder_addr(dt_addr);
                                item.setOrder_sphl(dt_sphl);
                                item.setOrder_cyll(dt_cyll);
                                item.setOrder_addl(dt_addl);

                                item.setOrder_facet(dt_facet);

                                list.add(item);

                                start = req_start + 1;
                                until = jsonArray.length() + req_start;

                                String counter = "show " + start + " - " + until + " from " + total_item + " orders";
                                txt_counter.setText(counter);

                                if (total_item.equals(until))
                                {
                                    btn_next.setEnabled(false);
                                }
                                else if (total_item == null)
                                {
                                    String error = "No record found";
                                    txt_counter.setText(error);
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
                        }

                        //adapter_track_order.notifyDataSetChanged();
                        adapter_track_orderview.notifyDataSetChanged();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                        Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG, true).show();
//                informationTrackOrder("Error connection", "Can't connect to server, press ok to reconnect ",
//                        R.drawable.failed_outline, DefaultBootstrapBrand.WARNING);
                error.printStackTrace();
                loading.dismiss();
                String notfound = "0 record";
                txt_counter.setText(notfound);
                progress.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_customer", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showTrackOrderyByCustomer(final String key, final int record, final String customer)
    {
        progress.setVisibility(View.VISIBLE);
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_CUSTNAME + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                rl_track.removeView(img_track);
                btn_next.setEnabled(true);
                loading.dismiss();

                String data1 = "order_number";
                String data2 = "order_entrydate";
                String data3 = "order_custname";
                String data4 = "order_reference";
                String data5 = "order_tinting_descr";
                String data6 = "order_status";
                String data7 = "order_statusdt";
                String data8 = "order_statustm";
                String data9 = "order_lensname";
                String data10= "total_row";
                String data11= "invalid";

                String data12= "order_sphr";
                String data13= "order_cylr";
                String data14= "order_addr";
                String data15= "order_sphl";
                String data16= "order_cyll";
                String data17= "order_addl";

                String data18= "order_facet";

                int start, until;

                try {
                    String detail_data1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String detail_data2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    String detail_data3 = MCrypt.bytesToHex(mCrypt.encrypt(data3));
                    String detail_data4 = MCrypt.bytesToHex(mCrypt.encrypt(data4));
                    String detail_data5 = MCrypt.bytesToHex(mCrypt.encrypt(data5));
                    String detail_data6 = MCrypt.bytesToHex(mCrypt.encrypt(data6));
                    String detail_data7 = MCrypt.bytesToHex(mCrypt.encrypt(data7));
                    String detail_data8 = MCrypt.bytesToHex(mCrypt.encrypt(data8));
                    String detail_data9 = MCrypt.bytesToHex(mCrypt.encrypt(data9));
                    String detail_data10= MCrypt.bytesToHex(mCrypt.encrypt(data10));
                    String detail_data11= MCrypt.bytesToHex(mCrypt.encrypt(data11));

                    String detail_data12= MCrypt.bytesToHex(mCrypt.encrypt(data12));
                    String detail_data13= MCrypt.bytesToHex(mCrypt.encrypt(data13));
                    String detail_data14= MCrypt.bytesToHex(mCrypt.encrypt(data14));
                    String detail_data15= MCrypt.bytesToHex(mCrypt.encrypt(data15));
                    String detail_data16= MCrypt.bytesToHex(mCrypt.encrypt(data16));
                    String detail_data17= MCrypt.bytesToHex(mCrypt.encrypt(data17));

                    String detail_data18= MCrypt.bytesToHex(mCrypt.encrypt(data18));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(detail_data11))
                            {
                                /*showErrorImage();
                                btn_next.setEnabled(false);
                                btn_prev.setEnabled(false);
                                String error = "0 record";
                                txt_counter.setText(error);
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();*/

                                showTrackOrderyByReference(id_data, req_start, txt_customer.getText().toString());
                            }
                            else
                            {
                                String dt_jobnumber = new String(mCrypt.decrypt(jsonObject.getString(detail_data1)));
                                String dt_entrydate = new String(mCrypt.decrypt(jsonObject.getString(detail_data2)));
                                String dt_custname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data3)));
                                String dt_reference = new String(mCrypt.decrypt(jsonObject.getString(detail_data4)));
                                String dt_tinting   = new String(mCrypt.decrypt(jsonObject.getString(detail_data5)));
                                String dt_status    = new String(mCrypt.decrypt(jsonObject.getString(detail_data6)));
                                String dt_datestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data7)));
                                String dt_timestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data8)));
                                String dt_lensname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data9)));
                                String dt_totalrow  = new String(mCrypt.decrypt(jsonObject.getString(detail_data10)));

                                String dt_sphr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data12)));
                                String dt_cylr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data13)));
                                String dt_addr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data14)));
                                String dt_sphl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data15)));
                                String dt_cyll      = new String(mCrypt.decrypt(jsonObject.getString(detail_data16)));
                                String dt_addl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data17)));

                                String dt_facet     = new String(mCrypt.decrypt(jsonObject.getString(detail_data18)));

                                total_row = Integer.parseInt(dt_totalrow);

                                Data_track_order item = new Data_track_order();
                                item.setOrder_number(dt_jobnumber);
                                item.setOrder_entrydate(dt_entrydate);
                                item.setOrder_custname(dt_custname);
                                item.setOrder_reference(dt_reference);
                                item.setOrder_tint_descr(dt_tinting);
                                item.setOrder_status(dt_status);
                                item.setOrder_statusdate(dt_datestatus);
                                item.setOrder_statustime(dt_timestatus);
                                item.setOrder_lenscode(dt_lensname);

                                item.setOrder_sphr(dt_sphr);
                                item.setOrder_cylr(dt_cylr);
                                item.setOrder_addr(dt_addr);
                                item.setOrder_sphl(dt_sphl);
                                item.setOrder_cyll(dt_cyll);
                                item.setOrder_addl(dt_addl);
                                item.setOrder_facet(dt_facet);

                                //Toasty.info(getApplicationContext(), dt_tinting, Toast.LENGTH_SHORT, true).show();

                                list.add(item);

                                start = req_start + 1;
                                until = jsonArray.length() + req_start;

                                String counter = "show " + start + " - " + until + " from " + total_row + " orders";
                                txt_counter.setText(counter);

                                if (total_row.equals(until))
                                {
                                    btn_next.setEnabled(false);
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
                        }

                        //adapter_track_order.notifyDataSetChanged();
                        adapter_track_orderview.notifyDataSetChanged();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                        Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG, true).show();
                //loading.hide();
//                informationTrackOrderByCustomer("Error connection", "Can't connect to server, press ok to reconnect ",
//                        R.drawable.failed_outline, DefaultBootstrapBrand.WARNING);
                error.printStackTrace();
                loading.dismiss();

                String notfound = "0 record";
                txt_counter.setText(notfound);
                progress.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_customer", key);
                hashMap.put("customer_name", customer);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showTrackOrderyByReference(final String key, int record, final String customer)
    {
        progress.setVisibility(View.VISIBLE);
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_REFERENCE + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                rl_track.removeView(img_track);
                btn_next.setEnabled(true);
                loading.dismiss();

                String data1 = "order_number";
                String data2 = "order_entrydate";
                String data3 = "order_custname";
                String data4 = "order_reference";
                String data5 = "order_tinting_descr";
                String data6 = "order_status";
                String data7 = "order_statusdt";
                String data8 = "order_statustm";
                String data9 = "order_lensname";
                String data10= "total_row";
                String data11= "invalid";

                String data12= "order_sphr";
                String data13= "order_cylr";
                String data14= "order_addr";
                String data15= "order_sphl";
                String data16= "order_cyll";
                String data17= "order_addl";

                String data18= "order_facet";

                int start, until;

                try {
                    String detail_data1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String detail_data2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    String detail_data3 = MCrypt.bytesToHex(mCrypt.encrypt(data3));
                    String detail_data4 = MCrypt.bytesToHex(mCrypt.encrypt(data4));
                    String detail_data5 = MCrypt.bytesToHex(mCrypt.encrypt(data5));
                    String detail_data6 = MCrypt.bytesToHex(mCrypt.encrypt(data6));
                    String detail_data7 = MCrypt.bytesToHex(mCrypt.encrypt(data7));
                    String detail_data8 = MCrypt.bytesToHex(mCrypt.encrypt(data8));
                    String detail_data9 = MCrypt.bytesToHex(mCrypt.encrypt(data9));
                    String detail_data10= MCrypt.bytesToHex(mCrypt.encrypt(data10));
                    String detail_data11= MCrypt.bytesToHex(mCrypt.encrypt(data11));

                    String detail_data12= MCrypt.bytesToHex(mCrypt.encrypt(data12));
                    String detail_data13= MCrypt.bytesToHex(mCrypt.encrypt(data13));
                    String detail_data14= MCrypt.bytesToHex(mCrypt.encrypt(data14));
                    String detail_data15= MCrypt.bytesToHex(mCrypt.encrypt(data15));
                    String detail_data16= MCrypt.bytesToHex(mCrypt.encrypt(data16));
                    String detail_data17= MCrypt.bytesToHex(mCrypt.encrypt(data17));

                    String detail_data18= MCrypt.bytesToHex(mCrypt.encrypt(data18));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(detail_data11))
                            {
                                showErrorImage();
                                btn_next.setEnabled(false);
                                btn_prev.setEnabled(false);
                                String error = "0 record";
                                txt_counter.setText(error);
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                            }
                            else
                            {
                                String dt_jobnumber = new String(mCrypt.decrypt(jsonObject.getString(detail_data1)));
                                String dt_entrydate = new String(mCrypt.decrypt(jsonObject.getString(detail_data2)));
                                String dt_custname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data3)));
                                String dt_reference = new String(mCrypt.decrypt(jsonObject.getString(detail_data4)));
                                String dt_tinting   = new String(mCrypt.decrypt(jsonObject.getString(detail_data5)));
                                String dt_status    = new String(mCrypt.decrypt(jsonObject.getString(detail_data6)));
                                String dt_datestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data7)));
                                String dt_timestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data8)));
                                String dt_lensname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data9)));
                                String dt_totalrow  = new String(mCrypt.decrypt(jsonObject.getString(detail_data10)));

                                String dt_sphr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data12)));
                                String dt_cylr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data13)));
                                String dt_addr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data14)));
                                String dt_sphl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data15)));
                                String dt_cyll      = new String(mCrypt.decrypt(jsonObject.getString(detail_data16)));
                                String dt_addl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data17)));

                                String dt_facet     = new String(mCrypt.decrypt(jsonObject.getString(detail_data18)));

                                total_row = Integer.parseInt(dt_totalrow);

                                Data_track_order item = new Data_track_order();
                                item.setOrder_number(dt_jobnumber);
                                item.setOrder_entrydate(dt_entrydate);
                                item.setOrder_custname(dt_custname);
                                item.setOrder_reference(dt_reference);
                                item.setOrder_tint_descr(dt_tinting);
                                item.setOrder_status(dt_status);
                                item.setOrder_statusdate(dt_datestatus);
                                item.setOrder_statustime(dt_timestatus);
                                item.setOrder_lenscode(dt_lensname);

                                item.setOrder_sphr(dt_sphr);
                                item.setOrder_cylr(dt_cylr);
                                item.setOrder_addr(dt_addr);
                                item.setOrder_sphl(dt_sphl);
                                item.setOrder_cyll(dt_cyll);
                                item.setOrder_addl(dt_addl);
                                item.setOrder_facet(dt_facet);

                                //Toasty.info(getApplicationContext(), dt_tinting, Toast.LENGTH_SHORT, true).show();

                                list.add(item);

                                start = req_start + 1;
                                until = jsonArray.length() + req_start;

                                String counter = "show " + start + " - " + until + " from " + total_row + " orders";
                                txt_counter.setText(counter);

                                if (total_row.equals(until))
                                {
                                    btn_next.setEnabled(false);
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
                        }

                        //adapter_track_order.notifyDataSetChanged();
                        adapter_track_orderview.notifyDataSetChanged();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                        Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG, true).show();
                //loading.hide();
//                informationTrackOrderByCustomer("Error connection", "Can't connect to server, press ok to reconnect ",
//                        R.drawable.failed_outline, DefaultBootstrapBrand.WARNING);
                error.printStackTrace();
                loading.dismiss();

                String notfound = "0 record";
                txt_counter.setText(notfound);
                progress.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_customer", key);
                hashMap.put("reference_id", customer);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showTrackOrderyByDaterange(int record, final String key, final String start_date, final String end_date)
    {
        progress.setVisibility(View.VISIBLE);
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_BYDATE + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                rl_track.removeView(img_track);
                btn_next.setEnabled(true);
                loading.dismiss();

                String data1 = "order_number";
                String data2 = "order_entrydate";
                String data3 = "order_custname";
                String data4 = "order_reference";
                String data5 = "order_tinting_descr";
                String data6 = "order_status";
                String data7 = "order_statusdt";
                String data8 = "order_statustm";
                String data9 = "order_lensname";
                String data10= "total_row";
                String data11= "invalid";

                String data12= "order_sphr";
                String data13= "order_cylr";
                String data14= "order_addr";
                String data15= "order_sphl";
                String data16= "order_cyll";
                String data17= "order_addl";

                String data18= "order_facet";

                int start, until;

                try {
                    String detail_data1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String detail_data2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    String detail_data3 = MCrypt.bytesToHex(mCrypt.encrypt(data3));
                    String detail_data4 = MCrypt.bytesToHex(mCrypt.encrypt(data4));
                    String detail_data5 = MCrypt.bytesToHex(mCrypt.encrypt(data5));
                    String detail_data6 = MCrypt.bytesToHex(mCrypt.encrypt(data6));
                    String detail_data7 = MCrypt.bytesToHex(mCrypt.encrypt(data7));
                    String detail_data8 = MCrypt.bytesToHex(mCrypt.encrypt(data8));
                    String detail_data9 = MCrypt.bytesToHex(mCrypt.encrypt(data9));
                    String detail_data10= MCrypt.bytesToHex(mCrypt.encrypt(data10));
                    String detail_data11= MCrypt.bytesToHex(mCrypt.encrypt(data11));

                    String detail_data12= MCrypt.bytesToHex(mCrypt.encrypt(data12));
                    String detail_data13= MCrypt.bytesToHex(mCrypt.encrypt(data13));
                    String detail_data14= MCrypt.bytesToHex(mCrypt.encrypt(data14));
                    String detail_data15= MCrypt.bytesToHex(mCrypt.encrypt(data15));
                    String detail_data16= MCrypt.bytesToHex(mCrypt.encrypt(data16));
                    String detail_data17= MCrypt.bytesToHex(mCrypt.encrypt(data17));

                    String detail_data18= MCrypt.bytesToHex(mCrypt.encrypt(data18));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(detail_data11))
                            {
                                showErrorImage();
                                btn_next.setEnabled(false);
                                btn_prev.setEnabled(false);
                                String error = "0 record";
                                txt_counter.setText(error);
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                            }
                            else
                            {
                                String dt_jobnumber = new String(mCrypt.decrypt(jsonObject.getString(detail_data1)));
                                String dt_entrydate = new String(mCrypt.decrypt(jsonObject.getString(detail_data2)));
                                String dt_custname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data3)));
                                String dt_reference = new String(mCrypt.decrypt(jsonObject.getString(detail_data4)));
                                String dt_tinting   = new String(mCrypt.decrypt(jsonObject.getString(detail_data5)));
                                String dt_status    = new String(mCrypt.decrypt(jsonObject.getString(detail_data6)));
                                String dt_datestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data7)));
                                String dt_timestatus= new String(mCrypt.decrypt(jsonObject.getString(detail_data8)));
                                String dt_lensname  = new String(mCrypt.decrypt(jsonObject.getString(detail_data9)));
                                String dt_totalrow  = new String(mCrypt.decrypt(jsonObject.getString(detail_data10)));

                                String dt_sphr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data12)));
                                String dt_cylr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data13)));
                                String dt_addr      = new String(mCrypt.decrypt(jsonObject.getString(detail_data14)));
                                String dt_sphl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data15)));
                                String dt_cyll      = new String(mCrypt.decrypt(jsonObject.getString(detail_data16)));
                                String dt_addl      = new String(mCrypt.decrypt(jsonObject.getString(detail_data17)));

                                String dt_facet     = new String(mCrypt.decrypt(jsonObject.getString(detail_data18)));

                                total_filter = Integer.parseInt(dt_totalrow);

                                Data_track_order item = new Data_track_order();
                                item.setOrder_number(dt_jobnumber);
                                item.setOrder_entrydate(dt_entrydate);
                                item.setOrder_custname(dt_custname);
                                item.setOrder_reference(dt_reference);
                                item.setOrder_tint_descr(dt_tinting);
                                item.setOrder_status(dt_status);
                                item.setOrder_statusdate(dt_datestatus);
                                item.setOrder_statustime(dt_timestatus);
                                item.setOrder_lenscode(dt_lensname);

                                item.setOrder_sphr(dt_sphr);
                                item.setOrder_cylr(dt_cylr);
                                item.setOrder_addr(dt_addr);
                                item.setOrder_sphl(dt_sphl);
                                item.setOrder_cyll(dt_cyll);
                                item.setOrder_addl(dt_addl);

                                item.setOrder_facet(dt_facet);

                                list.add(item);

                                start = req_start + 1;
                                until = jsonArray.length() + req_start;

                                String counter = "show " + start + " - " + until + " from " + total_filter + " orders";
                                txt_counter.setText(counter);

                                if (total_filter.equals(until))
                                {
                                    btn_next.setEnabled(false);
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
                        }

                        //adapter_track_order.notifyDataSetChanged();
                        adapter_track_orderview.notifyDataSetChanged();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                        btn_next.setEnabled(false);
                        Toasty.warning(getApplicationContext(), "No more data available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG, true).show();
                //loading.hide();
//                informationTrackOrderByDate("Error connection", "Can't connect to server, press ok to reconnect ",
//                        R.drawable.failed_outline, DefaultBootstrapBrand.WARNING);
                error.printStackTrace();
                loading.dismiss();
                String notfound = "0 record";
                txt_counter.setText(notfound);
                progress.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_customer", key);
                hashMap.put("start_date", start_date);
                hashMap.put("end_date", end_date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
