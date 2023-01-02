package com.sofudev.trackapptrl.Form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_courier_filter;
import com.sofudev.trackapptrl.Fragment.CategoryFragment;
import com.sofudev.trackapptrl.Fragment.CourierTrackingFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormCourierTrackingActivity extends AppCompatActivity {

    Config config = new Config();
    String URLCOUNTERSTATUS = config.Ip_address + config.dpodk_statuscounterbyoptic;
    String URLCOUNTERPROCESS= config.Ip_address + config.dpodk_processcounterbyoptic;
    String URLCOUNTERDELIVERED = config.Ip_address + config.dpodk_deliveredcounterbyoptic;

    SlidingTabLayout tabLayout;
    UniversalFontTextView txtCounter;
    RippleView btnBack, btnFilter, rp_filterdate;
    Button btn_filterdate;
    MaterialEditText txt_startdate, txt_enddate;
    ProgressBar progressBar;
    ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    View decorView;

    String username = "";
    int sTotal = 0;
    int sProcess = 0;
    int sHistory = 0;

    DateFormat customDateFormat;
    String stDateProcess, edDateProcess, start_date, end_date;
    String stDateHistory, edDateHistory;

    Calendar calendar;
    long lastClick = 0;
    int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_courier_tracking);

        txtCounter = (UniversalFontTextView) findViewById(R.id.form_couriertrack_txtcounter);
        btnBack    = (RippleView) findViewById(R.id.form_trackcourier_ripplebtnback);
        btnFilter  = (RippleView) findViewById(R.id.form_trackcourier_ripplebtnfilter);
        progressBar= (ProgressBar) findViewById(R.id.form_couriertrack_progressbar);
        tabLayout  = (SlidingTabLayout) findViewById(R.id.form_couriertrack_slidingtab);
        viewPager  = (ViewPager) findViewById(R.id.form_deliverytrack_viewpager);

        decorView  = getWindow().getDecorView();
        viewPager  = ViewFindUtils.find(decorView, R.id.form_couriertrack_viewpager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customDateFormat = new DateFormat();
        setDateNow();
        ClickFilter();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            Log.d(FormCourierTrackingActivity.class.getSimpleName(), "Username : " + username);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void setDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat, dateFormat1;

        String format = "dd-MM-yyyy";
        String format1 = "dd-MM-yyyy";

        String date, date1;
        dateFormat = new SimpleDateFormat(format);
        dateFormat1 = new SimpleDateFormat(format1);
        date = dateFormat.format(calendar.getTime());
        date1 = dateFormat1.format(calendar.getTime());

        String[] temp = date1.split("-");
        String[] temp1 = date.split("-");

        int dayStProcess = totalDay(Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
        int dayStProcOut = 0;
        int monStProcOut = 0;

        if (Integer.parseInt(temp[0]) <= 3)
        {
            dayStProcOut = Integer.parseInt(temp[0]) + dayStProcess - 3;
            monStProcOut = Integer.parseInt(temp[1]) - 2;
        }
        else
        {
            dayStProcOut = Integer.parseInt(temp[0]) - 3;
            monStProcOut = Integer.parseInt(temp[1]) - 1;
        }

        stDateProcess = customDateFormat.ValueDbDate(dayStProcOut,
                monStProcOut, Integer.parseInt(temp[2]));
        edDateProcess = customDateFormat.ValueDbDate(Integer.parseInt(temp1[0]),
                Integer.parseInt(temp1[1]) - 1, Integer.parseInt(temp1[2]));

        stDateHistory = customDateFormat.ValueDbDate(Integer.parseInt(temp[0]),
                Integer.parseInt(temp[1]) - 2, Integer.parseInt(temp[2]));
        edDateHistory = customDateFormat.ValueDbDate(Integer.parseInt(temp1[0]),
                Integer.parseInt(temp1[1]) - 1, Integer.parseInt(temp1[2]));

        Log.d("Total Process stdate : ", String.valueOf(dayStProcess));
        Log.d("Process date : ", stDateProcess + " / " + edDateProcess);
        Log.d("History date : ", stDateHistory + " / " + edDateHistory);

//        countCourierProcess(stDateProcess, edDateProcess);
        countProcess();
    }

    private int totalDay(int mon, int year){
        Log.d("Input mon : ", String.valueOf(mon));
        Log.d("Input year : ", String.valueOf(year));
        int total = 0;
        if (year % 4 == 0 && mon == 2)
        {
            total = 29;
        }
        else
        {
            if (mon == 2)
            {
                total = 28;
            }
            else if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
            {
                total = 30;
            }
            else
            {
                total = 31;
            }
        }

        return total;
    }

    private void ClickFilter(){
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(FormCourierTrackingActivity.this);
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
                            countCourierHistory(start_date, end_date);

                            stDateHistory = start_date;
                            edDateHistory = end_date;
                            setupPager();
                        }
                    }
                });
            }
        });
    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormCourierTrackingActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormCourierTrackingActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void countCourierProcess(final String stDate, final String edDate){
//        Log.d("Process optik : ", username);
//        Log.d("Process stDate: ", stDate);
//        Log.d("Process edDate: ", edDate);
        StringRequest request = new StringRequest(Request.Method.POST, URLCOUNTERSTATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormCourierTrackingActivity.class.getSimpleName(), "Output proses : " + response);

                    sProcess = jsonObject.getInt("totalrow");

                    countCourierHistory(stDateHistory, edDateHistory);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("optic", username);
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void countProcess(){
        Log.d("Process optik : ", username);
        StringRequest request = new StringRequest(Request.Method.POST, URLCOUNTERPROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormCourierTrackingActivity.class.getSimpleName(), "Output proses : " + response);

                    sProcess = jsonObject.getInt("totalrow");

                    countCourierHistory(stDateHistory, edDateHistory);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("optic", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void countCourierHistory(final String stDate, final String edDate){
//        Log.d("History optik : ", username);
//        Log.d("History stDate: ", stDate);
//        Log.d("History edDate: ", edDate);
        StringRequest request = new StringRequest(Request.Method.POST, URLCOUNTERDELIVERED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormCourierTrackingActivity.class.getSimpleName(), "Output history : " + response);

                    sHistory = jsonObject.getInt("totalrow");

                    sTotal = sHistory + sProcess;
                    txtCounter.setText(String.valueOf(sTotal));
                    setupPager();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("optic", username);
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void setupPager(){
        pagerAdapter.removeAllFragment();
        pagerAdapter.addFragment(new CourierTrackingFragment(), new Data_courier_filter("On process (" + sProcess + ")", username, stDateProcess, edDateProcess, 0));
        pagerAdapter.addFragment(new CourierTrackingFragment(), new Data_courier_filter("Delivered (" + sHistory + ")", username, stDateHistory, edDateHistory,1));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        if (viewPager != null && viewPager.getAdapter() != null)
        {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private List<Data_courier_filter> item = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return CourierTrackingFragment.newInstance(item.get(position));
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return item.get(position).getTitle();
        }

        private void addFragment(Fragment fragment, Data_courier_filter data_courier_filter){
            fragmentList.add(fragment);
            item.add(data_courier_filter);
        }

        private void removeAllFragment(){
            fragmentList.clear();
            item.clear();
        }
    }
}