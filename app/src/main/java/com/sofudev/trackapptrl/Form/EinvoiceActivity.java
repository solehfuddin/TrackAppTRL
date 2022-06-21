package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_einvoice;
import com.sofudev.trackapptrl.Adapter.Adapter_sortbycategory;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_einvoice;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class EinvoiceActivity extends AppCompatActivity {

    private Config config = new Config();
    private String TAG = EinvoiceActivity.class.getSimpleName();
    private String URLGETDATA = config.Ip_address + config.einvoice_getdata;
    private String URLGETCATEGORY = config.Ip_address + config.einvoice_getcategory;
    private String URLGETDATAPAID = config.Ip_address + config.einvoicepaid_getdata;
    private String URLGETCATEGORYPAID = config.Ip_address + config.einvoicepaid_getcategory;
    private String username;

    UniversalFontTextView txtOpticName, txtPeriod, txtCounter, txtTitle;
    RippleView rpBack, rpSetting, rpFilter, rpExport;
    RecyclerView recyclerView;
    ShimmerRecyclerView shimmer;
    ImageView imgNotFound;
    BootstrapButton btnNext, btnPrev;

    Adapter_einvoice adapter_einvoice;
    Adapter_sortbycategory adapter_sortbycategory;
    List<Data_einvoice> data_einvoices = new ArrayList<>();
    List<String> listSortBy = new ArrayList<>();
    DateFormat customDateFormat;
    String tgAwal, tgAkhir, awal, akhir;
    String divisi   = "";
    String filterby = "All Data";
    Boolean isPaid = false;
    int limiter, pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniversalFontComponents.init(this);
       // TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_einvoice);

        txtTitle = findViewById(R.id.form_einvoice_txtTitle);
        txtOpticName = findViewById(R.id.form_einvoice_txtopticname);
        txtPeriod   = findViewById(R.id.form_einvoice_txtPeriod);
        txtCounter  = findViewById(R.id.form_einvoice_txtCounter);
        rpBack = findViewById(R.id.form_einvoice_ripplebtnback);
        rpSetting = findViewById(R.id.form_einvoice_ripplebtnsetting);
        rpExport = findViewById(R.id.form_einvoice_ripplebtndownload);
        rpFilter = findViewById(R.id.form_einvoice_ripplebtnfilter);
        recyclerView = findViewById(R.id.form_einvoice_recyclerview);
        shimmer = findViewById(R.id.form_einvoice_shimmerview);
        imgNotFound = findViewById(R.id.form_einvoice_imgLensnotfound);
        btnPrev = findViewById(R.id.form_einvoice_btnprev);
        btnNext = findViewById(R.id.form_einvoice_btnnext);

        shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
        shimmer.showShimmerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        customDateFormat = new DateFormat();
        adapter_einvoice = new Adapter_einvoice(this, data_einvoices, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Intent intent = new Intent(getApplicationContext(), DetailEinvoiceActivity.class);
                intent.putExtra("INVNUMBER", id);
                intent.putExtra("ispaid", isPaid);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter_einvoice);

        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        imgNotFound.setVisibility(View.GONE);

        getDataIntent();
        setDateNow();

        rpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rpSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogFilter();
            }
        });
        rpExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogExport();
            }
        });

        rpFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategory(username, awal, akhir);
            }
        });

        if(isPaid){
            txtTitle.setText("Einvoice Lunas");
        }else {
            txtTitle.setText("Einvoice");
        }

        limiter = 0;
        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limiter -= 10;
                getInvoice(username, awal, akhir, divisi, String.valueOf(limiter));

                shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
                shimmer.showShimmerAdapter();

                shimmer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.GONE);

                adapter_einvoice.notifyDataSetChanged();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limiter += 10;
                getInvoice(username, awal, akhir, divisi, String.valueOf(limiter));

                shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
                shimmer.showShimmerAdapter();

                shimmer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.GONE);

                adapter_einvoice.notifyDataSetChanged();
            }
        });
    }

    private void getDataIntent()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            username = bundle.getString("username");
            String opticname = bundle.getString("custname");
            isPaid = bundle.getBoolean("ispaid", false);

            txtOpticName.setText(opticname);

            Log.d(TAG, "USERNAME : " + username);
            Log.d(TAG, "OPTICNAME : " + opticname);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void setDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat, dateFormat1;

        String format = "dd-MM-yyyy";
        String format1 = "MM-yyyy";

        String date, date1;
        dateFormat = new SimpleDateFormat(format);
        dateFormat1 = new SimpleDateFormat(format1);
        date = dateFormat.format(calendar.getTime());
        date1 = "01-" + dateFormat1.format(calendar.getTime());

        String[] temp = date1.split("-");
        String[] temp1 = date.split("-");

        tgAwal = customDateFormat.ValueUserDate(Integer.parseInt(temp[0]),
                Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
        tgAkhir = customDateFormat.ValueUserDate(Integer.parseInt(temp1[0]),
                Integer.parseInt(temp1[1]) - 1, Integer.parseInt(temp1[2]));

        awal = customDateFormat.ValueDbDate(Integer.parseInt(temp[0]),
                Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
        akhir = customDateFormat.ValueDbDate(Integer.parseInt(temp1[0]),
                Integer.parseInt(temp1[1]) - 1, Integer.parseInt(temp1[2]));

        Log.d("Initialize date : ", date1 + " / " + date);
        Log.d("Cek db date : ", awal + " / " + akhir);

        String periode = "Periode " + tgAwal + " s/d " + tgAkhir;
        txtPeriod.setText(periode);

        divisi = "";
        getInvoice(username, awal, akhir, divisi, String.valueOf(limiter));
    }

    private void dialogFilter(){
        limiter = 0;
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
                                txtPeriod.setText("");
                                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                                firstDate.set(Calendar.MINUTE, minutes);
                                secondDate.set(Calendar.HOUR_OF_DAY, hours);
                                secondDate.set(Calendar.MINUTE, minutes);

                                String date1 = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                        Locale.getDefault()).format(firstDate.getTime());
                                String date = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                        Locale.getDefault()).format(secondDate.getTime());

                                String[] monAwal = date1.split("-");
                                String[] monAkhir = date.split("-");

                                tgAwal = customDateFormat.ValueUserDate(Integer.parseInt(monAwal[0]),
                                        Integer.parseInt(monAwal[1]) - 1, Integer.parseInt(monAwal[2]));
                                tgAkhir = customDateFormat.ValueUserDate(Integer.parseInt(monAkhir[0]),
                                        Integer.parseInt(monAkhir[1]) - 1, Integer.parseInt(monAkhir[2]));

                                awal = customDateFormat.ValueDbDate(Integer.parseInt(monAwal[0]),
                                        Integer.parseInt(monAwal[1]) - 1, Integer.parseInt(monAwal[2]));
                                akhir = customDateFormat.ValueDbDate(Integer.parseInt(monAkhir[0]),
                                        Integer.parseInt(monAkhir[1]) - 1, Integer.parseInt(monAkhir[2]));

                                Log.d("Choose date : ", date1 + " / " + date);
                                Log.d("Cek db date : ", awal + " / " + akhir);

                                String periode = "Periode " + tgAwal + " s/d " + tgAkhir;
                                txtPeriod.setText(periode);

                                shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
                                shimmer.showShimmerAdapter();

                                shimmer.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                imgNotFound.setVisibility(View.GONE);
                                getInvoice(username, awal, akhir, divisi, String.valueOf(limiter));
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

    private void dialogExport(){
        limiter = 0;
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
                                txtPeriod.setText("");
                                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                                firstDate.set(Calendar.MINUTE, minutes);
                                secondDate.set(Calendar.HOUR_OF_DAY, hours);
                                secondDate.set(Calendar.MINUTE, minutes);

                                String date1 = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                        Locale.getDefault()).format(firstDate.getTime());
                                String date = new SimpleDateFormat(getString(R.string.titleDateStatement),
                                        Locale.getDefault()).format(secondDate.getTime());

                                String[] monAwal = date1.split("-");
                                String[] monAkhir = date.split("-");

                                tgAwal = customDateFormat.ValueUserDate(Integer.parseInt(monAwal[0]),
                                        Integer.parseInt(monAwal[1]) - 1, Integer.parseInt(monAwal[2]));
                                tgAkhir = customDateFormat.ValueUserDate(Integer.parseInt(monAkhir[0]),
                                        Integer.parseInt(monAkhir[1]) - 1, Integer.parseInt(monAkhir[2]));

                                awal = customDateFormat.ValueDbDate(Integer.parseInt(monAwal[0]),
                                        Integer.parseInt(monAwal[1]) - 1, Integer.parseInt(monAwal[2]));
                                akhir = customDateFormat.ValueDbDate(Integer.parseInt(monAkhir[0]),
                                        Integer.parseInt(monAkhir[1]) - 1, Integer.parseInt(monAkhir[2]));

                                Log.d("Choose date : ", date1 + " / " + date);
                                Log.d("Cek db date : ", awal + " / " + akhir);
                                checkInvoice(username, awal, akhir, divisi, String.valueOf(limiter));
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

    private void dialogSorting(List<String> allSort) {
        if(getApplicationContext() != null){
            limiter = 0;
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

                    shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
                    shimmer.showShimmerAdapter();

                    shimmer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    imgNotFound.setVisibility(View.GONE);

                    getInvoice(username, awal, akhir, divisi, String.valueOf(limiter));
                    dialog.dismiss();
                }
            });

            if (!isFinishing())
            {
                dialog.show();
            }
        }
    }

    private void getInvoice(final String username, final String awal, final String akhir, final String div, final String limit) {
        data_einvoices.clear();
        Log.d(EinvoiceActivity.class.getSimpleName(), "Username : " + username);
        Log.d(EinvoiceActivity.class.getSimpleName(), "Awal : " + awal);
        Log.d(EinvoiceActivity.class.getSimpleName(), "akhir : " + akhir);
        Log.d(EinvoiceActivity.class.getSimpleName(), "div : " + div);
        Log.d(EinvoiceActivity.class.getSimpleName(), "limit : " + limit);

        String url;
        if(isPaid){
            url = URLGETDATAPAID;
        }else {
            url = URLGETDATA;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    boolean isDataFound = false;

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            Log.d("Invalid", "Data not found");

                            String output = "Data not found";
                            txtCounter.setText(output);

                            isDataFound = false;
                        }
                        else
                        {
                            JSONArray dataArr = object.getJSONArray("data");

                            String totalRec = object.getString("total_row");
                            String startRec = object.getString("start");
                            String untilRec = object.getString("until");

                            String output = "Show " + startRec + " - " + untilRec + " from " + totalRec + " data";
                            txtCounter.setText(output);

                            boolean isPrev = object.getBoolean("prev");
                            boolean isNext = object.getBoolean("next");

                            if (isPrev) {
                                btnPrev.setEnabled(true);
                            } else {
                                btnPrev.setEnabled(false);
                            }

                            if (isNext) {
                                btnNext.setEnabled(true);
                            } else {
                                btnNext.setEnabled(false);
                            }

                            isDataFound = true;

                            for (int j = 0; j < dataArr.length(); j++)
                            {
                                JSONObject obj = dataArr.getJSONObject(j);
                                Data_einvoice item = new Data_einvoice();

                                if(isPaid){
                                    item.setInv_category(obj.getString("product_div"));
                                    item.setInv_date(obj.getString("invoice_date"));
                                    item.setInv_number(obj.getString("inv_number"));
                                    item.setSales_name(obj.getString("username").trim());
                                    item.setInv_totalprice(obj.getInt("ammount_inv"));
                                }else {
                                    item.setInv_category(obj.getString("name"));
                                    item.setInv_date(obj.getString("inv_date"));
                                    item.setInv_number(obj.getString("trx_number"));
                                    item.setSales_name(obj.getString("sales_person").trim());
                                    item.setInv_totalprice(obj.getInt("total_price"));
                                }

                                data_einvoices.add(item);
                            }
                        }
                    }

                    if (isDataFound){
                        shimmer.hideShimmerAdapter();
                        shimmer.setVisibility(View.GONE);
                        imgNotFound.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        imgNotFound.setVisibility(View.VISIBLE);
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);

                        btnPrev.setEnabled(false);
                        btnNext.setEnabled(false);
                    }

                    adapter_einvoice.notifyDataSetChanged();
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
                map.put("username", username);
                map.put("startmon", awal + " 00:00:00");
                map.put("endmon", akhir + " 23:59:59");
                map.put("divisi", div);
                map.put("limit", limit);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void checkInvoice(final String username, final String awal, final String akhir, final String div, final String limit) {
        data_einvoices.clear();
        Log.d(EinvoiceActivity.class.getSimpleName(), "Username : " + username);
        Log.d(EinvoiceActivity.class.getSimpleName(), "Awal : " + awal);
        Log.d(EinvoiceActivity.class.getSimpleName(), "akhir : " + akhir);
        Log.d(EinvoiceActivity.class.getSimpleName(), "div : " + div);
        Log.d(EinvoiceActivity.class.getSimpleName(), "limit : " + limit);

        String url;
        if(isPaid){
            url = URLGETDATAPAID;
        }else {
            url = URLGETDATA;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    boolean isDataFound = false;

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Proses download excel
                            Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_SHORT).show();
                            String link;

                            if (isPaid){
                                link = "https://timurrayalab.com/download/mobExcelPaid?child="+ username + "&trx_number=&datefrom=" + awal + "&dateto=" + akhir;
                            }
                            else{
                                link = "https://timurrayalab.com/download/mobExcel?child="+ username + "&trx_number=&datefrom=" + awal + "&dateto=" + akhir;
                            }

                            Log.i(TAG, "Url : " + link);
                            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            startActivity(openBrowser);

                            JSONArray dataArr = object.getJSONArray("data");

                            String totalRec = object.getString("total_row");
                            String startRec = object.getString("start");
                            String untilRec = object.getString("until");

                            String output = "Show " + startRec + " - " + untilRec + " from " + totalRec + " data";
                            txtCounter.setText(output);

                            boolean isPrev = object.getBoolean("prev");
                            boolean isNext = object.getBoolean("next");

                            if (isPrev) {
                                btnPrev.setEnabled(true);
                            } else {
                                btnPrev.setEnabled(false);
                            }

                            if (isNext) {
                                btnNext.setEnabled(true);
                            } else {
                                btnNext.setEnabled(false);
                            }

                            isDataFound = true;

                            for (int j = 0; j < dataArr.length(); j++)
                            {
                                JSONObject obj = dataArr.getJSONObject(j);
                                Data_einvoice item = new Data_einvoice();

                                if(isPaid){
                                    item.setInv_category(obj.getString("product_div"));
                                    item.setInv_date(obj.getString("invoice_date"));
                                    item.setInv_number(obj.getString("inv_number"));
                                    item.setSales_name(obj.getString("username").trim());
                                    item.setInv_totalprice(obj.getInt("ammount_inv"));
                                }else {
                                    item.setInv_category(obj.getString("name"));
                                    item.setInv_date(obj.getString("inv_date"));
                                    item.setInv_number(obj.getString("trx_number"));
                                    item.setSales_name(obj.getString("sales_person").trim());
                                    item.setInv_totalprice(obj.getInt("total_price"));
                                }

                                data_einvoices.add(item);
                            }
                        }
                    }

                    if (isDataFound){
                        shimmer.hideShimmerAdapter();
                        shimmer.setVisibility(View.GONE);
                        imgNotFound.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        imgNotFound.setVisibility(View.VISIBLE);
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);

                        btnPrev.setEnabled(false);
                        btnNext.setEnabled(false);
                    }

                    adapter_einvoice.notifyDataSetChanged();
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
                map.put("username", username);
                map.put("startmon", awal + " 00:00:00");
                map.put("endmon", akhir + " 23:59:59");
                map.put("divisi", div);
                map.put("limit", limit);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getCategory(final String username, final String awal, final String akhir) {
        String url;

        if (isPaid){
            url = URLGETCATEGORYPAID;
        }else {
            url = URLGETCATEGORY;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> data = new ArrayList<>();

                    data.add("All data :");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i++);

                        if (object.names().get(0).equals("invalid")){
                            Log.d("Invalid", "Data not found");
                        }
                        else
                        {
                            JSONArray dataArr = object.getJSONArray("data");

                            for (int j = 0; j < dataArr.length(); j++){
                                JSONObject jsonObject = dataArr.getJSONObject(j);

                                Log.d("Divisi", jsonObject.getString("name"));
                                data.add(jsonObject.getString("name"));
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
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("startmon", awal + " 00:00:00");
                map.put("endmon", akhir + " 23:59:59");
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}