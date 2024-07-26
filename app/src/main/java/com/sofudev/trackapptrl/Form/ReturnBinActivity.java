package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_frame_brand;
import com.sofudev.trackapptrl.Adapter.Adapter_framesp_multi;
import com.sofudev.trackapptrl.Adapter.Adapter_retur_frame;
import com.sofudev.trackapptrl.Adapter.Adapter_sorting_onhand;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.MultiSelectSpFrame;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_frame_brand;
import com.sofudev.trackapptrl.Data.Data_sortingonhand;
import com.sofudev.trackapptrl.LocalDb.Db.ReturFrameHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelReturFrame;
import com.sofudev.trackapptrl.R;

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

import es.dmoral.toasty.Toasty;

public class ReturnBinActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = FormSpFrameActivity.class.getSimpleName();
    Config config = new Config();
    String URL_GETFRAMEBYBRANDFLAG      = config.Ip_address + config.spframe_get_byframe_flag;
    String URL_GETFRAMEBARCODEFLAG      = config.Ip_address + config.spframe_get_searchbarcodeflag;
    String URL_GETFRAMESEARCHBRANDFLAG  = config.Ip_address + config.spframe_get_searchframebrandflag;
    String URL_GETFRAMEBYITEMIDFLAG     = config.Ip_address + config.spframe_get_byitemidflag;
    String URL_GETBRANDFRAME            = config.Ip_address + config.spframe_get_framebrand;
    String URL_CEKQTYITEM               = config.Ip_address + config.spframe_get_qtybyitemflag;
    String URL_INSERTRETUR              = config.Ip_address + config.assignbin_insertretur;

    View custom;
    ImageView btnBack;
    Button btnSubmit;
    RippleView btnChooser;
    ProgressWheel loader;
    EditText txtSearch;
    TextView txtCounter, txtTmp, txtCounterBrand, txtCounterTotal;
    ImageView imgFrameNotFound;
    CardView cardView;
    LinearLayout linearCounter, progressLayout, linearEmpty;
    RecyclerView recyclerItemFrame;
    BottomDialog bottomDialog;

    Boolean check, isBarcode;
    String titleQtyBrand = "Total";
    String titleQtyTotal = "Total Qty : ";
    String totalData, addpos, newpos, dataCatItem, dataSortItem, itemSortId, itemSortDesc, flag, idPartySales,
    username, custname, scanContent;
    int qtyTemp;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ReturFrameHelper returFrameHelper;
    Adapter_framesp_multi adapter_framesp_multi;
    Adapter_sorting_onhand adapter_sorting_onhand;
    Adapter_frame_brand adapter_frame_brand;
    Adapter_retur_frame adapter_retur_frame;

    List<Data_sortingonhand> itemSorting = new ArrayList<>();
    List<Data_frame_brand> itemCategory = new ArrayList<>();
    List<ModelReturFrame> modelReturFrameList = new ArrayList<>();
    List<Data_fragment_bestproduct> itemBestProduct = new ArrayList<>();
    List<Data_fragment_bestproduct> returnItemBestProduct = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_bin);

        recyclerItemFrame = findViewById(R.id.form_returbin_recyclerview);
        linearEmpty  = findViewById(R.id.form_returbin_linearLayout);
        cardView     = findViewById(R.id.form_returbin_cardview);
        txtTmp       = findViewById(R.id.form_returbin_txttmp);
        btnBack      = findViewById(R.id.form_returbin_btnback);
        btnChooser   = findViewById(R.id.form_returbin_btnframe);
        btnSubmit    = findViewById(R.id.form_sreturbin_btncontinue);

        returFrameHelper = ReturFrameHelper.getINSTANCE(this);
        returFrameHelper.open();

        modelReturFrameList = returFrameHelper.getAllReturFrame();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerItemFrame.setLayoutManager(layoutManager);

        getBundle();
        getBrand();
        initialisePref();
        handlerItemCart();

        recyclerItemFrame.setAdapter(adapter_retur_frame);

        if (modelReturFrameList.size() > 0)
        {
            linearEmpty.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            recyclerItemFrame.setVisibility(View.VISIBLE);

            for (int i = 0; i < modelReturFrameList.size(); i++)
            {
                getStockItem(modelReturFrameList.get(i).getProductFlag(),
                        idPartySales,
                        String.valueOf(modelReturFrameList.get(i).getProductId()),
                        modelReturFrameList.get(i).getProductTempStock(),
                        modelReturFrameList.get(i).getProductQty(),
                        i);
            }
        }
        else
        {
            linearEmpty.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            recyclerItemFrame.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(this);
        btnChooser.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.form_returbin_btnback :
                finish();
                break;
            case R.id.form_returbin_btnframe :
                showDialogFrame();
                break;
            case R.id.form_sreturbin_btncontinue:
                showBottomDetail();
                break;
        }
    }

    private void getBundle(){
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            idPartySales  = bundle.getString("partysiteid");
            username      = bundle.getString("username");
            custname      = bundle.getString("custname");

            flag = "BIN";
        }
    }

    private void initialisePref(){
        pref = getApplicationContext().getSharedPreferences("PrefSPFrame", 0);
    }

    private void storingPref(){
        editor = pref.edit();
        editor.putString("DIALOGSP_ADDPOS", addpos);
        editor.putString("DIALOGSP_NEWPOS", newpos);
        editor.putString("DIALOGSP_ITEMSORTDESC", itemSortDesc);
        editor.apply();
    }

    private void showDialogFrame()
    {
        final RippleView rpFilter, rpSort;
        final RecyclerView recyclerFrame;
        final BootstrapButton btnAdd;
        final ImageView imgClear, imgScan;

        final Dialog dialog = new Dialog(ReturnBinActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_frame);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int) (width * 0.99f);
        int dialogHeight= (int) (height * 0.99f);
        layoutParams.width = dialogWidth;
        layoutParams.height= dialogHeight;
        dialog.getWindow().setAttributes(layoutParams);

        addpos = pref.getString("DIALOGSP_ADDPOS", "LEINZ");
        itemSortDesc = pref.getString("DIALOGSP_ITEMSORTDESC","DESC");
        newpos = pref.getString("DIALOGSP_NEWPOS","Qty Tertinggi");

        Log.d(FormSpFrameActivity.class.getSimpleName(), "Pos : " + addpos);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Sort : " + itemSortDesc);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Flag : " + flag);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales : " + idPartySales);

        rpFilter      = dialog.findViewById(R.id.dialog_chooseframe_rpfilter);
        rpSort        = dialog.findViewById(R.id.dialog_chooseframe_rpSort);
        loader        = dialog.findViewById(R.id.dialog_chooseframe_progressBar);
        txtSearch     = dialog.findViewById(R.id.dialog_chooseframe_txtSearch);
        txtCounter    = dialog.findViewById(R.id.dialog_chooseframe_txtcounter);
        linearCounter = dialog.findViewById(R.id.dialog_chooseframe_linearcounter);
        txtCounterBrand = dialog.findViewById(R.id.dialog_chooseframe_txtcounterbrand);
        txtCounterTotal = dialog.findViewById(R.id.dialog_chooseframe_txtcountertotal);
        recyclerFrame = dialog.findViewById(R.id.dialog_chooseframe_recyclerItem);
        imgFrameNotFound   = dialog.findViewById(R.id.dialog_chooseframe_imgNotfound);
        imgClear      = dialog.findViewById(R.id.dialog_chooseframe_btnClear);
        imgScan       = dialog.findViewById(R.id.dialog_chooseframe_btnScan);
        btnAdd        = dialog.findViewById(R.id.dialog_chooseframe_btnSave);
        progressLayout= dialog.findViewById(R.id.dialog_chooseframe_progressLayout);
        RecyclerView.LayoutManager verticalGrid = new LinearLayoutManager(ReturnBinActivity.this);
        recyclerFrame.setLayoutManager(verticalGrid);

        progressLayout.setVisibility(View.VISIBLE);

        getItemByBrand(addpos, itemSortDesc, flag, idPartySales);

        isBarcode = false;

        btnAdd.setEnabled(false);
        btnAdd.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow();
                isBarcode = true;
            }
        });

        adapter_framesp_multi = new Adapter_framesp_multi(ReturnBinActivity.this, itemBestProduct, new MultiSelectSpFrame() {
            @Override
            public void passResult(ArrayList<Data_fragment_bestproduct> itemList) {
                returnItemBestProduct = itemList;

                if (returnItemBestProduct.size() > 0)
                {
                    btnAdd.setEnabled(true);
                    btnAdd.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
                }
                else
                {
                    btnAdd.setEnabled(false);
                    btnAdd.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                }
            }
        });

        recyclerFrame.setAdapter(adapter_framesp_multi);

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSearch.getText().length() > 0)
                {
                    if (check)
                    {
                        txtSearch.setText("");
                        txtSearch.setEnabled(true);
                        isBarcode = false;
                        imgClear.setImageResource(R.drawable.ic_search_black);

                        progressLayout.setVisibility(View.VISIBLE);

                        itemBestProduct.clear();
                        getItemByBrand(addpos, itemSortDesc, flag, idPartySales);
                    }
                    else
                    {
                        recyclerFrame.setVisibility(View.VISIBLE);
                        txtCounter.setVisibility(View.VISIBLE);
                        linearCounter.setVisibility(View.VISIBLE);
                        imgFrameNotFound.setVisibility(View.GONE);

                        txtSearch.setText("");
                        txtSearch.setEnabled(true);
                        isBarcode = false;
                        imgClear.setImageResource(R.drawable.ic_search_black);

                        progressLayout.setVisibility(View.VISIBLE);

                        itemBestProduct.clear();
                        getItemByBrand(addpos, itemSortDesc, flag, idPartySales);
                    }
                }
                else
                {
                    txtSearch.setText("");
                    isBarcode = false;
                    progressLayout.setVisibility(View.VISIBLE);

                    itemBestProduct.clear();
                    getItemByBrand(addpos, itemSortDesc, flag, idPartySales);
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtSearch.getText().length() > 0)
                {
                    imgClear.setImageResource(R.drawable.ic_close);
                    if (isBarcode){
//                        Toast.makeText(getApplicationContext(), "Hasil dari scan barcode", Toast.LENGTH_LONG).show();

                        if (scanContent != null)
                        {
                            imgClear.setImageResource(R.drawable.ic_close);
                            txtSearch.setEnabled(false);

                            progressLayout.setVisibility(View.VISIBLE);
                            itemBestProduct.clear();
                            StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBARCODEFLAG, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressLayout.setVisibility(View.GONE);

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            if (object.names().get(0).equals("invalid")) {
                                                check = false;

                                                recyclerFrame.setVisibility(View.GONE);
                                                txtCounter.setVisibility(View.GONE);
                                                linearCounter.setVisibility(View.GONE);
                                                imgFrameNotFound.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                check = true;

                                                recyclerFrame.setVisibility(View.VISIBLE);
                                                txtCounter.setVisibility(View.VISIBLE);
                                                linearCounter.setVisibility(View.VISIBLE);
                                                imgFrameNotFound.setVisibility(View.GONE);

                                                String id = object.getString("frame_id");
                                                String title = object.getString("frame_name");
                                                String sku   = object.getString("frame_sku");
                                                String image = object.getString("frame_img");
                                                String price = object.getString("frame_price");
                                                String discperc = object.getString("frame_disc");
                                                String brandName = object.getString("frame_brand");
                                                String frameQty = object.getString("frame_qty");
                                                String frameWeight = object.getString("frame_weight");
                                                String collection = object.getString("frame_collect");
                                                String entryDate = object.getString("frame_entry");
                                                String qtySubtotal = object.getString("frame_brandqty");
                                                String qtyTotal = object.getString("frame_totalqty");
                                                String tempPrice = CurencyFormat(price);
                                                totalData = object.getString("total_output");

                                                Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                data.setProduct_id(id);
                                                data.setProduct_name(title);
                                                data.setProduct_code(sku);
                                                data.setProduct_image(image);
                                                data.setProduct_realprice("Rp " + tempPrice);
                                                data.setProduct_discpercent(discperc);
                                                data.setProduct_brand(brandName);
                                                data.setProduct_qty(frameQty);
                                                data.setProduct_weight(frameWeight);
                                                data.setProduct_collect(collection);
                                                data.setProduct_entry(entryDate);
                                                data.setProduct_brandqty(qtySubtotal);
                                                data.setProduct_totalqty(qtyTotal);

                                                itemBestProduct.add(data);

                                                String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                txtCounterBrand.setText(outBrandQty);
                                                txtCounterTotal.setText(outTotalQty);
                                            }
                                        }

                                        txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("key", scanContent);
                                    hashMap.put("flag", flag);
                                    hashMap.put("idsales", idPartySales);
//                hashMap.put("sort", itemSortDesc);,
                                    return hashMap;
                                }
                            };

                            AppController.getInstance().addToRequestQueue(request);
                        }
                        else
                        {
                            itemBestProduct.clear();
                        }
                    }
                    else
                    {
                        itemBestProduct.clear();
                    }
                }
                else
                {
                    itemBestProduct.clear();
                }
            }
        });

        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)))
                {
                    Boolean cekInput = false;
                    if (txtSearch.getText().toString().matches("[+-]?[0-9]+")){
                        cekInput = true;
                    }

                    progressLayout.setVisibility(View.VISIBLE);
                    txtSearch.setEnabled(false);
                    imgClear.setImageResource(R.drawable.ic_close);
                    itemBestProduct.clear();

                    if (cekInput && isBarcode){
                        StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBARCODEFLAG, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressLayout.setVisibility(View.GONE);

                                try {
                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        if (object.names().get(0).equals("invalid")) {
                                            check = false;

                                            recyclerFrame.setVisibility(View.GONE);
                                            txtCounter.setVisibility(View.GONE);
                                            linearCounter.setVisibility(View.GONE);
                                            imgFrameNotFound.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            check = true;

                                            recyclerFrame.setVisibility(View.VISIBLE);
                                            txtCounter.setVisibility(View.VISIBLE);
                                            linearCounter.setVisibility(View.VISIBLE);
                                            imgFrameNotFound.setVisibility(View.GONE);

                                            String id = object.getString("frame_id");
                                            String title = object.getString("frame_name");
                                            String sku   = object.getString("frame_sku");
                                            String image = object.getString("frame_img");
                                            String price = object.getString("frame_price");
                                            String discperc = object.getString("frame_disc");
                                            String brandName = object.getString("frame_brand");
                                            String frameQty = object.getString("frame_qty");
                                            String frameWeight = object.getString("frame_weight");
                                            String collection = object.getString("frame_collect");
                                            String entryDate = object.getString("frame_entry");
                                            String qtySubtotal = object.getString("frame_brandqty");
                                            String qtyTotal = object.getString("frame_totalqty");
                                            String tempPrice = CurencyFormat(price);
                                            totalData = object.getString("total_output");

                                            Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                            data.setProduct_id(id);
                                            data.setProduct_name(title);
                                            data.setProduct_code(sku);
                                            data.setProduct_image(image);
                                            data.setProduct_realprice("Rp " + tempPrice);
                                            data.setProduct_discpercent(discperc);
                                            data.setProduct_brand(brandName);
                                            data.setProduct_qty(frameQty);
                                            data.setProduct_weight(frameWeight);
                                            data.setProduct_collect(collection);
                                            data.setProduct_entry(entryDate);
                                            data.setProduct_brandqty(qtySubtotal);
                                            data.setProduct_totalqty(qtyTotal);

                                            itemBestProduct.add(data);

                                            String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                            String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                            txtCounterBrand.setText(outBrandQty);
                                            txtCounterTotal.setText(outTotalQty);
                                        }
                                    }

                                    txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                    List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                    newItem.addAll(itemBestProduct);
                                    itemBestProduct.clear();
                                    adapter_framesp_multi.notifyDataSetChanged();
                                    itemBestProduct.addAll(newItem);// add new data
                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("key", txtSearch.getText().toString());
                                hashMap.put("flag", flag);
                                hashMap.put("idsales", idPartySales);
//                hashMap.put("sort", itemSortDesc);
                                return hashMap;
                            }
                        };

                        AppController.getInstance().addToRequestQueue(request);
                    }
                    else
                    {
                        progressLayout.setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCHBRANDFLAG, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressLayout.setVisibility(View.GONE);
                                Log.d("Output 1 : ", response);

                                try {
                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        if (object.names().get(0).equals("invalid")) {
                                            check = false;

                                            recyclerFrame.setVisibility(View.GONE);
                                            txtCounter.setVisibility(View.GONE);
                                            linearCounter.setVisibility(View.GONE);
                                            imgFrameNotFound.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            check = true;

                                            recyclerFrame.setVisibility(View.VISIBLE);
                                            txtCounter.setVisibility(View.VISIBLE);
                                            linearCounter.setVisibility(View.VISIBLE);
                                            imgFrameNotFound.setVisibility(View.GONE);

                                            String id = object.getString("frame_id");
                                            String title = object.getString("frame_name");
                                            String sku   = object.getString("frame_sku");
                                            String image = object.getString("frame_img");
                                            String price = object.getString("frame_price");
                                            String discperc = object.getString("frame_disc");
                                            String brandName = object.getString("frame_brand");
                                            String frameQty = object.getString("frame_qty");
                                            String frameWeight = object.getString("frame_weight");
                                            String collection = object.getString("frame_collect");
                                            String entryDate = object.getString("frame_entry");
                                            String qtySubtotal = object.getString("frame_brandqty");
                                            String qtyTotal = object.getString("frame_totalqty");
                                            String tempPrice = CurencyFormat(price);
                                            totalData = object.getString("total_output");

                                            Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                            data.setProduct_id(id);
                                            data.setProduct_name(title);
                                            data.setProduct_code(sku);
                                            data.setProduct_image(image);
                                            data.setProduct_realprice("Rp " + tempPrice);
                                            data.setProduct_discpercent(discperc);
                                            data.setProduct_brand(brandName);
                                            data.setProduct_qty(frameQty);
                                            data.setProduct_weight(frameWeight);
                                            data.setProduct_collect(collection);
                                            data.setProduct_entry(entryDate);
                                            data.setProduct_brandqty(qtySubtotal);
                                            data.setProduct_totalqty(qtyTotal);

                                            itemBestProduct.add(data);

                                            String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                            String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                            txtCounterBrand.setText(outBrandQty);
                                            txtCounterTotal.setText(outTotalQty);
                                        }
                                    }

                                    txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                    List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                    newItem.addAll(itemBestProduct);
                                    itemBestProduct.clear();
                                    adapter_framesp_multi.notifyDataSetChanged();
                                    itemBestProduct.addAll(newItem);// add new data
                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("key", txtSearch.getText().toString());
                                hashMap.put("sort", itemSortDesc);
                                hashMap.put("brand", addpos);
                                hashMap.put("flag", flag);
                                hashMap.put("idsales", idPartySales);
                                return hashMap;
                            }
                        };

                        AppController.getInstance().addToRequestQueue(request);
                    }

                    return true;
                }
                return false;
            }
        });

        rpSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(ReturnBinActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                itemSorting.clear();
                itemSorting.add(new Data_sortingonhand("0", "Qty Terendah"));
                itemSorting.add(new Data_sortingonhand("1", "Qty Tertinggi"));

                adapter_sorting_onhand = new Adapter_sorting_onhand(ReturnBinActivity.this, itemSorting, newpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Urut Berdasarkan");
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

                if (!isFinishing()){
                    dialog.show();
                }

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        recyclerFrame.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.VISIBLE);

                        if (txtSearch.getText().length() > 0)
                        {
                            if (itemSortId.equals("0"))
                            {
                                itemSortDesc = "ASC";
                                storingPref();
                                itemBestProduct.clear();

                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCHBRANDFLAG, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressLayout.setVisibility(View.GONE);
                                        recyclerFrame.setVisibility(View.VISIBLE);
                                        Log.d("Output 2 : ", response);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            for (int i = 0; i < jsonArray.length(); i++)
                                            {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                if (object.names().get(0).equals("invalid")) {
                                                    check = false;

                                                    recyclerFrame.setVisibility(View.GONE);
                                                    txtCounter.setVisibility(View.GONE);
                                                    linearCounter.setVisibility(View.GONE);
                                                    imgFrameNotFound.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    check = true;

                                                    recyclerFrame.setVisibility(View.VISIBLE);
                                                    txtCounter.setVisibility(View.VISIBLE);
                                                    linearCounter.setVisibility(View.VISIBLE);
                                                    imgFrameNotFound.setVisibility(View.GONE);

                                                    String id = object.getString("frame_id");
                                                    String title = object.getString("frame_name");
                                                    String sku   = object.getString("frame_sku");
                                                    String image = object.getString("frame_img");
                                                    String price = object.getString("frame_price");
                                                    String discperc = object.getString("frame_disc");
                                                    String brandName = object.getString("frame_brand");
                                                    String frameQty = object.getString("frame_qty");
                                                    String frameWeight = object.getString("frame_weight");
                                                    String collection = object.getString("frame_collect");
                                                    String entryDate = object.getString("frame_entry");
                                                    String qtySubtotal = object.getString("frame_brandqty");
                                                    String qtyTotal = object.getString("frame_totalqty");
                                                    String tempPrice = CurencyFormat(price);
                                                    totalData = object.getString("total_output");

                                                    Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                    data.setProduct_id(id);
                                                    data.setProduct_name(title);
                                                    data.setProduct_code(sku);
                                                    data.setProduct_image(image);
                                                    data.setProduct_realprice("Rp " + tempPrice);
                                                    data.setProduct_discpercent(discperc);
                                                    data.setProduct_brand(brandName);
                                                    data.setProduct_qty(frameQty);
                                                    data.setProduct_weight(frameWeight);
                                                    data.setProduct_collect(collection);
                                                    data.setProduct_entry(entryDate);
                                                    data.setProduct_brandqty(qtySubtotal);
                                                    data.setProduct_totalqty(qtyTotal);

                                                    itemBestProduct.add(data);

                                                    String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                    String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                    txtCounterBrand.setText(outBrandQty);
                                                    txtCounterTotal.setText(outTotalQty);
                                                }
                                            }

                                            txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("key", txtSearch.getText().toString());
                                        hashMap.put("sort", itemSortDesc);
                                        hashMap.put("brand", addpos);
                                        hashMap.put("flag", flag);
                                        hashMap.put("idsales", idPartySales);
                                        return hashMap;
                                    }
                                };

                                AppController.getInstance().addToRequestQueue(request);
                            }
                            else {
                                itemSortDesc = "DESC";
                                storingPref();
                                itemBestProduct.clear();

                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCHBRANDFLAG, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressLayout.setVisibility(View.GONE);
                                        recyclerFrame.setVisibility(View.VISIBLE);
                                        Log.d("Output 3 : ", response);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            for (int i = 0; i < jsonArray.length(); i++)
                                            {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                if (object.names().get(0).equals("invalid")) {
                                                    check = false;

                                                    recyclerFrame.setVisibility(View.GONE);
                                                    txtCounter.setVisibility(View.GONE);
                                                    linearCounter.setVisibility(View.GONE);
                                                    imgFrameNotFound.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    check = true;

                                                    recyclerFrame.setVisibility(View.VISIBLE);
                                                    txtCounter.setVisibility(View.VISIBLE);
                                                    linearCounter.setVisibility(View.VISIBLE);
                                                    imgFrameNotFound.setVisibility(View.GONE);

                                                    String id = object.getString("frame_id");
                                                    String title = object.getString("frame_name");
                                                    String sku   = object.getString("frame_sku");
                                                    String image = object.getString("frame_img");
                                                    String price = object.getString("frame_price");
                                                    String discperc = object.getString("frame_disc");
                                                    String brandName = object.getString("frame_brand");
                                                    String frameQty = object.getString("frame_qty");
                                                    String frameWeight = object.getString("frame_weight");
                                                    String collection = object.getString("frame_collect");
                                                    String entryDate = object.getString("frame_entry");
                                                    String qtySubtotal = object.getString("frame_brandqty");
                                                    String qtyTotal = object.getString("frame_totalqty");
                                                    String tempPrice = CurencyFormat(price);
                                                    totalData = object.getString("total_output");

                                                    Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                    data.setProduct_id(id);
                                                    data.setProduct_name(title);
                                                    data.setProduct_code(sku);
                                                    data.setProduct_image(image);
                                                    data.setProduct_realprice("Rp " + tempPrice);
                                                    data.setProduct_discpercent(discperc);
                                                    data.setProduct_brand(brandName);
                                                    data.setProduct_qty(frameQty);
                                                    data.setProduct_weight(frameWeight);
                                                    data.setProduct_collect(collection);
                                                    data.setProduct_entry(entryDate);
                                                    data.setProduct_brandqty(qtySubtotal);
                                                    data.setProduct_totalqty(qtyTotal);

                                                    itemBestProduct.add(data);

                                                    String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                    String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                    txtCounterBrand.setText(outBrandQty);
                                                    txtCounterTotal.setText(outTotalQty);
                                                }
                                            }

                                            txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("key", txtSearch.getText().toString());
                                        hashMap.put("sort", itemSortDesc);
                                        hashMap.put("brand", addpos);
                                        hashMap.put("flag", flag);
                                        hashMap.put("idsales", idPartySales);
                                        return hashMap;
                                    }
                                };

                                AppController.getInstance().addToRequestQueue(request);
                            }
                        }
                        else {
                            if (itemSortId.equals("0")) {
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Pos1 : " + addpos);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Sort1 : " + itemSortDesc);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Flag1 : " + flag);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales1 : " + idPartySales);

                                itemSortDesc = "ASC";
                                storingPref();

                                itemBestProduct.clear(); //here items is an ArrayList populating the RecyclerView
                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBYBRANDFLAG, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressLayout.setVisibility(View.GONE);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            for (int i = 0; i < jsonArray.length(); i++)
                                            {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                if (object.names().get(0).equals("invalid"))
                                                {
                                                    check = false;

                                                    recyclerFrame.setVisibility(View.GONE);
                                                    txtCounter.setVisibility(View.GONE);
                                                    linearCounter.setVisibility(View.GONE);
                                                    imgFrameNotFound.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    check = true;

                                                    recyclerFrame.setVisibility(View.VISIBLE);
                                                    txtCounter.setVisibility(View.VISIBLE);
                                                    linearCounter.setVisibility(View.VISIBLE);
                                                    imgFrameNotFound.setVisibility(View.GONE);

                                                    String id = object.getString("frame_id");
                                                    String title = object.getString("frame_name");
                                                    String sku   = object.getString("frame_sku");
                                                    String image = object.getString("frame_img");
                                                    String price = object.getString("frame_price");
                                                    String discperc = object.getString("frame_disc");
                                                    String brandName = object.getString("frame_brand");
                                                    String frameQty = object.getString("frame_qty");
                                                    String frameWeight = object.getString("frame_weight");
                                                    String collection = object.getString("frame_collect");
                                                    String entryDate = object.getString("frame_entry");
                                                    String qtySubtotal = object.getString("frame_brandqty");
                                                    String qtyTotal = object.getString("frame_totalqty");
                                                    String tempPrice = CurencyFormat(price);
                                                    totalData = object.getString("total_output");

                                                    Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                    data.setProduct_id(id);
                                                    data.setProduct_name(title);
                                                    data.setProduct_code(sku);
                                                    data.setProduct_image(image);
                                                    data.setProduct_realprice("Rp " + tempPrice);
                                                    data.setProduct_discpercent(discperc);
                                                    data.setProduct_brand(brandName);
                                                    data.setProduct_qty(frameQty);
                                                    data.setProduct_weight(frameWeight);
                                                    data.setProduct_collect(collection);
                                                    data.setProduct_entry(entryDate);
                                                    data.setProduct_brandqty(qtySubtotal);
                                                    data.setProduct_totalqty(qtyTotal);

                                                    itemBestProduct.add(data);

                                                    String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                    String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                    txtCounterBrand.setText(outBrandQty);
                                                    txtCounterTotal.setText(outTotalQty);
                                                }
                                            }

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                            txtCounter.setText(newItem.size() + " Data Ditemukan");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("category", addpos);
                                        hashMap.put("sort", itemSortDesc);
                                        hashMap.put("flag", flag);
                                        hashMap.put("idsales", idPartySales);
                                        return hashMap;
                                    }
                                };

                                AppController.getInstance().addToRequestQueue(request);
                            }
                            else {
                                itemSortDesc = "DESC";
                                storingPref();

                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Pos2 : " + addpos);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Sort2 : " + itemSortDesc);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Flag2 : " + flag);
                                Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales2 : " + idPartySales);

                                itemBestProduct.clear(); //here items is an ArrayList populating the RecyclerView
                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBYBRANDFLAG, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressLayout.setVisibility(View.GONE);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            for (int i = 0; i < jsonArray.length(); i++)
                                            {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                if (object.names().get(0).equals("invalid"))
                                                {
                                                    check = false;

                                                    recyclerFrame.setVisibility(View.GONE);
                                                    txtCounter.setVisibility(View.GONE);
                                                    linearCounter.setVisibility(View.GONE);
                                                    imgFrameNotFound.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    check = true;

                                                    recyclerFrame.setVisibility(View.VISIBLE);
                                                    txtCounter.setVisibility(View.VISIBLE);
                                                    linearCounter.setVisibility(View.VISIBLE);
                                                    imgFrameNotFound.setVisibility(View.GONE);

                                                    String id = object.getString("frame_id");
                                                    String title = object.getString("frame_name");
                                                    String sku   = object.getString("frame_sku");
                                                    String image = object.getString("frame_img");
                                                    String price = object.getString("frame_price");
                                                    String discperc = object.getString("frame_disc");
                                                    String brandName = object.getString("frame_brand");
                                                    String frameQty = object.getString("frame_qty");
                                                    String frameWeight = object.getString("frame_weight");
                                                    String collection = object.getString("frame_collect");
                                                    String entryDate = object.getString("frame_entry");
                                                    String qtySubtotal = object.getString("frame_brandqty");
                                                    String qtyTotal = object.getString("frame_totalqty");
                                                    String tempPrice = CurencyFormat(price);
                                                    totalData = object.getString("total_output");

                                                    Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                    data.setProduct_id(id);
                                                    data.setProduct_name(title);
                                                    data.setProduct_code(sku);
                                                    data.setProduct_image(image);
                                                    data.setProduct_realprice("Rp " + tempPrice);
                                                    data.setProduct_discpercent(discperc);
                                                    data.setProduct_brand(brandName);
                                                    data.setProduct_qty(frameQty);
                                                    data.setProduct_weight(frameWeight);
                                                    data.setProduct_collect(collection);
                                                    data.setProduct_entry(entryDate);
                                                    data.setProduct_brandqty(qtySubtotal);
                                                    data.setProduct_totalqty(qtyTotal);

                                                    itemBestProduct.add(data);

                                                    String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                    String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                    txtCounterBrand.setText(outBrandQty);
                                                    txtCounterTotal.setText(outTotalQty);
                                                }
                                            }

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                            txtCounter.setText(newItem.size() + " Data Ditemukan");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("category", addpos);
                                        hashMap.put("sort", itemSortDesc);
                                        hashMap.put("flag", flag);
                                        hashMap.put("idsales", idPartySales);
                                        return hashMap;
                                    }
                                };

                                AppController.getInstance().addToRequestQueue(request);
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        rpFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(ReturnBinActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                adapter_frame_brand = new Adapter_frame_brand(ReturnBinActivity.this, itemCategory, addpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Pilih Category Item");
                listView.setAdapter(adapter_frame_brand);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dataCatItem = itemCategory.get(position).getItem();

                        addpos = dataCatItem;
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

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storingPref();
                        recyclerFrame.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.VISIBLE);

                        if (txtSearch.getText().length() > 0)
                        {
                            itemBestProduct.clear();
                            StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCHBRANDFLAG, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressLayout.setVisibility(View.GONE);
                                    recyclerFrame.setVisibility(View.VISIBLE);
                                    Log.d("Output 4 : ", response);

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            if (object.names().get(0).equals("invalid")) {
                                                check = false;

                                                recyclerFrame.setVisibility(View.GONE);
                                                txtCounter.setVisibility(View.GONE);
                                                linearCounter.setVisibility(View.GONE);
                                                imgFrameNotFound.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                check = true;

                                                recyclerFrame.setVisibility(View.VISIBLE);
                                                txtCounter.setVisibility(View.VISIBLE);
                                                linearCounter.setVisibility(View.VISIBLE);
                                                imgFrameNotFound.setVisibility(View.GONE);

                                                String id = object.getString("frame_id");
                                                String title = object.getString("frame_name");
                                                String sku   = object.getString("frame_sku");
                                                String image = object.getString("frame_img");
                                                String price = object.getString("frame_price");
                                                String discperc = object.getString("frame_disc");
                                                String brandName = object.getString("frame_brand");
                                                String frameQty = object.getString("frame_qty");
                                                String frameWeight = object.getString("frame_weight");
                                                String collection = object.getString("frame_collect");
                                                String entryDate = object.getString("frame_entry");
                                                String qtySubtotal = object.getString("frame_brandqty");
                                                String qtyTotal = object.getString("frame_totalqty");
                                                String tempPrice = CurencyFormat(price);
                                                totalData = object.getString("total_output");

                                                Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                data.setProduct_id(id);
                                                data.setProduct_name(title);
                                                data.setProduct_code(sku);
                                                data.setProduct_image(image);
                                                data.setProduct_realprice("Rp " + tempPrice);
                                                data.setProduct_discpercent(discperc);
                                                data.setProduct_brand(brandName);
                                                data.setProduct_qty(frameQty);
                                                data.setProduct_weight(frameWeight);
                                                data.setProduct_collect(collection);
                                                data.setProduct_entry(entryDate);
                                                data.setProduct_brandqty(qtySubtotal);
                                                data.setProduct_totalqty(qtyTotal);

                                                itemBestProduct.add(data);

                                                String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                txtCounterBrand.setText(outBrandQty);
                                                txtCounterTotal.setText(outTotalQty);
                                            }
                                        }

                                        txtCounter.setText(itemBestProduct.size() + " Data Ditemukan");

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("key", txtSearch.getText().toString());
                                    hashMap.put("sort", itemSortDesc);
                                    hashMap.put("brand", addpos);
                                    hashMap.put("flag", flag);
                                    hashMap.put("idsales", idPartySales);
                                    return hashMap;
                                }
                            };

                            AppController.getInstance().addToRequestQueue(request);
                        }
                        else
                        {
                            Log.d(FormSpFrameActivity.class.getSimpleName(), "Pos3 : " + addpos);
                            Log.d(FormSpFrameActivity.class.getSimpleName(), "Sort3 : " + itemSortDesc);
                            Log.d(FormSpFrameActivity.class.getSimpleName(), "Flag3 : " + flag);
                            Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales3 : " + idPartySales);

                            itemBestProduct.clear(); //here items is an ArrayList populating the RecyclerView
                            StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBYBRANDFLAG, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressLayout.setVisibility(View.GONE);
                                    Log.d(FormSpFrameActivity.class.getSimpleName(), response);

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            if (object.names().get(0).equals("invalid"))
                                            {
                                                check = false;

                                                recyclerFrame.setVisibility(View.GONE);
                                                txtCounter.setVisibility(View.GONE);
                                                linearCounter.setVisibility(View.GONE);
                                                imgFrameNotFound.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                check = true;

                                                recyclerFrame.setVisibility(View.VISIBLE);
                                                txtCounter.setVisibility(View.VISIBLE);
                                                linearCounter.setVisibility(View.VISIBLE);
                                                imgFrameNotFound.setVisibility(View.GONE);

                                                String id = object.getString("frame_id");
                                                String title = object.getString("frame_name");
                                                String sku   = object.getString("frame_sku");
                                                String image = object.getString("frame_img");
                                                String price = object.getString("frame_price");
                                                String discperc = object.getString("frame_disc");
                                                String brandName = object.getString("frame_brand");
                                                String frameQty = object.getString("frame_qty");
                                                String frameWeight = object.getString("frame_weight");
                                                String collection = object.getString("frame_collect");
                                                String entryDate = object.getString("frame_entry");
                                                String qtySubtotal = object.getString("frame_brandqty");
                                                String qtyTotal = object.getString("frame_totalqty");
                                                String tempPrice = CurencyFormat(price);
                                                totalData = object.getString("total_output");

                                                Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                                                data.setProduct_id(id);
                                                data.setProduct_name(title);
                                                data.setProduct_code(sku);
                                                data.setProduct_image(image);
                                                data.setProduct_realprice("Rp " + tempPrice);
                                                data.setProduct_discpercent(discperc);
                                                data.setProduct_brand(brandName);
                                                data.setProduct_qty(frameQty);
                                                data.setProduct_weight(frameWeight);
                                                data.setProduct_collect(collection);
                                                data.setProduct_entry(entryDate);
                                                data.setProduct_brandqty(qtySubtotal);
                                                data.setProduct_totalqty(qtyTotal);

                                                itemBestProduct.add(data);

                                                String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                                                String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                                                txtCounterBrand.setText(outBrandQty);
                                                txtCounterTotal.setText(outTotalQty);
                                            }
                                        }

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                        txtCounter.setText(newItem.size() + " Data Ditemukan");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("category", addpos);
                                    hashMap.put("sort", itemSortDesc);
                                    hashMap.put("flag", flag);
                                    hashMap.put("idsales", idPartySales);
                                    return hashMap;
                                }
                            };

                            AppController.getInstance().addToRequestQueue(request);
                        }

                        dialog.dismiss();
                    }
                });

                if (!isFinishing()){
                    dialog.show();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < returnItemBestProduct.size(); i++)
                {
                    Log.d(FormSpFrameActivity.class.getSimpleName(), "Item name (" + i + ") : "
                            + returnItemBestProduct.get(i).getProduct_name());
                    try {
                        Thread.sleep(150);

                        showDetailProdukMulti(returnItemBestProduct.get(i).getProduct_id(), returnItemBestProduct.get(i).getProduct_collect());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                dialog.dismiss();
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomDetail() {
        int len = modelReturFrameList.size();
        List<Boolean> sisanya = new ArrayList<>();

        for (int i = 0; i < len; i++)
        {
            String item = modelReturFrameList.get(i).getProductName();
            int stock = modelReturFrameList.get(i).getProductStock();
            int qty   = modelReturFrameList.get(i).getProductQty();

            Log.d("Stock " + item, " Sisa = " + stock);

            if (stock < 0)
            {
                sisanya.add(i, false);
            }
            else
            {
                sisanya.add(i, true);
            }
        }

        boolean cek = sisanya.contains(false);

        if (cek)
        {
            Log.d("Information Cart", "Ada item yang minus");

            final Dialog dialog = new Dialog(ReturnBinActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

            dialog.setContentView(R.layout.dialog_warning);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            lwindow.copyFrom(dialog.getWindow().getAttributes());
            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

            ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setAttributes(lwindow);
            if (!isFinishing()){
                dialog.show();
            }
        }
        else
        {
            custom = LayoutInflater.from(ReturnBinActivity.this).inflate(R.layout.bottom_dialog_returbin, null);
            bottomDialog = new BottomDialog.Builder(ReturnBinActivity.this)
                    .setTitle("Lengkapi Data")
                    .setCustomView(custom)
                    .build();

            final BootstrapEditText edNotes   = custom.findViewById(R.id.bottomdialog_returbin_edtNote);
            RippleView btnSave          = custom.findViewById(R.id.bottomdialog_returbin_btnsave);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < modelReturFrameList.size(); i++)
                    {
                        insertReturbin(
                                idPartySales,
                                String.valueOf(modelReturFrameList.get(i).getProductId()),
                                String.valueOf(modelReturFrameList.get(i).getProductQty()),
                                username,
                                edNotes.getText().toString(),
                                i == modelReturFrameList.size() - 1);
                    }
                }
            });

            bottomDialog.show();
        }
    }

    public void scanNow(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan 2d barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (scanContent != null)
        {
            txtTmp.setText(scanContent);
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

    private void tambahQty(String itemId){
        returFrameHelper.open();
        ModelReturFrame modelReturFrame = returFrameHelper.getReturFrame(Integer.parseInt(itemId));

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < modelReturFrameList.size(); i++)
        {
            Log.d(TAG, "item name : " + modelReturFrameList.get(i).getProductId() + " qty : " + modelReturFrameList.get(i).getProductQty());
            temp.add(String.valueOf(modelReturFrameList.get(i).getProductId()));
        }

        int position = -1;
        position = temp.lastIndexOf(itemId);
        if (position == -1) {
            Log.e(TAG, "Object not found in List");
        } else {
            Log.i(TAG, "" + position);
        }

        int price = modelReturFrame.getProductPrice();
        int qty   = modelReturFrame.getProductQty();
        int stock = modelReturFrame.getProductStock();
        int discprice = modelReturFrame.getProductDiscPrice();

        qty = qty + 1;
        int newprice = price * qty;
        int newdiscprice = discprice * qty;

        stock = stock - qty;
        Log.d("Stok Add", String.valueOf(stock));
        modelReturFrame.setProductStock(stock);

        modelReturFrame.setNewProductPrice(newprice);
        modelReturFrame.setProductQty(qty);
        modelReturFrame.setNewProductDiscPrice(newdiscprice);
        modelReturFrameList.set(position, modelReturFrame);
        returFrameHelper.updateReturFrameQty(modelReturFrame);
        adapter_retur_frame.notifyDataSetChanged();

        /*addFrameSpHelper.open();
        totalQty   = addFrameSpHelper.countTotalQty();
        totalPrice = addFrameSpHelper.countTotalPrice();
        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        txtShipping.setText("Rp. 0,00");
        shipmentPrice = "0";

        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/
    }

    private void handlerItemCart() {
        adapter_retur_frame = new Adapter_retur_frame(ReturnBinActivity.this, modelReturFrameList, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, final int pos, String id) {
                int btn = view.getId();

                switch (btn) {
                    case R.id.item_cartframesp_btnRemove:
                        returFrameHelper.deleteWishlist(modelReturFrameList.get(pos).getProductId());

                        modelReturFrameList.remove(pos);
                        adapter_retur_frame.notifyItemRemoved(pos);

                        if (modelReturFrameList.size() > 0) {
                            linearEmpty.setVisibility(View.GONE);
                            recyclerItemFrame.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                        } else {
                            linearEmpty.setVisibility(View.VISIBLE);
                            recyclerItemFrame.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
                        }

                        returFrameHelper.open();

                        /*totalQty = returFrameHelper.countTotalQty();
                        totalPrice = returFrameHelper.countTotalPrice();
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/

                        break;

                    case R.id.item_cartframesp_btnPlus:
                        returFrameHelper.open();

                        ModelReturFrame modelReturFrame = returFrameHelper.getReturFrame(modelReturFrameList.get(pos).getProductId());

                        int price = modelReturFrame.getProductPrice();
                        int qty = Math.max(modelReturFrame.getProductQty(), 1);
                        int stock = modelReturFrame.getProductStock();
                        int discprice = modelReturFrame.getProductDiscPrice();

                        qty = qty + 1;
                        int newprice = price * qty;
                        int newdiscprice = discprice * qty;

                        stock = stock - qty;
                        Log.d("Stok Add", String.valueOf(stock));
                        modelReturFrame.setProductStock(stock);

                        modelReturFrame.setNewProductPrice(newprice);
                        modelReturFrame.setProductQty(qty);
                        modelReturFrame.setNewProductDiscPrice(newdiscprice);
                        modelReturFrameList.set(pos, modelReturFrame);
                        returFrameHelper.updateReturFrameQty(modelReturFrame);
                        adapter_retur_frame.notifyDataSetChanged();

                        returFrameHelper.open();

                        /*totalQty = returFrameHelper.countTotalQty();
                        totalPrice = returFrameHelper.countTotalPrice();
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/

                        break;

                    case R.id.item_cartframesp_btnMinus:
                        ModelReturFrame mModelReturFrame = returFrameHelper.getReturFrame(modelReturFrameList.get(pos).getProductId());

                        int mPrice = mModelReturFrame.getProductPrice();
                        int mQty = mModelReturFrame.getProductQty();
                        int mDiscprice = mModelReturFrame.getProductDiscPrice();
                        int mStock = mModelReturFrame.getProductStock();

                        mQty = mQty - 1;

                        if (mQty == 0) {
                            mQty = mQty + 1;
                        }

                        mStock = mStock - mQty;
                        Log.d("Stok Minus", String.valueOf(mStock));
                        mModelReturFrame.setProductStock(mStock);

                        modelReturFrameList.set(pos, mModelReturFrame);
                        returFrameHelper.updateReturFrameQty(mModelReturFrame);
                        adapter_retur_frame.notifyDataSetChanged();

                        if (mQty == 0) {
                            Toasty.info(getApplicationContext(), "Minimal Order 1 pcs", Toast.LENGTH_SHORT).show();
                        } else {
                            int mNewPrice = mPrice * mQty;
                            int mNewdiscprice = mDiscprice * mQty;

                            mModelReturFrame.setNewProductPrice(mNewPrice);
                            mModelReturFrame.setProductQty(mQty);
                            mModelReturFrame.setNewProductDiscPrice(mNewdiscprice);

                            modelReturFrameList.set(pos, mModelReturFrame);
                            returFrameHelper.updateReturFrameQty(mModelReturFrame);
                            adapter_retur_frame.notifyDataSetChanged();
                        }

                        returFrameHelper.open();

                        /*totalQty = returFrameHelper.countTotalQty();
                        totalPrice = returFrameHelper.countTotalPrice();
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/

                        break;
                }
            }
        });

        adapter_retur_frame.notifyDataSetChanged();

        int len = modelReturFrameList.size();

        for (int i = 0; i < len; i++)
        {
            ModelReturFrame modelReturFrame = returFrameHelper.getReturFrame(modelReturFrameList.get(i).getProductId());

            String item = modelReturFrame.getProductName();
            int stock = modelReturFrame.getProductStock();
            int qty   = modelReturFrame.getProductQty();
            int sisa  = stock - qty;

            Log.d("Stock " + item, " Sisa = " + stock);
            modelReturFrame.setProductStock(sisa);

            modelReturFrameList.set(i, modelReturFrame);
            returFrameHelper.updateReturFrameQty(modelReturFrame);
            adapter_retur_frame.notifyDataSetChanged();
        }

        /*totalQty   = addFrameSpHelper.countTotalQty();
        totalPrice = addFrameSpHelper.countTotalPrice();
        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        txtShipping.setText("Rp. 0,00");
        shipmentPrice = "0";

        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/

        if (modelReturFrameList.size() > 0)
        {
            linearEmpty.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            recyclerItemFrame.setVisibility(View.VISIBLE);

            adapter_retur_frame.notifyDataSetChanged();
        }
        else
        {
            linearEmpty.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            recyclerItemFrame.setVisibility(View.GONE);
        }
    }

    private void updateStock(String itemId, int lastOrder, int index)
    {
        returFrameHelper.open();
        ModelReturFrame modelReturFrame = new ModelReturFrame();
        if (itemId != null)
        {
            modelReturFrame = returFrameHelper.getReturFrame(Integer.parseInt(itemId));
        }

        int stock = qtyTemp - lastOrder;
        Log.d("Stok Change 1 : ", String.valueOf(stock));
        modelReturFrame.setProductStock(stock);
        modelReturFrame.setProductTempStock(qtyTemp);

        modelReturFrameList.set(index, modelReturFrame);
        adapter_retur_frame.notifyDataSetChanged();

        returFrameHelper.updateReturFrameStock(modelReturFrame, qtyTemp);
        returFrameHelper.updateReturFrameStockTmp(modelReturFrame, qtyTemp);
    }

    public void enableButtonSubmit()
    {
        btnSubmit.setBackgroundColor(Color.parseColor("#ff9100"));
        btnSubmit.setTextColor(Color.parseColor("#FFFFFF"));
        btnSubmit.setEnabled(true);
    }

    public void disableButtonSubmit()
    {
        btnSubmit.setBackgroundColor(Color.parseColor("#b4b3b3"));
        btnSubmit.setTextColor(Color.parseColor("#58595e"));
        btnSubmit.setEnabled(false);
    }

    int getIndexList(int idProduct)
    {
        int idx = -1;
        for (int i = 0; i < modelReturFrameList.size(); i++)
        {
            if (modelReturFrameList.get(i).getProductId() == idProduct)
            {
                idx = i;
            }
        }
        return idx;
    }

    public void handleQty(ModelReturFrame returFrame){
        if (modelReturFrameList.size() > 0)
        {
            int idx = getIndexList(returFrame.getProductId());
            modelReturFrameList.set(idx, returFrame);

            /*totalQty   = inputTotalQty;
            totalPrice = inputTotalPrice;

            totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

            txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
            txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
            txtShipping.setText("Rp. 0,00");
            shipmentPrice = "0";

            totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));*/
        }

        if (!recyclerItemFrame.isComputingLayout() && recyclerItemFrame.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
        {
            adapter_retur_frame.notifyDataSetChanged();
        }
    }

    private void getItemByBrand(final String category, final String sorting, final String flag, final String salesid) {
        itemBestProduct.clear();
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Pos0 : " + category);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Sort0 : " + sorting);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Flag0 : " + flag);
        Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales0 : " + salesid);

        StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBYBRANDFLAG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressLayout.getVisibility() == View.VISIBLE)
                {
                    progressLayout.setVisibility(View.GONE);
                }

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            check = false;

                            txtCounter.setVisibility(View.GONE);
                            linearCounter.setVisibility(View.GONE);
                            imgFrameNotFound.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            check = true;

                            linearCounter.setVisibility(View.VISIBLE);

                            String id = object.getString("frame_id");
                            String title = object.getString("frame_name");
                            String sku   = object.getString("frame_sku");
                            String image = object.getString("frame_img");
                            String price = object.getString("frame_price");
                            String discperc = object.getString("frame_disc");
                            String brandName = object.getString("frame_brand");
                            String frameQty = object.getString("frame_qty");
                            String frameWeight = object.getString("frame_weight");
                            String collection = object.getString("frame_collect");
                            String entryDate = object.getString("frame_entry");
                            String qtySubtotal = object.getString("frame_brandqty");
                            String qtyTotal = object.getString("frame_totalqty");
                            String tempPrice = CurencyFormat(price);
                            totalData = object.getString("total_output");

                            Data_fragment_bestproduct data = new Data_fragment_bestproduct();
                            data.setProduct_id(id);
                            data.setProduct_name(title);
                            data.setProduct_code(sku);
                            data.setProduct_image(image);
                            data.setProduct_realprice("Rp " + tempPrice);
                            data.setProduct_discpercent(discperc);
                            data.setProduct_brand(brandName);
                            data.setProduct_qty(frameQty);
                            data.setProduct_weight(frameWeight);
                            data.setProduct_collect(collection);
                            data.setProduct_entry(entryDate);
                            data.setProduct_brandqty(qtySubtotal);
                            data.setProduct_totalqty(qtyTotal);

                            itemBestProduct.add(data);

                            String outBrandQty = titleQtyBrand + " " + itemBestProduct.get(0).getProduct_brand() + " : " + itemBestProduct.get(0).getProduct_brandqty();
                            String outTotalQty = titleQtyTotal + itemBestProduct.get(0).getProduct_totalqty();
                            txtCounterBrand.setText(outBrandQty);
                            txtCounterTotal.setText(outTotalQty);
                        }
                    }

                    List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                    newItem.addAll(itemBestProduct);
                    itemBestProduct.clear();
                    adapter_framesp_multi.notifyDataSetChanged();

                    itemBestProduct.addAll(newItem);// add new data
                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                    txtCounter.setText(newItem.size() + " Data Ditemukan");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("category", category);
                hashMap.put("sort", sorting);
                hashMap.put("flag", flag);
                hashMap.put("idsales", salesid);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getBrand() {
        itemCategory.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URL_GETBRANDFRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String item = object.getString("frame_brand");

                        Data_frame_brand dt = new Data_frame_brand();
                        dt.setItem(item);

                        Log.d(FormSpFrameActivity.class.getSimpleName(), "Brand : " + item);

                        itemCategory.add(dt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showDetailProdukMulti(final String id, final String collection) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMEBYITEMIDFLAG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.d("DETAIL PRODUK", object.getString("frame_id"));
                        Log.d("DETAIL PRODUK", object.getString("frame_name"));
                        Log.d("DETAIL PRODUK", object.getString("frame_sku"));
                        Log.d("DETAIL PRODUK", object.getString("frame_image"));
                        Log.d("DETAIL PRODUK", object.getString("frame_price"));
                        Log.d("DETAIL PRODUK", object.getString("frame_disc"));
                        Log.d("DETAIL PRODUK", object.getString("frame_disc_price"));
                        Log.d("DETAIL PRODUK", object.getString("frame_brand"));
                        Log.d("DETAIL PRODUK", object.getString("frame_qty"));
                        Log.d("DETAIL PRODUK", object.getString("frame_weight"));

                        returFrameHelper.open();

                        int sts = returFrameHelper.checkReturFrame(object.getInt("frame_id"));

                        if (sts < 1)
                        {
                            ModelReturFrame item = new ModelReturFrame();
                            item.setProductId(object.getInt("frame_id"));
                            item.setProductName(object.getString("frame_name"));
                            item.setProductCode(object.getString("frame_sku"));
                            item.setProductQty(1);
                            item.setProductPrice(object.getInt("frame_price"));
                            item.setProductDiscPrice(object.getInt("frame_disc_price"));
                            item.setProductDisc(object.getInt("frame_disc"));
                            item.setNewProductPrice(object.getInt("frame_price"));
                            item.setNewProductDiscPrice(object.getInt("frame_disc_price"));
                            item.setProductStock(object.getInt("frame_qty"));
                            item.setProductTempStock(object.getInt("frame_qty"));
                            item.setProductWeight(object.getInt("frame_weight"));
                            item.setProductImage(object.getString("frame_image"));
                            item.setProductFlag(flag);
                            item.setProductCollect(flag.equals("STORE") ? "" : object.getString("frame_collect"));

                            long status = returFrameHelper.insertReturFrame(item);

                            Toasty.success(getApplicationContext(), "Item berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                            modelReturFrameList = returFrameHelper.getAllReturFrame();

                            handlerItemCart();
                            recyclerItemFrame.setAdapter(adapter_retur_frame);
                        }
                        else
                        {
                            tambahQty(id);
                        }
                    }

                    for (int j = 0; j < modelReturFrameList.size(); j++)
                    {
                        Log.d(FormSpFrameActivity.class.getSimpleName(), "Db name (" + j + ") : "
                                + modelReturFrameList.get(j).getProductName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_lensa", id);
                hashMap.put("flag", flag);
                hashMap.put("idsales", idPartySales);
                hashMap.put("collection", collection);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getStockItem(final String flag, final String idSales, final String itemId, final int lastQty, final int lastOrder, final int index){
        StringRequest request = new StringRequest(Request.Method.POST, URL_CEKQTYITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("invalid"))
                    {
                        qtyTemp = 0;

                        Log.d("Invalid 1 : ", "Data tidak ditemukan");
                    }
                    else
                    {
                        qtyTemp = object.getInt("frame_qty");

                        Log.d("Last Qty 1 : ", String.valueOf(lastQty));
                        Log.d("Realtime Qty 1 :", String.valueOf(qtyTemp));
                        Log.d("Last Order 1 : ", String.valueOf(lastOrder));

                        updateStock(itemId, lastOrder, index);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(FormSpFrameActivity.class.getSimpleName(), "Error log : " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("flag", flag);
                map.put("key", itemId);
                map.put("idsales", idSales);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertReturbin(final String idSales, final String itemId, final String qty, final String username, final String note, final boolean isLast){
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTRETUR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("Success"))
                    {
                        if (isLast)
                        {
                            returFrameHelper.open();
                            returFrameHelper.truncReturFrame();

                            Toasty.success(getApplicationContext(), "Data has been save", Toast.LENGTH_SHORT).show();
                            bottomDialog.dismiss();

                            finish();
                        }
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), "Data failed to save", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_sales", idSales);
                map.put("item_id", itemId);
                map.put("qty", qty);
                map.put("nama_user", username);
                map.put("note", note);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}