package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.sofudev.trackapptrl.Adapter.Adapter_promo_frame;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
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

import es.dmoral.toasty.Toasty;

public class PromoFrameFragment extends Fragment {

    public static final int REQUEST_CODE = 101;

    Config config = new Config();
    String URLPOPULARFRAME = config.Ip_address + config.dashboard_popular_frame;

    UniversalFontTextView txtMore, txtTitle, txtIdTag;
    RecyclerView recyclerView;
    ShimmerRecyclerView shimmerRecyclerView;
    CardView cardBanner;

    GridLayoutManager gridLayoutManager;
    Adapter_promo_frame adapter_promo_frame;

    Context myContext;
    List<Data_fragment_bestproduct> itemProduct = new ArrayList<>();

    String ACTIVITY_TAG, ACCESS_FROM;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_frame, container, false);
        shimmerRecyclerView = view.findViewById(R.id.fragment_promoframe_shimmer);
        recyclerView = view.findViewById(R.id.fragment_promoframe_recyclerview);
        cardBanner = view.findViewById(R.id.fragment_promoframe_banner1);
        txtMore = view.findViewById(R.id.fragment_promoframe_txtMore);
        txtTitle = view.findViewById(R.id.fragment_promoframe_txtTitle);
        txtIdTag = view.findViewById(R.id.fragment_promoframe_txtIdTag);
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
                    intent.putExtra("activity", ACTIVITY_TAG);
                    intent.putExtra("tag_name", txtTitle.getText());
                    intent.putExtra("tag_id", txtIdTag.getText());
                    startActivity(intent);
                }
            }
        });

        cardBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ACTIVITY_TAG.equals("main"))
                if (ACCESS_FROM.equals("main"))
                {
                    Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getContext(), FrameProductActivity.class);
                    intent.putExtra("activity", ACTIVITY_TAG);
                    intent.putExtra("tag_name", "ALL FRAME");
                    intent.putExtra("tag_id", "");
                    startActivity(intent);
                }
            }
        });

        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter_promo_frame = new Adapter_promo_frame(myContext, itemProduct, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
//                if (ACTIVITY_TAG.equals("main"))
                if (ACCESS_FROM.equals("main"))
                {
                    Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("Item click = ", itemProduct.get(pos).getProduct_id());
                    if (itemProduct.get(pos).getProduct_id().equals("0"))
                    {
//                        Toasty.info(myContext, "Buka menu frame", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), FrameProductActivity.class);
                        intent.putExtra("activity", ACTIVITY_TAG);
                        intent.putExtra("tag_name", "ALL FRAME");
                        intent.putExtra("tag_id", "");
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

        recyclerView.setAdapter(adapter_promo_frame);

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

    private void showData()
    {
        itemProduct.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLPOPULARFRAME, new Response.Listener<String>() {
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

                        txtTitle.setText(jsonObject.getString("frame_tag").toUpperCase());
                        txtIdTag.setText(jsonObject.getString("frame_tagid"));

//                        txtTitle.setVisibility(View.INVISIBLE);
//                        txtIdTag.setVisibility(View.VISIBLE);
                    }

                    shimmerRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_promo_frame.notifyDataSetChanged();
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