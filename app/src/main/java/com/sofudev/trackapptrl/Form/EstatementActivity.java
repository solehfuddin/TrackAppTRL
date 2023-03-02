package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_child_estatement;
import com.sofudev.trackapptrl.Adapter.Adapter_sortbycategory;
import com.sofudev.trackapptrl.Adapter.Adapter_summary_estatement;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CounterData;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_child_estatement;
import com.sofudev.trackapptrl.Data.Data_filter_estatement;
import com.sofudev.trackapptrl.Fragment.EstatementItemFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

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
import ru.slybeaver.slycalendarview.SlyCalendarDialog;


public class EstatementActivity extends AppCompatActivity implements CounterData {

    private Config config = new Config();
    private String URLCATEGORYSTATEMENT = config.Ip_address + config.estatement_getcategory;
    private String URLCATEGORYSTATEMENTRANGE = config.Ip_address + config.estatement_getcategoryrange;
    private String URLSUMMARYLAST = config.Ip_address + config.estatement_getsummarylast;
    private String URLDETAILSUMMARY = config.Ip_address + config.estatement_getdetailsummary;

    SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    View decorView;
    RecyclerView recyclerView;
    ImageView imgCalendar, imgNotfound;
    private UniversalFontTextView txtAwal, txtAkhir, txtTitle;
    private TextView txtMonAwal, txtMonAkhir, txtYearAwal, txtYearAkhir;
    ImageButton btnBack, btnMore;
    RippleView btnFilter, btnExport;

    Adapter_sortbycategory adapter_sortbycategory;
    List<String> listSortBy = new ArrayList<>();
    Adapter_summary_estatement adapter_summary_estatement;
    private List<Data_child_estatement> item = new ArrayList<>();

    DateFormat customDateFormat;
    private ViewStatePagerAdapter stateAdapter;
    private View custom;
    String opticname, totalsummary;

    String username = "";
    String tmpOpticname = "";
    String divisi   = "";
    String filterby = "All Data";
    String stDateRange = "";
    String edDateRange = "";
    int pos = 0;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatement);

        btnBack     = findViewById(R.id.form_estatement_btnback);
        btnMore     = findViewById(R.id.form_estatement_btnmore);
        btnFilter   = findViewById(R.id.form_estatement_rplfilter);
        btnExport   = findViewById(R.id.form_estatement_rplexport);
        imgCalendar = findViewById(R.id.form_estatement_imgcalendar);
        txtAwal     = findViewById(R.id.form_estatement_txtTanggalAwal);
        txtAkhir    = findViewById(R.id.form_estatement_txtTanggalAkhir);
        txtTitle    = findViewById(R.id.form_estatement_txtTitle);
        txtMonAwal  = findViewById(R.id.txtMonAwal);
        txtMonAkhir = findViewById(R.id.txtMonAkhir);
        txtYearAwal = findViewById(R.id.txtYearAwal);
        txtYearAkhir= findViewById(R.id.txtYearAkhir);
//        txtOpticName = findViewById(R.id.form_estatement_txtopticname);
//        txtTotalSummary = findViewById(R.id.form_estatement_txtsummarycontent);
        tabLayout   = findViewById(R.id.form_estatement_slidinglayout);

        customDateFormat = new DateFormat();
        decorView = getWindow().getDecorView();
        viewPager = ViewFindUtils.find(decorView, R.id.form_estatement_viewpager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            tmpOpticname = bundle.getString("opticname");
            getSummary(username);


//            getDetailSummary(username);
//            Toasty.info(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
        }

        stateAdapter = new ViewStatePagerAdapter(getSupportFragmentManager());
        setDateNow();
        dialogCalendar();

//        stateAdapter.addFragment(new EstatementItemFragment(),
//                new Data_filter_estatement(filterby, username, txtMonAwal.getText().toString(), txtMonAkhir.getText().toString(),
//                        txtYearAwal.getText().toString(), divisi));

        stateAdapter.addFragment(new EstatementItemFragment(),
                new Data_filter_estatement(filterby, username, stDateRange, edDateRange,
                        txtYearAwal.getText().toString(), divisi));
        viewPager.setAdapter(stateAdapter);
        tabLayout.setViewPager(viewPager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle.setText(filterby);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom = LayoutInflater.from(EstatementActivity.this).inflate(R.layout.bottom_dialog_estatement, null);
                BottomDialog bottomDialog = new BottomDialog.Builder(EstatementActivity.this)
                        .setTitle("Summary Statement")
                        .setCustomView(custom)
                        .build();

                UniversalFontTextView txtOpticName = custom.findViewById(R.id.form_estatement_txtopticname);
                UniversalFontTextView txtTotalSummary = custom.findViewById(R.id.form_estatement_txtsummarycontent);
                recyclerView = custom.findViewById(R.id.bottom_dialog_estatement_recyclerview);
                imgNotfound  = custom.findViewById(R.id.bottom_dialog_estatement_imgnotfound);

                getDetailSummary(username);

                LinearLayoutManager lm = new LinearLayoutManager(EstatementActivity.this, LinearLayoutManager.VERTICAL, false);
                adapter_summary_estatement = new Adapter_summary_estatement(EstatementActivity.this, item, new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
                    }
                });

                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(adapter_summary_estatement);

                txtOpticName.setText(opticname);
                txtTotalSummary.setText(CurencyFormat(totalsummary));

                bottomDialog.show();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getCategory(username, txtMonAwal.getText().toString(), txtMonAkhir.getText().toString(),
//                        txtYearAwal.getText().toString());
                getCategoryRange(username, stDateRange, edDateRange);
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasty.info(getApplicationContext(), String.valueOf(total), Toast.LENGTH_SHORT).show();
                if (total > 0)
                {
                    String link = "http://180.250.96.154/trl-webs/index.php/PrintReceipt/EstatementNew/" + username + "/"
                            + stDateRange + "/" + edDateRange +"/" + txtYearAwal.getText().toString();
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(openBrowser);
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void dialogSorting(List<String> allSort) {
        if(getApplicationContext() != null){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            listSortBy.clear();
            listSortBy.addAll(allSort);

            adapter_sortbycategory = new Adapter_sortbycategory(getApplicationContext(), listSortBy, pos);

            dialog.setContentView(R.layout.dialog_sort);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(true);

            final ListView lvSortBy = dialog.findViewById(R.id.dialog_sort_listview);
            final ImageView imgNotFound = dialog.findViewById(R.id.dialog_sort_imgLensnotfound);

            if (allSort.size() > 0)
            {
                lvSortBy.setVisibility(View.VISIBLE);
                imgNotFound.setVisibility(View.GONE);
            }
            else
            {
                lvSortBy.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
            }

            lvSortBy.setAdapter(adapter_sortbycategory);

            lvSortBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    stateAdapter.removeAllFragment();
                    String chooser = listSortBy.get(i);
                    pos = i;

                    if (pos == 0)
                    {
                        divisi = "";
                        filterby = "All Data";
                    }
                    else {
                        divisi = chooser;
                        filterby = chooser.replace("Lensa", "").replace(":", "");
                    }

                    txtTitle.setText(filterby);

//                    Toasty.info(getApplicationContext(), divisi, Toast.LENGTH_SHORT).show();
                    if (viewPager != null && viewPager.getAdapter() != null)
                    {
                        stateAdapter.addFragment(new EstatementItemFragment(),
                                new Data_filter_estatement(filterby, username, stDateRange, edDateRange,
                                        txtYearAwal.getText().toString(), divisi));
                        viewPager.getAdapter().notifyDataSetChanged();
                    }

                    dialog.dismiss();
                }
            });

            if (!isFinishing())
            {
                dialog.show();
            }
        }
    }

    private void setDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat, dateFormat1, dateFormatRange, dateFormatRange1;

        String format = "dd-MM-yyyy";
        String format1 = "MM-yyyy";
        String formatRange = "yyyy-MM-dd";
        String formatRange1 = "yyyy-MM";
        String monAwal, monAkhir, yearAwal, yearAkhir;

        String date, date1;
        dateFormat = new SimpleDateFormat(format);
        dateFormat1 = new SimpleDateFormat(format1);
        dateFormatRange = new SimpleDateFormat(formatRange);
        dateFormatRange1 = new SimpleDateFormat(formatRange1);
        date = dateFormat.format(calendar.getTime());
        date1 = "01-" + dateFormat1.format(calendar.getTime());

        edDateRange = dateFormatRange.format(calendar.getTime());
        stDateRange = dateFormatRange1.format(calendar.getTime()) + "-01";

        String[] temp = date1.split("-");
        String[] temp1 = date.split("-");
        monAwal = temp[1];
        monAkhir = temp1[1];
        yearAwal = temp[2];
        yearAkhir = temp1[2];

        txtMonAwal.setText(monAwal);
        txtMonAkhir.setText(monAkhir);
        txtYearAwal.setText(yearAwal);
        txtYearAkhir.setText(yearAkhir);

        Log.d("Bulan awal ", txtMonAwal.getText().toString());
        Log.d("Bulan akhir ", txtMonAkhir.getText().toString());
        Log.d("Tahun awal ", txtYearAwal.getText().toString());
        Log.d("Tahun akhir ", txtYearAkhir.getText().toString());

        txtAwal.setText(customDateFormat.ValueUserDate(Integer.valueOf(temp[0]),
                Integer.valueOf(temp[1]) - 1, Integer.valueOf(temp[2])));
        txtAkhir.setText(customDateFormat.ValueUserDate(Integer.valueOf(temp1[0]),
                Integer.valueOf(temp1[1]) - 1, Integer.valueOf(temp1[2])));

        Log.d("Initialize date : ", date1 + " / " + date);
        Log.d("Initialize daterange : ", stDateRange + " / " + edDateRange);
    }

    private void getMonYear(String dateAwal, String dateAkhir) {
        String monAwal, monAkhir, yearAwal, yearAkhir;
        String[] temp = dateAwal.split("-");
        String[] temp1 = dateAkhir.split("-");
        monAwal = temp[1];
        monAkhir= temp1[1];
        yearAwal= temp[2];
        yearAkhir= temp1[2];

        txtMonAwal.setText(monAwal);
        txtMonAkhir.setText(monAkhir);
        txtYearAwal.setText(yearAwal);
        txtYearAkhir.setText(yearAkhir);

        if (viewPager != null && viewPager.getAdapter() != null)
        {
            stateAdapter.addFragment(new EstatementItemFragment(),
                    new Data_filter_estatement(filterby, username, stDateRange, edDateRange,
                            txtYearAwal.getText().toString(), divisi));
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private void dialogCalendar() {
        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(new SlyCalendarDialog.Callback() {
                            @Override
                            public void onCancelled() {

                            }

                            @Override
                            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                                if (firstDate != null)
                                {
                                    if (secondDate != null)
                                    {
                                        pos = 0;
                                        divisi = "";
                                        filterby = "All Data";
                                        stateAdapter.removeAllFragment();
//                                mAdapter.removeAllFragment();

                                        txtAwal.setText("");
                                        txtAkhir.setText("");
                                        firstDate.set(Calendar.HOUR_OF_DAY, hours);
                                        firstDate.set(Calendar.MINUTE, minutes);
                                        secondDate.set(Calendar.HOUR_OF_DAY, hours);
                                        secondDate.set(Calendar.MINUTE, minutes);
//                                Toast.makeText(getApplicationContext(),
//                                        getString(
//                                                R.string.period,
//                                                new SimpleDateFormat(getString(R.string.customDateStatement),
//                                                        Locale.getDefault()).format(firstDate.getTime()),
//                                                new SimpleDateFormat(getString(R.string.customDateStatement),
//                                                        Locale.getDefault()).format(secondDate.getTime())
//                                        ), Toast.LENGTH_LONG).show();

                                        String date1 = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                                Locale.getDefault()).format(firstDate.getTime());
                                        String date = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                                Locale.getDefault()).format(secondDate.getTime());

                                        stDateRange = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(firstDate.getTime());
                                        edDateRange = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(secondDate.getTime());

                                        String[] monAwal = date1.split("-");
                                        String[] monAkhir = date.split("-");

                                        txtAwal.setText(customDateFormat.ValueUserDate(Integer.valueOf(monAwal[0]),
                                                Integer.valueOf(monAwal[1]) - 1, Integer.valueOf(monAwal[2])));
                                        txtAkhir.setText(customDateFormat.ValueUserDate(Integer.valueOf(monAkhir[0]),
                                                Integer.valueOf(monAkhir[1]) - 1, Integer.valueOf(monAkhir[2])));

//                                txtAwal.setText(date);
//                                txtAkhir.setText(date1);

                                        Log.d("Choose date : ", date1 + " / " + date);
                                        Log.d("Choose daterange : ", stDateRange + " / " + edDateRange);
                                        getMonYear(date1, date);
                                    }
                                    else
                                    {
                                        Toasty.error(getApplicationContext(), "Harap pilih tanggal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toasty.error(getApplicationContext(), "Harap pilih tanggal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                        .setSelectedTextColor(Color.parseColor("#93c6fd"))
                        .setSelectedColor(Color.parseColor("#3395ff"))
                        .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
            }
        });
    }

    private void getCategory(final String username, final String dtAwal, final String dtAkhir, final String tahun) {
        StringRequest request = new StringRequest(Request.Method.POST, URLCATEGORYSTATEMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> data = new ArrayList<>();

                    data.add("All Data :");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            Log.d("Invalid", "Data not found");
                        }
                        else
                        {
                            JSONArray dataArray = object.getJSONArray("data");

                            for (int j = 0; j < dataArray.length(); j++) {
                                JSONObject obj = dataArray.getJSONObject(j);

                                Log.d("Divisi", obj.getString("divisi"));
                                data.add(obj.getString("divisi"));
                            }
                        }
                    }

                    dialogSorting(data);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("startmon", dtAwal);
                map.put("endmon", dtAkhir);
                map.put("year", tahun);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getCategoryRange(final String username, final String dtAwal, final String dtAkhir) {
        StringRequest request = new StringRequest(Request.Method.POST, URLCATEGORYSTATEMENTRANGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> data = new ArrayList<>();

                    data.add("All Data :");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            Log.d("Invalid", "Data not found");
                        }
                        else
                        {
                            JSONArray dataArray = object.getJSONArray("data");

                            for (int j = 0; j < dataArray.length(); j++) {
                                JSONObject obj = dataArray.getJSONObject(j);

                                Log.d("Divisi", obj.getString("divisi"));
                                data.add(obj.getString("divisi"));
                            }
                        }
                    }

                    dialogSorting(data);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("start_date", dtAwal);
                map.put("end_date", dtAkhir);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSummary(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSUMMARYLAST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("invalid"))
                    {
//                        Toasty.info(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        opticname = tmpOpticname;
                        totalsummary = "0";
                    }
                    else
                    {
                        opticname = jsonObject.getString("nama_optik");
                        totalsummary = jsonObject.getString("total_outstanding");
                        Log.d("Total Outstanding : ", jsonObject.getString("total_outstanding"));
                    }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDetailSummary(final String username)
    {
        item.clear();
        imgNotfound.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, URLDETAILSUMMARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            Log.d("Detail Summary : ", "Data tidak ditemukan");
                            imgNotfound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            JSONArray jsonArray1 = object.getJSONArray("data");
                            imgNotfound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int j = 0; j < jsonArray1.length(); j++)
                            {
                                JSONObject data = jsonArray1.getJSONObject(j);

                                Data_child_estatement child = new Data_child_estatement(
                                        data.getString("child_no"),
                                        data.getString("tahun"),
                                        data.getString("outstanding")
                                );

                                item.add(child);
                                Log.d("Detail Summary : ", data.getString("tahun"));
                            }
                        }
                    }

                    adapter_summary_estatement.notifyDataSetChanged();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void counterData(int size) {
        total =size;
    }

    private class ViewStatePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Data_filter_estatement> mFilterList = new ArrayList<>();

        private ViewStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return EstatementItemFragment.newInstance(mFilterList.get(i));
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
    }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFilterList.get(position).getFiltername();
        }

        private void addFragment(Fragment fragment, Data_filter_estatement item) {
            mFragmentList.add(fragment);
            mFilterList.add(item);
        }

        private void removeAllFragment() {
            mFragmentList.clear();
            mFilterList.clear();
        }
    }
}
