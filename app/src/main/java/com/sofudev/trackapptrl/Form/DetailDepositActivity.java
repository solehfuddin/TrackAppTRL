package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_deposit;
import com.sofudev.trackapptrl.Adapter.Adapter_panduantransfer;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_detail_deposit;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DetailDepositActivity extends AppCompatActivity {

    Config config = new Config();
    DateFormat formatTgl = new DateFormat();
    String URLRECENTDEPOSIT = config.Ip_address + config.depo_getrecentsaldo;
    String URLRECENTPENDING = config.Ip_address + config.depo_getrecentpending;

    ImageView btnBack, imgDepositNotfound, imgPendingNotfound;
    Button btnTopup;
    UniversalFontTextView txtNominal, txtDepositNotfound, txtPendingNotfound;
    RecyclerView recyclerDeposit, recyclerPending;

    Adapter_panduantransfer adapter_panduantransfer;
    Adapter_detail_deposit adapter_detail_deposit;
    Adapter_detail_deposit adapter_detail_pending;

    List<String> list_panduantransfer = new ArrayList<>();
    List<Data_detail_deposit> list_deposit = new ArrayList<>();
    List<Data_detail_deposit> list_pending = new ArrayList<>();

    ListView listView_panduanTransfer;
    String username, saldo, idparty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_deposit);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

//        getUserInfo();

        btnBack = findViewById(R.id.activity_detailbalance_btndepositback);
        txtNominal = findViewById(R.id.activity_detailbalance_txtdepositnominal);
        txtPendingNotfound = findViewById(R.id.activity_detailbalance_moreFrame);
        btnTopup = findViewById(R.id.activity_detaildeposit_btntopup);
        recyclerDeposit = findViewById(R.id.activity_detaildeposit_recycler);
        recyclerPending = findViewById(R.id.activity_detailpending_recycler);
        imgDepositNotfound = findViewById(R.id.activity_detaildeposit_imgLensnotfound);
        imgPendingNotfound = findViewById(R.id.activity_detailpending_imgLensnotfound);
        txtDepositNotfound = findViewById(R.id.activity_detaildeposit_more);
        txtPendingNotfound = findViewById(R.id.activity_detailpending_more);

        adapter_detail_deposit = new Adapter_detail_deposit(getApplicationContext(), list_deposit);
        adapter_detail_pending = new Adapter_detail_deposit(getApplicationContext(), list_pending);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerDeposit.setLayoutManager(manager);
        recyclerDeposit.setAdapter(adapter_detail_deposit);

        recyclerPending.setLayoutManager(manager1);
        recyclerPending.setAdapter(adapter_detail_pending);

        txtDepositNotfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDepositActivity.this, FilterDepositActivity.class);
                intent.putExtra("filter_type", "DEPO");
                intent.putExtra("nominal", saldo);
                intent.putExtra("user_info", idparty);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        txtPendingNotfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDepositActivity.this, FilterDepositActivity.class);
                intent.putExtra("filter_type", "PENDING");
                intent.putExtra("nominal", saldo);
                intent.putExtra("user_info", idparty);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DetailDepositActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                dialog.setContentView(R.layout.dialog_instruksi_topup);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);
//                lwindow.copyFrom(dialog.getWindow().getAttributes());

//                lwindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                Button btnDone = dialog.findViewById(R.id.dialog_instruksitopup_btnTutup);
                listView_panduanTransfer = dialog.findViewById(R.id.dialog_instruksitopup_listtransfer);

                showBankTransfer();

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                listView_panduanTransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String atmType = list_panduantransfer.get(i);

                        final Dialog dialog = new Dialog(DetailDepositActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                        if (atmType.contentEquals("BANK BCA") || atmType.equals("BANK BCA") || atmType.contains("BANK BCA"))
                        {
                            dialog.setContentView(R.layout.dialog_bank_bca);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.getWindow().setAttributes(lwindow);

                            if (!isFinishing())
                            {
                                dialog.show();
                            }
                        }
                        else if (atmType.contentEquals("BANK MANDIRI") || atmType.equals("BANK MANDIRI") || atmType.contains("BANK MANDIRIO"))
                        {
                            dialog.setContentView(R.layout.dialog_bank_mandiri);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.getWindow().setAttributes(lwindow);

                            if (!isFinishing())
                            {
                                dialog.show();
                            }
                        }
                    }
                });

                if (!isFinishing())
                {
                    dialog.show();
                }
//                dialog.getWindow().setAttributes(lwindow);
            }
        });

        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showRecentDeposit(username);
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void getUserInfo()
    {
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        saldo    = bundle.getString("nominal");
        idparty  = bundle.getString("user_info");

        String nominal = CurencyFormat(saldo);
        txtNominal.setText(nominal);
        showRecentDeposit(username);
        showRecentPending(username);
    }

    private void showBankTransfer()
    {
        adapter_panduantransfer = new Adapter_panduantransfer(getApplicationContext(), list_panduantransfer);

        List<String> allAtm = Arrays.asList("BANK BCA", "BANK MANDIRI");
        list_panduantransfer.addAll(allAtm);

        adapter_panduantransfer.notifyDataSetChanged();
        listView_panduanTransfer.setAdapter(adapter_panduantransfer);
    }

    private void showRecentDeposit(final String username)
    {
        list_deposit.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URLRECENTDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(DetailDepositActivity.class.getSimpleName(), response);

                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgDepositNotfound.setVisibility(View.VISIBLE);
                        recyclerDeposit.setVisibility(View.GONE);
                        txtDepositNotfound.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("insert_date");
                            String deskripsi= jsonObject.getString("keterangan");
                            String jenis    = jsonObject.getString("jenis_pembayaran");
                            String bank     = jsonObject.getString("bank_name");
                            int nominal     = jsonObject.getInt("saldo");

                            Data_detail_deposit item = new Data_detail_deposit();
                            item.setJenis_pembayaran(jenis);
                            item.setInsert_date(formatTgl.Indotime(tanggal));
                            item.setBank_name(bank);
                            item.setKeterangan(deskripsi);
                            item.setSaldo(nominal);

                            list_deposit.add(item);
                        }

                        imgDepositNotfound.setVisibility(View.GONE);
                        recyclerDeposit.setVisibility(View.VISIBLE);
                        txtDepositNotfound.setVisibility(View.VISIBLE);

                        adapter_detail_deposit.notifyDataSetChanged();
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
                hashMap.put("ship_number", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showRecentPending(final String username)
    {
        list_pending.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URLRECENTPENDING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(DetailDepositActivity.class.getSimpleName(), response);

                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgPendingNotfound.setVisibility(View.VISIBLE);
                        recyclerPending.setVisibility(View.GONE);
                        txtPendingNotfound.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("insert_date");
                            String deskripsi= jsonObject.getString("keterangan");
                            String jenis    = jsonObject.getString("jenis_pembayaran");
                            String bank     = jsonObject.getString("bank_name");
                            int nominal     = jsonObject.getInt("saldo");

                            Data_detail_deposit item = new Data_detail_deposit();
                            item.setJenis_pembayaran(jenis);
                            item.setInsert_date(formatTgl.Indotime(tanggal));
                            item.setBank_name(bank);
                            item.setKeterangan(deskripsi);
                            item.setSaldo(nominal);

                            list_pending.add(item);
                        }

                        imgPendingNotfound.setVisibility(View.GONE);
                        recyclerPending.setVisibility(View.VISIBLE);
                        txtPendingNotfound.setVisibility(View.VISIBLE);

                        adapter_detail_pending.notifyDataSetChanged();
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
                hashMap.put("ship_number", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
