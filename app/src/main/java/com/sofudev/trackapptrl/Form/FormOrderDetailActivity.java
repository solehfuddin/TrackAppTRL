package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;

public class FormOrderDetailActivity extends AppCompatActivity {
    Config config = new Config();
    UniversalFontTextView txtPasien, txtTanggal, txtOptik, txtHarga, txtStatus, txtNomorOrder, txtDownload,
                   txtInfo, txtEkspedisi, txtService;
    RippleView btnDownload;
    ImageView btnBack;
    RecyclerView recyclerViewItem;

    List<Data_item_orderdetail> itemList = new ArrayList<>();
    Adapter_item_orderdetail adapter_item_orderdetail;

    String key, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        txtPasien   = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_pasien);
        txtTanggal  = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_tanggal);
        txtOptik    = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_optik);
        txtHarga    = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_harga);
        txtStatus   = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_status);
        txtInfo     = findViewById(R.id.activity_orderdetail_txt_info);
        txtEkspedisi= findViewById(R.id.activity_orderdetail_txt_ekspedisi);
        txtService  = findViewById(R.id.activity_orderdetail_txt_service);
        txtNomorOrder = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_nomororder);
        txtDownload = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_download);

        btnDownload = (RippleView) findViewById(R.id.activity_orderdetail_btn_download);
        btnBack     = (ImageView) findViewById(R.id.activity_orderdetail_btn_back);

        recyclerViewItem = findViewById(R.id.activity_orderdetail_recyclerItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewItem.setLayoutManager(layoutManager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

        adapter_item_orderdetail = new Adapter_item_orderdetail(this, itemList);
        recyclerViewItem.setAdapter(adapter_item_orderdetail);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/lensa/" + key));

                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/lensaweb/" + key));
                startActivity(openBrowser);
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        key = bundle.getString("key");
        level = bundle.getString("level");
        getInformationDetailHeader(key);
        getInformationDetailItem(key);

        if (level.equals("3"))
        {
            btnDownload.setVisibility(View.GONE);
        }
    }

    private void getInformationDetailHeader(final String key_order)
    {
//        String URL = config.Ip_address + config.order_history_chooseOrder;
        //String URL = config.Ip_address + config.lenshistory_chooselens;

        String URL = config.Ip_address + config.lenshistory_chooselensheader;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nomor = "No Order #";
                    String nonpay = jsonObject.getString("keterangan");

                    switch (nonpay)
                    {
                        case "0":
                            nonpay = "Awaiting Approval";
                            txtInfo.setText(nonpay);
                            txtInfo.setTextColor(Color.parseColor("#ff9100"));
                            break;

                        case "1":
                            nonpay = "Approved";
                            txtInfo.setText(nonpay);
                            txtInfo.setTextColor(Color.parseColor("#45ac2d"));
                            break;

                        case "2":
                            nonpay = "Rejected";
                            txtInfo.setText(nonpay);
                            txtInfo.setTextColor(Color.parseColor("#f90606"));
                            break;

                        default:
                            nonpay = "";
                            txtInfo.setText(nonpay);
                            break;
                    }

                    if (level.equals("3"))
                    {
                        txtTanggal.setText(jsonObject.getString("tanggal_order"));
                        txtOptik.setText(jsonObject.getString("nama_optik"));
                        txtEkspedisi.setText(jsonObject.getString("ekspedisi"));
                        txtService.setText(jsonObject.getString("servis"));
                        txtHarga.setText("Unavailable");
                        txtStatus.setText(jsonObject.getString("status_bayar"));
                        txtNomorOrder.setText(nomor + jsonObject.getString("order_number"));
                    }
                    else
                    {
//                        txtPasien.setText(jsonObject.getString("nama_pasien"));
                        txtTanggal.setText(jsonObject.getString("tanggal_order"));
                        txtOptik.setText(jsonObject.getString("nama_optik"));
                        txtEkspedisi.setText(jsonObject.getString("ekspedisi"));
                        txtService.setText(jsonObject.getString("servis"));
                        txtHarga.setText("Rp. " + CurencyFormat(jsonObject.getString("total_harga")));
                        txtStatus.setText(jsonObject.getString("status_bayar"));
                        txtNomorOrder.setText(nomor + jsonObject.getString("order_number"));
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
                hashMap.put("key_order", key_order);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getInformationDetailItem(final String key_order)
    {
        String URL = config.Ip_address + config.lenshistory_chooselensitem;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Data_item_orderdetail data = new Data_item_orderdetail();
                        data.setNo(object.getInt("no"));
                        data.setItem_code(object.getString("item_code"));
                        data.setDeskripsi(object.getString("deskripsi"));
                        data.setJumlah(object.getInt("jumlah"));
                        data.setHarga(object.getInt("harga"));
                        data.setDiskon(object.getDouble("diskon"));
                        data.setTinting(object.getInt("tinting"));
                        data.setTotalAll(object.getInt("total_all"));
                        data.setCategory(Integer.parseInt(level));

                        itemList.add(data);
                    }

                    adapter_item_orderdetail.notifyDataSetChanged();
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key_order", key_order);
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
        String strFormat ="#,###.#";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }
}
