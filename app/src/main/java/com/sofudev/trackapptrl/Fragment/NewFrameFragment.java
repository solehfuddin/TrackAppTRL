package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.material.snackbar.Snackbar;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sofudev.trackapptrl.Adapter.Adapter_brandfilter;
import com.sofudev.trackapptrl.Adapter.Adapter_colorfilter;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_bestproduct;
import com.sofudev.trackapptrl.Adapter.Adapter_sortbyframe;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.BlackStyle;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomAdapterFrameBrand;
import com.sofudev.trackapptrl.Custom.CustomAdapterFrameColor;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.GridSpacingItemDecoration;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_brand_filter;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.DetailProductActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;
import viethoa.com.snackbar.BottomSnackBarMessage;

public class NewFrameFragment extends Fragment implements View.OnClickListener {

    Config config = new Config();
    Context myContext;

    String allDataUrl = config.Ip_address + config.frame_bestproduct_showAllData;
    String sortGroupUrl = config.Ip_address + config.frame_filterby_group;
    String sortPriceUrl = config.Ip_address + config.frame_filterby_price;
    String sortFilterUrl= config.Ip_address + config.frame_filterwith_sort;
    String showColorUrl = config.Ip_address + config.frame_showcolor_filter;
    String showBrandUrl = config.Ip_address + config.frame_showbrand_filter;
    String GETACTIVESALE_URL = config.Ip_address + config.flashsale_getActiveSale;

    RecyclerView recyclerView,recyclerColor, recyclerBrand;
    private EditText txtRangeMax;
    private SeekBar rangePrice;
    ProgressWheel progress;
    BootstrapButton btnApply, btnClear;
    AwesomeTextView btnFilter, btnSort;
    ImageView imgNotfound;
    LinearLayout linear_flashsale;
    CountdownView countdown_flashsale;
    CardView cardTop;
    ImageView imgTop;

    View view;
    Adapter_framefragment_bestproduct adapter_framefragment_bestproduct;
    Adapter_colorfilter adapter_colorfilter;
    Adapter_brandfilter adapter_brandfilter;
    Adapter_sortbyframe adapter_sortbyframe;
    List<Data_fragment_bestproduct> itemBestProduct = new ArrayList<>();
    List<Data_color_filter> dataColor = new ArrayList<>();
    List<Data_brand_filter> dataBrand = new ArrayList<>();
    Integer limit, from, hasil;
    String totalData, chooseBrand, sortCondition, filterPrice, filterBrand, filterColor, chooseColor, valueMin = "0",
            valueMax = "7000000";

    int pos = 0;
    int posSw = 0;
    String ACTIVITY_TAG, ACCESS_FROM;

    List<String> listSortBy = new ArrayList<>();
    List<String> listBrand = new ArrayList<>();
    List<String> listColor = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        TypefaceProvider.registerDefaultIconSets();
        view = inflater.inflate(R.layout.fragment_new_frame, container, false);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(getContext()));

        imgNotfound = view.findViewById(R.id.fragment_newframe_notfound);

        recyclerView = view.findViewById(R.id.fragment_newframe_recyclerview);
        recyclerView.setHasFixedSize(true);

        btnFilter = view.findViewById(R.id.fragment_newframe_btnfilter);
        btnSort = view.findViewById(R.id.fragment_newframe_btnsort);

        linear_flashsale = view.findViewById(R.id.fragment_newframe_linearSale);
        countdown_flashsale = view.findViewById(R.id.fragment_newframe_countdown);

        cardTop = view.findViewById(R.id.fragment_newframe_cartTop);
        imgTop  = view.findViewById(R.id.fragment_newframe_buttonTop);

        btnFilter.setBootstrapBrand(new BlackStyle(myContext));
        btnSort.setBootstrapBrand(new BlackStyle(myContext));

        btnFilter.setOnClickListener(this);
        btnSort.setOnClickListener(this);

        RecyclerView.LayoutManager verticalGrid = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(verticalGrid);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progress = view.findViewById(R.id.fragment_newframe_progressBar);
        getData();

        adapter_framefragment_bestproduct = new Adapter_framefragment_bestproduct(getContext(), itemBestProduct,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
//                        if (ACTIVITY_TAG.equals("main"))
                        if (ACCESS_FROM.equals("main"))
                        {
                            Toasty.warning(view.getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(getContext(), DetailProductActivity.class);
                            intent.putExtra("id", Integer.valueOf(itemBestProduct.get(pos).getProduct_id()));
                            startActivity(intent);
                        }
                    }
                }, ACTIVITY_TAG);

        recyclerView.setAdapter(adapter_framefragment_bestproduct);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        itemBestProduct.clear();
        from = 0;
        limit = 8;

        showData(from.toString(), limit.toString());
        getDurationSale();
        hasil = (from + limit);

        cardTop.setVisibility(View.GONE);
        imgTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
                    cardTop.setVisibility(View.VISIBLE);
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN))
                    {
                        switch (pos){
                            case 1:
                                showDataByGroup(hasil.toString(), limit.toString(), "item.`item_code` ASC");
                                hasil = hasil + limit;
                                //Toasty.info(getContext(), "hasil : " + hasil, Toast.LENGTH_SHORT).show();
                                break;

                            case 2:
                                showDataByGroup(hasil.toString(), limit.toString(), "item.`item_code` DESC");
                                hasil = hasil + limit;
                                break;

                            case 3:
                                showDataByPrice(hasil.toString(), limit.toString(), "price.`price_list_item` ASC");
                                hasil = hasil + limit;
                                break;

                            case 4:
                                showDataByPrice(hasil.toString(), limit.toString(), "price.`price_list_item` DESC");
                                hasil = hasil + limit;
                                break;

                            case 5:
                                showScrollByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                        hasil.toString(), limit.toString());
                                hasil = hasil + limit;
                                break;

                            default:
                                showData(hasil.toString(), limit.toString());
                                hasil = (hasil + limit);
                                //Toasty.info(getContext(), "hasil : " + hasil, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }
            }
        });


//        if (mListener != null)
//        {
//            mListener.onFragmentInteraction("Frame Corner");
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.fragment_newframe_btnfilter:
                //dialogFilter();
//                FilterFrameBottomsheet filterFrameBottomsheet = FilterFrameBottomsheet.getInstance(getContext());
//                filterFrameBottomsheet.setCanceledOnTouchOutside(false);
//                filterFrameBottomsheet.show();
                openDialogFilter();
                break;

            case R.id.fragment_newframe_btnsort:
                dialogSorting();
                break;
        }
    }

    private void dialogSorting()
    {
        if(getActivity() != null){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            List<String> allSort = Arrays.asList("Title (A-Z)", "Title (Z-A)", "Price (Low-High)", "Price (High-Low)");
            listSortBy.clear();
            listSortBy.addAll(allSort);

            adapter_sortbyframe = new Adapter_sortbyframe(getContext(), listSortBy, posSw);

            dialog.setContentView(R.layout.dialog_sort);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(true);
            final ListView lvSortBy = dialog.findViewById(R.id.dialog_sort_listview);
            lvSortBy.setAdapter(adapter_sortbyframe);

            lvSortBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String chooser = listSortBy.get(i);
                    itemBestProduct.clear();
                    from  = 0;
                    limit = 8;

                    if (chooseBrand == null)
                    {
                        if (chooser.contains("Title (A-Z)") || chooser.contentEquals("Title (A-Z)") || chooser.equals("Title (A-Z)"))
                        {
                            showDataByGroup(from.toString(), limit.toString(), "item.`item_code` ASC");
                            hasil = (from + limit);
                            pos = 1;
                            posSw = 1;
                        }
                        else if (chooser.contains("Title (Z-A)") || chooser.contentEquals("Title (Z-A)") || chooser.equals("Title (Z-A)"))
                        {
                            showDataByGroup(from.toString(), limit.toString(), "item.`item_code` DESC");
                            hasil = (from + limit);
                            pos = 2;
                            posSw = 2;
                        }
                        else if (chooser.contains("Price (Low-High)") || chooser.contentEquals("Price (Low-High)") ||
                                chooser.equals("Price (Low-High)"))
                        {
                            showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` ASC");
                            hasil = (from + limit);
                            pos = 3;
                            posSw = 3;
                        }
                        else if (chooser.contains("Price (High-Low)") || chooser.contentEquals("Price (High-Low)") ||
                                chooser.equals("Price (High-Low)"))
                        {
                            showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` DESC");
                            hasil = (from + limit);
                            pos = 4;
                            posSw = 4;
                        }
                    }
                    else
                    {
                        if (chooser.contains("Title (A-Z)") || chooser.contentEquals("Title (A-Z)") || chooser.equals("Title (A-Z)"))
                        {
                            //showDataByGroup(from.toString(), limit.toString(), "item.`item_code` ASC");
                            sortCondition = "item.`item_code` ASC";
                            showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                    from.toString(), limit.toString());
                            hasil = (from + limit);
                            pos = 5;
                            posSw = 1;
                        }
                        else if (chooser.contains("Title (Z-A)") || chooser.contentEquals("Title (Z-A)") || chooser.equals("Title (Z-A)"))
                        {
                            //showDataByGroup(from.toString(), limit.toString(), "item.`item_code` DESC");
                            sortCondition = "item.`item_code` DESC";
                            showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                    from.toString(), limit.toString());
                            hasil = (from + limit);
                            pos = 5;
                            posSw = 2;
                        }
                        else if (chooser.contains("Price (Low-High)") || chooser.contentEquals("Price (Low-High)") ||
                                chooser.equals("Price (Low-High)"))
                        {
                            //showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` ASC");
                            sortCondition = "price.`price_list_item` ASC";
                            showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                    from.toString(), limit.toString());
                            hasil = (from + limit);
                            pos = 5;
                            posSw = 3;
                        }
                        else if (chooser.contains("Price (High-Low)") || chooser.contentEquals("Price (High-Low)") ||
                                chooser.equals("Price (High-Low)"))
                        {
                            //showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` DESC");
                            sortCondition = "price.`price_list_item` DESC";
                            showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                    from.toString(), limit.toString());
                            hasil = (from + limit);
                            pos = 5;
                            posSw = 4;
                        }
                    }

                    dialog.dismiss();
                }
            });

            if (!getActivity().isFinishing())
            {
                dialog.show();
            }
        }
    }

    private void openDialogFilter()
    {
        if(getActivity() != null){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_filter);

            showColor();
            showBrand();

            recyclerColor = dialog.findViewById(R.id.dialog_filter_recyclerColor);
            recyclerBrand = dialog.findViewById(R.id.dialog_filter_recyclerBrand);
            //rangePrice    = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_rangebar);
            rangePrice    = dialog.findViewById(R.id.dialog_filter_rangebar);
//        txtRangeMin   = dialog.findViewById(R.id.dialog_filter_txtrangemin);
            txtRangeMax   = dialog.findViewById(R.id.dialog_filter_txtrangemax);
            btnClear      = dialog.findViewById(R.id.dialog_filter_btnClear);
            btnApply      = dialog.findViewById(R.id.dialog_filter_btnApply);

            recyclerColor.setLayoutManager(new GridLayoutManager(getContext(), 6));
            adapter_colorfilter = new Adapter_colorfilter(getContext(), dataColor, customAdapterFrameColor, chooseColor);
            recyclerColor.setAdapter(adapter_colorfilter);

            recyclerBrand.setLayoutManager(new GridLayoutManager(getContext(), 3));
            adapter_brandfilter = new Adapter_brandfilter(getContext(), dataBrand, customAdapterFrameFragment, chooseBrand);
            recyclerBrand.setAdapter(adapter_brandfilter);

//        listBrand.clear();
//        listColor.clear();

            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Clear to default
                    valueMin = "0";
                    valueMax = "7000000";

                    chooseBrand = null;

                    Log.d("RESETTER", "BACK TO DEFAULT");

                    itemBestProduct.clear();

                    valueMax = removeRupiah(txtRangeMax.getText().toString());

                    Object[] allBrand = listBrand.toArray();
                    Object[] allColor = listColor.toArray();

                    //Toasty.info(getContext(), "Pos " + Arrays.toString(allBrand), Toast.LENGTH_SHORT).show();

                    filterPrice = "AND price.`price_list_item` BETWEEN " + 0 + " AND " + valueMax;
                    if (filterBrand != null)
                    {
//                    chooseBrand = Arrays.toString(allBrand).replace('[', ' ').replace(']', ' ').trim();
//                    filterBrand = "AND category.`id` IN ( " + chooseBrand + ")";

//                    Toasty.info(getContext(), filterPrice + filterBrand, Toast.LENGTH_SHORT).show();
//                    Log.d("READ THIS : ", filterPrice + " " + filterBrand);
                        chooseBrand = filterBrand.replace("AND category.`id` IN (", "").replace(")","");
                        Log.d("READ THIS : Baca nih ", chooseBrand);
                    }
                    else
                    {
                        filterBrand = "";
                    }

                    if (filterColor != null)
                    {
//                    chooseColor = Arrays.toString(allColor).replace('[', ' ').replace(']', ' ').trim();
//                    filterColor = "AND item.`color_type` IN (" + chooseColor + ")";

//                    Toasty.info(getContext(), filterPrice + filterBrand, Toast.LENGTH_SHORT).show();
//                    Log.d("READ THIS : ", filterPrice + " " + filterBrand + " " + filterColor);

                        chooseColor = filterColor.replace("AND item.`color_type` IN (", "").replace(")","");
                        Log.d("READ THIS : Baca juga ", chooseColor);
                    }
                    else
                    {
                        filterColor = "";
//                    chooseColor = "";
                    }

                    from = 0;
                    limit = 8;

                    sortCondition = "item.`item_code` ASC";
                    showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                            from.toString(), limit.toString());
                    pos = 5;
                    hasil = (from + limit);

                    dialog.dismiss();
                }
            });

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valueMin = "0";
                    valueMax = "7000000";

                    chooseBrand = null;
                    chooseColor = null;

                    dialog.dismiss();
                }
            });

            txtRangeMax.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                    {
//                    String rangeMin = removeRupiah(txtRangeMin.getText().toString());
//                    String rangeMax = removeRupiah(txtRangeMax.getText().toString());

//                    valueMin = removeRupiah(txtRangeMin.getText().toString());
                        valueMax = removeRupiah(txtRangeMax.getText().toString());

//                    rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();

                        rangePrice.setMax(Integer.valueOf(valueMax));
                        txtRangeMax.setText("Rp " + valueMax);
                    }

                    return false;
                }
            });

            selectRangePrice();

            if (!getActivity().isFinishing())
            {
                dialog.show();
            }
        }
    }

    private void selectRangePrice()
    {
        rangePrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String range = "Rp " + String.valueOf(progress);
                txtRangeMax.setText(range);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

    private void showData(final String start, final String until)
    {
        //itemBestProduct.clear();
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, allDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        progress.setVisibility(View.GONE);

                        String id       = jsonObject.getString("frame_id");
                        String title    = jsonObject.getString("frame_name");
                        String image    = jsonObject.getString("frame_image");
                        String price    = jsonObject.getString("frame_price");
                        String discperc = jsonObject.getString("frame_disc");
                        String brandName= jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
                        String tempPrice = CurencyFormat(price);
                        totalData= jsonObject.getString("total_output");

                        Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                        data.setProduct_id(id);
                        data.setProduct_name(title);
                        data.setProduct_image(image);
                        data.setProduct_realprice("Rp " + tempPrice);
                        data.setProduct_discpercent(discperc);
                        data.setProduct_brand(brandName);
                        data.setProduct_qty(frameQty);
                        data.setProduct_weight(frameWeight);

                        itemBestProduct.add(data);
                    }

//                    txtCounter.setText(totalData + " data found");
                    adapter_framefragment_bestproduct.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(getActivity());
                    snackBarMessage.showErrorMessage("No more data found");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("start", start);
                hashMap.put("limit", until);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showDataByGroup(final String start, final String until, final String condition)
    {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, sortGroupUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        progress.setVisibility(View.GONE);

                        String id       = jsonObject.getString("frame_id");
                        String title    = jsonObject.getString("frame_name");
                        String image    = jsonObject.getString("frame_image");
                        String price    = jsonObject.getString("frame_price");
                        String discperc = jsonObject.getString("frame_disc");
                        String brandName= jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
                        String tempPrice = CurencyFormat(price);

                        Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                        data.setProduct_id(id);
                        data.setProduct_name(title);
                        data.setProduct_image(image);
                        data.setProduct_realprice("Rp " + tempPrice);
                        data.setProduct_discpercent(discperc);
                        data.setProduct_brand(brandName);
                        data.setProduct_qty(frameQty);
                        data.setProduct_weight(frameWeight);

                        itemBestProduct.add(data);
                    }

                    adapter_framefragment_bestproduct.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(getActivity());
                    snackBarMessage.showErrorMessage("No more data found");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("start", start);
                hashMap.put("limit", until);
                hashMap.put("condition", condition);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showDataByFilter(final String sortby, final String condition, final String start, final String limit)
    {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, sortFilterUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toasty.info(getContext(), String.valueOf(response.length()), Toast.LENGTH_SHORT).show();

                    if (response.length() <= 28)
                    {
                        JSONObject object = new JSONObject(response);

                        imgNotfound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(response);
//
                        recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            progress.setVisibility(View.GONE);

                            String id = jsonObject.getString("frame_id");
                            String title = jsonObject.getString("frame_name");
                            String image = jsonObject.getString("frame_image");
                            String price = jsonObject.getString("frame_price");
                            String discperc = jsonObject.getString("frame_disc");
                            String tempPrice = CurencyFormat(price);
                            String brandName = jsonObject.getString("frame_brand");
                            String frameQty     = jsonObject.getString("frame_qty");
                            String frameWeight  = jsonObject.getString("frame_weight");
                            totalData = jsonObject.getString("total_output");

                            Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                            data.setProduct_id(id);
                            data.setProduct_name(title);
                            data.setProduct_image(image);
                            data.setProduct_realprice("Rp " + tempPrice);
                            data.setProduct_discpercent(discperc);
                            data.setProduct_brand(brandName);
                            data.setProduct_qty(frameQty);
                            data.setProduct_weight(frameWeight);

                            itemBestProduct.add(data);


                            imgNotfound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
//                    txtCounter.setText(totalData + " data found");
                            adapter_framefragment_bestproduct.notifyDataSetChanged();

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

//                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(getActivity());
//                    snackBarMessage.showErrorMessage("No more data found");

                    recyclerView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("start", start);
                hashMap.put("limit", limit);
                hashMap.put("condition", condition);
                hashMap.put("sortby", sortby);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showDataByPrice(final String start, final String until, final String condition)
    {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, sortPriceUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        progress.setVisibility(View.GONE);

                        String id       = jsonObject.getString("frame_id");
                        String title    = jsonObject.getString("frame_name");
                        String image    = jsonObject.getString("frame_image");
                        String price    = jsonObject.getString("frame_price");
                        String discperc = jsonObject.getString("frame_disc");
                        String brandName= jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
                        String tempPrice = CurencyFormat(price);

                        Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                        data.setProduct_id(id);
                        data.setProduct_name(title);
                        data.setProduct_image(image);
                        data.setProduct_realprice("Rp " + tempPrice);
                        data.setProduct_discpercent(discperc);
                        data.setProduct_brand(brandName);
                        data.setProduct_qty(frameQty);
                        data.setProduct_weight(frameWeight);

                        itemBestProduct.add(data);
                    }

                    adapter_framefragment_bestproduct.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(getActivity());
                    snackBarMessage.showErrorMessage("No more data found");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("start", start);
                hashMap.put("limit", until);
                hashMap.put("condition", condition);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
    }

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "");

        return output;
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

                        //Toasty.info(getContext(), color, Toast.LENGTH_SHORT).show();

                        Data_color_filter item = new Data_color_filter();
                        item.setColorCode(color);
                        item.setColorId(id);
                        item.setColorName(name);

                        dataColor.add(item);
                    }

//                    adapter_colorfilter = new Adapter_colorfilter(getContext(), dataColor, customAdapterFrameColor);
                    adapter_colorfilter.notifyDataSetChanged();
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

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
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
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showScrollByFilter(final String sortby, final String condition, final String start, final String limit)
    {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, sortFilterUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        progress.setVisibility(View.GONE);

                        String id       = jsonObject.getString("frame_id");
                        String title    = jsonObject.getString("frame_name");
                        String image    = jsonObject.getString("frame_image");
                        String price    = jsonObject.getString("frame_price");
                        String discperc = jsonObject.getString("frame_disc");
                        String brandName= jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
                        String tempPrice = CurencyFormat(price);
                        totalData= jsonObject.getString("total_output");

                        Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                        data.setProduct_id(id);
                        data.setProduct_name(title);
                        data.setProduct_image(image);
                        data.setProduct_realprice("Rp " + tempPrice);
                        data.setProduct_discpercent(discperc);
                        data.setProduct_brand(brandName);
                        data.setProduct_qty(frameQty);
                        data.setProduct_weight(frameWeight);

                        itemBestProduct.add(data);
                    }

//                    txtCounter.setText(totalData + " data found");
                    adapter_framefragment_bestproduct.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);

//                    BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(getActivity());
//                    snackBarMessage.showErrorMessage("No more data found");

                    Snackbar snackbar = Snackbar.make(view, "No more data found", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("start", start);
                hashMap.put("limit", limit);
                hashMap.put("condition", condition);
                hashMap.put("sortby", sortby);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
//        Volley.newRequestQueue(getContext()).add(request);
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
                    }
                    else
                    {
                        int durr = object.getInt("expired");

                        linear_flashsale.setVisibility(View.GONE);

                        countdown_flashsale.start(durr);
                        countdown_flashsale.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                linear_flashsale.setVisibility(View.GONE);

                                Fragment frg = null;
                                frg = getFragmentManager().findFragmentByTag("newframe");
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();

                                RecyclerView.LayoutManager verticalGrid = new GridLayoutManager(getContext(), 2);
                                recyclerView.setLayoutManager(verticalGrid);
                                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                showData(from.toString(), limit.toString());
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

    CustomAdapterFrameBrand customAdapterFrameFragment = new CustomAdapterFrameBrand() {
        @Override
        public void response(String position, String title) {
            if (title.length() > 0)
            {
                filterBrand = position;
            }
            else
            {
                filterBrand = "";
            }
//            filterBrand = "AND category.`id` IN ( " + outputSelected + ")";

//            Log.d("Output Selected : ", outputSelected);
        }
    };

    CustomAdapterFrameColor customAdapterFrameColor = new CustomAdapterFrameColor() {
        @Override
        public void response(String position, String title) {
//            listColor.add(position);

            if (title.length() > 0)
            {
                filterColor = position;
            }
            else
            {
                filterColor = "";
            }
        }
    };
}
