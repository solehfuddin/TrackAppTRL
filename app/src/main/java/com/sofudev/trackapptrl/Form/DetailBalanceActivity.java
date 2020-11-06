package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_balance;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_detail_balance;
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
import viethoa.com.snackbar.BottomSnackBarMessage;

public class DetailBalanceActivity extends AppCompatActivity {

    Config config = new Config();
    String URLHISTORYTRXLENS = config.Ip_address + config.show_historytrx_lens;
    String URLHISTORYTRXFRAME= config.Ip_address + config.show_historytrx_frame;
    String URLHISTORYTRXLENSALL = config.Ip_address + config.show_historytrx_lensall;
    String URLHISTORYTRXFRAMEALL = config.Ip_address + config.show_historytrx_frameall;

    ImageView btnBack, imgNotfound, imgLensNotfound, imgFrameNotfound;
    UniversalFontTextView txtNominal, txtMoreLens, txtMoreFrame, dialogtxtTitle;
    ProgressWheel progress;
    ImageView dialogbtnBack;
    String username, saldo, idparty;

    RecyclerView recyclerLens, recyclerFrame, dialogrecyclerview;

    Adapter_detail_balance adapter_detail_balance_lens;
    Adapter_detail_balance adapter_detail_balance_frame;
    Adapter_detail_balance adapter_dialog_balance;

    List<Data_detail_balance> list_lens = new ArrayList<>();
    List<Data_detail_balance> list_frame= new ArrayList<>();
    List<Data_detail_balance> list_dialog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_balance);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack = findViewById(R.id.activity_detailbalance_btnbprksback);
        txtNominal = findViewById(R.id.activity_detailbalance_txtbprksnominal);
        txtMoreLens= findViewById(R.id.activity_detailbalance_moreLens);
        txtMoreFrame= findViewById(R.id.activity_detailbalance_moreFrame);
        recyclerLens = findViewById(R.id.activity_detailbalance_recyLens);
        recyclerFrame= findViewById(R.id.activity_detailbalance_recyFrame);
        imgLensNotfound = findViewById(R.id.activity_detailbalance_imgLensnotfound);
        imgFrameNotfound= findViewById(R.id.activity_detailbalance_imgFramenotfound);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager manager1= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerLens.setLayoutManager(manager);
        recyclerFrame.setLayoutManager(manager1);

        adapter_detail_balance_lens = new Adapter_detail_balance(getApplicationContext(), list_lens);
        adapter_detail_balance_frame= new Adapter_detail_balance(getApplicationContext(), list_frame);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMoreLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFilter(1);
            }
        });

        txtMoreFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFilter(2);
            }
        });

        getUserInfo();
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

    private void getUserInfo()
    {
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        saldo    = bundle.getString("nominal");
        idparty  = bundle.getString("user_info");

        String nominal = CurencyFormat(saldo);
        txtNominal.setText(nominal);

//        Toasty.info(getApplicationContext(), idparty, Toast.LENGTH_SHORT).show();
        showLensHistory(idparty);
//        showLensHistory("10000");
        showFrameHistory(idparty);
//        showFrameHistory("10000");
    }

    private void openDialogFilter(final int reqCode)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_balance_more);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(lp);

        dialogtxtTitle = dialog.findViewById(R.id.dialog_balancemore_txtTitle);
        dialogbtnBack  = dialog.findViewById(R.id.dialog_balancemore_btnback);
        dialogrecyclerview = dialog.findViewById(R.id.dialog_balancemore_recyclerview);
        imgNotfound = dialog.findViewById(R.id.dialog_balancemore_imgnotfound);
        progress    = dialog.findViewById(R.id.dialog_balancemore_progress);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dialogrecyclerview.setLayoutManager(lm);

        adapter_dialog_balance = new Adapter_detail_balance(this, list_dialog);
        dialogrecyclerview.setAdapter(adapter_dialog_balance);

        if (reqCode == 1)
        {
            dialogtxtTitle.setText("Recent Lens Transaction");

            showLensDialogMore(idparty);
//            showLensDialogMore("10000");
        }
        else if (reqCode == 2)
        {
            dialogtxtTitle.setText("Recent Frame Transaction");

            showFrameDialogMore(idparty);
        }


        dialogbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showLensHistory(final String idparty)
    {
        list_lens.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URLHISTORYTRXLENS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgLensNotfound.setVisibility(View.VISIBLE);
                        recyclerLens.setVisibility(View.GONE);
                        txtMoreLens.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < 3; i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("trandate");
                            String deskripsi= "Order Transaction #" + jsonObject.getString("transnumber");
                            int nominal     = jsonObject.getInt("transtotal");

                            Data_detail_balance item = new Data_detail_balance();
                            item.setTanggal(tanggal);
                            item.setDeskripsi(deskripsi);
                            item.setNominal(nominal);

                            list_lens.add(item);
                        }

                        imgLensNotfound.setVisibility(View.GONE);
                        recyclerLens.setVisibility(View.VISIBLE);
                        txtMoreLens.setVisibility(View.VISIBLE);

                        adapter_detail_balance_lens.notifyDataSetChanged();
                        recyclerLens.setAdapter(adapter_detail_balance_lens);
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
                hashMap.put("id_party", idparty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showFrameHistory(final String idparty)
    {
        list_frame.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URLHISTORYTRXFRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgFrameNotfound.setVisibility(View.VISIBLE);
                        recyclerFrame.setVisibility(View.GONE);
                        txtMoreFrame.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < 3; i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String tanggal  = jsonObject.getString("trandate");
                            String deskripsi= "Order Transaction #" + jsonObject.getString("transnumber");
                            int nominal     = jsonObject.getInt("transtotal");

                            Data_detail_balance item = new Data_detail_balance();
                            item.setTanggal(tanggal);
                            item.setDeskripsi(deskripsi);
                            item.setNominal(nominal);

                            list_frame.add(item);
                        }

                        imgFrameNotfound.setVisibility(View.GONE);
                        recyclerFrame.setVisibility(View.VISIBLE);
                        txtMoreFrame.setVisibility(View.VISIBLE);

                        adapter_detail_balance_frame.notifyDataSetChanged();
                        recyclerFrame.setAdapter(adapter_detail_balance_frame);
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
                hashMap.put("id_party", idparty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showLensDialogMore(final String idparty)
    {
        list_dialog.clear();
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLHISTORYTRXLENSALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgNotfound.setVisibility(View.VISIBLE);
                        dialogrecyclerview.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            progress.setVisibility(View.GONE);

                            String tanggal  = jsonObject.getString("trandate");
                            String deskripsi= "Order Transaction #" + jsonObject.getString("transnumber");
                            int nominal     = jsonObject.getInt("transtotal");

                            Data_detail_balance item = new Data_detail_balance();
                            item.setTanggal(tanggal);
                            item.setDeskripsi(deskripsi);
                            item.setNominal(nominal);

                            list_dialog.add(item);

                            imgNotfound.setVisibility(View.GONE);
                            dialogrecyclerview.setVisibility(View.VISIBLE);

                            adapter_dialog_balance.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(DetailBalanceActivity.this);
                    snackBarMessage.showErrorMessage("No more data found");
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
                hashMap.put("id_party", idparty);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showFrameDialogMore(final String idparty)
    {
        list_dialog.clear();
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLHISTORYTRXFRAMEALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgNotfound.setVisibility(View.VISIBLE);
                        dialogrecyclerview.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            progress.setVisibility(View.GONE);

                            String tanggal  = jsonObject.getString("trandate");
                            String deskripsi= "Order Transaction #" + jsonObject.getString("transnumber");
                            int nominal     = jsonObject.getInt("transtotal");

                            Data_detail_balance item = new Data_detail_balance();
                            item.setTanggal(tanggal);
                            item.setDeskripsi(deskripsi);
                            item.setNominal(nominal);

                            list_dialog.add(item);

                            imgNotfound.setVisibility(View.GONE);
                            dialogrecyclerview.setVisibility(View.VISIBLE);

                            adapter_dialog_balance.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(DetailBalanceActivity.this);
                    snackBarMessage.showErrorMessage("No more data found");
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
                hashMap.put("id_party", idparty);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
