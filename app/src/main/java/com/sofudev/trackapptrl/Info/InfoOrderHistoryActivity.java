package com.sofudev.trackapptrl.Info;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.sofudev.trackapptrl.Adapter.Adapter_sp_history;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_order_history;
import com.sofudev.trackapptrl.Data.Data_sp_history;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.sofudev.trackapptrl.Util.Executor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class InfoOrderHistoryActivity extends AppCompatActivity {
    Config config = new Config();
    String URL_INFOORDER = config.Ip_address + config.info_order_history;
    String URL_GETDETAILSP = config.Ip_address + config.ordersp_get_detailSp;
    String URL_INFOORDERPOS = config.Ip_address + config.info_order_historypos;
    String URL_INFOORDERJOB = config.Ip_address + config.info_order_historyjob;

    ACProgressFlower loading;
    RecyclerView recyclerView;
    RippleView btn_back;
    UniversalFontTextView txt_ordernumber;
    RecyclerView.LayoutManager recyclerLayout;
    Adapter_order_history adapter_order_history;
    Adapter_sp_history adapter_sp_history;
    List<Data_order_history> list = new ArrayList<>();
    List<Data_sp_history> lisSp = new ArrayList<>();

    MCrypt mCrypt;
    String order_number;
    int isSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order_history);

        recyclerView = findViewById(R.id.info_order_history_recyclerview);
        btn_back     = findViewById(R.id.info_order_history_ripplebtnback);
        txt_ordernumber = findViewById(R.id.info_order_history_txtordernumber);
        recyclerView.setHasFixedSize(true);

        recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        mCrypt = new MCrypt();

        getOrdernumber();
        backToTrackOrder();

        if (isSp == 0)
        {
            showLoading();
            adapter_order_history = new Adapter_order_history(getApplicationContext(), list);
            recyclerView.setAdapter(adapter_order_history);
            txt_ordernumber.setText("Job #" + order_number);

//            runOrder();
//            runOrderWithCallback();

            showHistoryOrderJob(order_number);
        }
        else
        {
            showLoading();
            //Ini Sp
            adapter_sp_history = new Adapter_sp_history(getApplicationContext(), lisSp);
            recyclerView.setAdapter(adapter_sp_history);
            txt_ordernumber.setText("Sp #" + order_number);
            getDetailSp(order_number);
        }
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(InfoOrderHistoryActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();

        if(!isFinishing()){
            loading.show();
        }
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
            isSp         = bundle.getInt("is_sp");
            //Toasty.warning(getApplicationContext(), "No Job nya : " + order_number, Toast.LENGTH_LONG).show();
        }
    }

    private void runOrder() {
        //Check PRNT
//        showHistoryOrder(order_number, "PRNT");
        try {
            Thread.sleep(100);
//            showHistoryOrder(order_number, "PRNT");
            showHistoryOrderPos(order_number, "PRNT", "PRNT");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check WARE
        try {
            Thread.sleep(200);
//            showHistoryOrder(order_number, "WARE");
            showHistoryOrderPos(order_number, "WARE", "WARE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check SURF
        try {
            Thread.sleep(300);
//            showHistoryOrder(order_number, "SURF");
            showHistoryOrderPos(order_number, "SURF", "SURF");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check HARDCOAT
//        try {
//            Thread.sleep(700);
////            showHistoryOrder(order_number, "COAT");
//            showHistoryOrderPos(order_number, "COAT", "HARD");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //Check TINTING
//        try {
//            Thread.sleep(900);
////            showHistoryOrder(order_number, "COAT");
//            showHistoryOrderPos(order_number, "COAT", "TINT");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //Check COAT
        try {
            Thread.sleep(400);
//            showHistoryOrder(order_number, "COAT");
            showHistoryOrderPos(order_number, "COAT", "COAT");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check QUI
        try {
            Thread.sleep(500);
//            showHistoryOrder(order_number, "QUI");
            showHistoryOrderPos(order_number, "QUI", "QUI2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check EDGE
        try {
            Thread.sleep(600);
//            showHistoryOrder(order_number, "EDGE");
            showHistoryOrderPos(order_number, "EDGE", "EDGE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check SHIP
        try {
            Thread.sleep(700);
//            showHistoryOrder(order_number, "SHIP");
            showHistoryOrderPos(order_number, "SHIP", "SHIP");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runOrderWithCallback() {
        final AtomicBoolean processing = new AtomicBoolean(true);

        new Executor.Builder().add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 1 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "PRNT");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 1 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 2 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "WARE");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 2 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 3 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "SURF");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 3 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 4 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "COAT");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 4 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 5 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "QUI");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 5 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 6 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "EDGE");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 6 Complete");
            }
        }).add(new Runnable() {
            @Override
            public void run() {
                System.out.println("TASK 7 Start");
                try {
                    Thread.sleep(100);
                    showHistoryOrder(order_number, "SHIP");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TASK 7 Complete");
            }
        }).callback(new Executor.Callback() {
            @Override
            public void onComplete() {
                System.out.println("All TASK COMPLETED");
                processing.set(false);
            }
        }).build().execute();
    }

    private void showHistoryOrder(final String key, final String status) {
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

    private void showHistoryOrderPos(final String key, final String status, final String pos) {
        adapter_order_history.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INFOORDERPOS, new Response.Listener<String>() {
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
                hashMap.put("pos", pos);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showHistoryOrderJob(final String key) {
        adapter_order_history.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INFOORDERJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();

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
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getDetailSp(final String noSp) {
        lisSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETDETAILSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String tipeSp = object.getString("type_sp");
                        String dateOut= object.getString("date_out");
                        String status = object.getString("status");
                        String approve= object.getString("approve");
                        String reject = object.getString("reason");
                        String duration = object.getString("duration_unit");
                        String approvalName = object.getString("approval_name");

                        Data_sp_history item = new Data_sp_history();
                        item.setTipesp(tipeSp);
                        item.setDateout(dateOut);
                        item.setStatus(status);
                        item.setApprove(approve);
                        item.setReject(reject);
                        item.setDurationunit(duration);
                        item.setApprovalName(approvalName);

                        lisSp.add(item);
                    }

                    adapter_sp_history.notifyDataSetChanged();
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
                hashMap.put("id_sp", noSp);
                return hashMap;
            }
        };

        request.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(request);
    }
}
