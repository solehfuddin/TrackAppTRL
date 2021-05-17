package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.sofudev.trackapptrl.Adapter.Adapter_promo_banner;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_promo_banner;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromoBannerFragment extends Fragment {
    private Config config = new Config();
    private String URLGETPROMO = config.Ip_address + config.dashboard_getpromo_banner;

    RecyclerView recyclerView;

    private Context myContext;
    private Adapter_promo_banner adapter_promo_banner;
    private List<Data_promo_banner> itemBanner = new ArrayList<>();

    public PromoBannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPromoBanner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_banner, container, false);
        recyclerView = view.findViewById(R.id.fragment_promobanner_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter_promo_banner = new Adapter_promo_banner(myContext, itemBanner, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {

            }
        });

        recyclerView.setAdapter(adapter_promo_banner);


//        autoScrollRecyclerView = view.findViewById(R.id.fragment_promobanner_autorecyclerview);
//        autoScrollRecyclerView.setLayoutManager(linearLayoutManager);
//        autoScrollRecyclerView.setReverse(true);
//        autoScrollRecyclerView.setLoopEnabled(true);
//        autoScrollRecyclerView.setAdapter(adapter_promo_banner);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!recyclerView.canScrollHorizontally(1))
//                {
//                    getPromoBanner();
//                }
//            }
//        });

        return view;
    }

    private void getPromoBanner()
    {
//        itemBanner.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETPROMO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Data_promo_banner data = new Data_promo_banner();
                        data.setUrl(object.getString("url"));

                        itemBanner.add(data);
                    }

                    adapter_promo_banner.notifyDataSetChanged();

                    Log.d("Promo Banner Log", response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}