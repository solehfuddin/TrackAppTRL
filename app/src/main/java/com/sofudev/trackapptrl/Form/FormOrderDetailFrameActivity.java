package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_item_orderdetail;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_item_orderdetail;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormOrderDetailFrameActivity extends AppCompatActivity {

    Config config = new Config();
    String URLHEADER = config.Ip_address + config.frame_getheader_frame;
    String URLITEM   = config.Ip_address + config.frame_getitem_frame;

    ImageView btnBack;
    UniversalFontTextView txtTanggal, txtNamaOptik, txtHarga, txtStatus, txtEkspedisi, txtService, txtOrderNumber;
    RippleView btnDownloadPdf;
    RecyclerView recyclerViewItem;

    String orderNumber, titleNote;
    List<Data_item_orderdetail> listItem = new ArrayList<>();
    Adapter_item_orderdetail adapter_item_orderdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail_frame);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack         = findViewById(R.id.activity_orderdetail_frame_btnback);
        btnDownloadPdf  = findViewById(R.id.activity_orderdetail_frame_btnDownloadPdf);
        txtTanggal      = findViewById(R.id.activity_orderdetail_frame_txt_tanggal);
        txtNamaOptik    = findViewById(R.id.activity_orderdetail_frame_txt_optik);
        txtHarga        = findViewById(R.id.activity_orderdetail_frame_txt_harga);
        txtStatus       = findViewById(R.id.activity_orderdetail_frame_txt_status);
        txtEkspedisi    = findViewById(R.id.activity_orderdetail_frame_txt_ekspedisi);
        txtService      = findViewById(R.id.activity_orderdetail_frame_txt_service);
        txtOrderNumber  = findViewById(R.id.activity_orderdetail_frame_txt_nomororder);
        recyclerViewItem= findViewById(R.id.activity_orderdetail_frame_recyclerItem);

        adapter_item_orderdetail = new Adapter_item_orderdetail(this, listItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewItem.setLayoutManager(layoutManager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/frame/" + orderNumber));
                startActivity(openBrowser);
            }
        });

        getInfo();
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();
        orderNumber = bundle.getString("key");

        getHeader(orderNumber);
        try {
            Thread.sleep(1000);

            getItem(orderNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getHeader(final String order_number)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String harga = "Rp. " + CurencyFormat(String.valueOf(object.getString("total_harga")));
                    String nomor = "No order #" + object.getString("order_number");

                    titleNote = object.getString("flash_note");

                    Log.d("FRAME HEADER", titleNote);

                    txtOrderNumber.setText(nomor);
                    txtTanggal.setText(object.getString("tanggal_order"));
                    txtHarga.setText(harga);
                    txtNamaOptik.setText(object.getString("nama_optik"));
                    txtEkspedisi.setText(object.getString("ekspedisi"));
                    txtService.setText(object.getString("servis"));
                    txtStatus.setText(object.getString("status_bayar"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Error Header Frame", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("transnumber", order_number);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getItem(final String order_number)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String note = titleNote;
                        Log.d("FRAME DETAIL", note);

                        Data_item_orderdetail data = new Data_item_orderdetail();
                        data.setNo(object.getInt("no"));
                        data.setItem_code(object.getString("item_code"));
                        data.setDeskripsi(object.getString("deskripsi"));
                        data.setJumlah(object.getInt("jumlah"));
                        data.setHarga(object.getInt("harga"));
                        data.setDiskon(object.getDouble("diskon"));
                        data.setTinting(object.getInt("tinting"));
                        data.setTotalAll(object.getInt("total_all"));
                        data.setTitleFlashSale(note);
                        data.setCategory(2);

                        listItem.add(data);
                    }

                    adapter_item_orderdetail.notifyDataSetChanged();
                    recyclerViewItem.setAdapter(adapter_item_orderdetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Error Item Frame", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("transnumber", order_number);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
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
}
