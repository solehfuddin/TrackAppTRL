package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_special_item;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.DetailProductActivity;
import com.sofudev.trackapptrl.FrameProductActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;

public class SpecialProductFragment extends Fragment {
    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE  = 201;

    public int COUNTER_WISHLIST = 0;
    public int COUNTER_CART = 0;

    Config config  = new Config();
    String BRANDRANDOM_URL = config.Ip_address + config.dashboard_brand_random;

    UniversalFontTextView txtMore;
    CountdownView txtCountDown;
    RecyclerView recyclerView;
    ShimmerRecyclerView shimmerRecyclerView;

    LinearLayoutManager horizontal_manager;
    Adapter_special_item adapter_special_item;

    Context myContext;
    List<Data_fragment_bestproduct> itemProduct = new ArrayList<>();

    String ACTIVITY_TAG, ACCESS_FROM;

    public SpecialProductFragment() {
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
        getData();
        showBrandrandom();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE)
        {
            if (data != null) {
                COUNTER_WISHLIST = data.getIntExtra("counter1", 0);
                COUNTER_CART     = data.getIntExtra("counter2", 0);
            }
        }


        Log.d("HOME Wishlist : ", String.valueOf(COUNTER_WISHLIST));
        Log.d("HOME Cart : ", String.valueOf(COUNTER_CART));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_product, container, false);

        shimmerRecyclerView = view.findViewById(R.id.fragment_specialproduct_shimmer);
        recyclerView = view.findViewById(R.id.fragment_specialproduct_recyclerview);
        txtCountDown = view.findViewById(R.id.fragment_specialproduct_countdown);
        txtMore      = view.findViewById(R.id.fragment_specialproduct_txtMore);
        txtCountDown.setVisibility(View.INVISIBLE);
        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ACTIVITY_TAG.equals("main"))
                if (ACCESS_FROM.equals("main"))
                {
                    Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getContext(), FrameProductActivity.class);
                    startActivity(intent);
                }
            }
        });

        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontal_manager);

        adapter_special_item = new Adapter_special_item(getContext(), itemProduct, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
//                if (ACTIVITY_TAG.equals("main"))
                if (ACCESS_FROM.equals("main"))
                {
                    Toasty.warning(view.getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("Item click = ", itemProduct.get(pos).getProduct_id());
                    if (itemProduct.get(pos).getProduct_id().equals("0"))
                    {
//                        Toasty.info(myContext, "Buka menu frame", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), FrameProductActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getContext(), DetailProductActivity.class);
                        intent.putExtra("id", Integer.valueOf(itemProduct.get(pos).getProduct_id()));
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            }
        }, ACTIVITY_TAG);

        recyclerView.setAdapter(adapter_special_item);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollHorizontally(1))
                {
                    Toasty.info(myContext, "Sudah data terakhir", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");
            ACCESS_FROM  = bundle.getString("access");
        }
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

    private void showBrandrandom()
    {
        itemProduct.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BRANDRANDOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String frameId      = jsonObject.getString("frame_id");
                        String frameName    = jsonObject.getString("frame_name");
                        String frameImage   = jsonObject.getString("frame_image");
                        String framePrice   = CurencyFormat(jsonObject.getString("frame_price"));
                        String frameDisc    = jsonObject.getString("frame_disc");
                        String frameBrand   = jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc);
                        item.setProduct_brand(frameBrand);
                        item.setProduct_qty(frameQty);
                        item.setProduct_weight(frameWeight);

                        itemProduct.add(item);
                    }

                    Data_fragment_bestproduct tambahan = new Data_fragment_bestproduct();
                    tambahan.setProduct_id("0");
                    tambahan.setProduct_name("kosong");
                    tambahan.setProduct_image("kosong");
                    tambahan.setProduct_realprice("Rp 0");
                    tambahan.setProduct_discpercent("0");
                    tambahan.setProduct_brand("kosong");
                    tambahan.setProduct_qty("0");
                    tambahan.setProduct_weight("0");
                    itemProduct.add(tambahan);

                    shimmerRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_special_item.notifyDataSetChanged();
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

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}