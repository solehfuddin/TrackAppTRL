package com.sofudev.trackapptrl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.ContactsContract;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;

import com.andexert.library.RippleView;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_banner_custom;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.LoginSession;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Custom.OnFragmentInteractionListener;
import com.sofudev.trackapptrl.Custom.SmsReceiver;
import com.sofudev.trackapptrl.Decoration.MyBanner;
import com.sofudev.trackapptrl.Form.AddCartProductActivity;
import com.sofudev.trackapptrl.Form.CheckBalanceActivity;
import com.sofudev.trackapptrl.Form.DetailDepositActivity;
import com.sofudev.trackapptrl.Form.EstatementActivity;
import com.sofudev.trackapptrl.Form.EwarrantyActivity;
import com.sofudev.trackapptrl.Form.FormBatchOrderActivity;
import com.sofudev.trackapptrl.Form.FormDeliveryTrackingActivity;
import com.sofudev.trackapptrl.Form.FormFilterOpticnameActivity;
import com.sofudev.trackapptrl.Form.FormOrderDashboardActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryFrameActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryPartaiActivity;
import com.sofudev.trackapptrl.Form.FormOrderLensActivity;
import com.sofudev.trackapptrl.Form.FormOrderSpActivity;
import com.sofudev.trackapptrl.Form.FormPDFViewerActivity;
import com.sofudev.trackapptrl.Form.FormProfileActivity;
import com.sofudev.trackapptrl.Form.FormTrackOrderActivity;
import com.sofudev.trackapptrl.Form.FormTrackingSpActivity;
import com.sofudev.trackapptrl.Form.FormUACActivity;
import com.sofudev.trackapptrl.Form.SearchProductActivity;
import com.sofudev.trackapptrl.Form.WishlistProductActivity;
import com.sofudev.trackapptrl.Fragment.BalanceFragment;
import com.sofudev.trackapptrl.Fragment.BannerFragment;
import com.sofudev.trackapptrl.Fragment.CategoryFragment;
import com.sofudev.trackapptrl.Fragment.CustomHomeFragment;
import com.sofudev.trackapptrl.Fragment.HomeFragment;
import com.sofudev.trackapptrl.Fragment.MoreFrameFragment;
import com.sofudev.trackapptrl.Fragment.NewFrameFragment;
import com.sofudev.trackapptrl.Fragment.OtherProductFragment;
import com.sofudev.trackapptrl.Fragment.PromoBannerFragment;
import com.sofudev.trackapptrl.Fragment.PromoFrameFragment;
import com.sofudev.trackapptrl.Fragment.SpecialProductFragment;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.sofudev.trackapptrl.Util.CustomTypefaceSpan;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.weiwangcn.betterspinner.library.BetterSpinner;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnBadgeCounter, OnFragmentInteractionListener {
    //PermissionSettings permission;
    Config config = new Config();
    private String URLDELIVERYCOUNTER = config.Ip_address + config.deliverytrack_counter;

    private String userDetail_URL = config.Ip_address + config.dashboard_user_detail;
    private String logout_URL     = config.Ip_address + config.dashboard_update_offline;
    private String check_userinfo = config.Ip_address + config.dashboard_check_userinfo;
    private String update_phone   = config.Ip_address + config.dashboard_update_phone;
    private String update_email   = config.Ip_address + config.dashboard_update_email;
    private String verify_mail    = config.Ip_address + config.dashboard_verify_mail;
    private String verify_phone   = config.Ip_address + config.dashboard_verify_phone;
    private String upload_txtfile = config.Ip_address + config.dashboard_upload_txtphone;
    private String view_pdf_filter= config.Ip_address + config.view_pdf_showAllDataByFilter;
    private String URLCHECKDEPOSIT= config.Ip_address + config.depo_getsaldo;
    private String URLGETSICCODE  = config.Ip_address + config.get_sic_code;
    private String URLSETSICCODE  = config.Ip_address + config.set_sic_code;
    private String URLVERIFYSICCODE = config.Ip_address + config.verify_siccode;
    private String URLSTATUSUSER  = config.Ip_address + config.status_user;
    private String URLPARENTINFO  = config.Ip_address + config.getparent_info;
    //    private String URLTOKEN       = config.Ip_address + config.payment_update_token;
    String URL_CHECKCITY = config.Ip_address + config.spinner_shipment_getProvinceOptic;
    String URL_GETALLPROVINCE= config.Ip_address + config.spinner_shipment_getAllProvince;
    String URL_GETCITYBYPROV = config.Ip_address + config.spinner_shipment_getCity;
    String URL_UPDATECITY    = config.Ip_address + config.spinner_shipment_updateCity;

    ImageView imgWishlist, imgCart;
    ImageView imgAction;
    TextView txt_title, txt_countwishlist, txt_countCart, txt_saldo;
    //UniversalFontTextView btn_orderframe, btn_orderbulk;
    ACProgressCustom loading;
    BottomDialog bottomDialogInput;
    private DrawerLayout drawer;
    private MaterialSearchView searchViews;
    private LovelyStandardDialog dialog;
    private MCrypt mCrypt;
    private String data, data1, level_user, image_user, email_address, mobile_number, verify_code, phone_number,
            phone_pincode, province_user, province_address, username, city, member_flag, urlPdf, sts;
    private NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private Menu menu;

    ArrayList<String> data_province = new ArrayList<>();
    ArrayList<String> data_city     = new ArrayList<>();

    CardView cardHide1, cardHide2, cardHide3, cardHide4, cardHide5, cardHide6;
    CardView cardShow1, cardShow2, cardShow3, cardShow4, cardShow5, cardShow6;
    LinearLayout linKode1, linKode2, linKode3, linKode4, linKode5, linKode6;
    UniversalFontTextView txtKode1, txtKode2, txtKode3, txtKode4, txtKode5, txtKode6;
    RippleView pinBtn1, pinBtn2, pinBtn3, pinBtn4, pinBtn5, pinBtn6, pinBtn7, pinBtn8, pinBtn9, pinBtn0;
    List<Integer> pinnumber = new ArrayList<>();

    CardView cardTop;
    ImageView imgTop;
    NestedScrollView scrollView;
    BootstrapButton btn_call, btn_wa, btn_mail, btn_web;
    Button btn_profile, btn_topup;
    public BootstrapCircleThumbnail img_profile;
    TextView navdash_username, navdash_id;
    FabSpeedDial fab_about;
    Date dt_time;
    SimpleDateFormat sdf;
    String nominal, sActive, sPast, sic_code;
    Boolean isHavingChild = false;
    Boolean isHide = true;
    int sTotal;
    int smsFlag = 0;
    int customerId = 0;
    private int oldScrollYPostion = 0;
    LoginSession session;

    WishlistHelper wishlistHelper;
    AddCartHelper addCartHelper;

//    private Adapter_banner_custom mAdapter;
//
//    private MyBanner bannerSlider;
//    private WormDotsIndicator myDots;
//    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_dashboard);
        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

      /*  permission = new PermissionSettings();
        permission.checkPermission(DashboardActivity.this, Manifest.permission.READ_SMS, 0);
        permission.checkPermission(DashboardActivity.this, Manifest.permission.RECEIVE_SMS, 0);*/

        imgWishlist = findViewById(R.id.appbardashboard_btn_wishlist);
        imgCart = findViewById(R.id.appbardashboard_btn_addcart);
        txt_title = (TextView) findViewById(R.id.appbardashboard_txt_titleheader);
        txt_countwishlist = findViewById(R.id.appbardashboard_badge_wishlist);
        txt_countCart = findViewById(R.id.appbardashboard_badge_cart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchViews = (MaterialSearchView) findViewById(R.id.dashboard_search);
        cardTop = findViewById(R.id.appbardashboard_cartTop);
        imgTop = findViewById(R.id.appbardashboard_buttonTop);
        scrollView = findViewById(R.id.dashboard_scrollview);
        session = new LoginSession(getApplicationContext());
//        bannerSlider = findViewById(R.id.layout_custom_mybanner);
//        myDots       = findViewById(R.id.layout_custom_mydots);
//        mLinearLayout = findViewById(R.id.layout_custom_pagesContainer);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() > oldScrollYPostion)
                {
                    cardTop.setVisibility(View.VISIBLE);
                }
                else if (scrollView.getScrollY() < oldScrollYPostion || scrollView.getScrollY() <= oldScrollYPostion)
                {
                    cardTop.setVisibility(View.INVISIBLE);
                }
                oldScrollYPostion = scrollView.getScrollY();
            }
        });
        imgTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0, 0, 2500);
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset != 0)
                {
                    invalidateOptionsMenu();
                    getSaldoDepo(username);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        UniversalFontComponents.init(this);

        View header = navigationView.getHeaderView(0);
        navdash_username = (TextView) header.findViewById(R.id.navdash_username);
        navdash_id       = (TextView) header.findViewById(R.id.navdash_id);
        txt_saldo = header.findViewById(R.id.navdash_txtSisaSaldo);
        btn_topup = header.findViewById(R.id.navdash_btntopup);
        btn_profile = (Button) header.findViewById(R.id.navdash_btnprofile);
        img_profile = (BootstrapCircleThumbnail) header.findViewById(R.id.navdash_imageProfile);
        //btn_orderframe = (UniversalFontTextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_orderframe));
        //btn_orderbulk  = (UniversalFontTextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_orderbulk));

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateProfile();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateProfile();
            }
        });

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DetailDepositActivity.class);
                intent.putExtra("nominal", nominal);
                intent.putExtra("user_info", data1);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        setCustomFontHeader();
        //initializeComingsoon(btn_orderframe);
        //initializeComingsoon(btn_orderbulk);
        getUserdata();
        checkUserInfo(data1);
        navdash_username.setText(data);
        navdash_id.setText(data1);
        getUserDetailDB(data1);
        getParentInfo(navdash_id.getText().toString());

//        homeProduk();
        isHomeActive();
        showBanner();
        showBalance();
        showCategory();
        showPromoFrame();
        showSpesialProduk();
        showAnotherProduk();
        showMoreFrame();
        showPromoBanner();
        frameProduk();
//        setupSlider();

        dt_time = new Date();

        imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, WishlistProductActivity.class);
                //intent.putExtra("activity", "main");
                startActivityForResult(intent, 1);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusUser(username, 2);
            }
        });

        wishlistHelper = WishlistHelper.getInstance(getApplicationContext());
        wishlistHelper.open();

        addCartHelper = AddCartHelper.getINSTANCE(getApplicationContext());
        addCartHelper.open();

        int counter = wishlistHelper.countWishlist();
        txt_countwishlist.setText(" " + counter + " ");

        int countCart = addCartHelper.countAddCart();
        txt_countCart.setText(" " + countCart + " ");

//        String token = FirebaseInstanceId.getInstance().getToken();
        //Toasty.info(getApplicationContext(), token, Toast.LENGTH_LONG).show();
//        updateToken(token);
    }

//    private void setupSlider() {
//        bannerSlider.setDurationScroll(800);
//        List<Fragment> fragments = new ArrayList<>();
//
//        //link image
//        fragments.add(CustomHomeFragment.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_17b56894-2608-4509-a4f4-ad86aa5d3b62.jpg"));
//        fragments.add(CustomHomeFragment.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_7da28e4c-a14f-4c10-8fec-845578f7d748.jpg"));
//        fragments.add(CustomHomeFragment.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/18/85531617/85531617_87a351f9-b040-4d90-99f4-3f3e846cd7ef.jpg"));
//        fragments.add(CustomHomeFragment.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/20/85531617/85531617_03e76141-3faf-45b3-8bcd-fc0962836db3.jpg"));
//
//        mAdapter = new Adapter_banner_custom(getSupportFragmentManager(), fragments);
//        bannerSlider.setAdapter(mAdapter);
//        myDots.setViewPager(bannerSlider);
//    }

    @Override
    protected void onResume() {
        super.onResume();

        int counter = wishlistHelper.countWishlist();
        txt_countwishlist.setText(" " + counter + " ");

        int countCart = addCartHelper.countAddCart();
        txt_countCart.setText(" " + countCart + " ");

        getSaldoDepo(username);
    }

    private void countData(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLDELIVERYCOUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("Output", response);

                    sActive = "Active ("+ jsonObject.getInt("countactive") +")";
                    sPast = "Past (" + jsonObject.getInt("countpast") +")";
                    sTotal = jsonObject.getInt("countactive") + jsonObject.getInt("countpast");

                    Log.d("Active", sActive);
                    Log.d("Past", sPast);
                    Log.d("Total", String.valueOf(sTotal));
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
                map.put("optic", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

//    private void updateToken(final String token)
//    {
//        StringRequest request = new StringRequest(Request.Method.POST, URLTOKEN, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//
//                    if (object.names().get(0).equals("success"))
//                    {
//                        Toasty.success(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toasty.warning(getApplicationContext(), "Failed update token", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("idparty", data1);
//                hashMap.put("token", token);
//                return hashMap;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(request);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            int counter = data.getIntExtra("counter", 0);
            txt_countwishlist.setText(" " + counter + " ");
        }
        else if (requestCode == 2)
        {
            int counter = data.getIntExtra("counter", 0);
            txt_countCart.setText(" " + counter + " ");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dialog = new LovelyStandardDialog(this);
        dialog.setMessage("Are you sure want to close this app ?");
        dialog.setTopTitle("Message Dialog");
        dialog.setTopTitleColor(Color.WHITE);
        dialog.setTopColorRes(R.color.bootstrap_brand_success);
        dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_primary);
        dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);

        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
                updateLogoutInfo(data1);
            }
        });
        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        MenuItem item = menu.findItem(R.id.action_searchs);

        //searchViews.setMenuItem(item);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(DashboardActivity.this, SearchProductActivity.class);
                startActivity(intent);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            isHomeActive();
//            homeProduk();
//            showBanner();
//            showBalance();
//            showCategory();
//            showSpesialProduk();
        }
        else if (id == R.id.nav_order)
        {
            try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_ordertrack).isVisible();
                //menu.findItem(R.id.nav_orderdashboard).setVisible(b);
                menu.findItem(R.id.nav_orderhistoryFrame).setVisible(b);
                menu.findItem(R.id.nav_orderhistory).setVisible(b);
                menu.findItem(R.id.nav_ordertrack).setVisible(b);
                menu.findItem(R.id.nav_deliverytrack).setVisible(b);
                menu.findItem(R.id.nav_orderlens).setVisible(b);
                menu.findItem(R.id.nav_orderbulk).setVisible(b);
                menu.findItem(R.id.nav_orderhistorybulk).setVisible(b);
                menu.findItem(R.id.nav_orderCheckBalance).setVisible(b);

                if (level_user != null)
                {
                    if (Integer.parseInt(level_user) == 1)
                    {
                        menu.findItem(R.id.nav_orderOnHand).setVisible(b);
                        menu.findItem(R.id.nav_orderSp).setVisible(b);
                        menu.findItem(R.id.nav_trackingSp).setVisible(b);
                    }
                    else
                    {
                        menu.findItem(R.id.nav_trackingSp).setVisible(b);
                    }
                }
//                else
//                {
//                    menu.findItem(R.id.nav_ordersp).setVisible(false);
//                }
                //menu.findItem(R.id.nav_orderhistory).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        else if (id == R.id.nav_orderhistory)
        {
            if (level_user != null) {
                if (Integer.parseInt(level_user) == 0) {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryActivity.class);
                        intent.putExtra("sales", username);
                        intent.putExtra("level", "1");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryActivity.class);
                        intent.putExtra("idparty", navdash_id.getText().toString());
                        intent.putExtra("user_info", username);
                        intent.putExtra("level", level_user);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FormOrderHistoryActivity.class);
                    intent.putExtra("sales", username);
                    intent.putExtra("level", level_user);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), FormOrderHistoryActivity.class);
                intent.putExtra("idparty", navdash_id.getText().toString());
                intent.putExtra("user_info", username);
                intent.putExtra("level", level_user);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_orderhistoryFrame)
        {
            if (level_user != null)
            {
                if (Integer.parseInt(level_user) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryFrameActivity.class);
                        intent.putExtra("sales", username);
                        intent.putExtra("level", "1");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryFrameActivity.class);
                        intent.putExtra("user_info", navdash_id.getText().toString());
                        intent.putExtra("username", username);
                        intent.putExtra("level", level_user);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FormOrderHistoryFrameActivity.class);
                    intent.putExtra("sales", username);
                    intent.putExtra("level", level_user);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), FormOrderHistoryFrameActivity.class);
                intent.putExtra("user_info", navdash_id.getText().toString());
                intent.putExtra("username", username);
                intent.putExtra("level", level_user);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_orderCheckBalance)
        {
            Intent intent = new Intent(getApplicationContext(), CheckBalanceActivity.class);
            intent.putExtra("user_info", navdash_id.getText().toString());
            intent.putExtra("username", username);
            startActivity(intent);
        }
        //Submenu Options click
        else if (id == R.id.nav_orderdashboard)
        {
            Intent intent = new Intent(getApplicationContext(), FormOrderDashboardActivity.class);
            intent.putExtra("idparty", navdash_id.getText().toString());
            startActivity(intent);
        }
        else if (id == R.id.nav_ordertrack)
        {
            if (level_user != null)
            {
                if (Integer.parseInt(level_user) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "OTRACK");
                        intent.putExtra("sales", username);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), FormTrackOrderActivity.class);
                        intent.putExtra("idparty", navdash_id.getText().toString());
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "OTRACK");
                    intent.putExtra("sales", username);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), FormTrackOrderActivity.class);
                intent.putExtra("idparty", navdash_id.getText().toString());
                startActivity(intent);
            }

            drawer.closeDrawers();
            return true;
        }
        else if (id == R.id.nav_deliverytrack)
        {
            if (level_user != null)
            {
                if (Integer.parseInt(level_user) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "DELIVTRACK");
                        intent.putExtra("sales", username);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), FormDeliveryTrackingActivity.class);
                        intent.putExtra("username", navdash_username.getText().toString());
                        intent.putExtra("activetitle", sActive);
                        intent.putExtra("pasttitle", sPast);
                        intent.putExtra("totalprocess", String.valueOf(sTotal));
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "DELIVTRACK");
                    intent.putExtra("sales", username);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), FormDeliveryTrackingActivity.class);
                intent.putExtra("username", navdash_username.getText().toString());
                intent.putExtra("activetitle", sActive);
                intent.putExtra("pasttitle", sPast);
                intent.putExtra("totalprocess", String.valueOf(sTotal));
                startActivity(intent);
            }

            return true;
        }

        else if (id == R.id.nav_orderlens)
        {
            statusUser(username, 0);
        }

        else if (id == R.id.nav_orderOnHand)
        {
            Intent intent = new Intent(getApplicationContext(), OnHandActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_orderSp)
        {
            Intent intent = new Intent(getApplicationContext(), FormOrderSpActivity.class);
            intent.putExtra("idparty", navdash_id.getText().toString());
            intent.putExtra("username", username);
            startActivity(intent);

//                Intent intent = new Intent(getApplicationContext(), FormSpFrameActivity.class);
//                startActivity(intent);
        }

        else if (id == R.id.nav_trackingSp)
        {
            if (level_user != null) {
                if (Integer.parseInt(level_user) == 1) {
                    Intent intent = new Intent(getApplicationContext(), FormTrackingSpActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("level", 1);

                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FormTrackingSpActivity.class);
                    intent.putExtra("username", navdash_username.getText().toString());
                    intent.putExtra("level", 0);

                    startActivity(intent);
                }
            }
        }

        else if (id == R.id.nav_orderbulk)
        {
            statusUser(username, 1);
        }

        else if (id == R.id.nav_orderhistorybulk)
        {
            if (level_user != null)
            {
                if (level_user.equals("0"))
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryPartaiActivity.class);
                        intent.putExtra("sales", username);
                        intent.putExtra("level", "1");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderHistoryPartaiActivity.class);
                        intent.putExtra("user_info", navdash_id.getText().toString());
                        intent.putExtra("username", username);
                        intent.putExtra("level", level_user);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FormOrderHistoryPartaiActivity.class);
                    intent.putExtra("sales", username);
                    intent.putExtra("level", level_user);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), FormOrderHistoryPartaiActivity.class);
                intent.putExtra("user_info", navdash_id.getText().toString());
                intent.putExtra("username", username);
                intent.putExtra("level", level_user);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_options)
        {
            try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_optionsuser).isVisible();
                menu.findItem(R.id.nav_optionsuser).setVisible(b);
                menu.findItem(R.id.nav_optionsreport).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        //Submenu Options click
        else if (id == R.id.nav_optionsuser) {
            Intent intent = new Intent(getApplicationContext(), FormUACActivity.class);
            startActivity(intent);

            drawer.closeDrawers();
            return true;
        }
        else if (id == R.id.nav_optionsreport) {
            Intent intent = new Intent(getApplicationContext(), ReportSalesActivity.class);
            startActivity(intent);

            drawer.closeDrawers();
            return true;
        }

        else if (id == R.id.nav_logout)
        {
            logoutApp();
        }
        else if (id == R.id.nav_pricelist)
        {
            try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_price1).isVisible();
                menu.findItem(R.id.nav_price1).setVisible(b);
                menu.findItem(R.id.nav_price2).setVisible(b);
                menu.findItem(R.id.nav_price3).setVisible(b);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return true;
        }
        else if (id == R.id.nav_price1){
            viewPdfFilter("link_leinz", "Price List Leinz");
        }
        else if (id == R.id.nav_price2){
            viewPdfFilter("link_oriental", "Price List Oriental");
        }
        else if (id == R.id.nav_price3){
            viewPdfFilter("link_nikon", "Price List Nikon");
        }
        else if (id == R.id.nav_lenses) {
            /*
            try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_lens1).isVisible();
                menu.findItem(R.id.nav_lens1).setVisible(b);
                menu.findItem(R.id.nav_lens2).setVisible(b);
                menu.findItem(R.id.nav_lens3).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        }
        /*else if (id == R.id.nav_lens1) {
            Toast.makeText(getApplicationContext(), "Nav Lens 1 has clicked", Toast.LENGTH_SHORT).show();
        }*/
        else if (id == R.id.nav_frames) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_frame1).isVisible();
                menu.findItem(R.id.nav_frame1).setVisible(b);
                menu.findItem(R.id.nav_frame2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            isFrameActive();

            //return true;
        }
        else if (id == R.id.nav_instruments) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_instrument1).isVisible();
                menu.findItem(R.id.nav_instrument1).setVisible(b);
                menu.findItem(R.id.nav_instrument2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        }
        else if (id == R.id.nav_opthalmic) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_opthalmic1).isVisible();
                menu.findItem(R.id.nav_opthalmic1).setVisible(b);
                menu.findItem(R.id.nav_opthalmic2).setVisible(b);
                menu.findItem(R.id.nav_opthalmic3).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        }
        else if (id == R.id.nav_contactlense) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_contactlens1).isVisible();
                menu.findItem(R.id.nav_contactlens1).setVisible(b);
                menu.findItem(R.id.nav_contactlens2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        }
        else if (id == R.id.nav_custcare) {
            Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
            intent.putExtra("data", "http://www.timurrayalab.com/custommer_care");
            startActivity(intent);

            return true;
        }
        else if (id == R.id.nav_ewarranty) {
            Intent intent = new Intent(getApplicationContext(), EwarrantyActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.nav_estatement) {
            if (level_user != null)
            {
                if (Integer.parseInt(level_user) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "ESTMENT");
                        intent.putExtra("sales", username);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                    }
                    else
                    {
                        String prefix = navdash_username.getText().toString().substring(navdash_username.length() - 7);
                        Log.d("Prefix : ", prefix);

                        if (prefix.equals("(STAFF)"))
                        {
                            showAccessDenied();
                        }
                        else
                        {
                            getSicCode(username);
                        }
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "ESTMENT");
                    intent.putExtra("sales", username);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    startActivity(intent);
                }
            }
            else
            {
                String prefix = navdash_username.getText().toString().substring(navdash_username.length() - 7);
                Log.d("Prefix : ", prefix);

                if (prefix.equals("(STAFF)"))
                {
                    showAccessDenied();
                }
                else
                {
                    getSicCode(username);
//                    Intent intent = new Intent(getApplicationContext(), EstatementActivity.class);
//                    intent.putExtra("username", username);
//                    intent.putExtra("opticname", navdash_username.getText().toString());
//                    startActivity(intent);
                }
            }

            return true;
        }
        else if (id == R.id.nav_about) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.form_about);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

            fab_about = (FabSpeedDial) dialog.findViewById(R.id.about_fab);
            fab_about.setMenuListener(new SimpleMenuListenerAdapter(){
                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.about_fb){
                        Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                        intent.putExtra("data", "https://www.facebook.com/");
                        startActivity(intent);
                    }
                    else if (menuItem.getItemId() == R.id.about_tw) {
                        Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                        intent.putExtra("data", "https://twitter.com/");
                        startActivity(intent);
                    }
                    else if (menuItem.getItemId() == R.id.about_ig) {
                        Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                        intent.putExtra("data", "https://www.instagram.com/trlinfo");
                        startActivity(intent);
                    }
                    return super.onMenuItemSelected(menuItem);
                }
            });

        }
        else if (id == R.id.nav_contactus) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.form_contact);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

            btn_call = (BootstrapButton) dialog.findViewById(R.id.contact_btn_call);
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent phone = new Intent(Intent.ACTION_CALL);
                    phone.setData(Uri.parse("tel:0214610154"));
                    if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(phone);*/
                }
            });

            btn_wa = (BootstrapButton) dialog.findViewById(R.id.contact_btn_chat);
            btn_wa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chat = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                            ContactsContract.Contacts.CONTENT_URI);
                    chat.setData(Uri.parse("tel:+628121145555"));
                    chat.putExtra(ContactsContract.Intents.Insert.PHONE, "CS TRL");
                    startActivity(chat);
                }
            });

            btn_mail = (BootstrapButton) dialog.findViewById(R.id.contact_btn_mail);
            btn_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    String to [] = {"cslab@trl.co.id"};
                    email.putExtra(Intent.EXTRA_EMAIL, to);
                    email.setType("plain/text");
                    startActivity(email);
                }
            });

            btn_web = (BootstrapButton) dialog.findViewById(R.id.contact_btn_web);
            btn_web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                    intent.putExtra("data", "http://timurrayalestari.com");
                    startActivity(intent);
                }
            });
        }else if (id == R.id.nav_guide)
        {
            try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_guide1).isVisible();
                menu.findItem(R.id.nav_guide1).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        } else if (id == R.id.nav_guide1)
        {
            viewPdfFilter("link_guide", "User Guide");
        }

        drawer.closeDrawers();
        return true;
    }

    /*private void initializeComingsoon(UniversalFontTextView button)
    {
        float density = getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        params.setMargins(0, (int) (20  * density), 0, (int) (20  * density));

        button.setLayoutParams(params);
        button.setTypeface(null, Typeface.BOLD_ITALIC);
        button.setText("soon");
        button.setTextColor(getResources().getColor(R.color.colorToolbar));
        button.setPadding(5, 5, 5, 5);
        //setMarginView(button, 0, 20, 0, 20);
        button.setTextSize(15);
        button.setGravity(Gravity.TOP | Gravity.LEFT);
    }*/

    private void setMarginView(View view, int left, int top, int right, int bottom)
    {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void setCustomFontHeader()
    {
        Typeface customFont = Typeface.createFromAsset(getAssets(), "DomotikaRegular.ttf");
        txt_title.setTypeface(customFont);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "DomotikaRegular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(DashboardActivity.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        if (!isFinishing()){
            loading.show();
        }
    }

    private void hideLoading()
    {
        loading.dismiss();
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(DashboardActivity.this);
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

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateLogoutInfo(data1);
            }
        });

        dialog.show();
    }

    private void logoutApp() {
        dialog = new LovelyStandardDialog(this);
        dialog.setMessage("Are you sure want to close this app ?");
        dialog.setTopTitle("Message Dialog");
        dialog.setTopTitleColor(Color.WHITE);
        dialog.setTopColorRes(R.color.colorPrimary);
        dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_primary);
        dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);

        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    session.RemoveLoginSession();
                }else
                {
                    updateLogoutInfo(data1);
                }
            }
        });
        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getUserdata() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            data    = bundle.getString("username");
            data1   = bundle.getString("idparty");
        }
    }

    public void createTemp(String msg)
    {
        FileOutputStream fileOutputStream;
        File rootFolder, childFolder;

        try {
            rootFolder = new File (Environment.getExternalStorageDirectory(), "TRLAPP");
            childFolder= new File (Environment.getExternalStorageDirectory(), "TRLAPP/Temp");

            if (!rootFolder.exists())
            {
                rootFolder.mkdirs();
            }

            if (!childFolder.exists())
            {
                childFolder.mkdirs();
            }

            fileOutputStream = new FileOutputStream(childFolder + "/" + "index.txt");
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void updateLogoutInfo(final String id_party)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        final String datetime = sdf.format(calendar.getTime());
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, logout_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCrypt = new MCrypt();
                String info1 = "success";
                String info2 = "invalid";
                String info3 = "failure";

                try {
                    String success = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String invalid = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String failure = MCrypt.bytesToHex(mCrypt.encrypt(info3));

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.names().get(0).equals(success))
                        {
                            String msg = "0";
                            createTemp(msg);
                            hideLoading();
                            finish();
                        }
                        else if (jsonObject.names().get(0).equals(invalid))
                        {
                            hideLoading();
                            Toasty.warning(getApplicationContext(), "Update data failed", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(failure))
                        {
                            hideLoading();
                            Toasty.error(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
//                information("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
//                        DefaultBootstrapBrand.WARNING);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id_party);
                hashMap.put("is_offline", "0");
                hashMap.put("dt_tm", datetime);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void getUserDetailDB(final String key) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, userDetail_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCrypt = new MCrypt();
                String dt = "level";
                String dt1 = "image";
                String dt2 = "province";
                String dt3 = "username";
                String dt4 = "city";
                String dt5 = "memberFlag";
                String dt6 = "address1";

                try {
                    String status = MCrypt.bytesToHex(mCrypt.encrypt(dt));
                    String image  = MCrypt.bytesToHex(mCrypt.encrypt(dt1));
                    String province = MCrypt.bytesToHex(mCrypt.encrypt(dt2));
                    String userInfo = MCrypt.bytesToHex(mCrypt.encrypt(dt3));
                    String cityInfo = MCrypt.bytesToHex(mCrypt.encrypt(dt4));
                    String memberFlag = MCrypt.bytesToHex(mCrypt.encrypt(dt5));
                    String address  = MCrypt.bytesToHex(mCrypt.encrypt(dt6));

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        level_user = new String(mCrypt.decrypt(jsonObject.getString(status)));
                        image_user = new String(mCrypt.decrypt(jsonObject.getString(image)));
                        province_user = new String(mCrypt.decrypt(jsonObject.getString(province)));
                        province_address = new String(mCrypt.decrypt(jsonObject.getString(address)));
                        username   = new String(mCrypt.decrypt(jsonObject.getString(userInfo)));
                        city = new String(mCrypt.decrypt(jsonObject.getString(cityInfo)));
                        member_flag = new String(mCrypt.decrypt(jsonObject.getString(memberFlag)));

                        getSaldoDepo(username);

                        if (Integer.parseInt(level_user) == 0) {
                            hideMenu();
                            countData(navdash_username.getText().toString());
                        }

                        image_user = image_user.replaceAll(" ", "%20");
                        Log.d("IMG PROFILE", image_user);
                        Picasso.with(DashboardActivity.this).load(image_user).into(img_profile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.warning(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", key);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void checkUserInfo(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_userinfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String email    = "email_address";
                String mob_num  = "mobile_number";

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    email_address = jsonObject.getString(email);
                    mobile_number = jsonObject.getString(mob_num).trim();

                    //Toasty.warning(getApplicationContext(), email_address + mobile_number, Toast.LENGTH_SHORT, true).show();

                    //Toasty.info(getApplicationContext(), mobile_number, Toast.LENGTH_LONG, true).show();
                    if (mobile_number.equalsIgnoreCase("-") || mobile_number.isEmpty())
                    {
                        //showUpdatephone();
                        showUpdateMail();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updatePhoneNumber(final String id, final String phone)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_phone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String success  = "success";
                String error    = "error";

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals(success))
                    {
                        Toasty.success(getApplicationContext(), jsonObject.getString(success), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals(error))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString(error), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("mobile_number", phone);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateEmailAddress(final String id, final String email)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String success  = "success";
                String error    = "error";
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals(success))
                    {
                        Toasty.success(getApplicationContext(), jsonObject.getString(success), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals(error))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString(error), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("email_address", email);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showUpdatephone()
    {
        final BootstrapEditText txt_phonenumber;
        BootstrapButton btn_save;

        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dashboard_updatephone);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_phonenumber = (BootstrapEditText) dialog.findViewById(R.id.form_dashboard_updatephone_txphone);
        btn_save        = (BootstrapButton) dialog.findViewById(R.id.form_dashboard_updatephone_btnsave);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = txt_phonenumber.getText().toString();
                if (phone.isEmpty())
                {
                    Toasty.error(getApplicationContext(), "Please insert phone number!", Toast.LENGTH_SHORT, true).show();
                }
                else
                {
                    if (isValidPhone(phone))
                    {
                        phone_number = phone;
                        showLoading();
                        sendVerifyPhone(data, phone);
                        dialog.dismiss();

                        /*if (email_address.equals("-") || email_address.isEmpty())
                        {
                            showUpdateMail();
                        }*/
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Your phone number is not valid", Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        });
        dialog.show();
    }

    private void showAccessDenied()
    {
        BootstrapButton btn_close;

        final Dialog dialog = new Dialog(DashboardActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_access_denied);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        btn_close  = dialog.findViewById(R.id.dialog_accessdenied_btnsave);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showCreatePin(int action)
    {
        View custom = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.bottom_dialog_create_pin, null);
        final BottomDialog bottomDialog = new BottomDialog.Builder(DashboardActivity.this)
                .setTitle("Membuat pin baru")
                .setCustomView(custom)
                .setCancelable(false)
                .build();

        cardHide1 = custom.findViewById(R.id.bottom_dialog_createpin_hide1);
        cardHide2 = custom.findViewById(R.id.bottom_dialog_createpin_hide2);
        cardHide3 = custom.findViewById(R.id.bottom_dialog_createpin_hide3);
        cardHide4 = custom.findViewById(R.id.bottom_dialog_createpin_hide4);
        cardHide5 = custom.findViewById(R.id.bottom_dialog_createpin_hide5);
        cardHide6 = custom.findViewById(R.id.bottom_dialog_createpin_hide6);
        cardShow1 = custom.findViewById(R.id.bottom_dialog_createpin_show1);
        cardShow2 = custom.findViewById(R.id.bottom_dialog_createpin_show2);
        cardShow3 = custom.findViewById(R.id.bottom_dialog_createpin_show3);
        cardShow4 = custom.findViewById(R.id.bottom_dialog_createpin_show4);
        cardShow5 = custom.findViewById(R.id.bottom_dialog_createpin_show5);
        cardShow6 = custom.findViewById(R.id.bottom_dialog_createpin_show6);
        linKode1  = custom.findViewById(R.id.bottom_dialog_createpin_kode1);
        linKode2  = custom.findViewById(R.id.bottom_dialog_createpin_kode2);
        linKode3  = custom.findViewById(R.id.bottom_dialog_createpin_kode3);
        linKode4  = custom.findViewById(R.id.bottom_dialog_createpin_kode4);
        linKode5  = custom.findViewById(R.id.bottom_dialog_createpin_kode5);
        linKode6  = custom.findViewById(R.id.bottom_dialog_createpin_kode6);
        pinBtn0   = custom.findViewById(R.id.bottom_dialog_createpin_btn0);
        pinBtn1   = custom.findViewById(R.id.bottom_dialog_createpin_btn1);
        pinBtn2   = custom.findViewById(R.id.bottom_dialog_createpin_btn2);
        pinBtn3   = custom.findViewById(R.id.bottom_dialog_createpin_btn3);
        pinBtn4   = custom.findViewById(R.id.bottom_dialog_createpin_btn4);
        pinBtn5   = custom.findViewById(R.id.bottom_dialog_createpin_btn5);
        pinBtn6   = custom.findViewById(R.id.bottom_dialog_createpin_btn6);
        pinBtn7   = custom.findViewById(R.id.bottom_dialog_createpin_btn7);
        pinBtn8   = custom.findViewById(R.id.bottom_dialog_createpin_btn8);
        pinBtn9   = custom.findViewById(R.id.bottom_dialog_createpin_btn9);
        txtKode1  = custom.findViewById(R.id.bottom_dialog_createpin_txt1);
        txtKode2  = custom.findViewById(R.id.bottom_dialog_createpin_txt2);
        txtKode3  = custom.findViewById(R.id.bottom_dialog_createpin_txt3);
        txtKode4  = custom.findViewById(R.id.bottom_dialog_createpin_txt4);
        txtKode5  = custom.findViewById(R.id.bottom_dialog_createpin_txt5);
        txtKode6  = custom.findViewById(R.id.bottom_dialog_createpin_txt6);

        RippleView btnAction = custom.findViewById(R.id.bottom_dialog_createpin_btnact);
        RippleView btnClose  = custom.findViewById(R.id.bottom_dialog_createpin_btnclose);
        RippleView btnDelete = custom.findViewById(R.id.bottom_dialog_createpin_btndel);
        RippleView btnSimpan = custom.findViewById(R.id.bottom_dialog_createpin_btnsave);
        LinearLayout linearAction = custom.findViewById(R.id.bottom_dialog_createpin_linearaction);
        LinearLayout linearCheck  = custom.findViewById(R.id.bottom_dialog_createpin_linearcheck);
        imgAction  = custom.findViewById(R.id.press_act);

        if (action == 1)
        {
            linearAction.setVisibility(View.GONE);
            linearCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            linearAction.setVisibility(View.VISIBLE);
            linearCheck.setVisibility(View.GONE);
        }

        pinBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(0);
                checkSelectedPin();
            }
        });

        pinBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(1);
                checkSelectedPin();
            }
        });

        pinBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(2);
                checkSelectedPin();
            }
        });

        pinBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(3);
                checkSelectedPin();
            }
        });

        pinBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(4);
                checkSelectedPin();
            }
        });

        pinBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(5);
                checkSelectedPin();
            }
        });

        pinBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(6);
                checkSelectedPin();
            }
        });

        pinBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(7);
                checkSelectedPin();
            }
        });

        pinBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(8);
                checkSelectedPin();
            }
        });

        pinBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(9);
                checkSelectedPin();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide)
                {
                    isHide = false;
                    imgAction.setImageResource(R.drawable.view);

                    positionShow();
                }
                else
                {
                    isHide = true;
                    imgAction.setImageResource(R.drawable.invisible);

                    positionHide();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
                checkSelectedPin();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Total pin : ", String.valueOf(pinnumber.size()));

                if (String.valueOf(pinnumber.size()).equals("6"))
                {
                    StringBuilder listString = new StringBuilder();
                    for (int s : pinnumber)
                    {
                        listString.append(s);
                    }

                    Log.d("Output pin : ", listString.toString());
                    setSicCode(username, listString.toString());
                    bottomDialog.dismiss();
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Harap melengkapi pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinnumber.clear();
                bottomDialog.dismiss();
                isHide = true;
            }
        });

        bottomDialog.show();
    }

    private void positionShow()
    {
        cardHide1.setVisibility(View.GONE);
        cardHide2.setVisibility(View.GONE);
        cardHide3.setVisibility(View.GONE);
        cardHide4.setVisibility(View.GONE);
        cardHide5.setVisibility(View.GONE);
        cardHide6.setVisibility(View.GONE);
        cardShow1.setVisibility(View.VISIBLE);
        cardShow2.setVisibility(View.VISIBLE);
        cardShow3.setVisibility(View.VISIBLE);
        cardShow4.setVisibility(View.VISIBLE);
        cardShow5.setVisibility(View.VISIBLE);
        cardShow6.setVisibility(View.VISIBLE);
    }

    private void positionHide()
    {
        cardHide1.setVisibility(View.VISIBLE);
        cardHide2.setVisibility(View.VISIBLE);
        cardHide3.setVisibility(View.VISIBLE);
        cardHide4.setVisibility(View.VISIBLE);
        cardHide5.setVisibility(View.VISIBLE);
        cardHide6.setVisibility(View.VISIBLE);
        cardShow1.setVisibility(View.GONE);
        cardShow2.setVisibility(View.GONE);
        cardShow3.setVisibility(View.GONE);
        cardShow4.setVisibility(View.GONE);
        cardShow5.setVisibility(View.GONE);
        cardShow6.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void checkSelectedPin()
    {
        switch (pinnumber.size())
        {
            case 0 :
                linKode1.setBackgroundResource(R.drawable.pinview_unselected);
                linKode2.setBackgroundResource(R.drawable.pinview_unselected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);
                txtKode1.setText("");
                txtKode2.setText("");
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 1 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_unselected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText("");
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 2 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 3 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 4 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 5 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_selected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText(pinnumber.get(4).toString());
                txtKode6.setText("");
                break;
            default :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_selected);
                linKode6.setBackgroundResource(R.drawable.pinview_selected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText(pinnumber.get(4).toString());
                txtKode6.setText(pinnumber.get(5).toString());
                break;
        }
    }

    private void addPin(int number)
    {
        if (pinnumber.size() + 1 > 6)
        {
            Toasty.info(getApplicationContext(), "Pin maksimal 6 Karakter", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pinnumber.add(number);
        }
    }

    private void removePin()
    {
        if (pinnumber.size() < 1)
        {
            Toasty.info(getApplicationContext(), "Anda belum memasukkan pin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pinnumber.remove(pinnumber.size() - 1);
        }
    }

    private void showInputPin(int action)
    {
        View custom = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.bottom_dialog_create_pin, null);
        bottomDialogInput = new BottomDialog.Builder(DashboardActivity.this)
                .setTitle("Masukkan Pin")
                .setCustomView(custom)
                .build();
        isHide = true;

        cardHide1 = custom.findViewById(R.id.bottom_dialog_createpin_hide1);
        cardHide2 = custom.findViewById(R.id.bottom_dialog_createpin_hide2);
        cardHide3 = custom.findViewById(R.id.bottom_dialog_createpin_hide3);
        cardHide4 = custom.findViewById(R.id.bottom_dialog_createpin_hide4);
        cardHide5 = custom.findViewById(R.id.bottom_dialog_createpin_hide5);
        cardHide6 = custom.findViewById(R.id.bottom_dialog_createpin_hide6);
        cardShow1 = custom.findViewById(R.id.bottom_dialog_createpin_show1);
        cardShow2 = custom.findViewById(R.id.bottom_dialog_createpin_show2);
        cardShow3 = custom.findViewById(R.id.bottom_dialog_createpin_show3);
        cardShow4 = custom.findViewById(R.id.bottom_dialog_createpin_show4);
        cardShow5 = custom.findViewById(R.id.bottom_dialog_createpin_show5);
        cardShow6 = custom.findViewById(R.id.bottom_dialog_createpin_show6);
        linKode1  = custom.findViewById(R.id.bottom_dialog_createpin_kode1);
        linKode2  = custom.findViewById(R.id.bottom_dialog_createpin_kode2);
        linKode3  = custom.findViewById(R.id.bottom_dialog_createpin_kode3);
        linKode4  = custom.findViewById(R.id.bottom_dialog_createpin_kode4);
        linKode5  = custom.findViewById(R.id.bottom_dialog_createpin_kode5);
        linKode6  = custom.findViewById(R.id.bottom_dialog_createpin_kode6);
        pinBtn0   = custom.findViewById(R.id.bottom_dialog_createpin_btn0);
        pinBtn1   = custom.findViewById(R.id.bottom_dialog_createpin_btn1);
        pinBtn2   = custom.findViewById(R.id.bottom_dialog_createpin_btn2);
        pinBtn3   = custom.findViewById(R.id.bottom_dialog_createpin_btn3);
        pinBtn4   = custom.findViewById(R.id.bottom_dialog_createpin_btn4);
        pinBtn5   = custom.findViewById(R.id.bottom_dialog_createpin_btn5);
        pinBtn6   = custom.findViewById(R.id.bottom_dialog_createpin_btn6);
        pinBtn7   = custom.findViewById(R.id.bottom_dialog_createpin_btn7);
        pinBtn8   = custom.findViewById(R.id.bottom_dialog_createpin_btn8);
        pinBtn9   = custom.findViewById(R.id.bottom_dialog_createpin_btn9);
        txtKode1  = custom.findViewById(R.id.bottom_dialog_createpin_txt1);
        txtKode2  = custom.findViewById(R.id.bottom_dialog_createpin_txt2);
        txtKode3  = custom.findViewById(R.id.bottom_dialog_createpin_txt3);
        txtKode4  = custom.findViewById(R.id.bottom_dialog_createpin_txt4);
        txtKode5  = custom.findViewById(R.id.bottom_dialog_createpin_txt5);
        txtKode6  = custom.findViewById(R.id.bottom_dialog_createpin_txt6);

        RippleView btnAction = custom.findViewById(R.id.bottom_dialog_createpin_btnact);
        RippleView btnDelete = custom.findViewById(R.id.bottom_dialog_createpin_btndel);
        RippleView btnCheck  = custom.findViewById(R.id.bottom_dialog_createpin_btncheck);
        LinearLayout linearAction = custom.findViewById(R.id.bottom_dialog_createpin_linearaction);
        LinearLayout linearCheck  = custom.findViewById(R.id.bottom_dialog_createpin_linearcheck);
        imgAction  = custom.findViewById(R.id.press_act);

        if (action == 1)
        {
            linearAction.setVisibility(View.GONE);
            linearCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            linearAction.setVisibility(View.VISIBLE);
            linearCheck.setVisibility(View.GONE);
        }

        pinBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(0);
                checkSelectedPin();
            }
        });

        pinBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(1);
                checkSelectedPin();
            }
        });

        pinBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(2);
                checkSelectedPin();
            }
        });

        pinBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(3);
                checkSelectedPin();
            }
        });

        pinBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(4);
                checkSelectedPin();
            }
        });

        pinBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(5);
                checkSelectedPin();
            }
        });

        pinBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(6);
                checkSelectedPin();
            }
        });

        pinBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(7);
                checkSelectedPin();
            }
        });

        pinBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(8);
                checkSelectedPin();
            }
        });

        pinBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(9);
                checkSelectedPin();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide)
                {
                    isHide = false;
                    imgAction.setImageResource(R.drawable.view);

                    positionShow();
                }
                else
                {
                    isHide = true;
                    imgAction.setImageResource(R.drawable.invisible);

                    positionHide();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
                checkSelectedPin();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Total pin : ", String.valueOf(pinnumber.size()));

                if (String.valueOf(pinnumber.size()).equals("6"))
                {
                    StringBuilder listString = new StringBuilder();
                    for (int s : pinnumber)
                    {
                        listString.append(s);
                    }

                    Log.d("Output pin : ", listString.toString());
                    verifySicCode(username, listString.toString());
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Harap melengkapi pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomDialogInput.show();
    }

    private void getSicCode(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.d("Error : ", "Data tidak ditemukan");
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Log.d("invalid : ", "Username tidak boleh kosong");
                    }
                    else
                    {
                        sic_code = jsonObject.getString("sic_code");
                        int act;
                        if (sic_code.isEmpty() || sic_code.equals("null"))
                        {
                            Log.d("Info : ", "Buat pin baru");
                            act = 0;
                            showCreatePin(act);
                        }
                        else
                        {
                            Log.d("Info : ", "Masukkan pin");
                            act = 1;
                            showInputPin(act);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void setSicCode(final String username, final String pin)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSETSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        //Gagal create pin
                        Log.d("Pin error : ", "Gagal membuat pin");
                    }
                    else
                    {
                        //Sukses create pin
                        Toasty.info(getApplicationContext(), "Berhasil menambahkan pin", Toast.LENGTH_SHORT).show();
                        pinnumber.clear();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("pinnumber", pin);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void verifySicCode(final String username, final String pin)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLVERIFYSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Pin yang anda masukkan tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        bottomDialogInput.dismiss();
                        pinnumber.clear();

                        Intent intent = new Intent(getApplicationContext(), EstatementActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("opticname", navdash_username.getText().toString());
                        startActivity(intent);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("pinnumber", pin);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void statusUser(final String username, final int pos)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSTATUSUSER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.d("Error : ", "Data tidak ditemukan");
                    }
                    else
                    {
                        sts = jsonObject.getString("status");

                        switch (pos){
                            case 1:
                                if (sts.contains("a") || sts.equals("A"))
                                {
                                    checkCityBatch(username);
                                }
                                else
                                {
                                    showAccessDenied();
                                }
                                break;

                            case 2:
                                if (sts.contains("a") || sts.equals("A"))
                                {
                                    checkCityCart(username);
                                }
                                else
                                {
                                    showAccessDenied();
                                }
                                break;
                            default:
                                if (sts.contains("a") || sts.equals("A"))
                                {
                                    if (level_user != null) {
                                        if (Integer.parseInt(level_user) == 0) {
                                            if (isHavingChild)
                                            {
                                                Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                                                intent.putExtra("cond", "LENSSALES");
                                                intent.putExtra("sales", username);
                                                intent.putExtra("havingChild", isHavingChild);
                                                intent.putExtra("customerId", customerId);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Intent intent = new Intent(getApplicationContext(), FormOrderLensActivity.class);
                                                intent.putExtra("idparty", navdash_id.getText().toString());
                                                intent.putExtra("opticname", data);
                                                intent.putExtra("province", province_user);
                                                intent.putExtra("usernameInfo", username);
                                                intent.putExtra("city", city);
                                                intent.putExtra("level", level_user);
                                                intent.putExtra("flag", member_flag);
                                                intent.putExtra("idSp", "0");
                                                intent.putExtra("isSp", "0");
                                                intent.putExtra("noHp", "0");
                                                startActivity(intent);
                                            }
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                                            intent.putExtra("cond", "LENSSALES");
                                            intent.putExtra("sales", username);
                                            intent.putExtra("havingChild", isHavingChild);
                                            intent.putExtra("customerId", customerId);
                                            startActivity(intent);

                                            Log.d("Sales Name Dashboard : ", username);
                                        }
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(getApplicationContext(), FormOrderLensActivity.class);
                                        intent.putExtra("idparty", navdash_id.getText().toString());
                                        intent.putExtra("opticname", data);
                                        intent.putExtra("province", province_user);
                                        intent.putExtra("usernameInfo", username);
                                        intent.putExtra("city", city);
                                        intent.putExtra("level", level_user);
                                        intent.putExtra("flag", member_flag);
                                        intent.putExtra("idSp", "0");
                                        intent.putExtra("isSp", "0");
                                        intent.putExtra("noHp", "0");
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    showAccessDenied();
                                }
                                break;
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showUpdateMail()
    {
        final BootstrapEditText txt_emailaddress;
        BootstrapButton btn_save;

        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dashboard_updatemail);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_emailaddress = (BootstrapEditText) dialog.findViewById(R.id.form_dashboard_updatemail_txtmail);
        btn_save         = (BootstrapButton) dialog.findViewById(R.id.form_dashboard_updatemail_btnsave);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_emailaddress.getText().toString();
                if (email.isEmpty())
                {
                    Toasty.error(getApplicationContext(), "Please insert email address!", Toast.LENGTH_SHORT, true).show();
                }
                else
                {
                    if (isValidEmail(email))
                    {
                        email_address = email;
                        //showLoading();
                        showUpdatephone();
                        //sendVerifyMail(data, email);
                        dialog.dismiss();
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Your email is not valid", Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        });
        dialog.show();
    }

    private void sendVerifyPhone(final String username, final String mobileNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, verify_phone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCrypt = new MCrypt();

                String data1 = "success";
                String data2 = "error";

                try {
                    String encrypt_data1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String encrypt_data2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));

                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.names().get(0).equals(encrypt_data1))
                        {
                            String pin = new String(mCrypt.decrypt(jsonObject.getString(encrypt_data1)));

                            //Toasty.info(getApplicationContext(), pin, Toast.LENGTH_SHORT, true).show();
                            phone_pincode = pin;

                            String pesan = phone_number + ",Terima kasih telah melengkapi data " + data +
                                    ". Masukkan kode = " + pin + " untuk mengaktifkan user anda.";

                            String enc_pincode = MCrypt.bytesToHex(mCrypt.encrypt(phone_pincode));

                            String filename = "verphone_" + data + "_" + phone_number + "_" + enc_pincode + ".txt";

                            createLogsms(pesan, filename);
                            readLogsms(filename);
                        }
                        else if (jsonObject.names().get(0).equals(jsonObject.getString(encrypt_data2)))
                        {
                            String error = new String(mCrypt.decrypt(encrypt_data2));

                            Toasty.warning(getApplicationContext(), error, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("user_info", username);
                hashMap.put("mobile_number", mobileNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void sendVerifyMail(final String username, final String email)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, verify_mail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    verify_code = jsonObject.getString("success");

                    //Toasty.info(getApplicationContext(), verify_code, Toast.LENGTH_SHORT, true).show();
                    showCountdownMail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("email_address", email);
                hashMap.put("user_info", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void sendVerifyMail2(final String username, final String email)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, verify_mail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    verify_code = jsonObject.getString("success");

                    //Toasty.info(getApplicationContext(), verify_code, Toast.LENGTH_SHORT, true).show();
                    //showCountdownMail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("email_address", email);
                hashMap.put("user_info", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showCountdownPhone()
    {
        final UniversalFontTextView lbl_timer, lbl_error;
        final VerificationCodeEditText txt_pin;
        final BootstrapButton btn_resend, btn_confirm;
        smsFlag = 1;

        hideLoading();
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_auth_order);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lbl_timer = (UniversalFontTextView) dialog.findViewById(R.id.form_authorder_lbltimer);
        lbl_error = (UniversalFontTextView) dialog.findViewById(R.id.form_authorder_lblerror);
        txt_pin   = (VerificationCodeEditText) dialog.findViewById(R.id.form_authorder_txtpin);
        btn_resend= (BootstrapButton) dialog.findViewById(R.id.form_authorder_btnresend);
        btn_confirm = (BootstrapButton) dialog.findViewById(R.id.form_authorder_btnconfirm);

        txt_pin.setFigures(5);
        txt_pin.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend, txt_pin);

        btn_resend.setEnabled(false);
        if (smsFlag == 1)
        {
            enableBroadcastReceiver(DashboardActivity.this);
            //readNewSMS(txt_pin);
        }

        txt_pin.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onInputCompleted(CharSequence s) {
                String pin = txt_pin.getText().toString().toUpperCase();
                if (pin.equals(phone_pincode))
                {
                    lbl_error.setVisibility(View.GONE);
                    updateEmailAddress(data1, email_address);
                    updatePhoneNumber(data1, phone_number);
                    smsFlag = 0;
                    disableBroadcastReceiver(DashboardActivity.this);
                    dialog.dismiss();

                    //showUpdateMail();
                }
                {
                    lbl_error.setText("Sorry your input pin not correct");
                    lbl_error.setVisibility(View.VISIBLE);
                }
            }
        });

        //Action control
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbl_error.setVisibility(View.GONE);
                btn_confirm.setEnabled(true);
                btn_resend.setEnabled(false);

                sendVerifyPhone(data, phone_number);
                setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend, txt_pin);
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = txt_pin.getText().toString().toUpperCase();
                if (pin.equals(phone_pincode))
                {
                    lbl_error.setVisibility(View.GONE);
                    updateEmailAddress(data1, email_address);
                    updatePhoneNumber(data1, phone_number);
                    dialog.dismiss();

                    //showUpdateMail();
                }
                {
                    lbl_error.setText("Sorry your input pin not correct");
                    lbl_error.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    private void showCountdownMail()
    {
        final UniversalFontTextView lbl_timer, lbl_error;
        final VerificationCodeEditText txt_pin;
        final BootstrapButton btn_resend, btn_confirm;

        hideLoading();
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_auth_order);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lbl_timer = (UniversalFontTextView) dialog.findViewById(R.id.form_authorder_lbltimer);
        lbl_error = (UniversalFontTextView) dialog.findViewById(R.id.form_authorder_lblerror);
        txt_pin   = (VerificationCodeEditText) dialog.findViewById(R.id.form_authorder_txtpin);
        btn_resend= (BootstrapButton) dialog.findViewById(R.id.form_authorder_btnresend);
        btn_confirm = (BootstrapButton) dialog.findViewById(R.id.form_authorder_btnconfirm);

        txt_pin.setFigures(5);
        txt_pin.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend, txt_pin);

        btn_resend.setEnabled(false);

        //Action control
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbl_error.setVisibility(View.GONE);
                btn_confirm.setEnabled(true);
                btn_resend.setEnabled(false);

                sendVerifyMail2(data, email_address);
                setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend, txt_pin);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = txt_pin.getText().toString().toUpperCase();
                if (pin.equals(verify_code))
                {
                    lbl_error.setVisibility(View.GONE);
                    updateEmailAddress(data1, email_address);
                    dialog.dismiss();
                }
                else
                {
                    lbl_error.setText("Sorry your input pin not correct");
                    lbl_error.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    private void setTimer(final UniversalFontTextView lbl_timer, final UniversalFontTextView lbl_error,
                          final BootstrapButton btn_confirm, final BootstrapButton btn_resend,
                          final VerificationCodeEditText txt_pin)
    {
        new CountDownTimer(181000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timer = String.format(Locale.getDefault(), "%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                lbl_timer.setText(timer);
            }

            @Override
            public void onFinish() {
                lbl_error.setText("Sory your session time is expired, try to resend code");
                lbl_error.setVisibility(View.VISIBLE);
                lbl_timer.setText("00 : 00");
                btn_confirm.setEnabled(false);
                btn_resend.setEnabled(true);
                txt_pin.setText("");
                verify_code = "";
            }
        }.start();
    }

    private void readLogsms(String filename)
    {
        File pathfile = new File(Environment.getExternalStorageDirectory(), "TRLAPP/smslog/" + filename);

        if (pathfile.exists())
        {
            uploadVerifyPhoneTxt(pathfile.toString());
            showCountdownPhone();
        }
    }

    private void createLogsms(String msg, String filename)
    {
        FileOutputStream fileOutputStream;
        File rootFolder, childFolder;

        try {
            rootFolder = new File(Environment.getExternalStorageDirectory(), "TRLAPP");
            childFolder= new File(Environment.getExternalStorageDirectory(), "TRLAPP/smslog");

            if (!rootFolder.exists())
            {
                rootFolder.mkdirs();
            }

            if (!childFolder.exists())
            {
                childFolder.mkdirs();
            }

            fileOutputStream = new FileOutputStream(childFolder + "/" + filename);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void uploadVerifyPhoneTxt(final String filepath)
    {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, upload_txtfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String psn = jsonObject.getString("info");

                    if (psn.equals("File uploaded"))
                    {
                        Toasty.info(getApplicationContext(), "Check SMS verification code", Toast.LENGTH_LONG, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });

        smr.addFile("file", filepath);
        AppController.getInstance().addToRequestQueue(smr);
    }

    private void hideMenu() {
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_options).setVisible(false);
    }

    private void openUpdateProfile() {
        Intent intent = new Intent(getApplicationContext(), FormProfileActivity.class);
        intent.putExtra("idparty", navdash_id.getText().toString());
        startActivity(intent);
        finish();
    }

    private void homeProduk() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        homeFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbarmain_fragment_container, homeFragment, "home");
        fragmentTransaction.commit();
    }

    private void showBanner() {
        BannerFragment bannerFragment = new BannerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_header, bannerFragment, "home");
        fragmentTransaction.commit();
    }

    private void showBalance() {
        getSaldoDepo(username);

        BalanceFragment balanceFragment = new BalanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        bundle.putString("nominal", nominal);
        bundle.putString("user_info", data1);
        bundle.putString("username", data);
        balanceFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_balance, balanceFragment);
        fragmentTransaction.commit();
    }

    private void showCategory() {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        bundle.putString("partySiteId", data1);
        bundle.putString("username", data);
        categoryFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_category, categoryFragment);
        fragmentTransaction.commit();
    }

    private void showPromoFrame() {
        PromoFrameFragment fragment = new PromoFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_promo_frame, fragment);
        fragmentTransaction.commit();
    }

    private void showSpesialProduk() {
        SpecialProductFragment fragment = new SpecialProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_special_product, fragment);
        fragmentTransaction.commit();
    }

    private void showAnotherProduk() {
        OtherProductFragment fragment = new OtherProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_another_product, fragment);
        fragmentTransaction.commit();
    }

    private void showMoreFrame() {
        MoreFrameFragment fragment = new MoreFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_other_frame, fragment);
        fragmentTransaction.commit();
    }

    private void showPromoBanner() {
        PromoBannerFragment fragment = new PromoBannerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_promo_banner, fragment);
        fragmentTransaction.commit();
    }

    private void frameProduk() {
        NewFrameFragment newFrameFragment = new NewFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        newFrameFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbardashboard_fragment_container, newFrameFragment, "newframe");
        fragmentTransaction.commit();
    }

    private void isHomeActive(){
        NestedScrollView nestedMain = findViewById(R.id.dashboard_scrollview);
        FrameLayout fmcontainer = findViewById(R.id.appbardashboard_fragment_container);
        nestedMain.setVisibility(View.VISIBLE);
        fmcontainer.setVisibility(View.GONE);
    }

    private void isFrameActive(){
        NestedScrollView nestedMain = findViewById(R.id.dashboard_scrollview);
        FrameLayout fmcontainer = findViewById(R.id.appbardashboard_fragment_container);
        nestedMain.setVisibility(View.GONE);
        fmcontainer.setVisibility(View.VISIBLE);
    }

    public void enableBroadcastReceiver(Context context)
    {
        ComponentName receiver = new ComponentName(context, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //Toast.makeText(this, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    public void disableBroadcastReceiver(Context context){
        ComponentName receiver = new ComponentName(context, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
    }

    /*
    private void readNewSMS(final VerificationCodeEditText txt_pincode)
    {
        SmsReceiver.getNewMessage(new SmsListener() {
            @Override
            public void ReceiveMsg(String msg) {
                if (msg.contains("=")) {
                    String[] pesan = msg.split("=");

                    String pin = pesan[1].trim().substring(0, 5);

                    txt_pincode.setText(pin);
                }
                else
                {
                    Toasty.error(getApplicationContext(), "Incorrect sms format", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    } */

    private void viewPdfFilter(final String title, final String header)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, view_pdf_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    urlPdf = jsonObject.getString("url");

                    Intent intent = new Intent(DashboardActivity.this, FormPDFViewerActivity.class);
                    intent.putExtra("data", urlPdf);
                    intent.putExtra("title", header);
                    startActivity(intent);

                    //Toasty.info(getApplicationContext(), urlPdf, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("title", title);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getProvince()
    {
        data_province.clear();

        StringRequest request = new StringRequest(Request.Method.POST, URL_GETALLPROVINCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String prov = object.getString("province");

                        data_province.add(prov);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PROVINCE GET", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getCity(final String prov)
    {
        data_city.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETCITYBYPROV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String city = object.getString("city");

                        data_city.add(city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR CITY", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("province", prov);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateCityCart(final String namaOptik, final String alamat, final String provinsi, final String kota)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATECITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), "Information address has been update", Toast.LENGTH_SHORT).show();

                        city = kota;

                        Intent intent = new Intent(DashboardActivity.this, AddCartProductActivity.class);
                        intent.putExtra("idparty", navdash_id.getText().toString());
                        intent.putExtra("opticname", data);
                        intent.putExtra("province", province_user);
                        intent.putExtra("province_address", province_address);
                        intent.putExtra("usernameInfo", username);
                        intent.putExtra("level", level_user);
                        intent.putExtra("city", city);
                        intent.putExtra("flag", member_flag);
                        startActivityForResult(intent, 2);
                    }
                    else if (object.names().get(0).equals("error"))
                    {
                        Toasty.error(getApplicationContext(), "Failed to update address", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR UPDATE CITY", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("province", provinsi);
                hashMap.put("address", alamat);
                hashMap.put("city", kota);
                hashMap.put("username", namaOptik);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateCityBatch(final String namaOptik, final String alamat, final String provinsi, final String kota)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATECITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), "Information address has been update", Toast.LENGTH_SHORT).show();

                        city = kota;

                        Intent intent = new Intent(getApplicationContext(), FormBatchOrderActivity.class);

                        intent.putExtra("idparty", navdash_id.getText().toString());
                        intent.putExtra("opticname", data);
                        intent.putExtra("province", province_user);
                        intent.putExtra("usernameInfo", username);
                        intent.putExtra("province_address", province_address);
                        intent.putExtra("city", city);
                        intent.putExtra("idSp", "0");
                        intent.putExtra("isSp", 0);
                        intent.putExtra("flag", member_flag);
                        startActivity(intent);
                    }
                    else if (object.names().get(0).equals("error"))
                    {
                        Toasty.error(getApplicationContext(), "Failed to update address", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR UPDATE CITY", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("province", provinsi);
                hashMap.put("address", alamat);
                hashMap.put("city", kota);
                hashMap.put("username", namaOptik);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void checkCityCart(final String namaOptik)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKCITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String city = object.getString("city");

                    if (city.equals("-") || city.isEmpty())
                    {
                        final UniversalFontTextView lblAddress, lblProvince, lblCity;
                        final BootstrapEditText txtAddress;
                        final BetterSpinner spinCity, spinProvince;
                        BootstrapButton btnUpdate, btnCancel;

                        getProvince();

                        final LovelyCustomDialog lovelyCustomDialog = new LovelyCustomDialog(DashboardActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.form_profile_address, null);
                        lovelyCustomDialog.setView(view);
                        lovelyCustomDialog.setTopColorRes(R.color.bootstrap_brand_danger);
                        lovelyCustomDialog.setTopTitleColor(Color.WHITE);
                        lovelyCustomDialog.setTopTitle("Update information address");

                        lblAddress = view.findViewById(R.id.form_profile_address_lblAddress);
                        lblProvince= view.findViewById(R.id.form_profile_address_lblProvince);
                        lblCity    = view.findViewById(R.id.form_profile_address_lblCity);
                        txtAddress = view.findViewById(R.id.form_profile_address_txtAddress);
                        spinProvince= view.findViewById(R.id.form_profile_address_spinProvince);
                        spinCity    = view.findViewById(R.id.form_profile_address_spinCity);
                        btnUpdate  = view.findViewById(R.id.form_profile_address_btnUpdate);
                        btnCancel  = view.findViewById(R.id.form_profile_address_btnCancel);

//                        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(FormOrderLensActivity.this,
//                                R.layout.spin_framemodel_item, spin_province);
//                        spinProvince.setAdapter(spin_adapter);

                        spinProvince.setAdapter(new ArrayAdapter<>(DashboardActivity.this,
                                android.R.layout.simple_spinner_item, data_province));

                        spinCity.setVisibility(View.GONE);

                        spinProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String prov = data_province.get(position);

//                                Toasty.warning(FormOrderLensActivity.this, prov, Toast.LENGTH_SHORT).show();

                                getCity(prov);

                                spinCity.setVisibility(View.VISIBLE);
                                spinCity.setText("");

                                spinCity.setAdapter(new ArrayAdapter<>(DashboardActivity.this,
                                        android.R.layout.simple_spinner_item, data_city));
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lovelyCustomDialog.dismiss();
                            }
                        });

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String alamat   = txtAddress.getText().toString();
                                String provinsi = spinProvince.getText().toString();
                                String kota     = spinCity.getText().toString();

                                if (alamat.isEmpty())
                                {
                                    lblAddress.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblAddress.setVisibility(View.GONE);
                                }

                                if (provinsi.isEmpty())
                                {
                                    lblProvince.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblProvince.setVisibility(View.GONE);
                                }

                                if (kota.isEmpty())
                                {
                                    lblCity.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblCity.setVisibility(View.GONE);
                                }

                                if (!alamat.isEmpty() && !provinsi.isEmpty() && !kota.isEmpty())
                                {
                                    updateCityCart(namaOptik, alamat, provinsi, kota);

                                    lovelyCustomDialog.dismiss();
                                }
                            }
                        });

                        lovelyCustomDialog.show();
                    }
                    else
                    {
                        if(Integer.parseInt(level_user) == 0)
                        {
                            if (isHavingChild)
                            {
                                Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                                intent.putExtra("cond", "CARTSALES");
                                intent.putExtra("sales", username);
                                intent.putExtra("havingChild", isHavingChild);
                                intent.putExtra("customerId", customerId);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(DashboardActivity.this, AddCartProductActivity.class);
                                intent.putExtra("idparty", navdash_id.getText().toString());
                                intent.putExtra("opticname", data);
                                intent.putExtra("province", province_user);
                                intent.putExtra("province_address", province_address);
                                intent.putExtra("usernameInfo", username);
                                intent.putExtra("level", level_user);
                                intent.putExtra("city", city);
                                intent.putExtra("flag", member_flag);
                                startActivityForResult(intent, 2);
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                            intent.putExtra("cond", "CARTSALES");
                            intent.putExtra("sales", username);
                            intent.putExtra("havingChild", isHavingChild);
                            intent.putExtra("customerId", customerId);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CITY LOG", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", namaOptik);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void checkCityBatch(final String namaOptik)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKCITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String city = object.getString("city");

                    if (city.equals("-") || city.isEmpty())
                    {
                        final UniversalFontTextView lblAddress, lblProvince, lblCity;
                        final BootstrapEditText txtAddress;
                        final BetterSpinner spinCity, spinProvince;
                        BootstrapButton btnUpdate, btnCancel;

                        getProvince();

                        final LovelyCustomDialog lovelyCustomDialog = new LovelyCustomDialog(DashboardActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.form_profile_address, null);
                        lovelyCustomDialog.setView(view);
                        lovelyCustomDialog.setTopColorRes(R.color.bootstrap_brand_danger);
                        lovelyCustomDialog.setTopTitleColor(Color.WHITE);
                        lovelyCustomDialog.setTopTitle("Update information address");

                        lblAddress = view.findViewById(R.id.form_profile_address_lblAddress);
                        lblProvince= view.findViewById(R.id.form_profile_address_lblProvince);
                        lblCity    = view.findViewById(R.id.form_profile_address_lblCity);
                        txtAddress = view.findViewById(R.id.form_profile_address_txtAddress);
                        spinProvince= view.findViewById(R.id.form_profile_address_spinProvince);
                        spinCity    = view.findViewById(R.id.form_profile_address_spinCity);
                        btnUpdate  = view.findViewById(R.id.form_profile_address_btnUpdate);
                        btnCancel  = view.findViewById(R.id.form_profile_address_btnCancel);

//                        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(FormOrderLensActivity.this,
//                                R.layout.spin_framemodel_item, spin_province);
//                        spinProvince.setAdapter(spin_adapter);

                        spinProvince.setAdapter(new ArrayAdapter<>(DashboardActivity.this,
                                android.R.layout.simple_spinner_item, data_province));

                        spinCity.setVisibility(View.GONE);

                        spinProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String prov = data_province.get(position);

//                                Toasty.warning(FormOrderLensActivity.this, prov, Toast.LENGTH_SHORT).show();

                                getCity(prov);

                                spinCity.setVisibility(View.VISIBLE);
                                spinCity.setText("");

                                spinCity.setAdapter(new ArrayAdapter<>(DashboardActivity.this,
                                        android.R.layout.simple_spinner_item, data_city));
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lovelyCustomDialog.dismiss();
                            }
                        });

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String alamat   = txtAddress.getText().toString();
                                String provinsi = spinProvince.getText().toString();
                                String kota     = spinCity.getText().toString();

                                if (alamat.isEmpty())
                                {
                                    lblAddress.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblAddress.setVisibility(View.GONE);
                                }

                                if (provinsi.isEmpty())
                                {
                                    lblProvince.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblProvince.setVisibility(View.GONE);
                                }

                                if (kota.isEmpty())
                                {
                                    lblCity.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    lblCity.setVisibility(View.GONE);
                                }

                                if (!alamat.isEmpty() && !provinsi.isEmpty() && !kota.isEmpty())
                                {
                                    updateCityBatch(namaOptik, alamat, provinsi, kota);
                                    lovelyCustomDialog.dismiss();
                                }
                            }
                        });

                        lovelyCustomDialog.show();
                    }
                    else
                    {
                        if (Integer.parseInt(level_user) == 0)
                        {
                            if (isHavingChild)
                            {
                                Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                                intent.putExtra("cond", "BATCHSALES");
                                intent.putExtra("sales", username);
                                intent.putExtra("havingChild", isHavingChild);
                                intent.putExtra("customerId", customerId);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(getApplicationContext(), FormBatchOrderActivity.class);

                                intent.putExtra("idparty", navdash_id.getText().toString());
                                intent.putExtra("opticname", data);
                                intent.putExtra("province", province_user);
                                intent.putExtra("usernameInfo", username);
                                intent.putExtra("province_address", province_address);
                                intent.putExtra("level", level_user);
                                intent.putExtra("city", city);
                                intent.putExtra("idSp", "0");
                                intent.putExtra("isSp", 0);
                                intent.putExtra("flag", member_flag);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), FormFilterOpticnameActivity.class);
                            intent.putExtra("cond", "BATCHSALES");
                            intent.putExtra("sales", username);
                            intent.putExtra("havingChild", isHavingChild);
                            intent.putExtra("customerId", customerId);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CITY LOG", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", namaOptik);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void countWishlist(int counter) {
//        txt_countwishlist.setText(" " + counter + " ");

        Log.d("WISHLIST : ", String.valueOf(counter));
    }

    @Override
    public void countCartlist(int counter) {
//        txt_countCart.setText(" " + counter + " ");
        Log.d("CART : ", String.valueOf(counter));
    }

    @Override
    public void onFragmentInteraction(String title) {
        txt_title.setText(title);
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void getSaldoDepo(final String shipNumber){
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    nominal = jsonObject.getString("sisa_saldo");
                    String val = CurencyFormat(nominal);

                    txt_saldo.setText("IDR " + val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ship_number", shipNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getParentInfo(final String key){
        StringRequest request = new StringRequest(Request.Method.POST, URLPARENTINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d(DashboardActivity.class.getSimpleName(), response);

                    if (object.getInt("code") == 200){
                        JSONObject dataObject = object.getJSONObject("data");
                        String idFlag = dataObject.getString("id_flag");
                        customerId = dataObject.getInt("customer_id");

                        Log.d(DashboardActivity.class.getSimpleName(), "Id flag : " + idFlag);
                        Log.d(DashboardActivity.class.getSimpleName(), "Customer Id : " + customerId);
                        if (idFlag.equals("Y") && customerId > 0)
                        {
                            isHavingChild = true;
                        }
                    }
                    else
                    {
                        isHavingChild = false;
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
                HashMap<String, String> map = new HashMap<>();
                map.put("key", key);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}