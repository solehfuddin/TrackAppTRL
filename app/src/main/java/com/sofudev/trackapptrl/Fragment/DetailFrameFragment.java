package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_another_product;
import com.sofudev.trackapptrl.Adapter.Adapter_colordetail;
import com.sofudev.trackapptrl.Adapter.Adapter_slider_product;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_recent_view;
import com.sofudev.trackapptrl.Form.AddCartProductActivity;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.RecentViewHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;
import com.sofudev.trackapptrl.R;
import com.varunest.sparkbutton.SparkButton;

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
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class DetailFrameFragment extends Fragment {

    OnBadgeCounter badgeCounter;
    Config config = new Config();

    String URLDETAILPRODUCT = config.Ip_address + config.frame_showdetail_product;
    String URLDETAILCOLOR   = config.Ip_address + config.frame_showdetail_color;
    String HOTSALEURL       = config.Ip_address + config.dashboard_hot_sale;

    ViewPager pagerImage;
    RippleView btnCart;

    UniversalFontTextView txtLenscode, txtDiscPrice, txtRealPrice, txtDisc, txtBrand, txtAvailability, txtDescription;
    RecyclerView recyColor, recyAnotherProduct;

    private static int currentPage = 0, frameId;
    private static int NUM_PAGES = 0;
    String frameName, frameSku, frameImage, framePrice, frameDisc, frameDiscPrice, frameAvailibilty,
            frameBrand, frameColor, frameQty, frameWeight;

    LinearLayoutManager horizontal_manager;
    List<String> allImg = new ArrayList<>();
    List<Data_color_filter> allColor = new ArrayList<>();
    List<Data_fragment_bestproduct> list_hotsale = new ArrayList<>();
    Adapter_another_product adapter_hotsale_product;
    Adapter_slider_product adapter_slider_product;
    Adapter_colordetail adapter_colordetail;

    //Lokal db
    AddCartHelper addCartHelper;
    RecentViewHelper recentViewHelper;

    public DetailFrameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        badgeCounter = (OnBadgeCounter) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        badgeCounter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_frame, container, false);

        final String from     = getArguments().getString("from");
        String value = getArguments().getString("product_id");
        //Toasty.info(getContext(), value, Toast.LENGTH_SHORT).show();

        pagerImage  = view.findViewById(R.id.fragment_detailproduct_imgProduct);
        btnCart     = view.findViewById(R.id.fragment_detailproduct_btnCart);

        txtLenscode = view.findViewById(R.id.fragment_detailproduct_txtLenscode);
        txtDiscPrice= view.findViewById(R.id.fragment_detailproduct_txtDiscPrice);
        txtRealPrice= view.findViewById(R.id.fragment_detailproduct_txtRealPrice);
        txtDisc     = view.findViewById(R.id.fragment_detailproduct_txtDisc);
        txtBrand    = view.findViewById(R.id.fragment_detailproduct_txtBrandName);
        txtAvailability = view.findViewById(R.id.fragment_detailproduct_txtAvailability);
        txtDescription = view.findViewById(R.id.fragment_detailproduct_txtDescription);
        recyColor   = view.findViewById(R.id.fragment_detailproduct_recyclerColor);
        recyAnotherProduct = view.findViewById(R.id.fragment_detailproduct_recyclerAnother);

        showProductDetail(value);
        showHotsale();

        adapter_slider_product = new Adapter_slider_product(allImg, getContext());
        adapter_colordetail = new Adapter_colordetail(allColor, getContext());
        pagerImage.setAdapter(adapter_slider_product);

        recyColor.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyColor.setAdapter(adapter_colordetail);

        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyAnotherProduct.setLayoutManager(horizontal_manager);
        recyAnotherProduct.setHasFixedSize(true);

        NUM_PAGES = allImg.size();

        addCartHelper = AddCartHelper.getINSTANCE(getContext());
        recentViewHelper = RecentViewHelper.getINSTANCE(getContext());
        recentViewHelper.open();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }

                pagerImage.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

        adapter_hotsale_product = new Adapter_another_product(getContext(), list_hotsale,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
//                        Intent intent = new Intent(getContext(), DetailProductActivity.class);
//                        intent.putExtra("id_lensa", list_hotsale.get(pos).getProduct_id());
//                        startActivity(intent);

                        if (from.contentEquals("0"))
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "0");
                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .remove(detailFrameFragment)
                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "1");
                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .remove(detailFrameFragment)
                                    .replace(R.id.detailproduct_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });

        recyAnotherProduct.setAdapter(adapter_hotsale_product);


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCartHelper.open();

                ModelAddCart item = new ModelAddCart();
                item.setProductId(frameId);
                item.setProductName(frameName);
                item.setProductCode(frameSku);
                item.setProductQty(1);
                item.setProductPrice(Integer.valueOf(removeRupiah(framePrice)));
                item.setProductDiscPrice(Integer.valueOf(removeRupiah(frameDiscPrice)));
                item.setProductDisc(Integer.valueOf(removeDiskon(frameDisc)));
                item.setNewProductPrice(Integer.valueOf(removeRupiah(framePrice)));
                item.setNewProductDiscPrice(Integer.valueOf(removeRupiah(frameDiscPrice)));
                item.setProductStock(Integer.valueOf(frameQty));
                item.setProductWeight(Integer.valueOf(frameWeight));
                item.setProductImage(frameImage);

                long status = addCartHelper.insertAddCart(item);

                if (status > 0)
                {
                    Toasty.success(getContext(), "Item has been added to cart", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(getContext(), AddCartProductActivity.class);
//                    startActivity(intent);
                }

                int count = addCartHelper.countAddCart();
                if (badgeCounter != null)
                {
                    badgeCounter.countCartlist(count);
                }
            }
        });

        return view;
    }

    private void insertRecentSearch(Data_recent_view item)
    {
        recentViewHelper.insertRecentView(item);
    }

    private void showProductDetail(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDETAILPRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        frameId     = jsonObject.getInt("frame_id");
                        frameName   = jsonObject.getString("frame_name");
                        frameSku    = jsonObject.getString("frame_sku");
                        frameImage  = jsonObject.getString("frame_image");
                        framePrice  = jsonObject.getString("frame_price");
                        frameDisc   = jsonObject.getString("frame_disc");
                        frameDiscPrice = jsonObject.getString("frame_disc_price");
                        frameAvailibilty = jsonObject.getString("frame_availibilty");
                        frameBrand  = jsonObject.getString("frame_brand");
                        frameColor  = jsonObject.getString("frame_color");
                        frameQty    = jsonObject.getString("frame_qty");
                        frameWeight = jsonObject.getString("frame_weight");

                        allImg.add(frameImage);
                    }

                    frameDiscPrice = "Rp. " + CurencyFormat(frameDiscPrice);
                    framePrice     = "Rp. " + CurencyFormat(framePrice);
                    int disc = Integer.valueOf(frameDisc);
                    if (disc > 0)
                    {
                        frameDisc = frameDisc + " % ";
                        txtDisc.setVisibility(View.VISIBLE);
                        txtDisc.setText(frameDisc);

                        txtRealPrice.setVisibility(View.VISIBLE);
                        txtRealPrice.setPaintFlags(txtRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        txtRealPrice.setText(framePrice);
                    }
                    else
                    {
                        txtDisc.setVisibility(View.GONE);
                        txtRealPrice.setVisibility(View.GONE);
                    }

                    // String stock = "IN STOCK";
                    int qty = Integer.valueOf(frameQty);
                    if (qty > 0)
                    {
                        txtAvailability.setText("Tersedia");
                        txtAvailability.setTextColor(Color.parseColor("#45ac2d"));

                        btnCart.setEnabled(true);
                        btnCart.setBackgroundColor(Color.parseColor("#3395ff"));
                    }
                    else
                    {
                        txtAvailability.setText("Habis");
                        txtAvailability.setTextColor(Color.parseColor("#f90606"));

                        btnCart.setEnabled(false);
                        btnCart.setBackgroundColor(Color.parseColor("#757575"));
                    }

                    txtLenscode.setText(frameName);
                    txtDiscPrice.setText(frameDiscPrice);

                    txtBrand.setText(frameBrand);
                    txtDescription.setText(frameSku);

                    Data_recent_view dataRecentView = new Data_recent_view();
                    dataRecentView.setId(frameId);
                    dataRecentView.setImage(frameImage);

                    insertRecentSearch(dataRecentView);

//                    if (frameColor.contains("[") && frameColor.contains("]"))
//                    {
//                        frameColor.replace("[", "").replace("]", "");
//                    }

//                    Toasty.info(getApplicationContext(), "Color : " + frameColor, Toast.LENGTH_SHORT).show();

                    showProductColor(frameColor);

                    adapter_slider_product.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_lensa", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showProductColor(final String color)
    {
        allColor.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDETAILCOLOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0;i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("color_id");
                        String colorName = jsonObject.getString("color_name");
                        String colorCode = jsonObject.getString("color_code");

                        Data_color_filter item = new Data_color_filter();
                        item.setColorId(id);
                        item.setColorName(colorName);
                        item.setColorCode(colorCode);

                        allColor.add(item);
                    }

                    adapter_colordetail.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                Data_color_filter item = new Data_color_filter();
                item.setColorId(0);
                item.setColorName("Black");
                item.setColorCode("#000000");

                allColor.add(item);

                adapter_colordetail.notifyDataSetChanged();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("colorType", color);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showHotsale()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOTSALEURL, new Response.Listener<String>() {
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
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc);
                        item.setProduct_qty(frameQty);
                        item.setProduct_weight(frameWeight);

                        list_hotsale.add(item);
                    }

                    adapter_hotsale_product.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(stringRequest);
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

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "").replace(".","").trim();

        return output;
    }

    private String removeDiskon(String diskon)
    {
        String output;
        output = diskon.replace("%", "").trim();

        return output;
    }
}
