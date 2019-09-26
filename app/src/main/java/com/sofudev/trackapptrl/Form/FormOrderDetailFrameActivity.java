package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sofudev.trackapptrl.Adapter.Adapter_detailhistory_frame;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_detailhistory_frame;
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

public class FormOrderDetailFrameActivity extends AppCompatActivity {

    Config config = new Config();
    String URLLINEITEM = config.Ip_address + config.frame_getlineitem_frame;
    String URLLINESHIP = config.Ip_address + config.frame_getlineship_frame;

    ImageView btnBack;
    UniversalFontTextView txtOrderNumber, txtShipName, txtShipCity, txtShipProvince, txtShipPrice;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recycleManager;
    RippleView btnDownloadPdf;
    CardView cardShip;

    Adapter_detailhistory_frame adapter_detailhistory_frame;
    List<Data_detailhistory_frame> itemLine = new ArrayList<>();
    String orderNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail_frame);

        btnBack = findViewById(R.id.activity_orderdetail_frame_btnback);
        txtOrderNumber = findViewById(R.id.activity_orderdetail_frame_txtorder);
        recyclerView = findViewById(R.id.activity_orderdetail_frame_recyclerview);
        btnDownloadPdf = findViewById(R.id.activity_orderdetail_frame_btnDownloadPdf);

        txtShipName = findViewById(R.id.activity_orderdetail_frame_txtshipname);
        txtShipCity = findViewById(R.id.activity_orderdetail_frame_txtshipcity);
        txtShipProvince = findViewById(R.id.activity_orderdetail_frame_txtshipprovince);
        txtShipPrice    = findViewById(R.id.activity_orderdetail_frame_txtshipprice);
        cardShip        = findViewById(R.id.activity_orderdetail_frame_cardShip);

        recycleManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycleManager);

        adapter_detailhistory_frame = new Adapter_detailhistory_frame(this, itemLine);

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

        txtOrderNumber.setText(orderNumber);

        getLine(orderNumber);

        getShipInfo(orderNumber);
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

    private void getLine(final String id)
    {
        itemLine.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLLINEITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String itemCode = jsonObject.getString("itemcode");
                        int itemPrice   = jsonObject.getInt("itemprice");
                        int itemDisc    = jsonObject.getInt("itemdiscount");
                        int itemQty     = jsonObject.getInt("itemqty");
                        int itemTotal   = jsonObject.getInt("itemTotal");

                        Data_detailhistory_frame item = new Data_detailhistory_frame();
                        item.setItemCode(itemCode);
                        item.setPrice(itemPrice);
                        item.setDisc(itemDisc);
                        item.setQty(itemQty);
                        item.setAmount(itemTotal);

                        itemLine.add(item);
                    }

                    adapter_detailhistory_frame.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter_detailhistory_frame);
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
                hashMap.put("transnumber", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getShipInfo(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLLINESHIP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("Error"))
                    {
                        cardShip.setVisibility(View.GONE);
                    }
                    else
                    {
                        String itemShipName     = jsonObject.getString("shippingName");
                        String itemShipPrice    = jsonObject.getString("shippingPrice");
                        String itemShipCity     = jsonObject.getString("shippingCity");
                        String itemShipProvince = jsonObject.getString("shippingProvince");
//                    String itemShipIcon     = jsonObject.getString("shippingIcon");

                        cardShip.setVisibility(View.VISIBLE);
                        itemShipPrice = "Rp. " + CurencyFormat(itemShipPrice);

                        txtShipName.setText(itemShipName);
                        txtShipPrice.setText(itemShipPrice);
                        txtShipCity.setText(itemShipCity);
                        txtShipProvince.setText(itemShipProvince);
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
                hashMap.put("transnumber", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
