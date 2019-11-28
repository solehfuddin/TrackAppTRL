package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
//import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
//import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
//import com.orhanobut.dialogplus.DialogPlus;
//import com.orhanobut.dialogplus.ViewHolder;
//import com.orhanobut.dialogplus.DialogPlus;
//import com.orhanobut.dialogplus.ViewHolder;
import com.google.gson.annotations.SerializedName;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sofudev.trackapptrl.Adapter.Adapter_brandfilter;
import com.sofudev.trackapptrl.Adapter.Adapter_colorfilter;
import com.sofudev.trackapptrl.Adapter.Adapter_framefragment_bestproduct;
import com.sofudev.trackapptrl.Adapter.Adapter_sortbyframe;
import com.sofudev.trackapptrl.Custom.BlackStyle;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomAdapterFrameBrand;
import com.sofudev.trackapptrl.Custom.CustomAdapterFrameColor;
import com.sofudev.trackapptrl.Custom.GridSpacingItemDecoration;
import com.sofudev.trackapptrl.Custom.OnFragmentInteractionListener;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_brand_filter;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.DetailProductActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import viethoa.com.snackbar.BottomSnackBarMessage;

public class NewFrameFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    Config config = new Config();

    String allDataUrl = config.Ip_address + config.frame_bestproduct_showAllData;
    String sortGroupUrl = config.Ip_address + config.frame_filterby_group;
    String sortPriceUrl = config.Ip_address + config.frame_filterby_price;
    String sortFilterUrl= config.Ip_address + config.frame_filterwith_sort;
    String showColorUrl = config.Ip_address + config.frame_showcolor_filter;
    String showBrandUrl = config.Ip_address + config.frame_showbrand_filter;

    RecyclerView recyclerView,recyclerColor, recyclerBrand;
//    private EditText txtRangeMin, txtRangeMax;
//    CrystalRangeSeekbar rangePrice;
    private EditText txtRangeMax;
    private SeekBar rangePrice;
    ProgressWheel progress;
    private BootstrapButton btnApply, btnClear;
    AwesomeTextView btnFilter, btnSort;
    ImageView imgNotfound;

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
    String ACTIVITY_TAG;

    List<String> listSortBy = new ArrayList<>();
//    ArrayList<String> itemBrand  = new ArrayList<>();
    String itemBrand, outputSelected;
    List<String> listBrand = new ArrayList<>();
    List<String> listColor = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_frame, container, false);

        imgNotfound = view.findViewById(R.id.fragment_newframe_notfound);

        recyclerView = view.findViewById(R.id.fragment_newframe_recyclerview);
        recyclerView.setHasFixedSize(true);

        btnFilter = view.findViewById(R.id.fragment_newframe_btnfilter);
        btnSort = view.findViewById(R.id.fragment_newframe_btnsort);

        btnFilter.setBootstrapBrand(new BlackStyle(getContext()));
        btnSort.setBootstrapBrand(new BlackStyle(getContext()));

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
//                Toasty.success(getContext(), itemBestProduct.get(pos).getProduct_id(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), DetailProductActivity.class);
//                intent.putExtra("id_lensa", itemBestProduct.get(pos).getProduct_id());
//                startActivity(intent);

                if (ACTIVITY_TAG.equals("main"))
                {
                    Toasty.warning(getContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("product_id", itemBestProduct.get(pos).getProduct_id());
//                    detailFrameFragment.setArguments(bundle);
//
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
//                            .addToBackStack(null)
//                            .commit();

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
        hasil = (from + limit);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
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


        if (mListener != null)
        {
            mListener.onFragmentInteraction("Frame Corner");
        }

        return view;
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        ACTIVITY_TAG = bundle.getString("activity");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

        List<String> allSort = Arrays.asList("Title (A-Z)", "Title (Z-A)", "Price (Low-High)", "Price (High-Low)");
        listSortBy.clear();
        listSortBy.addAll(allSort);

        //Toasty.info(getContext(), "Position " + pos, Toast.LENGTH_SHORT).show();

        //ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listSortBy);
        adapter_sortbyframe = new Adapter_sortbyframe(getContext(), listSortBy, pos);

        dialog.setContentView(R.layout.dialog_sort);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        //lwindow.copyFrom(dialog.getWindow().getAttributes());
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
                    }
                    else if (chooser.contains("Title (Z-A)") || chooser.contentEquals("Title (Z-A)") || chooser.equals("Title (Z-A)"))
                    {
                        showDataByGroup(from.toString(), limit.toString(), "item.`item_code` DESC");
                        hasil = (from + limit);
                        pos = 2;
                    }
                    else if (chooser.contains("Price (Low-High)") || chooser.contentEquals("Price (Low-High)") ||
                            chooser.equals("Price (Low-High)"))
                    {
                        showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` ASC");
                        hasil = (from + limit);
                        pos = 3;
                    }
                    else if (chooser.contains("Price (High-Low)") || chooser.contentEquals("Price (High-Low)") ||
                            chooser.equals("Price (High-Low)"))
                    {
                        showDataByPrice(from.toString(), limit.toString(), "price.`price_list_item` DESC");
                        hasil = (from + limit);
                        pos = 4;
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
                    }
                    else if (chooser.contains("Title (Z-A)") || chooser.contentEquals("Title (Z-A)") || chooser.equals("Title (Z-A)"))
                    {
                        //showDataByGroup(from.toString(), limit.toString(), "item.`item_code` DESC");
                        sortCondition = "item.`item_code` DESC";
                        showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
                                from.toString(), limit.toString());
                        hasil = (from + limit);
                        pos = 5;
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
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogFilter()
    {
        final Dialog dialog = new Dialog(getContext());
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
        dialog.show();
    }

//    private void dialogFilter()
//    {
//        final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
//                .setContentHolder(new ViewHolder(R.layout.dialog_filter))
//                .create();
//
//        showBrand();
//        showColor();
//
//        recyclerColor = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_recyclerColor);
//        recyclerBrand = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_recyclerBrand);
//        //rangePrice    = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_rangebar);
//        txtRangeMin   = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_txtrangemin);
//        txtRangeMax   = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_txtrangemax);
//        btnClear      = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_btnClear);
//        btnApply      = dialogPlus.getHolderView().findViewById(R.id.dialog_filter_btnApply);
//
//        //Toasty.info(getContext(), chooseBrand, Toast.LENGTH_SHORT).show();
//
//        recyclerColor.setLayoutManager(new GridLayoutManager(getContext(), 6));
//        adapter_colorfilter = new Adapter_colorfilter(getContext(), dataColor, customAdapterFrameColor, chooseColor);
//        recyclerColor.setAdapter(adapter_colorfilter);
//
//        recyclerBrand.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        adapter_brandfilter = new Adapter_brandfilter(getContext(), dataBrand, customAdapterFrameFragment, chooseBrand);
//        recyclerBrand.setAdapter(adapter_brandfilter);
//
//       // rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();
//
//        listBrand.clear();
//        listColor.clear();
//
//        btnApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemBestProduct.clear();
//                valueMin = removeRupiah(txtRangeMin.getText().toString());
//                valueMax = removeRupiah(txtRangeMax.getText().toString());
//
//                Object[] allBrand = listBrand.toArray();
//                Object[] allColor = listColor.toArray();
//
//                //Toasty.info(getContext(), "Pos " + Arrays.toString(allBrand), Toast.LENGTH_SHORT).show();
//
//                filterPrice = "AND price.`price_list_item` BETWEEN " + valueMin + " AND " + valueMax;
//                if (allBrand.length > 0)
//                {
//                    chooseBrand = Arrays.toString(allBrand).replace('[', ' ').replace(']', ' ').trim();
//                    filterBrand = "AND item.`category_item` IN ( " + chooseBrand + ")";
//
////                    Toasty.info(getContext(), filterPrice + filterBrand, Toast.LENGTH_SHORT).show();
////                    Log.d("READ THIS : ", filterPrice + " " + filterBrand);
//                }
//                else
//                {
//                    filterBrand = "";
//                }
//
//                if (allColor.length > 0)
//                {
//                    chooseColor = Arrays.toString(allColor).replace('[', ' ').replace(']', ' ').trim();
//                    filterColor = "AND item.`color_type` IN (" + chooseColor + ")";
//
////                    Toasty.info(getContext(), filterPrice + filterBrand, Toast.LENGTH_SHORT).show();
////                    Log.d("READ THIS : ", filterPrice + " " + filterBrand + " " + filterColor);
//                }
//                else
//                {
//                    filterColor = "";
//                }
//
//                //Toasty.info(getContext(), filterPrice, Toast.LENGTH_SHORT).show();
//                from = 0;
//                limit = 8;
//
//                sortCondition = "item.`item_code` ASC";
//                showDataByFilter(sortCondition, filterPrice + " " + filterBrand + " " + filterColor,
//                        from.toString(), limit.toString());
//                pos = 5;
//                hasil = (from + limit);
//                dialogPlus.dismiss();
//            }
//        });
//
//        btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                valueMin = "0";
//                valueMax = "7000000";
//
//                chooseBrand = null;
//                chooseColor = null;
//
//                dialogPlus.dismiss();
//            }
//        });
//
////        txtRangeMin.setOnKeyListener(new View.OnKeyListener() {
////            @Override
////            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
////                {
//////                    Toasty.info(getContext(), "Min " + txtRangeMin.getText(), Toast.LENGTH_SHORT).show();
//////                    String rangeMin = removeRupiah(txtRangeMin.getText().toString());
//////                    String rangeMax = removeRupiah(txtRangeMax.getText().toString());
////
////                    valueMin = removeRupiah(txtRangeMin.getText().toString());
////                    valueMax = removeRupiah(txtRangeMax.getText().toString());
////
////                    rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();
////
////                    txtRangeMin.setText("Rp " + valueMin);
////                }
////
////                return false;
////            }
////        });
////
////        txtRangeMax.setOnKeyListener(new View.OnKeyListener() {
////            @Override
////            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
////                {
//////                    String rangeMin = removeRupiah(txtRangeMin.getText().toString());
//////                    String rangeMax = removeRupiah(txtRangeMax.getText().toString());
////
////                    valueMin = removeRupiah(txtRangeMin.getText().toString());
////                    valueMax = removeRupiah(txtRangeMax.getText().toString());
////
////                    rangePrice.setMinStartValue(Float.valueOf(valueMin)).setMaxStartValue(Float.valueOf(valueMax)).apply();
////
////                    txtRangeMax.setText("Rp " + valueMax);
////                }
////
////                return false;
////            }
////        });
//
//      selectRangePrice();
//
//        dialogPlus.show();
//    }

    private void selectRangePrice()
    {
        rangePrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRangeMax.setText("Rp " + String.valueOf(progress));
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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

                    Snackbar snackbar = Snackbar.make(getView(), "No more data found", Snackbar.LENGTH_LONG);

                    snackbar.show();
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
                hashMap.put("start", start);
                hashMap.put("limit", limit);
                hashMap.put("condition", condition);
                hashMap.put("sortby", sortby);
                return hashMap;
            }
        };

//        AppController.getInstance().addToRequestQueue(request);
        Volley.newRequestQueue(getContext()).add(request);
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
