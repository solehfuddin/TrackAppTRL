package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_compare_product;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.MultiSelectLensProduct;
import com.sofudev.trackapptrl.Data.Data_compare;
import com.sofudev.trackapptrl.Data.Data_compare_category;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCompareActivity extends AppCompatActivity {
    Config config = new Config();
    String URLGETPRODUCT = config.Ip_address + config.compare_getbyId;
    String URLCOMPARECATEGORY = config.Ip_address + config.compare_getcategory;

    Animation animation;
    RippleView btnBack, btnFilter, rippleDetailLeft, rippleDetailRight;
    ImageView imageLeft, imageRight;
    UniversalFontTextView txtProductLeft, txtProductRight;
    WebView webViewLeft, webViewRight;
    ExpandableLayout expandDetailLeft, expandDetailRight;

    View compare;
    RecyclerView recyclerCompare;
    RippleView rippleCompare;
    Button btnCompare;

    Adapter_compare_product adapter_compare_product;
    List<Data_compare_category> listCompareCategory = new ArrayList<>();
    Data_compare dataLeft, dataRight;
    boolean isExpandLeft = false, isExpandRight = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_compare);

        btnBack = findViewById(R.id.product_compare_ripplebtnback);
        btnFilter = findViewById(R.id.product_compare_ripplebtnfilter);
        imageLeft     = findViewById(R.id.product_compare_imageleft);
        imageRight    = findViewById(R.id.product_compare_imageright);
        txtProductLeft= findViewById(R.id.product_compare_txtproductleft);
        txtProductRight= findViewById(R.id.product_compare_txtproductright);
        webViewLeft = findViewById(R.id.product_compare_webviewleft);
        webViewRight = findViewById(R.id.product_compare_webviewright);
        rippleDetailLeft = findViewById(R.id.product_compare_rippledetailLeft);
        rippleDetailRight= findViewById(R.id.product_compare_rippledetailRight);
        expandDetailLeft = findViewById(R.id.product_compare_expandLeft);
        expandDetailRight= findViewById(R.id.product_compare_expandRight);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        getDataLeft("10");
        getDataRight("10");

        rippleDetailLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandLeft = !isExpandLeft;

                if (isExpandLeft)
                {
                    expandDetailLeft.expand();
                }
                else
                {
                    expandDetailLeft.collapse();
                }
            }
        });

        rippleDetailRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandRight = !isExpandRight;

                if (isExpandRight)
                {
                    expandDetailRight.expand();
                }
                else
                {
                    expandDetailRight.collapse();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showDialog() {
        final List<Data_compare_category> categoryList = new ArrayList<>();
        compare = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_compareproduct, null);
        recyclerCompare = compare.findViewById(R.id.dialog_compareproduct_recyclerview);
        rippleCompare = compare.findViewById(R.id.dialog_compareproduct_ripplebtnsave);
        btnCompare  = compare.findViewById(R.id.dialog_compareproduct_btnselect);

        rippleCompare.setEnabled(false);
        btnCompare.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));

        categoryList.clear();

        getCompareCategory();

        recyclerCompare.setLayoutManager(new GridLayoutManager(this, 2));

        final BottomDialog dialog = new BottomDialog.Builder(this)
                .setTitle("Pilih Produk Lensa")
                .setCustomView(compare)
                .build();

        adapter_compare_product = new Adapter_compare_product(this, listCompareCategory, new MultiSelectLensProduct() {
            @Override
            public void passResult(ArrayList<Data_compare_category> itemList) {
                categoryList.clear();
                categoryList.addAll(itemList);

                if (itemList.size() == 2)
                {
                    rippleCompare.setEnabled(true);
                    btnCompare.setBackgroundColor(getResources().getColor(R.color.colorFirst));
                }
                else
                {
                    rippleCompare.setEnabled(false);
                    btnCompare.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
                }
            }
        });

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < categoryList.size(); i++)
                {
                    Log.d("Category Lens : ", categoryList.get(i).getTitle());
                }

                getDataLeft(categoryList.get(0).getId());
                getDataRight(categoryList.get(1).getId());

                dialog.dismiss();
            }
        });

        recyclerCompare.setAdapter(adapter_compare_product);

        dialog.show();
    }

    private void getCompareCategory() {
        listCompareCategory.clear();
        StringRequest request = new StringRequest(Request.Method.GET, URLCOMPARECATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Data_compare_category category = new Data_compare_category();
                        category.setId(object.getString("id"));
                        category.setTitle(object.getString("title"));

                        listCompareCategory.add(category);
                    }

                    adapter_compare_product.notifyDataSetChanged();
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

    private void getDataLeft(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLGETPRODUCT, new Response.Listener<String>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Data_compare item = new Data_compare();
                    item.setId(object.getString("id"));
                    item.setTitle(object.getString("title"));
                    item.setSlug(object.getString("slug"));
                    item.setDescription(object.getString("description"));
                    item.setImages(object.getString("images"));
                    item.setType(object.getString("type"));
                    item.setCategory(object.getString("category"));
                    item.setCreateDate(object.getString("create_date"));

                    dataLeft = item;

                    txtProductLeft.setText(object.getString("title"));
//                    txtSubproductLeft.setText(object.getString("category"));

                    Picasso.with(getApplicationContext()).load(object.getString("images")).fit().into(imageLeft);
                    imageLeft.startAnimation(animation);
                    webViewLeft.getSettings().setJavaScriptEnabled(true);
                    webViewLeft.setWebViewClient(new WebViewClient());
                    webViewLeft.loadData(object.getString("description"), "text/html", "UTF-8");
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
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDataRight(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLGETPRODUCT, new Response.Listener<String>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Data_compare item = new Data_compare();
                    item.setId(object.getString("id"));
                    item.setTitle(object.getString("title"));
                    item.setSlug(object.getString("slug"));
                    item.setDescription(object.getString("description"));
                    item.setImages(object.getString("images"));
                    item.setType(object.getString("type"));
                    item.setCategory(object.getString("category"));
                    item.setCreateDate(object.getString("create_date"));

                    dataRight = item;

                    txtProductRight.setText(object.getString("title"));
//                    txtSubproductRight.setText(object.getString("category"));
                    Picasso.with(getApplicationContext()).load(object.getString("images")).fit().into(imageRight);
                    imageRight.startAnimation(animation);
                    webViewRight.getSettings().setJavaScriptEnabled(true);
                    webViewRight.setWebViewClient(new WebViewClient());
                    webViewRight.loadData(object.getString("description"), "text/html", "UTF-8");
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
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}