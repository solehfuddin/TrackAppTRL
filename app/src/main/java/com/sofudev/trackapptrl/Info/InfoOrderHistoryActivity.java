package com.sofudev.trackapptrl.Info;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_order_history;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_order_history;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InfoOrderHistoryActivity extends AppCompatActivity {
    Config config = new Config();
    String URL_INFOORDER = config.Ip_address + config.info_order_history;

    RecyclerView recyclerView;
    RippleView btn_back;
    UniversalFontTextView txt_ordernumber;
    RecyclerView.LayoutManager recyclerLayout;
    Adapter_order_history adapter_order_history;
    List<Data_order_history> list = new ArrayList<>();

    MCrypt mCrypt;
    String order_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order_history);

        recyclerView = (RecyclerView) findViewById(R.id.info_order_history_recyclerview);
        btn_back     = (RippleView) findViewById(R.id.info_order_history_ripplebtnback);
        txt_ordernumber = (UniversalFontTextView) findViewById(R.id.info_order_history_txtordernumber);
        recyclerView.setHasFixedSize(true);

        recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        adapter_order_history = new Adapter_order_history(getApplicationContext(), list);
        recyclerView.setAdapter(adapter_order_history);

        mCrypt = new MCrypt();

        getOrdernumber();
        backToTrackOrder();
        txt_ordernumber.setText("Job #" + order_number);

        runOrder();
    }

    private void backToTrackOrder()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOrdernumber()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            order_number = bundle.getString("order_number");
            //Toasty.warning(getApplicationContext(), "No Job nya : " + order_number, Toast.LENGTH_LONG).show();
        }
    }

    private void runOrder()
    {
        //Check PRNT
        showHistoryOrder(order_number, "PRNT");

        //Check WARE
        try {
            Thread.sleep(150);
            showHistoryOrder(order_number, "WARE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check SURF
        try {
            Thread.sleep(150);
            showHistoryOrder(order_number, "SURF");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check COAT
        try {
            Thread.sleep(200);
            showHistoryOrder(order_number, "COAT");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check QUI
        try {
            Thread.sleep(200);
            showHistoryOrder(order_number, "QUI");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check EDGE
        try {
            Thread.sleep(250);
            showHistoryOrder(order_number, "EDGE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check SHIP
        try {
            Thread.sleep(300);
            showHistoryOrder(order_number, "SHIP");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showHistoryOrder(final String key, final String status)
    {
        adapter_order_history.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INFOORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "order_custname";
                String info2 = "order_statusdt";
                String info3 = "order_statustm";
                String info4 = "order_lensname";
                String info5 = "order_located";
                String info6 = "order_statustxt";

                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String encrypt_info4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));
                    String encrypt_info5 = MCrypt.bytesToHex(mCrypt.encrypt(info5));
                    String encrypt_info6 = MCrypt.bytesToHex(mCrypt.encrypt(info6));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String order_custname   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                            String order_statusdate = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));
                            String order_statustime = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info3)));
                            String order_lensname   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info4)));
                            String order_status     = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info5)));
                            String order_statustext = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info6)));

                            Data_order_history data = new Data_order_history();
                            data.setCustname(order_custname);
                            data.setDatetime(order_statusdate + " " + order_statustime);
                            data.setLensname(order_lensname);
                            data.setStatus(order_status);
                            data.setStatusdesc(order_statustext);

                            list.add(data);
                        }

                        adapter_order_history.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_tracking", key);
                hashMap.put("status", status);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
