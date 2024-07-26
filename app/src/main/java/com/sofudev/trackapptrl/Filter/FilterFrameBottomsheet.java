package com.sofudev.trackapptrl.Filter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
//import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.sofudev.trackapptrl.Adapter.Adapter_brandfilter;
import com.sofudev.trackapptrl.Adapter.Adapter_colorfilter;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_brand_filter;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FilterFrameBottomsheet extends BottomSheetDialog implements View.OnClickListener {

    private Config config = new Config();
    private String showColorUrl = config.Ip_address + config.frame_showcolor_filter;
    String showBrandUrl = config.Ip_address + config.frame_showbrand_filter;

    private Context context;
    private String valueMin = "0", valueMax = "7000000", chooseColor, chooseBrand;

    private BootstrapButton btnApply;
    private RecyclerView recyclerColor, recyclerBrand;
    private EditText txtRangeMax;
//    private CrystalRangeSeekbar rangePrice;

    private Adapter_colorfilter adapter_colorfilter;
    private Adapter_brandfilter adapter_brandfilter;

    private List<Data_color_filter> dataColor = new ArrayList<>();
    private List<Integer> listColor = new ArrayList<>();
    List<Data_brand_filter> dataBrand = new ArrayList<>();
    List<String> listBrand = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private static FilterFrameBottomsheet instance;

    public static FilterFrameBottomsheet getInstance(@NonNull Context context) {
        return instance == null ? new FilterFrameBottomsheet(context) : instance;
    }

    public FilterFrameBottomsheet(@NonNull Context context) {
        super(context);
        this.context = context;

        showColor();
        showBrand();

        try {
            Thread.sleep(1000);
            create();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void create()
    {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetBehavior.setPeekHeight(2000);
        btnApply = bottomSheetView.findViewById(R.id.dialog_filter_btnApply);

//        rangePrice    = bottomSheetView.findViewById(R.id.dialog_filter_rangebar);
       // txtRangeMin   = bottomSheetView.findViewById(R.id.dialog_filter_txtrangemin);
        txtRangeMax   = bottomSheetView.findViewById(R.id.dialog_filter_txtrangemax);

        recyclerColor = bottomSheetView.findViewById(R.id.dialog_filter_recyclerColor);
        recyclerBrand = bottomSheetView.findViewById(R.id.dialog_filter_recyclerBrand);

//        rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();

//        recyclerColor.setLayoutManager(new GridLayoutManager(getContext(), 6));
//        adapter_colorfilter = new Adapter_colorfilter(getContext(), dataColor, customAdapterFrameColor, chooseColor);
//        recyclerColor.setAdapter(adapter_colorfilter);

//        recyclerBrand.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        adapter_brandfilter = new Adapter_brandfilter(getContext(), dataBrand, customAdapterFrameFragment, chooseBrand);
//        recyclerBrand.setAdapter(adapter_brandfilter);

//        txtRangeMin.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
//                {
//                    valueMin = removeRupiah(txtRangeMin.getText().toString());
//                    valueMax = removeRupiah(txtRangeMax.getText().toString());
//
////                    rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();
//
//                    txtRangeMin.setText("Rp " + valueMin);
//                }
//
//                return false;
//            }
//        });

        txtRangeMax.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
//                    valueMin = removeRupiah(txtRangeMin.getText().toString());
                    valueMax = removeRupiah(txtRangeMax.getText().toString());

//                    rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();

                    txtRangeMax.setText("Rp " + valueMax);
                }

                return false;
            }
        });

        listBrand.clear();
        listColor.clear();

//        selectRangePrice();
        btnApply.setOnClickListener(this);
    }

    private void showBrand()
    {
        dataBrand.clear();

        StringRequest request = new StringRequest(Request.Method.POST, showBrandUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Data_brand_filter data = new Data_brand_filter();
                        data.setBrandId(jsonObject.getInt("brand_id"));
                        data.setBrandName(jsonObject.getString("brand_name"));
                        data.setBrandPrefix(jsonObject.getString("brand_prefix"));

                        dataBrand.add(data);
                    }

//                    adapter_brandfilter = new Adapter_brandfilter(getContext(), dataBrand, customAdapterFrameFragment, chooseBrand);
                    adapter_brandfilter.notifyDataSetChanged();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showColor()
    {
        dataColor.clear();
        StringRequest request = new StringRequest(Request.Method.POST, showColorUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("color_id");
                        String color = jsonObject.getString("color_code");
                        String name  = jsonObject.getString("color_name");

//                        Toasty.info(getContext(), color, Toast.LENGTH_SHORT).show();

                        Data_color_filter item = new Data_color_filter();
                        item.setColorCode(color);
                        item.setColorId(id);
                        item.setColorName(name);

                        dataColor.add(item);
                    }

                    adapter_colorfilter.notifyDataSetChanged();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
    }

//    private void selectRangePrice()
//    {
//        rangePrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                txtRangeMin.setText("Rp " + String.valueOf(minValue));
//                txtRangeMax.setText("Rp " + String.valueOf(maxValue));
//            }
//        });
//    }

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "");

        return output;
    }

//    CustomAdapterFrameBrand customAdapterFrameFragment = new CustomAdapterFrameBrand() {
//        @Override
//        public void response(int position, String title) {
////            Toasty.info(getContext(), "Pos " + position + " title " + title, Toast.LENGTH_SHORT).show();
//            //listBrand.add(position);
//
//        }
//    };

//    CustomAdapterFrameBrand customAdapterFrameFragment  = new CustomAdapterFrameBrand() {
//        @Override
//        public void response(String position, String title) {
//            listBrand.add(position);
//        }
//    };

//    CustomAdapterFrameColor customAdapterFrameColor = new CustomAdapterFrameColor() {
//        @Override
//        public void response(int position, String title) {
//            listColor.add(position);
//        }
//    };

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.dialog_filter_btnApply:
////                valueMin = removeRupiah(txtRangeMin.getText().toString());
//                valueMax = removeRupiah(txtRangeMax.getText().toString());
//
//                Object[] allBrand = listBrand.toArray();
//                Object[] allColor = listColor.toArray();
//
//
//                hide();
//                break;
//        }
    }
}
