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
import com.sofudev.trackapptrl.Adapter.Adapter_detailhistory_partai;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_detailhistory_partai;
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

public class FormOrderDetailPartaiActivity extends AppCompatActivity {

    Config config = new Config();
    String URLITEM = config.Ip_address + config.orderpartai_show_detailitem;
    String URLSHIP = config.Ip_address + config.orderpartai_show_detailship;

    ImageView btnBack;
    UniversalFontTextView txtOrderNumber, txtShipName, txtShipCity, txtShipProvince, txtShipAmount;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CardView cardShip;
    RippleView btnDownload;

    String orderNumber;

    Adapter_detailhistory_partai adapter_detailhistory_partai;
    List<Data_detailhistory_partai> itemPartai = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail_partai);

        btnBack = findViewById(R.id.activity_orderdetail_partai_btnback);
        txtOrderNumber = findViewById(R.id.activity_orderdetail_partai_txtorder);
        txtShipName = findViewById(R.id.activity_orderdetail_partai_txtshipname);
        txtShipCity = findViewById(R.id.activity_orderdetail_partai_txtshipcity);
        txtShipProvince = findViewById(R.id.activity_orderdetail_partai_txtshipprovince);
        txtShipAmount = findViewById(R.id.activity_orderdetail_partai_txtshipprice);
        cardShip = findViewById(R.id.activity_orderdetail_partai_cardShip);

        recyclerView = findViewById(R.id.activity_orderdetail_partai_recyclerview);
        btnDownload = findViewById(R.id.activity_orderdetail_partai_btnDownloadPdf);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter_detailhistory_partai = new Adapter_detailhistory_partai(this, itemPartai);

        recyclerView.setAdapter(adapter_detailhistory_partai);

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
        orderNumber = bundle.getString("key");

        txtOrderNumber.setText(orderNumber);

        getItem(orderNumber);
        getShip(orderNumber);
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

    private void getItem(final String id)
    {
        itemPartai.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URLITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String itemCode     = object.getString("itemCode");
                        String itemSide     = object.getString("itemSide");
                        int itemPrice       = object.getInt("itemPrice");
                        int itemDiscount    = object.getInt("itemDiscount");
                        int itemQty         = object.getInt("itemQty");
                        int itemTotal       = object.getInt("itemTotal");

                        Data_detailhistory_partai data_partai = new Data_detailhistory_partai();
                        data_partai.setItemCode(itemCode);
                        data_partai.setItemSide(itemSide);
                        data_partai.setPrice(itemPrice);
                        data_partai.setDisc(itemDiscount);
                        data_partai.setQty(itemQty);
                        data_partai.setAmount(itemTotal);

                        itemPartai.add(data_partai);
                    }

                    adapter_detailhistory_partai.notifyDataSetChanged();
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
                hashMap.put("order_number", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getShip(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSHIP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("Error"))
                    {
                        cardShip.setVisibility(View.GONE);
                    }
                    else
                    {
                        String shipName = object.getString("shippingName");
                        String shipPrice= object.getString("shippingPrice");
                        String shipCity = object.getString("shippingCity");
                        String shipProvince = object.getString("shippingProvince");

                        if (shipPrice.isEmpty())
                        {
                            cardShip.setVisibility(View.GONE);
                        }

                        cardShip.setVisibility(View.VISIBLE);
                        shipPrice = "Rp. " + CurencyFormat(shipPrice);

                        txtShipName.setText(shipName);
                        txtShipAmount.setText(shipPrice);
                        txtShipProvince.setText(shipProvince);
                        txtShipCity.setText(shipCity);
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
                hashMap.put("order_number", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
