package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_bestproduct;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_dashboard;
import com.sofudev.trackapptrl.Adapter.Adapter_homecategory;
import com.sofudev.trackapptrl.Adapter.Adapter_homeproduct;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.OnFragmentInteractionListener;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_home_category;
import com.sofudev.trackapptrl.Data.Data_home_product;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    BannerSlider banner_header;
    Config config  = new Config();

    String BANNER_URL = config.Ip_address + config.dashboard_banner_slide;
    String CATEGORY_URL = config.Ip_address + config.dashboard_category;
    String PRODUCT_URL  = config.Ip_address + config.dashboard_product_home;
    String HOTSALE_URL  = config.Ip_address + config.dashboard_hot_sale;
    String BRANDRANDOM_URL = config.Ip_address + config.dashboard_brand_random;

    RecyclerView recyclerView_category, recyclerView_product;
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
    String ACTIVITY_TAG;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("");

        banner_header = (BannerSlider) thisView.findViewById(R.id.fragment_home_headerslider);
        recyclerView_category = (RecyclerView) thisView.findViewById(R.id.fragment_home_recyclercategory);
        recyclerView_category.setHasFixedSize(true);
        recyclerView_product  = (RecyclerView) thisView.findViewById(R.id.fragment_home_recyclerproducts);
        recyclerView_product.setHasFixedSize(true);

        //Make horizontal on recyclerview
        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_category.setLayoutManager(horizontal_manager);

        //Make vertical recycler with grid
        RecyclerView.LayoutManager verticalgridLayout = new GridLayoutManager(getContext(), 2);
        recyclerView_product.setLayoutManager(verticalgridLayout);
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
                            Toasty.warning(getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
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
                            Toasty.warning(getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("product_id", list_brandrandom.get(pos).getProduct_id());
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }, ACTIVITY_TAG);

        recyclerView_category.setAdapter(adapter_hotsale_product);

        //adapter_brand_product = new Adapter_framefragment_brand(getContext(), list_brandavail);
        recyclerView_product.setAdapter(adapter_framefragment_bestproduct);

        showLoading();
        showBannerFromDb();
//        showFromDb();
        //showProduct();

        showHotsale();
        showBrandrandom();
        return thisView;
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        ACTIVITY_TAG = bundle.getString("activity");
    }

    private void showBannerFromDb()
    {
        //banner_header.removeAllBanners();

        final StringRequest string = new StringRequest(BANNER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //banner_header.removeAllBanners();
                //banner_header.removeViewAt(0);
                /*List<Banner> bumpersbanner = new ArrayList<>();
                bumpersbanner.add(new DrawableBanner(R.drawable.bumperbanner));
                banner_header.setBanners(bumpersbanner);*/

                try {
                    List<Banner> banners = new ArrayList<>();
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);

                        String dt_url = jsonObj.getString("img_source");

                        //Toast.makeText(getApplicationContext(), dt_url, Toast.LENGTH_SHORT).show();

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
                try {
                    Thread.sleep(3000);
                    hideLoading();
                    information("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                            DefaultBootstrapBrand.WARNING);

                    error.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc + " %");

                        list_hotsale.add(item);
                    }

                    hideLoading();
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

    private void showBrandrandom()
    {
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
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc + " %");

                        list_brandrandom.add(item);
                    }

                    adapter_framefragment_bestproduct.notifyDataSetChanged();
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

//    private void showProduct()
//    {
//        //list_product.clear();
//        StringRequest stringRequest = new StringRequest(PRODUCT_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//
//                    for (int i = 0; i < jsonArray.length(); i++)
//                    {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String title = jsonObject.getString("title");
//                        String image = jsonObject.getString("image");
//
//                        Data_home_product data = new Data_home_product();
//                        data.setTitle(title);
//                        data.setImage(image);
//
//                        list_product.add(data);
//                    }
//
//                    adapter_homeproduct = new Adapter_homeproduct(getContext(), list_product);
//                    //adapter_homeproduct.notifyDataSetChanged();
//                    recyclerView_product.setAdapter(adapter_homeproduct);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        stringRequest.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }

    private void showLoading()
    {
        loading = new ACProgressCustom.Builder(getActivity())
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(40)
                .build();
        loading.show();
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

        final Dialog dialog = new Dialog(getContext());
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

        dialog.show();
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
