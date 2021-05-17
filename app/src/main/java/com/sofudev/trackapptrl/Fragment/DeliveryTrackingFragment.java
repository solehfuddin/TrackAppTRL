package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_delivery_iteminv;
import com.sofudev.trackapptrl.Adapter.Adapter_delivery_track;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.GridSpacingItemDecoration;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_delivery_history_status;
import com.sofudev.trackapptrl.Data.Data_delivery_track;
import com.sofudev.trackapptrl.Data.Data_filter_deliverytrack;
import com.sofudev.trackapptrl.Form.DetailDeliveryTrackingActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class DeliveryTrackingFragment extends Fragment {
    private Config config = new Config();
    private String URLGETINVNUMBER   = config.Ip_address + config.deliverytrack_byawbnumber;
    private String URLDELTRACKBYDATE = config.Ip_address + config.deliverytrack_bydate;
    private String URLGETINFOSTATUS  = config.Ip_21express + config.getstatus21_info;
    private String URLGETINFORECEIVE = config.Ip_21express + config.receive21_info;
    private String URLGETHISTORYTIKI = config.Ip_tiki + config.getconnote_status;

    private View custom;
    private ImageView imgnotfound, imgPicked, imgTransit, imgDelivered, imgNotFoundInv;
    private ProgressBar progressBar;
    private ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView recyclerView, recyclerInv;
    private RippleView btnDetail;
    private Adapter_delivery_track adapter_delivery_track;
    private Adapter_delivery_iteminv adapter_delivery_iteminv;
    private List<Data_delivery_track> listData = new ArrayList<>();
    private List<Data_delivery_history_status> listStatus = new ArrayList<>();
    private List<String> listInv = new ArrayList<>();

    UniversalFontTextView txtAwb, txtCourier, txtMessName, txtMessId, txtPicked, txtTransit, txtDelivered;
    ProgressBar progressHeader, progressContent;

    String username, condition, token, tikitoken, awbnumber;
    private Context myContext;

    public DeliveryTrackingFragment() {
        // Required empty public constructor
    }

    public static DeliveryTrackingFragment newInstance(Data_filter_deliverytrack item)
    {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putString("condition", item.getCondition());
        bundle.putString("mytoken", item.getMytoken());
        bundle.putString("tikitoken", item.getTikitoken());

        DeliveryTrackingFragment fragment = new DeliveryTrackingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerRecyclerView = view.findViewById(R.id.fragment_delivery_tracking_shimmer);
        recyclerView = view.findViewById(R.id.fragment_delivery_tracking_recyclerview);
        imgnotfound  = view.findViewById(R.id.fragment_delivery_tracking_imgnottound);
        progressBar  = view.findViewById(R.id.fragment_delivery_tracking_progressbar);

        setData();
        bindingView();
        getData(username, condition);
    }

    private void setData()
    {
        Bundle data = this.getArguments();
        if (data != null)
        {
            username = data.getString("username");
            condition = data.getString("condition");
            token = data.getString("mytoken");
            tikitoken = data.getString("tikitoken");
        }
    }

    @SuppressLint("InflateParams")
    private void bindingView()
    {
        custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_delivery_track, null);

        imgNotFoundInv = custom.findViewById(R.id.bottom_dialog_deliverytrack_imgnotfound);
        progressHeader = custom.findViewById(R.id.bottom_dialog_deliverytrack_progressheader);
        progressContent= custom.findViewById(R.id.bottom_dialog_deliverytrack_progresscontent);
        txtAwb = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtawb);
        txtCourier = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtcourier);
        txtMessName = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtmessname);
        txtMessId = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtmessid);
        imgPicked = custom.findViewById(R.id.bottom_dialog_deliverytrack_imgpicked);
        imgTransit= custom.findViewById(R.id.bottom_dialog_deliverytrack_imgtransit);
        imgDelivered = custom.findViewById(R.id.bottom_dialog_deliverytrack_imgdelivered);
        txtPicked = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtpicked);
        txtTransit= custom.findViewById(R.id.bottom_dialog_deliverytrack_txttransit);
        txtDelivered = custom.findViewById(R.id.bottom_dialog_deliverytrack_txtdelivered);
        btnDetail = custom.findViewById(R.id.bottom_dialog_deliverytrack_btndetail);
        recyclerInv = custom.findViewById(R.id.bottom_dialog_deliverytrack_recyclerview);

        progressHeader.setVisibility(View.VISIBLE);
        progressContent.setVisibility(View.VISIBLE);
        txtMessId.setVisibility(View.GONE);
        txtMessName.setVisibility(View.GONE);
        txtAwb.setVisibility(View.GONE);
        txtCourier.setVisibility(View.GONE);

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter_delivery_track = new Adapter_delivery_track(getContext(), listData, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                BottomDialog bottomDialog = new BottomDialog.Builder(myContext)
                        .setTitle("Detail Package")
                        .setCustomView(custom)
                        .build();

                bottomDialog.show();

                progressHeader.setVisibility(View.GONE);
                txtAwb.setVisibility(View.VISIBLE);
                txtCourier.setVisibility(View.VISIBLE);

                imgTransit.setImageResource(R.drawable.transit_disabled);
                txtTransit.setTextColor(Color.parseColor("#b4b3b3"));
                imgPicked.setImageResource(R.drawable.picked_disabled);
                txtPicked.setTextColor(Color.parseColor("#b4b3b3"));
                imgDelivered.setImageResource(R.drawable.delivered_disabled);
                txtDelivered.setTextColor(Color.parseColor("#b4b3b3"));

                awbnumber = id;
                getInvNumber(username, id);
                txtCourier.setText(listData.get(pos).getEkspedisi());
                txtAwb.setText(listData.get(pos).getAwbNumber());

                String prefix = awbnumber.substring(0,3);

                if (prefix.contains("100"))
                {
                    //Tracking 21 Express
                    getStatusInfo(token, awbnumber);
                    getReceiveInfo(token, awbnumber);
//                    Toasty.info(myContext, "Tracking 21 Express", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Tracking Tiki
                    setTikiHeader(listData.get(pos).getServiceName());
                    getTikiConnoteHistory(tikitoken, awbnumber);
                }
//                Toasty.info(myContext, id, Toast.LENGTH_SHORT).show();
            }
        });

        adapter_delivery_iteminv = new Adapter_delivery_iteminv(getContext(), listInv);

        RecyclerView.LayoutManager verticalGrid = new GridLayoutManager(getContext(), 2);
        recyclerInv.setLayoutManager(verticalGrid);
        recyclerInv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerInv.setItemAnimator(new DefaultItemAnimator());
        recyclerInv.setAdapter(adapter_delivery_iteminv);

        shimmerRecyclerView.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
        shimmerRecyclerView.showShimmerAdapter();
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter_delivery_track);

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefix = awbnumber.substring(0,3);

                if (prefix.contains("100"))
                {
                    //Tracking 21 Express
                    Intent intent = new Intent(myContext, DetailDeliveryTrackingActivity.class);
                    intent.putExtra("access_token", token);
                    intent.putExtra("no_resi", awbnumber);
                    intent.putExtra("ekspedisi", "21 Express");
                    startActivity(intent);
                }
                else
                {
                    //Tracking Tiki
                    Intent intent = new Intent(myContext, DetailDeliveryTrackingActivity.class);
                    intent.putExtra("access_token", tikitoken);
                    intent.putExtra("no_resi", awbnumber);
                    intent.putExtra("ekspedisi", "Tiki");
                    startActivity(intent);
                }
            }
        });
    }

    private void setTikiHeader(String paket)
    {
        progressContent.setVisibility(View.GONE);
        txtMessId.setVisibility(View.VISIBLE);
        txtMessName.setVisibility(View.VISIBLE);

        String tikiHeader = "TIKI-JKT";
        String tikiName = tikiHeader + " (" + paket + ")";
        txtMessId.setText(tikiHeader);
        txtMessName.setText(tikiName);
    }

    private void getData(final String username, final String cond)
    {
        shimmerRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        listData.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLDELTRACKBYDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d("Output", response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
                            //Data tidak ditemukan
                            imgnotfound.setVisibility(View.VISIBLE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_delivery_track data = new Data_delivery_track();
                            data.setId(jsonObject.getString("id_delivery"));
                            data.setAwbNumber(jsonObject.getString("awb_number"));
                            data.setDatePickup(jsonObject.getString("date_pickup"));
                            data.setOpticName(jsonObject.getString("optic_name"));
                            data.setServiceName(jsonObject.getString("service_name"));
                            data.setNote(jsonObject.getString("note"));
                            data.setEkspedisi(jsonObject.getString("ekspedisi"));

                            listData.add(data);
                            imgnotfound.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    shimmerRecyclerView.hideShimmerAdapter();
                    shimmerRecyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_delivery_track.notifyDataSetChanged();
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
                map.put("condition", cond);

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getStatusInfo(final String token, final String awbnumber)
    {
        listStatus.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETINFOSTATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("shipment"))
                    {
                        JSONObject dataObj = jsonObject.getJSONObject("shipment");
                        txtMessId.setText(dataObj.getString("messenger_id"));
                        txtMessName.setText(dataObj.getString("messenger_name"));

                        progressContent.setVisibility(View.GONE);
                        txtMessId.setVisibility(View.VISIBLE);
                        txtMessName.setVisibility(View.VISIBLE);

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
                    isPickup(listStatus);
                    isTransit(listStatus);
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

    private void getReceiveInfo(final String token, final String awbnumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETINFORECEIVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Receive Info", response);

                    if (jsonArray.length() > 0)
                    {
                        Log.d("Receive Status", "Barang sudah diterima");

                        imgDelivered.setImageResource(R.drawable.delivered);
                        txtDelivered.setTextColor(Color.parseColor("#58595e"));
                    }
                    else
                    {
                        Log.d("Receive Status", "Barang belum diterima");

                        imgDelivered.setImageResource(R.drawable.delivered_disabled);
                        txtDelivered.setTextColor(Color.parseColor("#b4b3b3"));
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

    private void getInvNumber(final String optic, final String awbnumber)
    {
        listInv.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETINVNUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("note").length() > 0)
                    {
                        imgNotFoundInv.setVisibility(View.GONE);
                        recyclerInv.setVisibility(View.VISIBLE);

                        String note = jsonObject.getString("note");

                        if (note.contains("."))
                        {
                            note = note.replace(".", ",");
                        }

                        String [] item = note.split(",");
                        Collections.addAll(listInv, item);
                    }
                    else
                    {
                        imgNotFoundInv.setVisibility(View.VISIBLE);
                        recyclerInv.setVisibility(View.GONE);
                    }

                    adapter_delivery_iteminv.notifyDataSetChanged();
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
                map.put("optic", optic);
                map.put("awbnumber", awbnumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTikiConnoteHistory(final String token, final String awbnumber)
    {
        listStatus.clear();
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

                    Log.d("Connote history : ", response);
                    Log.d("Connote number : ", res.getString("cnno"));
                    isPickup(listStatus);
                    isTransit(listStatus);
                    isReceive(listStatus);
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

    private void isReceive(List<Data_delivery_history_status> item)
    {
        String sts = item.get(0).getStatus_note();
        if (sts.contains("RECEIVED"))
        {
            Log.d("Receive Status", "Barang sudah diterima");

            imgDelivered.setImageResource(R.drawable.delivered);
            txtDelivered.setTextColor(Color.parseColor("#58595e"));
        }
        else
        {
            Log.d("Receive Status", "Barang belum diterima");

            imgDelivered.setImageResource(R.drawable.delivered_disabled);
            txtDelivered.setTextColor(Color.parseColor("#b4b3b3"));
        }

        Log.d("Tiki status : ", sts);
    }

    private void isTransit(List<Data_delivery_history_status> item)
    {
        if (item.size() > 3)
        {
            imgTransit.setImageResource(R.drawable.transit);
            txtTransit.setTextColor(Color.parseColor("#58595e"));

            Log.d("Transit Status : ", "Barang sedang transit");
        }
        else
        {
            imgTransit.setImageResource(R.drawable.transit_disabled);
            txtTransit.setTextColor(Color.parseColor("#b4b3b3"));

            Log.d("Transit Status : ", "Barang belum transit");
        }
    }

    private void isPickup(List<Data_delivery_history_status> item)
    {
        if (item.size() > 1)
        {
            imgPicked.setImageResource(R.drawable.picked);
            txtPicked.setTextColor(Color.parseColor("#58595e"));

            Log.d("Pickup Status : ", "Barang sudah dipickup");
        }
        else
        {
            imgPicked.setImageResource(R.drawable.picked_disabled);
            txtPicked.setTextColor(Color.parseColor("#b4b3b3"));

            Log.d("Pickup Status : ", "Barang belum dipickup");
        }
    }
}