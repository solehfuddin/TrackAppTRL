package com.sofudev.trackapptrl.Fragment;


import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_child_estatement;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CounterData;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_child_estatement;
import com.sofudev.trackapptrl.Data.Data_filter_estatement;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EstatementItemFragment extends Fragment {

    private Config config = new Config();
    private String URLDATA = config.Ip_address + config.estatement_getdata;

    private ShimmerRecyclerView shimmer;
    private RecyclerView recyclerView;
    private ImageView imgnotfound;
    private LinearLayout linearAll, linearFilter;
    private ProgressBar progresstotalall, progresstotal, progresssubtotal;
    private UniversalFontTextView txtsubtotal, txttotal, txttotalall;

    CounterData counterData;
    Adapter_child_estatement adapter_child_estatement;
    private List<Data_child_estatement> item = new ArrayList<>();
    String filterby, username, blnawal, blnakhir, tahun, divisi;
//    private Data_filter_estatement itemFilter = new Data_filter_estatement();

    public EstatementItemFragment() {
        // Required empty public constructor
    }

    public static EstatementItemFragment newInstance(Data_filter_estatement itemFilter)
    {
        Bundle bundle = new Bundle();
        bundle.putString("filterby", itemFilter.getFiltername());
        bundle.putString("username", itemFilter.getUsername());
        bundle.putString("blnawal", itemFilter.getBlnAwal());
        bundle.putString("blnakhir",itemFilter.getBlnAkhir());
        bundle.putString("tahun", itemFilter.getThn());
        bundle.putString("divisi", itemFilter.getDivisi());

        EstatementItemFragment fragment = new EstatementItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        counterData = (CounterData) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estatement_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmer         = view.findViewById(R.id.fragment_estatement_item_shimmer);
        recyclerView    = view.findViewById(R.id.fragment_estatement_item_recyclerview);
        imgnotfound     = view.findViewById(R.id.fragment_estatement_item_imgLensnotfound);
        linearAll       = view.findViewById(R.id.fragment_estatement_item_linearall);
        linearFilter    = view.findViewById(R.id.fragment_estatement_item_linearfilter);
        txtsubtotal     = view.findViewById(R.id.fragment_estatement_item_txtsubtotal);
        txttotal        = view.findViewById(R.id.fragment_estatement_item_txttotal);
        txttotalall     = view.findViewById(R.id.fragment_estatement_item_txttotalall);
        progresstotalall= view.findViewById(R.id.fragment_estatement_item_progresstotalall);
        progresstotal   = view.findViewById(R.id.fragment_estatement_item_progresstotal);
        progresssubtotal= view.findViewById(R.id.fragment_estatement_item_progresssubtotal);

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter_child_estatement = new Adapter_child_estatement(getContext(), item, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
            }
        });

        shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
        shimmer.showShimmerAdapter();
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter_child_estatement);

        Bundle data = this.getArguments();

        if (data != null)
        {
            filterby = data.getString("filterby");
            username = data.getString("username");
            blnawal  = data.getString("blnawal");
            blnakhir = data.getString("blnakhir");
            tahun    = data.getString("tahun");
            divisi   = data.getString("divisi");

            Log.d("filterby = ", filterby);
            Log.d("username = ", username);
            Log.d("blnawal = ", blnawal);
            Log.d("blnakhir = ", blnakhir);
            Log.d("tahun = ", tahun);
            Log.d("divisi = ", divisi);
        }

        if (!divisi.isEmpty())
        {
            linearFilter.setVisibility(View.VISIBLE);
            linearAll.setVisibility(View.GONE);

            progresssubtotal.setVisibility(View.VISIBLE);
            txtsubtotal.setVisibility(View.GONE);
            progresstotal.setVisibility(View.VISIBLE);
            txttotal.setVisibility(View.GONE);
            Log.d("Loading data ", "Showing " + divisi);
        }
        else
        {
            linearAll.setVisibility(View.VISIBLE);
            linearFilter.setVisibility(View.GONE);

            progresstotalall.setVisibility(View.VISIBLE);
            txttotalall.setVisibility(View.GONE);

            Log.d("Loading data ", "All data");
        }

        getData(username, blnawal, blnakhir, tahun, divisi);
    }

    private List<Data_child_estatement> itemList() {
        List<Data_child_estatement> itemList = new ArrayList<>();

//        try {
//            Thread.sleep(3000);

            Data_child_estatement data1 = new Data_child_estatement("950017032", "SEP 2020", "374500");
            Data_child_estatement data2 = new Data_child_estatement("950017029", "SEP 2020", "1122500");
            Data_child_estatement data3 = new Data_child_estatement("950017028", "SEP 2020", "371000");
            Data_child_estatement data4 = new Data_child_estatement("950017014", "SEP 2020", "369500");
            Data_child_estatement data5 = new Data_child_estatement("950017029", "SEP 2020", "1122500");
            Data_child_estatement data6 = new Data_child_estatement("950017032", "SEP 2020", "374500");
            Data_child_estatement data7 = new Data_child_estatement("950017014", "SEP 2020", "369500");
            Data_child_estatement data8 = new Data_child_estatement("950017028", "SEP 2020", "371000");
            Data_child_estatement data9 = new Data_child_estatement("950017014", "SEP 2020", "369500");
            Data_child_estatement data10 = new Data_child_estatement("950017029", "SEP 2020", "1122500");

            itemList.add(data1);
            itemList.add(data2);
            itemList.add(data3);
            itemList.add(data4);
            itemList.add(data5);
            itemList.add(data6);
            itemList.add(data7);
            itemList.add(data8);
            itemList.add(data9);
            itemList.add(data10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return itemList;
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

    private void getData(final String username, final String dtAwal, final String dtAkhir, final String tahun, final String div) {
        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        item.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLDATA, new Response.Listener<String>() {
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
                            shimmer.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);

                            if (divisi.length() > 0)
                            {
                                txttotal.setText(CurencyFormat("0"));
                                txtsubtotal.setText(CurencyFormat("0"));

                                progresssubtotal.setVisibility(View.GONE);
                                txtsubtotal.setVisibility(View.VISIBLE);
                                progresstotal.setVisibility(View.GONE);
                                txttotal.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                txttotalall.setText(CurencyFormat("0"));

                                progresstotalall.setVisibility(View.GONE);
                                txttotalall.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONArray total      = jsonObject.getJSONArray("count");
                            JSONArray subtotal   = jsonObject.getJSONArray("subtotal");

                            for (int j = 0; j < jsonArray1.length(); j++)
                            {
                                JSONObject object = jsonArray1.getJSONObject(j);

                                Data_child_estatement child = new Data_child_estatement(
                                        object.getString("trx_no"),
                                        object.getString("tgl"),
                                        object.getString("outstanding")
                                );

                                item.add(child);
                            }

                            counterData.counterData(item.size());

                            for (int t = 0; t < total.length(); t++)
                            {
                                JSONObject object = total.getJSONObject(t);

                                String totalamount = object.getString("totalamount");
                                String totaloutstand = object.getString("totaloutstanding");

                                if (divisi.length() > 0)
                                {
                                    txttotal.setText(CurencyFormat(String.valueOf(totaloutstand)));

                                    progresstotal.setVisibility(View.GONE);
                                    txttotal.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    txttotalall.setText(CurencyFormat(String.valueOf(totaloutstand)));
//                                    txtsubtotal.setText(CurencyFormat(String.valueOf(totalamount)));

                                    progresstotalall.setVisibility(View.GONE);
                                    txttotalall.setVisibility(View.VISIBLE);
//                                    txtsubtotal.setVisibility(View.VISIBLE);
                                }
                            }

                            for (int s = 0; s < subtotal.length(); s++)
                            {
                                JSONObject object = subtotal.getJSONObject(s);

                                String subtotalamount = object.getString("totalamount");
                                String subtotaloutstand = object.getString("totaloutstanding");

                                if (divisi.length() > 0)
                                {
                                    txtsubtotal.setText(CurencyFormat(String.valueOf(subtotaloutstand)));
//                                    txttotal.setText(CurencyFormat(String.valueOf(subtotalamount)));
                                    progresssubtotal.setVisibility(View.GONE);
                                    txtsubtotal.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    shimmer.hideShimmerAdapter();
                    shimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_child_estatement.notifyDataSetChanged();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("startmon", dtAwal);
                map.put("endmon", dtAkhir);
                map.put("year", tahun);
                map.put("divisi", div);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
