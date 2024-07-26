package com.sofudev.trackapptrl;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_category_onhand;
import com.sofudev.trackapptrl.Adapter.Adapter_master_onhand;
import com.sofudev.trackapptrl.Adapter.Adapter_master_onhandframe;
import com.sofudev.trackapptrl.Adapter.Adapter_sorting_onhand;
import com.sofudev.trackapptrl.Adapter.Adapter_subcategory_onhand;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_categoryonhand;
import com.sofudev.trackapptrl.Data.Data_master_onhand;
import com.sofudev.trackapptrl.Data.Data_sortingonhand;
import com.sofudev.trackapptrl.Data.Data_sub_categoryonhand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class OnHandActivity extends AppCompatActivity {

    Config config = new Config();
    String URLGETITEMNAME = config.Ip_address + config.masteronhand_getitemname;
    String URLGETITEMFRAME= config.Ip_address + config.masteronhand_getitemframe;
    String URLGETBYORGID  = config.Ip_address + config.masteronhand_getbyorgid;
    String URLGETBYSORT   = config.Ip_address + config.masteronhand_getbysorting;
    String URLGETBYFRAMESORT = config.Ip_address + config.masteronhand_getbyframesorting;
    String URLGETBYFRAMEBINSORT = config.Ip_address + config.masteronhand_getbyframebinsorting;
    String URLGETBYQTY    = config.Ip_address + config.masteronhand_getbyqty;
    String URLGETSUGGEST  = config.Ip_address + config.masteronhand_getsuggestion;
    String URLSEARCHDATA  = config.Ip_address + config.masteronhand_search;
    String URLSEARCHDATAFRAME = config.Ip_address + config.masteronhand_searchFrame;
    String URLSEARCHDATAFRAMEBIN = config.Ip_address + config.masteronhand_searchFrameBin;
    String URLGETTRANSLASI= config.Ip_address + config.masteronhand_gettranslation;
    String URLGETBYTRANSLASI = config.Ip_address + config.masteronhand_getbytranslation;

    ACProgressFlower loading;
    CircleProgressBar loader;
    RecyclerView recyclerView, recyclerViewFrame;
    Switch swFlag, swBin;
    UniversalFontTextView txtCounter, txtFlag, txtBin;
//    AutoCompleteTextView autoTxtSearch;
    MaterialEditText autoTxtSearch;
    ImageView imgNotFound;
    ImageView img_track;
    RippleView rpFilter, rpBack, rpSorting, rpMore;
    ArrayList<String> itemSuggestion = new ArrayList<>();
    List<Data_categoryonhand> itemCategory = new ArrayList<>();
    List<Data_sub_categoryonhand> itemSubCategory = new ArrayList<>();
    List<Data_sortingonhand> itemSorting = new ArrayList<>();
    List<Data_master_onhand> itemOnHand = new ArrayList<>();
    List<Data_master_onhand> itemOnHandFrame = new ArrayList<>();
    String idParty, filterpos, addpos, addposFrame, addsubpos, newpos, dataCatItem, dataCatItemFrame, dataSubCatItem, dataSortItem, itemOrgId, itemOrgIdFrame, flag, flagBin, itemSortId, itemSortDesc, counter;
    String [] dataSuggestion;
    RecyclerView.LayoutManager recyclerLM, recyclerLMFrame;
    RelativeLayout rlTrack;
    LinearLayout linCounter;
    TextView txtCountBrand, txtCountTotal;
    RippleView rpSearch;

    Adapter_subcategory_onhand adapter_subcategory_onhand;
    Adapter_category_onhand adapter_category_onhand;
    Adapter_sorting_onhand adapter_sorting_onhand;
    Adapter_master_onhand adapter_master_onhand;
    Adapter_master_onhandframe adapter_master_onhandframe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_hand);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        UniversalFontComponents.init(this);

        getItemInfo();
        getUserInfo();

        recyclerView    = findViewById(R.id.form_onhand_recycleview);
        recyclerViewFrame = findViewById(R.id.form_onhand_recycleviewframe);
        loader          = findViewById(R.id.form_onhand_progressBar);
        txtCounter      = findViewById(R.id.form_onhand_txtCounter);
        txtFlag         = findViewById(R.id.form_onhand_txtflag);
        txtBin          = findViewById(R.id.form_onhand_txtBin);
        swFlag          = findViewById(R.id.form_onhand_swflag);
        swBin           = findViewById(R.id.form_onhand_swBin);
        autoTxtSearch   = findViewById(R.id.form_onhand_txtSearch);
        imgNotFound     = findViewById(R.id.form_onhand_imgnotfound);
        rlTrack         = findViewById(R.id.form_tracksp_relativelayout);
        linCounter      = findViewById(R.id.form_onhand_linearcounter);
        txtCountBrand   = findViewById(R.id.form_onhand_txtcounterbrand);
        txtCountTotal   = findViewById(R.id.form_onhand_txtcountertotal);

        loader.setVisibility(View.VISIBLE);
        showLoading();
        imgNotFound.setVisibility(View.GONE);
        txtBin.setVisibility(View.GONE);
        swBin.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        recyclerLM = new LinearLayoutManager(this);
        recyclerLMFrame = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLM);
        recyclerViewFrame.setHasFixedSize(true);
        recyclerViewFrame.setLayoutManager(recyclerLMFrame);

        flag = "LENSA";
        flagBin = "STORE";
        filterpos = "Category";
        dataCatItem = "FG MOE LENS";
        dataCatItemFrame = "LEINZ";
        itemOrgId = "130";
        itemOrgIdFrame = "3";
        itemSortDesc = "DESC";
        addpos = dataCatItem;
        addposFrame = dataCatItemFrame;
        addsubpos = "All";
        newpos = "Qty Terendah";
        rpBack = findViewById(R.id.form_onhand_ripplebtnback);
        rpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, flag.equals("LENSA") ? addpos : addposFrame);
        adapter_subcategory_onhand = new Adapter_subcategory_onhand(OnHandActivity.this, itemSubCategory, addsubpos);

        rpFilter = findViewById(R.id.form_onhand_rpfilter);
        rpMore   = findViewById(R.id.form_onhand_ripplebtnFilter);
        rpMore.setVisibility(View.GONE);
        rpMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListView listView;
                final BootstrapButton btnChoose;
                final MaterialEditText txtSearch;
                final RippleView btnSearch;

                final Dialog dialog = new Dialog(OnHandActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_subcategory_onhand);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                listView = dialog.findViewById(R.id.dialog_subcategory_listview);
                txtSearch= dialog.findViewById(R.id.dialog_subcategory_txtsearch);
                btnSearch= dialog.findViewById(R.id.dialog_subcategory_ripplebtnsearch);
                btnChoose= dialog.findViewById(R.id.dialog_subcategory_btnsave);

                listView.setAdapter(adapter_subcategory_onhand);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                txtSearch.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        btnChoose.setEnabled(false);
                        btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {
                            String check = txtSearch.getText().toString();
                            getTranslationCategory(check);
                        }

                        return false;
                    }
                });

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        String check = txtSearch.getText().toString();
                        btnChoose.setEnabled(false);
                        btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                        getTranslationCategory(check);
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dataSubCatItem = itemSubCategory.get(position).getDesc();

                        if (dataSubCatItem.isEmpty())
                        {
                            btnChoose.setEnabled(false);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btnChoose.setEnabled(true);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });


                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        addsubpos = dataSubCatItem;
                        adapter_subcategory_onhand = new Adapter_subcategory_onhand(OnHandActivity.this, itemSubCategory, addsubpos);

                        getOnHandBySubcategory(addsubpos);

//                        if (itemOrgId.equals("0"))
//                        {
//                            getOnHandByQty(itemOrgId);
//                        }
//                        else
//                        {
//                            getOnHandBySort(itemOrgId, itemSortDesc);
//                        }

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        rpFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(OnHandActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Pilih Category Item");
                listView.setAdapter(adapter_category_onhand);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (flag.equals("LENSA"))
                        {
                            dataCatItem = itemCategory.get(position).getItemName();
                            itemOrgId= itemCategory.get(position).getOrgId();
                            dataSubCatItem = dataCatItem;

                            if (dataCatItem.isEmpty())
                            {
                                btnChoose.setEnabled(false);
                                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                            }
                            else
                            {
                                btnChoose.setEnabled(true);
                                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            }
                        }
                        else
                        {
                            dataCatItemFrame = itemCategory.get(position).getItemName();
                            itemOrgIdFrame = itemCategory.get(position).getOrgId();
//                            dataSubCatItem = dataCatItemFrame;

                            if (dataCatItemFrame.isEmpty())
                            {
                                btnChoose.setEnabled(false);
                                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                            }
                            else
                            {
                                btnChoose.setEnabled(true);
                                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            }
                        }
                    }
                });

                if (!isFinishing())
                {
                    dialog.show();
                }

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        autoTxtSearch.setText("");
//                        addpos = dataCatItem;
//                        adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addpos);

                        if (flag.equals("LENSA"))
                        {
                            addpos = dataCatItem;
                            adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addpos);

                            if (itemOrgId.equals("0"))
                            {
                                getOnHandByQty(itemOrgId);
                            }
                            else
                            {
//                            getOnHandByOrgId(itemOrgId);
                                getOnHandBySort(itemOrgId, itemSortDesc);
                            }
                        }
                        else
                        {
                            addposFrame = dataCatItemFrame;
                            adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addposFrame);

                            if (flagBin.equals("STORE"))
                            {
                                getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);
                            }
                            else
                            {
                                getOnHandFrameBinBySort(dataCatItemFrame, itemSortDesc);
                            }
                        }
//                        getSuggestion(itemOrgId);
                        dialog.dismiss();
                    }
                });
            }
        });

        rpSorting = findViewById(R.id.form_onhand_rpsorting);
        rpSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(OnHandActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                itemSorting.clear();
                itemSorting.add(new Data_sortingonhand("0", "Qty Terendah"));
                itemSorting.add(new Data_sortingonhand("1", "Qty Tertinggi"));

                adapter_sorting_onhand = new Adapter_sorting_onhand(OnHandActivity.this, itemSorting, newpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                String urutkan = "Urut Berdasarkan";

                txtTitle.setText(urutkan);
                listView.setAdapter(adapter_sorting_onhand);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dataSortItem = itemSorting.get(position).getDescription();
                        itemSortId   = itemSorting.get(position).getIdSort();

                        newpos = dataSortItem;
                        if (dataSortItem.isEmpty())
                        {
                            btnChoose.setEnabled(false);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btnChoose.setEnabled(true);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });

                if(!isFinishing())
                {
                    dialog.show();
                }

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        autoTxtSearch.setText("");
//                        Toasty.info(getApplicationContext(), itemOrgId, Toast.LENGTH_SHORT).show();

                        if (flag.equals("LENSA"))
                        {
                            if (itemOrgId.equals("0"))
                            {
                                getOnHandByQty(itemOrgId);
                            }
                            else
                            {
                                if (itemSortId.equals("0"))
                                {
                                    itemSortDesc = "ASC";
                                    getOnHandBySort(itemOrgId, itemSortDesc);
                                }
                                else
                                {
                                    itemSortDesc = "DESC";
                                    getOnHandBySort(itemOrgId, itemSortDesc);
                                }
                            }
                        }
                        else
                        {
                            if (itemSortId.equals("0"))
                            {
                                itemSortDesc = "ASC";
//                                getOnHandBySort(itemOrgId, itemSortDesc);
                                if (flagBin.equals("STORE"))
                                {
                                    getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);
                                }
                                else
                                {
                                    getOnHandFrameBinBySort(dataCatItemFrame, itemSortDesc);
                                }
                            }
                            else
                            {
                                itemSortDesc = "DESC";
//                                getOnHandBySort(itemOrgId, itemSortDesc);
                                if (flagBin.equals("STORE"))
                                {
                                    getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);
                                }
                                else
                                {
                                    getOnHandFrameBinBySort(dataCatItemFrame, itemSortDesc);
                                }
                            }
                        }

//                        getSuggestion(itemOrgId);
                        dialog.dismiss();
                    }
                });
            }
        });

        autoTxtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)))
                {
                    showLoading();
                    if (flag.equals("LENSA"))
                    {
                        searchData(itemOrgId, autoTxtSearch.getText().toString(), itemSortDesc);
                    }
                    else
                    {
                        if (flagBin.equals("STORE"))
                        {
                            searchDataFrame(dataCatItemFrame, autoTxtSearch.getText().toString(), itemSortDesc);
                        }
                        else
                        {
                            searchDataFrameBin(dataCatItemFrame, autoTxtSearch.getText().toString(), itemSortDesc);
                        }
                    }

                    return true;
                }
                return false;
            }
        });

        adapter_master_onhand = new Adapter_master_onhand(this, itemOnHand, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {

            }
        });

        adapter_master_onhandframe = new Adapter_master_onhandframe(this, itemOnHandFrame, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {

            }
        });

//        getOnHandByOrgId("89");
        getOnHandBySort(itemOrgId, itemSortDesc);
        getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);
        getTranslationCategory("");
//        getSuggestion(itemOrgId);

        recyclerView.setAdapter(adapter_master_onhand);
        recyclerViewFrame.setAdapter(adapter_master_onhandframe);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewFrame.setVisibility(View.GONE);

        swFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                autoTxtSearch.setText("");
                if (b)
                {
                    txtFlag.setText("LENSA");
                    flag = "LENSA";

                    getItemInfo();
                    getOnHandBySort(itemOrgId, itemSortDesc);

                    adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addpos);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewFrame.setVisibility(View.GONE);

                    txtBin.setVisibility(View.GONE);
                    swBin.setVisibility(View.GONE);
                }
                else
                {
                    txtFlag.setText("FRAME");
                    flag = "FRAME";

                    getItemFrame();

                    if (flagBin.equals("STORE"))
                    {
                        getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);
                    }
                    else
                    {
                        getOnHandFrameBinBySort(dataCatItemFrame, itemSortDesc);
                    }

                    adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addposFrame);

                    recyclerView.setVisibility(View.GONE);
                    recyclerViewFrame.setVisibility(View.VISIBLE);

                    txtBin.setVisibility(View.VISIBLE);
                    swBin.setVisibility(View.VISIBLE);
                }
            }
        });

        swBin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b)
                {
                    txtBin.setText("STORE");
                    flagBin = "STORE";

                    getOnHandFrameBySort(dataCatItemFrame, itemSortDesc);

                    recyclerView.setVisibility(View.GONE);
                    recyclerViewFrame.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtBin.setText("BIN");
                    flagBin = "BIN";

                    getOnHandFrameBinBySort(dataCatItemFrame, itemSortDesc);

                    recyclerView.setVisibility(View.GONE);
                    recyclerViewFrame.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getUserInfo()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idParty = bundle.getString("idparty");
        }
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(OnHandActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        if (!isFinishing()){
            loading.show();
        }
    }

    private void showErrorImage()
    {
        rlTrack.removeView(img_track);
        img_track = new ImageView(this);
        img_track.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);

        img_track.setLayoutParams(lp);
        rlTrack.addView(img_track);
    }

    private void getItemInfo() {
        itemCategory.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETITEMNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String orgId = object.getString("orgId");
                        String itemName = object.getString("itemName").toUpperCase();

                        Data_categoryonhand onHand = new Data_categoryonhand();
                        onHand.setOrgId(orgId);
                        onHand.setItemName(itemName);

                        itemCategory.add(onHand);
                    }

//                    addpos = itemCategory.get(0).getItemName();
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

    private void getItemFrame() {
        itemCategory.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETITEMFRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String orgId = object.getString("orgId");
                        String itemName = object.getString("itemName").toUpperCase();

                        Data_categoryonhand onHand = new Data_categoryonhand();
                        onHand.setOrgId(orgId);
                        onHand.setItemName(itemName);

                        itemCategory.add(onHand);
                    }

//                    addpos = itemCategory.get(0).getItemName();
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

    private void getOnHandBySort(final String orgId, final String sort)
    {
        itemOnHand.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYSORT, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
                            showErrorImage();

                            if (flag.equals("LENSA"))
                            {
                                recyclerView.setVisibility(View.GONE);
                                txtCounter.setVisibility(View.GONE);
                                linCounter.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setTotalItem(totalItem);
                            data.setItemImg(null);

                            itemOnHand.add(data);

                            counter = totalItem + " Data Ditemukan";

                            txtCounter.setText(counter);

                            txtCountBrand.setText("Total " + dataCatItem.replace("FG", "").replace("LENS", "") + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);

                            if (img_track != null) {
                                img_track.setVisibility(View.GONE);
                            }

                            if (flag.equals("LENSA"))
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                txtCounter.setVisibility(View.VISIBLE);
                                linCounter.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    adapter_master_onhand.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", orgId);
                hashMap.put("sort", sort);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOnHandFrameBySort(final String orgId, final String sort)
    {
        itemOnHandFrame.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYFRAMESORT, new Response.Listener<String>() {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
                            showErrorImage();

                            if (flag.equals("FRAME"))
                            {
                                recyclerViewFrame.setVisibility(View.GONE);
                                txtCounter.setVisibility(View.GONE);
                                linCounter.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String itemImg = jsonObject.getString("itemImage");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Log.d(OnHandActivity.class.getSimpleName(), "Image : " + itemImg);

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setTotalItem(totalItem);
                            data.setItemImg(itemImg);

                            itemOnHandFrame.add(data);

                            counter = totalItem + " Data Ditemukan";

                            txtCounter.setText(counter);
                            txtCountBrand.setText("Total " + orgId + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);

                            if (img_track != null) {
                                img_track.setVisibility(View.GONE);
                            }

                            if (flag.equals("FRAME"))
                            {
                                recyclerViewFrame.setVisibility(View.VISIBLE);
                                txtCounter.setVisibility(View.VISIBLE);
                                linCounter.setVisibility(View.VISIBLE);
                            }
                        }
                    }

//                    txtCounter.setText(counter);
//                    adapter_master_onhand.notifyDataSetChanged();

                    adapter_master_onhandframe.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", orgId);
                hashMap.put("sort", sort);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOnHandFrameBinBySort(final String orgId, final String sort)
    {
        itemOnHandFrame.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYFRAMEBINSORT, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
                            showErrorImage();

                            if (flag.equals("FRAME"))
                            {
                                recyclerViewFrame.setVisibility(View.GONE);
                                txtCounter.setVisibility(View.GONE);
                                linCounter.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String itemImg = jsonObject.getString("itemImage");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Log.d(OnHandActivity.class.getSimpleName(), "Image : " + itemImg);

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setTotalItem(totalItem);
                            data.setItemImg(itemImg);

                            itemOnHandFrame.add(data);

                            counter = totalItem + " Data Ditemukan";

                            txtCounter.setText(counter);
                            txtCountBrand.setText("Total " + orgId + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);

                            if (flag.equals("FRAME"))
                            {
                                if (img_track != null)
                                {
                                    img_track.setVisibility(View.GONE);
                                }

                                recyclerViewFrame.setVisibility(View.VISIBLE);
                                txtCounter.setVisibility(View.VISIBLE);
                                linCounter.setVisibility(View.VISIBLE);
                            }
                        }
                    }

//                    adapter_master_onhand.notifyDataSetChanged();

                    adapter_master_onhandframe.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", orgId);
                hashMap.put("sort", sort);
                hashMap.put("id_sales", idParty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOnHandByQty(final String qty)
    {
        itemOnHand.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYQTY, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String itemId = jsonObject.getString("itemId");
                        String itemCode = jsonObject.getString("itemCode");
                        String itemCategory = jsonObject.getString("itemCategory");
                        String itemName = jsonObject.getString("itemName");
                        String itemDesc = jsonObject.getString("itemDesc");
                        String qty = jsonObject.getString("qty");
                        String totalItem = jsonObject.getString("totalItem");

                        Data_master_onhand data = new Data_master_onhand();
                        data.setItemId(itemId);
                        data.setItemCode(itemCode);
                        data.setItemCategory(itemCategory);
                        data.setItemName(itemName);
                        data.setItemDesc(itemDesc);
                        data.setQty(qty);
                        data.setTotalItem(totalItem);

                        itemOnHand.add(data);

                        counter = totalItem + " Data Ditemukan";
                    }

                    txtCountBrand.setText("Total Out Stock : 0");
                    txtCountTotal.setText("Total Qty : 0");
                    txtCounter.setText(counter);
                    adapter_master_onhand.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("qty", qty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchData(final String itemOrgId, final String keyword, final String orderBy)
    {
        itemOnHand.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCHDATA, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
//                            Toasty.error(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            imgNotFound.setVisibility(View.VISIBLE);
                            txtCounter.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            linCounter.setVisibility(View.GONE);
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setTotalItem(totalItem);

                            itemOnHand.add(data);

                            counter = totalItem + " Data Ditemukan";

                            imgNotFound.setVisibility(View.GONE);
                            linCounter.setVisibility(View.VISIBLE);
                            txtCounter.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            txtCounter.setText(counter);
                            txtCountBrand.setText("Total " + dataCatItem.replace("FG", "").replace("LENS", "") + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);
                            adapter_master_onhand.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", itemOrgId);
                hashMap.put("item_desc", keyword);
                hashMap.put("sort", orderBy);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchDataFrame(final String itemOrgId, final String keyword, final String orderBy)
    {
        itemOnHandFrame.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCHDATAFRAME, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
//                            Toasty.error(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            imgNotFound.setVisibility(View.VISIBLE);
                            txtCounter.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            linCounter.setVisibility(View.GONE);
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String itemImage = jsonObject.getString("itemImage");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setItemImg(itemImage);
                            data.setTotalItem(totalItem);

                            itemOnHandFrame.add(data);

                            counter = totalItem + " Data Ditemukan";

                            imgNotFound.setVisibility(View.GONE);
                            txtCounter.setVisibility(View.VISIBLE);
                            linCounter.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewFrame.setVisibility(View.VISIBLE);

                            txtCounter.setText(counter);
                            txtCountBrand.setText("Total " + itemOrgId + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);
                            adapter_master_onhandframe.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", itemOrgId);
                hashMap.put("item_desc", keyword);
                hashMap.put("sort", orderBy);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchDataFrameBin(final String itemOrgId, final String keyword, final String orderBy)
    {
        itemOnHandFrame.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCHDATAFRAMEBIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.dismiss();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
//                            Toasty.error(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            imgNotFound.setVisibility(View.VISIBLE);
                            txtCounter.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            linCounter.setVisibility(View.GONE);
                        }
                        else
                        {
                            String itemId = jsonObject.getString("itemId");
                            String itemCode = jsonObject.getString("itemCode");
                            String itemCategory = jsonObject.getString("itemCategory");
                            String itemName = jsonObject.getString("itemName");
                            String itemDesc = jsonObject.getString("itemDesc");
                            String itemImage = jsonObject.getString("itemImage");
                            String qty = jsonObject.getString("qty");
                            String totalItem = jsonObject.getString("totalItem");
                            String totalBrand = jsonObject.getString("itemBrandqty");
                            String totalAll   = jsonObject.getString("itemTotalqty");

                            Data_master_onhand data = new Data_master_onhand();
                            data.setItemId(itemId);
                            data.setItemCode(itemCode);
                            data.setItemCategory(itemCategory);
                            data.setItemName(itemName);
                            data.setItemDesc(itemDesc);
                            data.setQty(qty);
                            data.setItemImg(itemImage);
                            data.setTotalItem(totalItem);

                            itemOnHandFrame.add(data);

                            counter = totalItem + " Data Ditemukan";

                            imgNotFound.setVisibility(View.GONE);
                            txtCounter.setVisibility(View.VISIBLE);
                            linCounter.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewFrame.setVisibility(View.VISIBLE);

                            txtCountBrand.setText("Total " + itemOrgId + " : "  + totalBrand);
                            txtCountTotal.setText("Total Qty : " + totalAll);
                            txtCounter.setText(counter);
                            adapter_master_onhandframe.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", itemOrgId);
                hashMap.put("item_desc", keyword);
                hashMap.put("sort", orderBy);
                hashMap.put("id_sales", idParty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSuggestion(final String orgId){
        itemSuggestion.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSUGGEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(OnHandActivity.class.getSimpleName(), "Subcategory : " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String data = object.getString("itemCategory");

                        itemSuggestion.add(data);
                    }

                    //Convert arraylist to String array
                    dataSuggestion = new String[itemSuggestion.size()];
                    dataSuggestion = itemSuggestion.toArray(dataSuggestion);
                    Log.d(OnHandActivity.class.getSimpleName(), Arrays.toString(dataSuggestion));

                    ArrayAdapter<String> adapterSuggestion = new ArrayAdapter<>(OnHandActivity.this,
                            android.R.layout.simple_spinner_item, dataSuggestion);
//                    autoTxtSearch.setThreshold(1);
//                    autoTxtSearch.setAdapter(adapterSuggestion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("org_id", orgId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTranslationCategory(final String itemname)
    {
        itemSubCategory.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETTRANSLASI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Translation category : ", response);
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("invalid"))
                        {
                            //do something
                        }
                        else
                        {
                            Data_sub_categoryonhand sub_categoryonhand = new Data_sub_categoryonhand();
                            sub_categoryonhand.setItemName(jsonObject.getString("item_name"));
                            sub_categoryonhand.setDesc(jsonObject.getString("desc"));

                            itemSubCategory.add(sub_categoryonhand);
                        }

                        adapter_subcategory_onhand.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String, String> map = new HashMap<>();
                map.put("desc", itemname);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOnHandBySubcategory(final String desc)
    {
        itemOnHand.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYTRANSLASI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.GONE);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String itemId = jsonObject.getString("itemId");
                        String itemCode = jsonObject.getString("itemCode");
                        String itemCategory = jsonObject.getString("itemCategory");
                        String itemName = jsonObject.getString("itemName");
                        String itemDesc = jsonObject.getString("itemDesc");
                        String qty = jsonObject.getString("qty");
                        String totalItem = jsonObject.getString("totalItem");

                        Data_master_onhand data = new Data_master_onhand();
                        data.setItemId(itemId);
                        data.setItemCode(itemCode);
                        data.setItemCategory(itemCategory);
                        data.setItemName(itemName);
                        data.setItemDesc(itemDesc);
                        data.setQty(qty);
                        data.setTotalItem(totalItem);

                        itemOnHand.add(data);

                        counter = totalItem + " Data Ditemukan";
                    }

                    txtCounter.setText(counter);
                    adapter_master_onhand.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("desc", desc);
                hashMap.put("sort", "DESC");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
