package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_track;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.Data.Data_courier_filter;
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

public class CourierTrackingFragment extends Fragment {
    Config config = new Config();
    private String GETSTSBYDATE = config.Ip_address + config.dpodk_statuscheckbyoptic;
    private String GETPROCESSBYOPTIC = config.Ip_address + config.dpodk_getprocessbyoptic;
    private String GETDELIVEREDBYOPTIC = config.Ip_address + config.dpodk_getdeliveredbyoptic;

    ShimmerRecyclerView shimmerRecyclerView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView imgNotFound;
    ProgressBar progressBar;

    Adapter_courier_track adapter_courier_track;
    List<Data_courier> listData = new ArrayList<>();
    private Context myContext;
    private View custom;

    String username, startDate, endDate;
    int condition;

    public CourierTrackingFragment() {
        // Required empty public constructor
    }

    public static CourierTrackingFragment newInstance(Data_courier_filter item) {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putInt("condition", item.getCondition());
        bundle.putString("startdate", item.getStDate());
        bundle.putString("enddate", item.getEdDate());

        CourierTrackingFragment fragment = new CourierTrackingFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courier_tracking, container, false);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerRecyclerView = view.findViewById(R.id.fragment_courier_tracking_shimmer);
        recyclerView        = view.findViewById(R.id.fragment_courier_tracking_recyclerview);
        imgNotFound         = view.findViewById(R.id.fragment_courier_tracking_imgnottound);
        progressBar         = view.findViewById(R.id.fragment_courier_tracking_progressbar);

//        custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_delivery_track, null);

        final UniversalFontTextView txtDpodkNumber, txtInvoiceNumber, txtNamaOptic, txtStatus,
                                    txtStatusTitle, txtTanggal, txtTotalInv, txtCourier, txtJobform;
        final ImageView imgIcon;
        custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_courier_track, null);
        txtDpodkNumber      = custom.findViewById(R.id.bottomdialog_courierinv_txtdpodknumber);
        txtInvoiceNumber    = custom.findViewById(R.id.bottomdialog_courierinv_txtinvnumber);
        txtNamaOptic        = custom.findViewById(R.id.bottomdialog_courierinv_txtto);
        txtStatus           = custom.findViewById(R.id.bottomdialog_courierinv_txtstatus);
        txtStatusTitle      = custom.findViewById(R.id.bottomdialog_courierinv_txttitlestatus);
        txtTanggal          = custom.findViewById(R.id.bottomdialog_courierinv_txttitletgl);
        txtTotalInv         = custom.findViewById(R.id.bottomdialog_courierinv_txttotalinv);
        txtCourier          = custom.findViewById(R.id.bottomdialog_courierinv_txtcouriername);
        txtJobform          = custom.findViewById(R.id.bottomdialog_courierinv_txtjobnumber);
        imgIcon             = custom.findViewById(R.id.bottomdialog_courierinv_icpackage);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter_courier_track = new Adapter_courier_track(getContext(), listData, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d(CourierTrackingFragment.class.getSimpleName(), "Item clicked : " + id);
                BottomDialog bottomDialog = new BottomDialog.Builder(myContext)
                        .setTitle("Courier Status")
                        .setCustomView(custom)
                        .build();

                bottomDialog.show();

                String jobNumber = listData.get(pos).getNo_jobform().length() > 0 ? listData.get(pos).getNo_jobform() : "-";
                jobNumber = "Job Number : " + jobNumber;

                txtDpodkNumber.setText(listData.get(pos).getNo_trx());
                txtInvoiceNumber.setText(listData.get(pos).getNo_inv());
                txtNamaOptic.setText(listData.get(pos).getNama_optik());
                txtTanggal.setText(listData.get(pos).getTgl_diterima());
                txtCourier.setText(listData.get(pos).getNama_kurir());
                txtTotalInv.setText(CurencyFormat(listData.get(pos).getTotal_inv().replace(",", ".")));
                txtJobform.setText(jobNumber);

                String sts, penerima;
                int drawable;
                if (listData.get(pos).getStatus().equals("TERKIRIM"))
                {
                    if (listData.get(pos).getNama_penerima().equals("null"))
                    {
                        penerima = "-";
                    }
                    else
                    {
                        penerima = listData.get(pos).getNama_penerima();
                    }

                    String tmpSts = "DITERIMA : " + penerima;

                    if (tmpSts.length() > 23)
                    {
                        sts = tmpSts.substring(0, 23).concat("...");
                    }
                    else
                    {
                        sts = tmpSts;
                    }

                    txtStatus.setTextColor(Color.parseColor("#45ac2d"));
                    txtTanggal.setVisibility(View.VISIBLE);
                    drawable = R.drawable.delivered_courier;
                }
                else if (listData.get(pos).getStatus().equals("RETUR"))
                {
                    String tmpSts = listData.get(pos).getStatus() + " : " + listData.get(pos).getNote_opd().toUpperCase();

                    if (tmpSts.length() >= 23)
                    {
                        sts = tmpSts.substring(0, 23).concat("...");
                    }
                    else
                    {
                        sts = tmpSts;
                    }

                    txtStatus.setTextColor(Color.parseColor("#ff9100"));
                    txtTanggal.setVisibility(View.VISIBLE);
                    drawable = R.drawable.ic_package;
                }
                else
                {
                    drawable = R.drawable.ic_package;
                    sts = "SEDANG DIPERJALANAN";

                    txtStatus.setTextColor(Color.parseColor("#f90606"));
                    txtTanggal.setVisibility(View.GONE);
                }

                txtStatus.setTypeface(null, Typeface.BOLD);
                txtStatus.setText(sts);
                txtStatusTitle.setVisibility(View.GONE);
                imgIcon.setImageResource(drawable);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter_courier_track);

        setData();
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.##";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void setData()
    {
        Bundle data = this.getArguments();
        if (data != null)
        {
            username = data.getString("username");
            condition = data.getInt("condition", 0);
            startDate = data.getString("startdate");
            endDate   = data.getString("enddate");

            Log.d(CourierTrackingFragment.class.getSimpleName(), "Username : " + username);

            if (condition == 0)
            {
                getProcessByCourier(username);
            }
            else
            {
                getDeliveredByOptic(username, startDate, endDate);
            }

//            getStatusByDate(username, startDate, endDate);
        }
    }

    private void getStatusByDate(final String username, final String stDate, final String edDate){
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, GETSTSBYDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error")) {
                            imgNotFound.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                            Toasty.error(myContext, "Data not found", Toast.LENGTH_LONG, true).show();
                        } else {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));
                            dt.setTgl_diterima(jsonObject.getString("tgl_diterima"));

                            listData.add(dt);

                            progressBar.setVisibility(View.GONE);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                        }

                        progressBar.setVisibility(View.GONE);
                        adapter_courier_track.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                shimmerRecyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("idOptic", username);
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getProcessByCourier(final String username){
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, GETPROCESSBYOPTIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error")) {
                            imgNotFound.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                            Toasty.error(myContext, "Data not found", Toast.LENGTH_LONG, true).show();
                        } else {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));
                            dt.setTgl_diterima(jsonObject.getString("tgl_diterima"));
                            dt.setNo_jobform(jsonObject.getString("no_jobform"));

                            listData.add(dt);

                            progressBar.setVisibility(View.GONE);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                        }

                        progressBar.setVisibility(View.GONE);
                        adapter_courier_track.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                shimmerRecyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("idOptic", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDeliveredByOptic(final String username, final String stDate, final String edDate){
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, GETDELIVEREDBYOPTIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error")) {
                            imgNotFound.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                            Toasty.error(myContext, "Data not found", Toast.LENGTH_LONG, true).show();
                        } else {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));
                            dt.setTgl_diterima(jsonObject.getString("tgl_diterima"));
                            dt.setNo_jobform(jsonObject.getString("no_jobform"));

                            listData.add(dt);

                            progressBar.setVisibility(View.GONE);
                            imgNotFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                        }

                        progressBar.setVisibility(View.GONE);
                        adapter_courier_track.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                shimmerRecyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("idOptic", username);
                map.put("dateSt", stDate);
                map.put("dateEd", edDate);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}