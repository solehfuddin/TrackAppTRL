package com.sofudev.trackapptrl;

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
import android.widget.ImageView;
import android.widget.ListView;

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
    String URLGETBYORGID  = config.Ip_address + config.masteronhand_getbyorgid;
    String URLGETBYSORT   = config.Ip_address + config.masteronhand_getbysorting;
    String URLGETBYQTY    = config.Ip_address + config.masteronhand_getbyqty;
    String URLGETSUGGEST  = config.Ip_address + config.masteronhand_getsuggestion;
    String URLSEARCHDATA  = config.Ip_address + config.masteronhand_search;
    String URLGETTRANSLASI= config.Ip_address + config.masteronhand_gettranslation;
    String URLGETBYTRANSLASI = config.Ip_address + config.masteronhand_getbytranslation;

    ACProgressFlower loading;
    CircleProgressBar loader;
    RecyclerView recyclerView;
    UniversalFontTextView txtCounter;
    AutoCompleteTextView autoTxtSearch;
    ImageView imgNotFound;
    RippleView rpFilter, rpBack, rpSorting, rpMore;
    ArrayList<String> itemSuggestion = new ArrayList<>();
    List<Data_categoryonhand> itemCategory = new ArrayList<>();
    List<Data_sub_categoryonhand> itemSubCategory = new ArrayList<>();
    List<Data_sortingonhand> itemSorting = new ArrayList<>();
    List<Data_master_onhand> itemOnHand = new ArrayList<>();
    String filterpos, addpos, addsubpos, newpos, dataCatItem, dataSubCatItem, dataSortItem, itemOrgId, itemSortId, itemSortDesc, counter;
    String [] dataSuggestion;
    RecyclerView.LayoutManager recyclerLM;
    RippleView rpSearch;

    Adapter_subcategory_onhand adapter_subcategory_onhand;
    Adapter_category_onhand adapter_category_onhand;
    Adapter_sorting_onhand adapter_sorting_onhand;
    Adapter_master_onhand adapter_master_onhand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_hand);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        UniversalFontComponents.init(this);

        getItemInfo();

        recyclerView = findViewById(R.id.form_onhand_recycleview);
        loader = findViewById(R.id.form_onhand_progressBar);
        txtCounter   = findViewById(R.id.form_onhand_txtCounter);
        autoTxtSearch= findViewById(R.id.form_onhand_txtSearch);
        imgNotFound = findViewById(R.id.form_onhand_imgnotfound);

        loader.setVisibility(View.VISIBLE);
        showLoading();
        imgNotFound.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        recyclerLM = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLM);

        filterpos = "Category";
        dataCatItem = "FG MOE LENS";
        itemOrgId = "130";
        itemSortDesc = "ASC";
        addpos = dataCatItem;
        addsubpos = "All";
        newpos = "Qty Terendah";
        rpBack = findViewById(R.id.form_onhand_ripplebtnback);
        rpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter_category_onhand = new Adapter_category_onhand(OnHandActivity.this, itemCategory, addpos);
        adapter_subcategory_onhand = new Adapter_subcategory_onhand(OnHandActivity.this, itemSubCategory, addsubpos);

        rpFilter = findViewById(R.id.form_onhand_rpfilter);
        rpMore   = findViewById(R.id.form_onhand_ripplebtnFilter);
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

                        dialog.hide();
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
                });

                dialog.show();

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
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
//                        getSuggestion(itemOrgId);
                        dialog.hide();
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

                dialog.show();

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
//                        Toasty.info(getApplicationContext(), itemOrgId, Toast.LENGTH_SHORT).show();
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
//                        getSuggestion(itemOrgId);
                        dialog.hide();
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
                    searchData(itemOrgId, autoTxtSearch.getText().toString(), itemSortDesc);
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

//        getOnHandByOrgId("89");
        getOnHandBySort(itemOrgId, itemSortDesc);
        getTranslationCategory("");
        getSuggestion(itemOrgId);

        recyclerView.setAdapter(adapter_master_onhand);
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

    private void getItemInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETITEMNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String orgId = object.getString("orgId");
                        String itemName = object.getString("itemName");

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
                hashMap.put("org_id", orgId);
                hashMap.put("sort", sort);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOnHandByQty(final String qty)
    {
        itemOnHand.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYQTY, new Response.Listener<String>() {
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
                            txtCounter.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            txtCounter.setText(counter);
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

    private void getSuggestion(final String orgId){
        itemSuggestion.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSUGGEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                    autoTxtSearch.setThreshold(1);
                    autoTxtSearch.setAdapter(adapterSuggestion);
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
