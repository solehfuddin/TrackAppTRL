package com.sofudev.trackapptrl.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_another_product;
import com.sofudev.trackapptrl.Adapter.Adapter_colordetail;
import com.sofudev.trackapptrl.Adapter.Adapter_more_frame;
import com.sofudev.trackapptrl.Adapter.Adapter_slider_product;
import com.sofudev.trackapptrl.Adapter.Adapter_special_item;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_recent_view;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.RecentViewHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

public class DetailFrameFragment extends Fragment {
    OnBadgeCounter badgeCounter;
    Config config = new Config();

    private String FRAMEPRICEURL = config.Ip_address + config.frame_price_mode;
    String URLDETAILPRODUCT = config.Ip_address + config.frame_showdetail_product;
    String URLDETAILCOLOR   = config.Ip_address + config.frame_showdetail_color;
    String HOTSALEURL       = config.Ip_address + config.dashboard_hot_sale;
    String GETACTIVESALE_URL = config.Ip_address + config.flashsale_getActiveSale;

    ViewPager pagerImage;
    RippleView btnCart;

    UniversalFontTextView txtLenscode, txtDiscPrice, txtRealPrice, txtDisc, txtBrand, txtAvailability, txtDescription;
    RecyclerView recyColor, recyAnotherProduct;
    LinearLayout linear_flashsale, linear_price;
    CountdownView countdown_flashsale;
    WebView webView;
    TextView textView;
    FloatingActionButton fabDownload;


    private static int currentPage = 0, frameId;
    private static int NUM_PAGES = 0;
    String frameName, frameSku, frameImage, framePrice, frameDisc, frameDiscPrice, frameAvailibilty,
            frameBrand, frameColor, frameQty, frameWeight, value, from, ACTIVITY_TAG;
    boolean outputFrameMode;

    View view;
    LinearLayoutManager horizontal_manager;
    List<String> allImg = new ArrayList<>();
    List<Data_color_filter> allColor = new ArrayList<>();
    List<Data_fragment_bestproduct> list_hotsale = new ArrayList<>();
    Adapter_another_product adapter_hotsale_product;
    Adapter_special_item adapter_special_item;
    Adapter_slider_product adapter_slider_product;
    Adapter_colordetail adapter_colordetail;

    //Lokal db
    AddCartHelper addCartHelper;
    RecentViewHelper recentViewHelper;

    String [] permission = new String[] {
            Manifest.permission.READ_MEDIA_IMAGES
    };

    boolean is_images_permitted = false;

    public DetailFrameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_detail_frame, container, false);
    }

    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(getContext()));

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            from = getArguments().getString("from");
            value = getArguments().getString("product_id");
            ACTIVITY_TAG = getArguments().getString("activity");

            Log.d(DetailFrameFragment.class.getSimpleName(), "TAG DETAIL FRAGMENT : " + ACTIVITY_TAG);
        }

        getFrameMode();

        //Toasty.info(getContext(), value, Toast.LENGTH_SHORT).show();

        pagerImage  = view.findViewById(R.id.fragment_detailproduct_imgProduct);
        btnCart     = view.findViewById(R.id.fragment_detailproduct_btnCart);

        txtLenscode = view.findViewById(R.id.fragment_detailproduct_txtLenscode);
        txtDiscPrice= view.findViewById(R.id.fragment_detailproduct_txtDiscPrice);
        txtRealPrice= view.findViewById(R.id.fragment_detailproduct_txtRealPrice);
        txtDisc     = view.findViewById(R.id.fragment_detailproduct_txtDisc);
        txtBrand    = view.findViewById(R.id.fragment_detailproduct_txtBrandName);
        txtAvailability = view.findViewById(R.id.fragment_detailproduct_txtAvailability);
        txtDescription = view.findViewById(R.id.fragment_detailproduct_txtDescription);
        webView     = view.findViewById(R.id.fragment_detailproduct_webview);
        textView    = view.findViewById(R.id.fragment_detailproduct_textview);
        recyColor   = view.findViewById(R.id.fragment_detailproduct_recyclerColor);
        recyAnotherProduct = view.findViewById(R.id.fragment_detailproduct_recyclerAnother);
        linear_flashsale = view.findViewById(R.id.fragment_detailproduct_linearSale);
        linear_price = view.findViewById(R.id.fragment_detailproduct_linearPrice);
        countdown_flashsale = view.findViewById(R.id.fragment_detailproduct_countdown);
        fabDownload = view.findViewById(R.id.fragment_detailproduct_btnDownloadImage);

//        textView.setMovementMethod(LinkMovementMethod.getInstance());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());

        showProductDetail(value);
        showHotsale();
        getDurationSale();

        adapter_slider_product = new Adapter_slider_product(allImg, getContext());
        adapter_colordetail = new Adapter_colordetail(allColor, getContext());
        pagerImage.setAdapter(adapter_slider_product);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            request_permission_images.launch(permission[0]);
            fabDownload.setVisibility(View.VISIBLE);

            fabDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(DetailFrameFragment.class.getSimpleName(), "Download Image");
                    final boolean[] status = new boolean[1];

                    for (int i = 0; i < allImg.size(); i++)
                    {
                        final int finalI = i;
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                   status[0] = getBitmap(allImg.get(finalI));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                        if(status[0])
                        {
                            Toasty.info(getContext(), "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else
        {
            /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            {
                fabDownload.setVisibility(View.VISIBLE);

                fabDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < allImg.size(); i++)
                        {
                            downloadImageNew(String.valueOf(frameId), allImg.get(i));
                        }
                    }
                });
            }
            else
            {
                fabDownload.setVisibility(View.INVISIBLE);
            }*/

            fabDownload.setVisibility(View.VISIBLE);

            fabDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < allImg.size(); i++)
                    {
                        downloadImageNew(String.valueOf(frameId), allImg.get(i));
                    }
                }
            });
        }

        recyColor.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyColor.setAdapter(adapter_colordetail);

        horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyAnotherProduct.setLayoutManager(horizontal_manager);
        recyAnotherProduct.setHasFixedSize(true);

        NUM_PAGES = allImg.size();

        addCartHelper = AddCartHelper.getINSTANCE(getContext());
        recentViewHelper = RecentViewHelper.getINSTANCE(getContext());
        recentViewHelper.open();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }

                pagerImage.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

//        adapter_hotsale_product = new Adapter_another_product(getContext(), list_hotsale,
//                new RecyclerViewOnClickListener() {
//                    @Override
//                    public void onItemClick(View view, int pos, String id) {
////                        Intent intent = new Intent(getContext(), DetailProductActivity.class);
////                        intent.putExtra("id_lensa", list_hotsale.get(pos).getProduct_id());
////                        startActivity(intent);
//
//                        if (from.contentEquals("0"))
//                        {
//                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("from", "0");
//                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
//                            detailFrameFragment.setArguments(bundle);
//
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .remove(detailFrameFragment)
//                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                        else
//                        {
//                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("from", "1");
//                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
//                            detailFrameFragment.setArguments(bundle);
//
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .remove(detailFrameFragment)
//                                    .replace(R.id.detailproduct_fragment_container, detailFrameFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
//                });
//
//        recyAnotherProduct.setAdapter(adapter_hotsale_product);

        adapter_special_item = new Adapter_special_item(getContext(), list_hotsale,
                new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(View view, int pos, String id) {
//                        Intent intent = new Intent(getContext(), DetailProductActivity.class);
//                        intent.putExtra("id_lensa", list_hotsale.get(pos).getProduct_id());
//                        startActivity(intent);

                        if (from.contentEquals("0"))
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "0");
                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
                            bundle.putString("activity", ACTIVITY_TAG);
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .remove(detailFrameFragment)
                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else
                        {
                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "1");
                            bundle.putString("product_id", list_hotsale.get(pos).getProduct_id());
                            bundle.putString("activity", ACTIVITY_TAG);
                            detailFrameFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .remove(detailFrameFragment)
                                    .replace(R.id.detailproduct_fragment_container, detailFrameFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }, ACTIVITY_TAG);

        recyAnotherProduct.setAdapter(adapter_special_item);


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outputFrameMode)
                {
                    addCartHelper.open();

                    ModelAddCart item = new ModelAddCart();
                    item.setProductId(frameId);
                    item.setProductName(frameName);
                    item.setProductCode(frameSku);
                    item.setProductQty(1);
                    item.setProductPrice(Integer.valueOf(removeRupiah(framePrice)));
                    item.setProductDiscPrice(Integer.valueOf(removeRupiah(frameDiscPrice)));
                    item.setProductDisc(Integer.valueOf(removeDiskon(frameDisc)));
                    item.setNewProductPrice(Integer.valueOf(removeRupiah(framePrice)));
                    item.setNewProductDiscPrice(Integer.valueOf(removeRupiah(frameDiscPrice)));
                    item.setProductStock(Integer.valueOf(frameQty));
                    item.setProductWeight(Integer.valueOf(frameWeight));
                    item.setProductImage(frameImage);

                    long status = addCartHelper.insertAddCart(item);

                    if (status > 0)
                    {
                        Toasty.success(view.getContext(), "Item has been added to cart", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(getContext(), AddCartProductActivity.class);
//                    startActivity(intent);
                    }

                    int count = addCartHelper.countAddCart();
                    if (badgeCounter != null)
                    {
                        badgeCounter.countCartlist(count);
                    }
                }
                else
                {
                    Toasty.warning(requireContext(), "Harap hubungi CS untuk informasi lebih lanjut", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toasty.success(getContext(), "Gambar berhasil disimpan", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toasty.error(getContext(), "Gambar gagal disimpan", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean getBitmap(String imageurl) {
        InputStream in = null;
        boolean isSuccess;
        StorageManager storageManager = (StorageManager) getContext().getSystemService(Context.STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0); // internal Storage

        try
        {
            Log.i("URL", imageurl);
            URL url = new URL(imageurl);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();

            in = httpConn.getInputStream();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (imageurl.contains(".jpg"))
            {
                BitmapFactory.decodeStream(in).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }
            else if (imageurl.contains(".webp"))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    BitmapFactory.decodeStream(in).compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, byteArrayOutputStream);
                }
            }
            else
            {
                BitmapFactory.decodeStream(in).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            }
            byte[] bytesArray = byteArrayOutputStream.toByteArray();

            File fileOutput = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                fileOutput = new File(Objects.requireNonNull(storageVolume.getDirectory()).getPath() + "/Download/" + frameId + ".jpeg");
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(fileOutput);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                fileOutputStream.write(bytesArray);
                fileOutputStream.close();
                isSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess = false;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    private ActivityResultLauncher<String> request_permission_images = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        is_images_permitted = true;
                    }
                    else
                    {
                        is_images_permitted = false;
                    }
                }
            });

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

    private void insertRecentSearch(Data_recent_view item)
    {
        recentViewHelper.insertRecentView(item);
    }

    private void getFrameMode() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FRAMEPRICEURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    outputFrameMode = jsonObject.getBoolean("show_price_frame");

                    Log.d("Frame Mode : ", String.valueOf(outputFrameMode));

                    if (outputFrameMode)
                    {
                        linear_price.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        linear_price.setVisibility(View.GONE);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.warning(requireContext(), "Please check your connection", Toast.LENGTH_SHORT, true).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private void showProductDetail(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDETAILPRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        frameId     = jsonObject.getInt("frame_id");
                        frameName   = jsonObject.getString("frame_name");
                        frameSku    = jsonObject.getString("frame_sku");
                        frameImage  = jsonObject.getString("frame_image");
                        framePrice  = jsonObject.getString("frame_price");
                        frameDisc   = jsonObject.getString("frame_disc");
                        frameDiscPrice = jsonObject.getString("frame_disc_price");
                        frameAvailibilty = jsonObject.getString("frame_availibilty");
                        frameBrand  = jsonObject.getString("frame_brand");
                        frameColor  = jsonObject.getString("frame_color");
                        frameQty    = jsonObject.getString("frame_qty");
                        frameWeight = jsonObject.getString("frame_weight");

                        allImg.add(frameImage);
                    }

                    frameDiscPrice = "Rp. " + CurencyFormat(frameDiscPrice);
                    framePrice     = "Rp. " + CurencyFormat(framePrice);
                    int disc = Integer.valueOf(frameDisc);
                    if (disc > 0)
                    {
                        frameDisc = frameDisc + " % ";
                        txtDisc.setVisibility(View.VISIBLE);
                        txtDisc.setText(frameDisc);

                        txtRealPrice.setVisibility(View.VISIBLE);
                        txtRealPrice.setPaintFlags(txtRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        txtRealPrice.setText(framePrice);
                    }
                    else
                    {
                        txtDisc.setVisibility(View.GONE);
                        txtRealPrice.setVisibility(View.GONE);
                    }

                    // String stock = "IN STOCK";
                    int qty = Integer.valueOf(frameQty);
                    if (qty > 0)
                    {
                        txtAvailability.setText("Tersedia");
                        txtAvailability.setTextColor(Color.parseColor("#45ac2d"));

                        btnCart.setEnabled(true);
                        btnCart.setBackgroundColor(Color.parseColor("#3395ff"));
                    }
                    else
                    {
                        txtAvailability.setText("Habis");
                        txtAvailability.setTextColor(Color.parseColor("#f90606"));

                        btnCart.setEnabled(false);
                        btnCart.setBackgroundColor(Color.parseColor("#757575"));
                    }

                    txtLenscode.setText(frameName);
                    txtDiscPrice.setText(frameDiscPrice);

                    txtBrand.setText(frameBrand);
//                    txtDescription.setText(frameSku);
                    txtDescription.setVisibility(View.GONE);

                    webView.loadUrl("https://timurrayalab.com/adminproduct/description_product/long_description/" + frameId);
//                    webView.loadData(frameSku, "text/html; charset=utf-8", "UTF-8");
//                    webView.setVisibility(View.GONE);

                    textView.setVisibility(View.GONE);
                    /*HtmlText.from(frameSku).setImageLoader(new HtmlImageLoader() {
                        @Override
                        public void loadImage(String url, final Callback callback) {
                            Picasso.with(getContext()).load(url).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    callback.onLoadComplete(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    callback.onLoadFailed();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                        }

                        @Override
                        public Drawable getDefaultDrawable() {
                            return null;
                        }

                        @Override
                        public Drawable getErrorDrawable() {
                            return null;
                        }

                        @Override
                        public int getMaxWidth() {
                            return textView.getMaxWidth();
                        }

                        @Override
                        public boolean fitWidth() {
                            return false;
                        }
                    }).into(textView);*/

                    Data_recent_view dataRecentView = new Data_recent_view();
                    dataRecentView.setId(frameId);
                    dataRecentView.setImage(frameImage);

                    insertRecentSearch(dataRecentView);

//                    if (frameColor.contains("[") && frameColor.contains("]"))
//                    {
//                        frameColor.replace("[", "").replace("]", "");
//                    }

//                    Toasty.info(getApplicationContext(), "Color : " + frameColor, Toast.LENGTH_SHORT).show();

                    showProductColor(frameColor);

                    adapter_slider_product.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_lensa", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showProductColor(final String color)
    {
        allColor.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDETAILCOLOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0;i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("color_id");
                        String colorName = jsonObject.getString("color_name");
                        String colorCode = jsonObject.getString("color_code");

                        Data_color_filter item = new Data_color_filter();
                        item.setColorId(id);
                        item.setColorName(colorName);
                        item.setColorCode(colorCode);

                        allColor.add(item);
                    }

                    adapter_colordetail.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

                Data_color_filter item = new Data_color_filter();
                item.setColorId(0);
                item.setColorName("Black");
                item.setColorCode("#000000");

                allColor.add(item);

                adapter_colordetail.notifyDataSetChanged();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("colorType", color);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showHotsale()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOTSALEURL, new Response.Listener<String>() {
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
                        String brandName    = jsonObject.getString("frame_brand");
                        String frameQty     = jsonObject.getString("frame_qty");
                        String frameWeight  = jsonObject.getString("frame_weight");
//                        String totalFrame   = jsonObject.getString("total_output");

                        Data_fragment_bestproduct item = new Data_fragment_bestproduct();
                        item.setProduct_id(frameId);
                        item.setProduct_name(frameName);
                        item.setProduct_image(frameImage);
                        item.setProduct_brand(brandName);
                        item.setProduct_realprice("Rp " + framePrice);
                        item.setProduct_discpercent(frameDisc);
                        item.setProduct_qty(frameQty);
                        item.setProduct_weight(frameWeight);

                        list_hotsale.add(item);
                    }

//                    adapter_hotsale_product.notifyDataSetChanged();
                    adapter_special_item.notifyDataSetChanged();
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

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "").replace(".","").trim();

        return output;
    }

    private String removeDiskon(String diskon)
    {
        String output;
        output = diskon.replace("%", "").trim();

        return output;
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

                        linear_flashsale.setVisibility(View.VISIBLE);

                        countdown_flashsale.start(durr);
                        countdown_flashsale.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                linear_flashsale.setVisibility(View.GONE);

                                Fragment frg = null;
                                frg = getFragmentManager().findFragmentByTag("detail");
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();

                                recyColor.setLayoutManager(new GridLayoutManager(getContext(), 3));

                                horizontal_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                recyAnotherProduct.setLayoutManager(horizontal_manager);
                                recyAnotherProduct.setHasFixedSize(true);

                                showProductDetail(value);
                                showHotsale();
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
}
