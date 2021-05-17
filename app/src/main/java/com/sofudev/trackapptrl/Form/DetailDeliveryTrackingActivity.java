package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_deliverytrack;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_delivery_history_status;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailDeliveryTrackingActivity extends AppCompatActivity {
    private Config config = new Config();
    private String URLRECEIVEDINFO = config.Ip_21express + config.receive21_info;
    private String URLTRACKINFO = config.Ip_21express + config.getstatus21_info;
    private String URLGETCONNOTETIKI = config.Ip_tiki + config.getconnote_info;
    private String URLGETHISTORYTIKI = config.Ip_tiki + config.getconnote_status;

    DateFormat formatDate = new DateFormat();
    String awbnumber, token, ekspedisi, name, address, rcvdate;
    int courier;
    private UniversalFontTextView txtTitleReceive, txtName, txtAddress, txtPhone, txtDate, txtAwbNumber;
    private CardView cardReceive;
    private ProgressBar progressRcBy, progressRcDate, progressRc;
    private ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView recyclerView;
    private Adapter_detail_deliverytrack adapter_detail_deliverytrack;
    private List<Data_delivery_history_status> listStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_delivery_tracking);

        RippleView btnBack = findViewById(R.id.form_tracksp_ripplebtnback);
        txtAwbNumber = findViewById(R.id.detail_deliverytrack_txtawbnumber);
        txtTitleReceive = findViewById(R.id.detail_deliverytrack_txttitlereceive);
        txtName = findViewById(R.id.detail_deliverytrack_txtname);
        txtAddress = findViewById(R.id.detail_deliverytrack_txtaddress);
        txtPhone   = findViewById(R.id.detail_deliverytrack_txtphone);
        txtDate    = findViewById(R.id.detail_deliverytrack_txtdate);
        cardReceive = findViewById(R.id.detail_deliverytrack_cardreceive);
        progressRcBy= findViewById(R.id.detail_deliverytrack_progressrcby);
        progressRcDate = findViewById(R.id.detail_deliverytrack_progressrcdate);
        progressRc = findViewById(R.id.detail_deliverytrack_progressreceive);
        shimmerRecyclerView = findViewById(R.id.detail_deliverytrack_shimmer);
        recyclerView = findViewById(R.id.detail_deliverytrack_recyclerview);

        setData();

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter_detail_deliverytrack = new Adapter_detail_deliverytrack(this, listStatus, courier);
        shimmerRecyclerView.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL);
        shimmerRecyclerView.showShimmerAdapter();
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter_detail_deliverytrack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            token       = bundle.getString("access_token");
            awbnumber   = bundle.getString("no_resi");
            ekspedisi   = bundle.getString("ekspedisi");

            String awb = "#" + awbnumber;
            txtAwbNumber.setText(awb);

            if (ekspedisi.contains("Tiki"))
            {
                getTikiConnoteHistory(token, awbnumber);
                courier = 1;
            }
            else
            {
                getTrack(token, awbnumber);
                getReceivedStatus(token, awbnumber);
                courier = 0;
            }
        }

        Log.d("Tokennya : ", token);
        Log.d("Awbnumber : ", awbnumber);
    }

    private void getReceivedStatus(final String token, final String awbnumber)
    {
        progressRc.setVisibility(View.VISIBLE);
        progressRcBy.setVisibility(View.VISIBLE);
        progressRcDate.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLRECEIVEDINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    progressRc.setVisibility(View.GONE);

                    if (jsonArray.length() > 0)
                    {
                        txtTitleReceive.setVisibility(View.VISIBLE);
                        cardReceive.setVisibility(View.VISIBLE);
                        progressRcBy.setVisibility(View.GONE);
                        progressRcDate.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            txtName.setText(jsonObject.getString("received_by"));
                            txtAddress.setText(jsonObject.getString("consignee_address"));
                            txtPhone.setText(jsonObject.getString("consignee_phone"));
                            txtDate.setText(formatDate.Indotime(jsonObject.getString("received_date")));
                        }
                    }
                    else
                    {
                        txtTitleReceive.setVisibility(View.GONE);
                        cardReceive.setVisibility(View.GONE);
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
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("access_token", token);
                map.put("no_resi", awbnumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTrack(final String token, final String awbnumber)
    {
        listStatus.clear();
        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, URLTRACKINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("shipment"))
                    {
                        JSONObject dataObj = object.getJSONObject("shipment");

                        JSONArray historyArray = dataObj.getJSONArray("statuses");
                        for (int i = 0; i < historyArray.length(); i++)
                        {
                            JSONObject obj = historyArray.getJSONObject(i);

                            Data_delivery_history_status dtStatus = new Data_delivery_history_status();
                            dtStatus.setTanggal(obj.getString("datetime"));
                            dtStatus.setStatus(obj.getString("status"));
                            dtStatus.setBranch_id(obj.getString("branch_id"));
                            dtStatus.setBranch(obj.getString("branch"));
                            dtStatus.setStatus_by(obj.getString("status_by"));
                            dtStatus.setStatus_note(obj.getString("status_note"));

                            listStatus.add(dtStatus);
                        }
                    }
                    else
                    {
                        Log.d("Invalid", "Data not found");
                    }

                    Log.d("Detail Status", response);

                    shimmerRecyclerView.hideShimmerAdapter();
                    shimmerRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_detail_deliverytrack.notifyDataSetChanged();
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
                map.put("access_token", token);
                map.put("no_resi", awbnumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTikiConnoteHistory(final String token, final String awbnumber)
    {
        listStatus.clear();
        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, URLGETHISTORYTIKI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    JSONObject res = jsonArray.getJSONObject(0);
                    JSONArray his = res.getJSONArray("history");
                    for (int i = 0; i < his.length(); i++)
                    {
                        JSONObject dt = his.getJSONObject(i);

                        Data_delivery_history_status data = new Data_delivery_history_status();
                        data.setTanggal(dt.getString("entry_date"));
                        data.setStatus(dt.getString("status"));
                        data.setBranch_id(dt.getString("entry_place"));
                        data.setBranch(dt.getString("entry_name"));
                        data.setStatus_by("");
                        data.setStatus_note(dt.getString("noted"));

                        listStatus.add(data);
                    }

                    shimmerRecyclerView.hideShimmerAdapter();
                    shimmerRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    //Sort Descending
                    Collections.reverse(listStatus);
                    adapter_detail_deliverytrack.notifyDataSetChanged();
                    if (listStatus.get(listStatus.size() - 1).getStatus().contains("POD 01"))
                    {
                        getTikiConnoteInfo(token, awbnumber);
                    }
                    else
                    {
                        progressRc.setVisibility(View.GONE);
                    }
                    Log.d("Connote history : ", response);
                    Log.d("Connote number : ", res.getString("cnno"));
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
                map.put("token", token);
                map.put("awb", awbnumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTikiConnoteInfo(final String token, final String awbnumber)
    {
        progressRc.setVisibility(View.VISIBLE);
        progressRcBy.setVisibility(View.VISIBLE);
        progressRcDate.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLGETCONNOTETIKI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressRc.setVisibility(View.GONE);
                    txtTitleReceive.setVisibility(View.VISIBLE);
                    cardReceive.setVisibility(View.VISIBLE);
                    progressRcBy.setVisibility(View.GONE);
                    progressRcDate.setVisibility(View.GONE);

                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("response");
                    JSONObject res = jsonArray.getJSONObject(0);

                    int total = listStatus.size();
                    address = res.getString("consignee_address");

                    if (listStatus.get(total - 1).getStatus().contains("POD 01"))
                    {
                        String[] dt = listStatus.get(total-1).getStatus_note().split(":");
                        name = dt[dt.length - 1];
                        rcvdate = formatDate.Indotime(listStatus.get(total - 1).getTanggal());
                    }

                    txtName.setText(name.trim());
                    txtAddress.setText(address);
                    txtPhone.setText("-");
                    txtDate.setText(rcvdate);
                    Log.d("Consigne name : ", name);
                    Log.d("Consigne address : ", address);
                    Log.d("Receive date : ", rcvdate);
                    Log.d("Connote Info : ", response);
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
                map.put("token", token);
                map.put("awb", awbnumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}