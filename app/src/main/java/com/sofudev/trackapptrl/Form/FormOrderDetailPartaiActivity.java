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

public class FormOrderDetailPartaiActivity extends AppCompatActivity {

    Config config = new Config();
    String URLHEADER = config.Ip_address + config.orderpartai_detail_header;
    String URLITEM   = config.Ip_address + config.orderpartai_detail_item;

    ImageView btnBack;
    UniversalFontTextView txtTanggal, txtNamaOptik, txtHarga, txtInfo, txtStatus, txtEkspedisi, txtService, txtNomorOrder;
    RippleView btnDownload;
    RecyclerView recyclerViewItem;

    boolean isSp;
    String orderNumber, titleSale;

    List<Data_item_orderdetail> listItem = new ArrayList<>();
    Adapter_item_orderdetail adapter_item_orderdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail_partai);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack         = findViewById(R.id.activity_orderdetail_partai_btnback);
        txtTanggal      = findViewById(R.id.activity_orderdetail_partai_txt_tanggal);
        txtNamaOptik    = findViewById(R.id.activity_orderdetail_partai_txt_optik);
        txtHarga        = findViewById(R.id.activity_orderdetail_partai_txt_harga);
        txtStatus       = findViewById(R.id.activity_orderdetail_partai_txt_status);
        txtInfo         = findViewById(R.id.activity_orderdetail_partai_txt_info);
        txtEkspedisi    = findViewById(R.id.activity_orderdetail_partai_txt_ekspedisi);
        txtService      = findViewById(R.id.activity_orderdetail_partai_txt_service);
        txtNomorOrder   = findViewById(R.id.activity_orderdetail_partai_txt_nomororder);
        recyclerViewItem= findViewById(R.id.activity_orderdetail_partai_recyclerItem);

        btnDownload = findViewById(R.id.activity_orderdetail_partai_btnDownloadPdf);

        adapter_item_orderdetail = new Adapter_item_orderdetail(this, listItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewItem.setLayoutManager(layoutManager);
        recyclerViewItem.setNestedScrollingEnabled(false);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/partai/" + orderNumber));
                startActivity(openBrowser);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getInfo();
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            orderNumber = bundle.getString("key");

            assert orderNumber != null;
            if (orderNumber.contains("SPAL"))
            {
                Log.d("Is SP : ", "true");

                isSp = true;
                btnDownload.setVisibility(View.GONE);
            }
            else
            {
                Log.d("Is SP : ", "false");
                isSp = false;

                btnDownload.setVisibility(View.VISIBLE);
            }

            getHeader(orderNumber);

            try {
                Thread.sleep(1000);

                getItem(orderNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    private void getHeader(final String order_number)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String harga = "Rp. " + CurencyFormat(String.valueOf(object.getString("total_harga")));
                    String nomor = "No order #" + object.getString("order_number");

                    String nonpay = object.getString("keterangan");

                    switch (nonpay)
                    {
                        case "0":
                            nonpay = "Awaiting Approval";
                            if (isSp)
                            {
                                txtInfo.setText("");
                            }
                            else
                            {
                                txtInfo.setText(nonpay);
                                txtInfo.setTextColor(Color.parseColor("#ff9100"));
                            }
                            break;

                        case "1":
                            nonpay = "Approved";
                            if (isSp)
                            {
                                txtInfo.setText("");
                            }
                            else
                            {
                                txtInfo.setText(nonpay);
                                txtInfo.setTextColor(Color.parseColor("#45ac2d"));
                            }
                            break;

                        case "2":
                            nonpay = "Rejected";
                            if (isSp)
                            {
                                txtInfo.setText("");
                            }
                            else
                            {
                                txtInfo.setText(nonpay);
                                txtInfo.setTextColor(Color.parseColor("#f90606"));
                            }
                            break;

                        default:
                            nonpay = "";
                            txtInfo.setText(nonpay);
                            break;
                    }

                    String kurir = object.getString("ekspedisi");
                    String service = object.getString("servis");
                    titleSale = object.getString("flash_note");

                    Log.d("Header Partai", titleSale);

                    if (kurir.equals("null"))
                    {
                        kurir = "-";
                    }

                    if (service.equals("null"))
                    {
                        service = "-";
                    }

                    txtNomorOrder.setText(nomor);
                    txtTanggal.setText(object.getString("tanggal_order"));
                    txtHarga.setText(harga);
                    txtNamaOptik.setText(object.getString("nama_optik"));
                    txtEkspedisi.setText(kurir);
                    txtService.setText(service);
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
                    Log.d("Error Partai Header", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_number", order_number);
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

                        String note = titleSale;

//                        Log.d("ITEM PARTAI", note);

                        Data_item_orderdetail data = new Data_item_orderdetail();
                        data.setNo(object.getInt("no"));
                        data.setItem_code(object.getString("item_code"));
                        data.setDeskripsi(object.getString("deskripsi"));
                        data.setJumlah(object.getInt("jumlah"));
                        data.setHarga(object.getInt("harga"));
                        data.setDiskon(object.getDouble("diskon"));
                        data.setDiskonFlashSale(object.getInt("diskon_sale"));
                        data.setTinting(object.getInt("tinting"));
                        data.setTotalAll(object.getInt("total_all"));
                        data.setTitleFlashSale(note);
                        data.setCategory(3);

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
                hashMap.put("order_number", order_number);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
