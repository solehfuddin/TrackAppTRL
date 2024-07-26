package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_banner_custom;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Decoration.MyBanner;
import com.sofudev.trackapptrl.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BannerFragment extends Fragment {

    Config config = new Config();
    String BANNER_URL = config.Ip_address + config.dashboard_banner_slide;

    private Adapter_banner_custom mAdapter;
    private MyBanner bannerSlider;
    private ShimmerRecyclerView shimmerLayout;
    private WormDotsIndicator myDots;

    List<Fragment> fragments = new ArrayList<>();

    public BannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);

        bannerSlider = view.findViewById(R.id.layout_custom_mybanner);
        myDots       = view.findViewById(R.id.layout_custom_mydots);
        shimmerLayout= view.findViewById(R.id.layout_custom_shimmer);

//        shimmerLayout.setVisibility(View.VISIBLE);
//        bannerSlider.setVisibility(View.INVISIBLE);

        setupSlider();
        return view;
    }

    private void setupSlider() {
        bannerSlider.setDurationScroll(700);
//        ViewGroup.LayoutParams params = bannerSlider.getLayoutParams();
//        params.height = 800;
//        bannerSlider.setLayoutParams(params);
//        bannerSlider.requestLayout();

        if (getFragmentManager() != null) {
            mAdapter = new Adapter_banner_custom(getFragmentManager(), fragments);
        }
        bannerSlider.setAdapter(mAdapter);

        myDots.setViewPager(bannerSlider);

        showBannerFromDb();
    }

    private void showBannerFromDb()
    {
        final StringRequest string = new StringRequest(BANNER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);

                        String dt_url = jsonObj.getString("img_source");

                        fragments.add(CustomHomeFragment.newInstance(dt_url));
                    }

                    try {
                        Thread.sleep(3000);
                        bannerSlider.setVisibility(View.VISIBLE);
//                        shimmerLayout.setVisibility(View.INVISIBLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyDataSetChanged();
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

    public void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        if(getContext() != null){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.info_status);
            dialog.setCancelable(false);

            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            img_status      = dialog.findViewById(R.id.info_status_imageview);
            txt_information = dialog.findViewById(R.id.info_status_txtInformation);
            txt_message     = dialog.findViewById(R.id.info_status_txtMessage);
            btn_ok          = dialog.findViewById(R.id.info_status_btnOk);

            img_status.setImageResource(resource);
            txt_information.setText(info);
            txt_message.setText(message);
            btn_ok.setBootstrapBrand(defaultcolorbtn);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBannerFromDb();
                    //showProduct();
                    dialog.dismiss();
                }
            });

            if (getActivity().isFinishing()){
                dialog.show();
            }
        }
    }
}