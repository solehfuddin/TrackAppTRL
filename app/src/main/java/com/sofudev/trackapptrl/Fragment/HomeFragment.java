package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_bestproduct;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_dashboard;
import com.sofudev.trackapptrl.Adapter.Adapter_homecategory;
import com.sofudev.trackapptrl.Adapter.Adapter_homeproduct;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Custom.OnFragmentInteractionListener;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_home_category;
import com.sofudev.trackapptrl.Data.Data_home_product;
import com.sofudev.trackapptrl.DetailProductActivity;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressCustom;
import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeFragment extends Fragment {

    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE  = 201;

    public int COUNTER_WISHLIST = 0;
    public int COUNTER_CART = 0;

    OnBadgeCounter badgeCounter;
    private OnFragmentInteractionListener mListener;
    BannerSlider banner_header;
    Config config  = new Config();

    String BANNER_URL = config.Ip_address + config.dashboard_banner_slide;
    String CATEGORY_URL = config.Ip_address + config.dashboard_category;
    String PRODUCT_URL  = config.Ip_address + config.dashboard_product_home;
    String HOTSALE_URL  = config.Ip_address + config.dashboard_hot_sale;
    String BRANDRANDOM_URL = config.Ip_address + config.dashboard_brand_random;
    String GETACTIVESALE_URL = config.Ip_address + config.flashsale_getActiveSale;
    String GETIMAGEPROMO_IRL = config.Ip_address + config.flashsale_getImagePromo;

    RecyclerView recyclerView_category, recyclerView_product;
    CountdownView count_flashsale;
    LinearLayout linear_flashsale, linear_hotsale;

    LinearLayoutManager horizontal_manager;
    Adapter_homecategory adapter_homecategory;
    Adapter_homeproduct adapter_homeproduct;
    Adapter_framefragment_dashboard adapter_hotsale_product;
    Adapter_framefragment_bestproduct adapter_framefragment_bestproduct;

    List<Data_home_category> list = new ArrayList<>();
    List<Data_home_product> list_product = new ArrayList<>();
    List<Data_fragment_bestproduct> list_brandrandom = new ArrayList<>();
    List<Data_fragment_bestproduct> list_hotsale = new ArrayList<>();

    ACProgressCustom loading;
    String ACTIVITY_TAG, banner_promo;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
//        getActivity().setTitle("");
//
//        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(getContext()));
//
//        banner_header = (BannerSlider) thisView.findViewById(R.id.fragment_home_headerslider);
//        recyclerView_category = (RecyclerView) thisView.findViewById(R.id.fragment_home_recyclercategory);
//        recyclerView_category.setHasFixedSize(true);
//        recyclerView_product  = (RecyclerView) thisView.findViewById(R.id.fragment_home_recyclerproducts);
//        recyclerView_product.setHasFixedSize(true);
//
//        count_flashsale = thisView.findViewById(R.id.fragment_home_countdown);
//        linear_flashsale= (LinearLayout) thisView.findViewById(R.id.fragment_home_linearSale);
//        linear_hotsale  = (LinearLayout) thisView.findViewById(R.id.fragment_home_linearhotsale);
//
////        DynamicConfig dynamicConfig = new DynamicConfig.Builder().setConvertDaysToHours(true).build();
////        count_flashsale.dynamicShow(dynamicConfig);
//
//        //Make horizontal on recyclerview
//        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView_category.setLayoutManager(horizontal_manager);
//
//        //Make vertical recycler with grid
//        RecyclerView.LayoutManager verticalgridLayout = new GridLayoutManager(getContext(), 2);
//        recyclerView_product.setLayoutManager(verticalgridLayout);
////        recyclerView_product.setNestedScrollingEnabled(false);
//        recyclerView_product.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
//        recyclerView_product.setItemAnimator(new DefaultItemAnimator());
//
//        getData();
//
//        adapter_hotsale_product = new Adapter_framefragment_dashboard(getContext(), list_hotsale,
//                new RecyclerViewOnClickListener() {
//                    @Override
//                    public void onItemClick(View view, int pos, String id) {
////                Toasty.success(getContext(), list_hotsale.get(pos).getProduct_id(), Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
////
////                        startActivity(intent);
//
////                        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
////                        intent.putExtra("id_lensa", list_hotsale.get(pos).getProduct_id());
////                        startActivity(intent);
//
//                        if (ACTIVITY_TAG.equals("main"))
//                        {
//                            Toasty.warning(getActivity(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
////                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
////                            Bundle bundle = new Bundle();
////                            bundle.putString("from", "0");
////                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
////                            detailFrameFragment.setArguments(bundle);
////
////                            getActivity().getSupportFragmentManager().beginTransaction()
////                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
////                                    .addToBackStack(null)
////                                    .commit();
//
//                            Intent intent = new Intent(getContext(), DetailProductActivity.class);
//                            intent.putExtra("id", Integer.valueOf(list_hotsale.get(pos).getProduct_id()));
////                            startActivity(intent);
//                            startActivityForResult(intent, REQUEST_CODE);
//                        }
//                    }
//                }, ACTIVITY_TAG);
//
//        adapter_framefragment_bestproduct = new Adapter_framefragment_bestproduct(getContext(), list_brandrandom,
//                new RecyclerViewOnClickListener() {
//                    @Override
//                    public void onItemClick(View view, int pos, String id) {
////                        Toasty.info(getContext(), list_brandrandom.get(pos).getProduct_id(), Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
////
////                        startActivity(intent);
//
////                        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
////                        intent.putExtra("id_lensa", list_brandrandom.get(pos).getProduct_id());
////                        startActivity(intent);
//
//                        if (ACTIVITY_TAG.equals("main"))
//                        {
//                            Toasty.warning(getActivity(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
////                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
////                            Bundle bundle = new Bundle();
////                            bundle.putString("product_id", list_brandrandom.get(pos).getProduct_id());
////                            detailFrameFragment.setArguments(bundle);
////
////                            getActivity().getSupportFragmentManager().beginTransaction()
////                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
////                                    .addToBackStack(null)
////                                    .commit();
//
//                            Intent intent = new Intent(getContext(), DetailProductActivity.class);
//                            intent.putExtra("id", Integer.valueOf(list_brandrandom.get(pos).getProduct_id()));
////                            startActivity(intent);
//                            startActivityForResult(intent, REQUEST_CODE);
//                        }
//                    }
//                }, ACTIVITY_TAG);
//
//
//
//        //adapter_brand_product = new Adapter_framefragment_brand(getContext(), list_brandavail);
//
//        showLoading();
//        showBannerFromDb();
////        showFromDb();
//        //showProduct();
//
//        showHotsale();
//        showBrandrandom();
//        getDurationSale();
//        getImagePromo();
//
////        badgeCounter.countCartlist(COUNTER_CART);
////        badgeCounter.countWishlist(COUNTER_WISHLIST);
////
////        Log.d("Sent HOME Wishlist : ", String.valueOf(COUNTER_WISHLIST));
////        Log.d("Sent HOME Cart : ", String.valueOf(COUNTER_CART));
//
////        Intent intent1 = new Intent();
////        intent1.putExtra("counter", COUNTER_WISHLIST);
////        getActivity().setResult(1, intent1);

//        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getActivity().setTitle("");

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(getContext()));

        banner_header = (BannerSlider) view.findViewById(R.id.fragment_home_headerslider);
        recyclerView_category = (RecyclerView) view.findViewById(R.id.fragment_home_recyclercategory);
        recyclerView_category.setHasFixedSize(true);
        recyclerView_product  = (RecyclerView) view.findViewById(R.id.fragment_home_recyclerproducts);
        recyclerView_product.setHasFixedSize(true);

        count_flashsale = view.findViewById(R.id.fragment_home_countdown);
        linear_flashsale= (LinearLayout) view.findViewById(R.id.fragment_home_linearSale);
        linear_hotsale  = (LinearLayout) view.findViewById(R.id.fragment_home_linearhotsale);

//        DynamicConfig dynamicConfig = new DynamicConfig.Builder().setConvertDaysToHours(true).build();
//        count_flashsale.dynamicShow(dynamicConfig);

        //Make horizontal on recyclerview
        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_category.setLayoutManager(horizontal_manager);

        //Make vertical recycler with grid
        RecyclerView.LayoutManager verticalgridLayout = new GridLayoutManager(getContext(), 2);
        recyclerView_product.setLayoutManager(verticalgridLayout);
//        recyclerView_product.setNestedScrollingEnabled(false);
        recyclerView_product.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView_product.setItemAnimator(new DefaultItemAnimator());

        getData();

        adapter_hotsale_product = new Adapter_framefragment_dashboard(getContext(), list_hotsale,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
//                Toasty.success(getContext(), list_hotsale.get(pos).getProduct_id(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//
//                        startActivity(intent);

//                        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
//                        intent.putExtra("id_lensa", list_hotsale.get(pos).getProduct_id());
//                        startActivity(intent);

                        if (ACTIVITY_TAG.equals("main"))
                        {
                            Toasty.warning(view.getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
//                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("from", "0");
//                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
//                            detailFrameFragment.setArguments(bundle);
//
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
//                                    .addToBackStack(null)
//                                    .commit();

                            Intent intent = new Intent(getContext(), DetailProductActivity.class);
                            intent.putExtra("id", Integer.valueOf(list_hotsale.get(pos).getProduct_id()));
//                            startActivity(intent);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                }, ACTIVITY_TAG);

        adapter_framefragment_bestproduct = new Adapter_framefragment_bestproduct(getContext(), list_brandrandom,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
//                        Toasty.info(getContext(), list_brandrandom.get(pos).getProduct_id(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//
//                        startActivity(intent);

//                        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
//                        intent.putExtra("id_lensa", list_brandrandom.get(pos).getProduct_id());
//                        startActivity(intent);

                        if (ACTIVITY_TAG.equals("main"))
                        {
                            Toasty.warning(view.getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
//                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("product_id", list_brandrandom.get(pos).getProduct_id());
//                            detailFrameFragment.setArguments(bundle);
//
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
//                                    .addToBackStack(null)
//                                    .commit();

                            Intent intent = new Intent(getContext(), DetailProductActivity.class);
                            intent.putExtra("id", Integer.valueOf(list_brandrandom.get(pos).getProduct_id()));
//                            startActivity(intent);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                }, ACTIVITY_TAG);



        //adapter_brand_product = new Adapter_framefragment_brand(getContext(), list_brandavail);

        showLoading();
        showBannerFromDb();
//        showFromDb();
        //showProduct();

        showHotsale();
        showBrandrandom();
        getDurationSale();
        getImagePromo();

//        badgeCounter.countCartlist(COUNTER_CART);
//        badgeCounter.countWishlist(COUNTER_WISHLIST);
//
//        Log.d("Sent HOME Wishlist : ", String.valueOf(COUNTER_WISHLIST));
//        Log.d("Sent HOME Cart : ", String.valueOf(COUNTER_CART));

//        Intent intent1 = new Intent();
//        intent1.putExtra("counter", COUNTER_WISHLIST);
//        getActivity().setResult(1, intent1);
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

    private void getDurationSale()
    {
        StringRequest request = new StringRequest(Request.Method.POST, GETACTIVESALE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        linear_flashsale.setVisibility(View.GONE);
                        linear_hotsale.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        int durr = object.getInt("expired");
                        banner_promo = object.getString("banner");

                        linear_flashsale.setVisibility(View.VISIBLE);
                        linear_hotsale.setVisibility(View.GONE);

//                        dialogPromo();

                        count_flashsale.start(durr);
                        count_flashsale.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                linear_flashsale.setVisibility(View.GONE);

                                linear_hotsale.setVisibility(View.VISIBLE);

                                Fragment frg = null;
                                frg = getFragmentManager().findFragmentByTag("home");
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commitAllowingStateLoss();

                                horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                recyclerView_category.setLayoutManager(horizontal_manager);

                                RecyclerView.LayoutManager verticalgridLayout = new GridLayoutManager(getContext(), 2);
                                recyclerView_product.setLayoutManager(verticalgridLayout);
                                recyclerView_product.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
                                recyclerView_product.setItemAnimator(new DefaultItemAnimator());

                                showHotsale();
                                showBrandrandom();
                            }
                        });
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
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_WISHLIST && resultCode == RESULT_CODE_WISHLIST)
//        {
//            int counter = data.getIntExtra(COUNTER_WISHLIST, 0);
////            txt_countwishlist.setText(" " + counter + " ");
//
//            Log.d("COUNTER WISHLIST : ", String.valueOf(counter));
//        }
//        else if (requestCode == REQUEST_CODE_CART && resultCode == RESULT_CODE_CART)
//        {
//            int counter = data.getIntExtra(COUNTER_CART, 0);
////            txt_countCart.setText(" " + counter + " ");
//            Log.d("COUNTER CART : ", String.valueOf(counter));
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE)
        {
            COUNTER_WISHLIST = data.getIntExtra("counter1", 0);
            COUNTER_CART     = data.getIntExtra("counter2", 0);
//            txt_countwishlist.setText(" " + counter + " ");
//            Log.d("HOME Wishlist : ", String.valueOf(counter));
        }

//        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE)
//        {
//            COUNTER_CART = data.getIntExtra("counter2", 0);
////            txt_countCart.setText(" " + counter + " ");
////            Log.d("HOME CartList : ", String.valueOf(counter));
//        }

        Log.d("HOME Wishlist : ", String.valueOf(COUNTER_WISHLIST));
        Log.d("HOME Cart : ", String.valueOf(COUNTER_CART));
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");
        }
    }

    private void showBannerFromDb()
    {
        final StringRequest string = new StringRequest(BANNER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Banner> banners = new ArrayList<>();
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);

                        String dt_url = jsonObj.getString("img_source");

                        banners.add(new RemoteBanner(dt_url));
                        banners.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    banner_header.setBanners(banners);
                    hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                try {
//                    Thread.sleep(3000);
//                    hideLoading();
//                    information("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
//                            DefaultBootstrapBrand.WARNING);
//
//                    error.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                error.printStackTrace();
            }
        });

        string.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(string);
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

    private void showFromDb()
    {
        //list.clear();
        StringRequest string = new StringRequest(CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String dt_id = jsonObject.getString("title");
                        String dt_img = jsonObject.getString("image");

                        Data_home_category item = new Data_home_category();
                        item.setTitle(dt_id);
                        item.setImage(dt_img);

                        list.add(item);
                    }

                    adapter_homecategory = new Adapter_homecategory(getContext(), list);
                    //adapter_homecategory.notifyDataSetChanged();
                    recyclerView_category.setAdapter(adapter_homecategory);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        string.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(string);
    }

    private void showHotsale()
    {
        list_hotsale.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOTSALE_URL, new Response.Listener<String>() {
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
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc);
                        item.setProduct_brand(frameBrand);
                        item.setProduct_qty(frameQty);
                        item.setProduct_weight(frameWeight);

                        list_hotsale.add(item);
                    }

                    hideLoading();
                    adapter_hotsale_product.notifyDataSetChanged();
                    recyclerView_category.setAdapter(adapter_hotsale_product);

                    if (badgeCounter != null)
                    {
                        badgeCounter.countWishlist(COUNTER_WISHLIST);
                        badgeCounter.countCartlist(COUNTER_CART);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        stringRequest.setShouldCache(false);
//        Volley.newRequestQueue(getContext()).add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showBrandrandom()
    {
        list_brandrandom.clear();
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
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc);
                        item.setProduct_brand(frameBrand);
                        item.setProduct_qty(frameQty);
                        item.setProduct_weight(frameWeight);

                        list_brandrandom.add(item);
                    }

                    adapter_framefragment_bestproduct.notifyDataSetChanged();
                    recyclerView_product.setAdapter(adapter_framefragment_bestproduct);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        stringRequest.setShouldCache(false);
//        Volley.newRequestQueue(getContext()).add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getImagePromo()
    {
        StringRequest stringRequest = new StringRequest(GETIMAGEPROMO_IRL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respon Promo", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.names().get(0).equals("error"))
                        {
                            Log.d("Info", "Tidak ada promo");
                        }
                        else
                        {
                            String image = jsonObject.getString("image");

                            if (!image.isEmpty())
                            {
                                Log.d("Image Promo", "link : " + image);

                                dialogPromo(image);
                            }
                        }
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
        });

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showLoading()
    {
        loading = new ACProgressCustom.Builder(getActivity())
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(40)
                .build();

        if(getActivity().isFinishing()){
            loading.show();
        }
    }

    private void hideLoading()
    {
        loading.dismiss();
    }

    public void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        if(getActivity() != null){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.info_status);
            dialog.setCancelable(false);

            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            img_status      = (ImageView) dialog.findViewById(R.id.info_status_imageview);
            txt_information = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtInformation);
            txt_message     = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtMessage);
            btn_ok          = (BootstrapButton) dialog.findViewById(R.id.info_status_btnOk);

            img_status.setImageResource(resource);
            txt_information.setText(info);
            txt_message.setText(message);
            btn_ok.setBootstrapBrand(defaultcolorbtn);

            showLoading();

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFromDb();
                    showBannerFromDb();
                    //showProduct();
                    dialog.dismiss();
                }
            });

            if (!getActivity().isFinishing())
            {
                dialog.show();
            }
        }
    }

    private void dialogPromo(String url)
    {
        if (getContext() != null)
        {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        Window window = getDialog().getWindow();
//        int width = displayMetrics.widthPixels;
//        int height= displayMetrics.heightPixels;
//
//        int dialogWidth = (int) (width * 0.7f);
//        int dialogHeight= (int) (height * 0.7f);

            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

            dialog.setContentView(R.layout.dialog_promo);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            lwindow.copyFrom(dialog.getWindow().getAttributes());
            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            lwindow.height= WindowManager.LayoutParams.MATCH_PARENT;

            ImageView imgClose = dialog.findViewById(R.id.dialog_promo_imgClose);
            ImageView imgBgDialogSale = dialog.findViewById(R.id.dialog_promo_imgBg);
            ProgressWheel progressDialogSale = dialog.findViewById(R.id.dialog_promo_progressBar);
            progressDialogSale.setVisibility(View.GONE);

//        String url = "http://180.250.96.154/trl-webs/assets/images/promo/extra_diskon_frame.jpg";
//        String uri = "http://www.timurrayalab.com/dev/uploads/img/127.jpg";
            Picasso.with(getContext()).load(url).into(imgBgDialogSale);

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            if (!getActivity().isFinishing())
            {
                dialog.show();
            }
            dialog.getWindow().setAttributes(lwindow);
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
