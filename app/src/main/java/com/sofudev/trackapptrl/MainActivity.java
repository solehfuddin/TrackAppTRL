package com.sofudev.trackapptrl;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontCheckBox;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Custom.OnFragmentInteractionListener;
import com.sofudev.trackapptrl.Custom.VersionChecker;
import com.sofudev.trackapptrl.Custom.WSCallerVersionListener;
import com.sofudev.trackapptrl.Form.AddCartProductActivity;
import com.sofudev.trackapptrl.Form.EwarrantyActivity;
import com.sofudev.trackapptrl.Form.FormPDFViewerActivity;
import com.sofudev.trackapptrl.Form.WishlistProductActivity;
import com.sofudev.trackapptrl.Fragment.HomeFragment;
import com.sofudev.trackapptrl.Fragment.NewFrameFragment;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.sofudev.trackapptrl.Util.PermissionSettings;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.zfdang.devicemodeltomarketingname.DeviceMarketingName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WSCallerVersionListener,
        OnFragmentInteractionListener, OnBadgeCounter {
    PermissionSettings permission;

    public static final String PREF_COUNTWISHLIST = "COUNTWISHLIST";
    public static final String VALUE_COUNTWISHLIST= "VALUEWISHLIST";

    SharedPreferences preferences;

    Config config = new Config();

    private String LoginURL = config.Ip_address + config.login_apps;
    private String UpdateIsOnlineURL = config.Ip_address + config.login_update_isOnline;
    private String ViewPdfURL = config.Ip_address + config.view_pdf_showAllDataByFilter;

    private String link_leinz = "https://drive.google.com/file/d/14Tz-vuXQ48fxH9wefl34TBL9wlHONPcm/view?ts=5b4860a0";
    private String link_oriental = "https://drive.google.com/file/d/1C6S4MQhlOnkMNewSEsfE4p5At5F6Fe6g/view";
    private String link_nikon = "https://drive.google.com/file/d/18j7JtkaYkNubp_9lRjlvQ5R1_fQ7-rNt/view?usp=sharing";
    private String link_guide = "https://drive.google.com/file/d/1IIlTk-eeUsVScI-Rh15Un_buaeuq-w93/view?usp=sharing";

    private Menu menu;
    private NavigationView navigationView;
    TextView txt_title, txt_countwishlist, txt_countcart;
    ACProgressCustom loading;
    LovelyCustomDialog dialog;
    private DrawerLayout drawer;
    private MaterialSearchView searchView;
    BootstrapButton btn_call, btn_wa, btn_mail, btn_web;
    private BootstrapEditText txt_user, txt_pass;
    private UniversalFontCheckBox cb_pass;
    ImageView imgWishlist, imgCart;
    FabSpeedDial fab_about;
    LayoutInflater inflater;
    View dialogView;
    //Encrypted data class
    private MCrypt mCrypt;
    Animation slidedown;
    private String deviceInfo, datetime, readTxt, urlPdf;
    boolean isForceUpdate = true;
    AddCartHelper addCartHelper;
    WishlistHelper wishlistHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TypefaceProvider.registerDefaultIconSets();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        imgWishlist = findViewById(R.id.main_btn_wishlist);
        imgCart = findViewById(R.id.main_btn_addcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchView = (MaterialSearchView) findViewById(R.id.main_search);
        txt_countwishlist = findViewById(R.id.main_badge_wishlist);
        txt_countcart = findViewById(R.id.main_badge_cart);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toasty.info(getApplicationContext(), "Input : " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Snackbar.make(findViewById(R.id.container), "Query: " + newText, Snackbar.LENGTH_LONG)
//                        .show();
                searchView.showSuggestions();
                return false;
            }
        });

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

        txt_title = (TextView) findViewById(R.id.appbarmain_txt_titleheader);

        setCustomFontHeader();
        UniversalFontComponents.init(this);

        //View header = navigationView.getHeaderView(0);

        new VersionChecker(getApplicationContext(), this).execute();

        permission = new PermissionSettings();
        allpermission();

        slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        deviceInfo = DeviceMarketingName.getInstance(this).getDeviceMarketingName(false);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        datetime = sdf.format(calendar.getTime());

        homeProduk();

        imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, WishlistProductActivity.class);
//                //intent.putExtra("activity", "main");
//                startActivityForResult(intent, 1);

                Toasty.warning(getApplicationContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, AddCartProductActivity.class);
//                //intent.putExtra("activity", "main");
//                startActivityForResult(intent, 2);

                Toasty.warning(getApplicationContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        wishlistHelper = WishlistHelper.getInstance(getApplicationContext());
        wishlistHelper.open();

        addCartHelper = AddCartHelper.getINSTANCE(getApplicationContext());
        addCartHelper.open();

        int counter = wishlistHelper.countWishlist();
        int countcart = addCartHelper.countAddCart();

//        preferences = getSharedPreferences(PREF_COUNTWISHLIST, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(VALUE_COUNTWISHLIST, counter);
//        editor.apply();
//        editor.commit();
//
//        int value = preferences.getInt(VALUE_COUNTWISHLIST, 0);
        txt_countwishlist.setText(" " + counter + " ");
        txt_countcart.setText(" " + countcart + " ");
        //frameProduk();
    }

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
            txt_countcart.setText(" " + counter + " ");
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        //searchView.setMenuItem(item);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toasty.warning(getApplicationContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();

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
        //int id = item.getItemId();

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
            homeProduk();
        } else if (id == R.id.nav_login) {
            readTemp();
        } else if (id == R.id.nav_pricelist)
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
            ViewPdfByFilter("link_leinz", "Price List Leinz");
        }
        else if (id == R.id.nav_price2){
            ViewPdfByFilter("link_oriental", "Price List Oriental");
        }
        else if (id == R.id.nav_price3){
            ViewPdfByFilter("link_nikon", "Price List Nikon");
        }
        else if (id == R.id.nav_lenses) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_lens1).isVisible();
                menu.findItem(R.id.nav_lens1).setVisible(b);
                menu.findItem(R.id.nav_lens2).setVisible(b);
                menu.findItem(R.id.nav_lens3).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        } else if (id == R.id.nav_frames) {
            //Toasty.info(getApplicationContext(), "Show another fragment", Toast.LENGTH_SHORT, true).show();
            frameProduk();
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_frame1).isVisible();
                menu.findItem(R.id.nav_frame1).setVisible(b);
                menu.findItem(R.id.nav_frame2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else if (id == R.id.nav_instruments) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_instrument1).isVisible();
                menu.findItem(R.id.nav_instrument1).setVisible(b);
                menu.findItem(R.id.nav_instrument2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return true;
        } else if (id == R.id.nav_opthalmic) {
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
        } else if (id == R.id.nav_contactlense) {
            /*try {
                Thread.sleep(200);
                Boolean b = !menu.findItem(R.id.nav_contactlens1).isVisible();
                menu.findItem(R.id.nav_contactlens1).setVisible(b);
                menu.findItem(R.id.nav_contactlens2).setVisible(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            */

            return true;
        }
        else if (id == R.id.nav_custcare) {
            Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
            intent.putExtra("data", "http://www.timurrayalab.com/custommer_care");
//            intent.putExtra("data", "http://docs.google.com/gview?embedded=true&url=http://180.250.96.154/trl-webs/index.php/printreceipt/lensa/ALOADMIN1206191");
            startActivity(intent);

            return true;
        }
        else if (id == R.id.nav_ewarranty) {
            Intent intent = new Intent(getApplicationContext(), EwarrantyActivity.class);
            startActivity(intent);

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

        } else if (id == R.id.nav_contactus) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.form_contact);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

            btn_call = (BootstrapButton) dialog.findViewById(R.id.contact_btn_call);
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent phone = new Intent(Intent.ACTION_CALL);
//                    phone.setData(Uri.parse("tel:0214610154"));
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    //startActivity(phone);
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
        } else if (id == R.id.nav_guide)
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
            ViewPdfByFilter("link_guide", "User Guide");
        }

        drawer.closeDrawers();
        return true;
    }

    private void setCustomFontHeader()
    {
        Typeface customFont = Typeface.createFromAsset(getAssets(), "DomotikaRegular.ttf");
        txt_title.setTypeface(customFont);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "DomotikaRegular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        //mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new RelativeSizeSpan(1f), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void allpermission()
    {
        permission.checkPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
    }

    private void openPlaystore()
    {
        String appName = "com.sofudev.trackapptrl";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
    }

    private void showDialogLogin()
    {
        dialog = new LovelyCustomDialog(this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_login, null);
        dialog.setView(dialogView);
        dialog.setTopColorRes(R.color.colorPrimary);
        dialog.setTopTitle("Login Customer");
        dialog.setTopTitleColor(Color.WHITE);

        txt_user = (BootstrapEditText) dialogView.findViewById(R.id.login_txt_user);
        txt_pass = (BootstrapEditText) dialogView.findViewById(R.id.login_txt_pass);
        cb_pass  = (UniversalFontCheckBox) dialogView.findViewById(R.id.login_cb_password);

        showPass();

        dialog.setListener(R.id.login_btn_login, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = txt_user.getText().toString();
                final String pass = txt_pass.getText().toString();

                if (user.isEmpty()) {
                    txt_user.setError("Username is required");
                }
                else if (pass.isEmpty()) {
                    txt_pass.setError("Password is required");
                }
                else {
                    loginApp(user, pass);
                }
            }
        });
        dialog.setListener(R.id.login_fb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                intent.putExtra("data", "https://www.facebook.com/");
                startActivity(intent);
            }
        });
        dialog.setListener(R.id.login_tw, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                intent.putExtra("data", "https://twitter.com/");
                startActivity(intent);
            }
        });
        dialog.setListener(R.id.login_ig, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FanpageActivity.class);
                intent.putExtra("data", "https://www.instagram.com/trlinfo");
                startActivity(intent);
            }
        });
        dialog.show();
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

    private void readTemp()
    {
        FileInputStream fileInputStream;
        File file;

        try
        {
            file = new File(Environment.getExternalStorageDirectory(), "TRLAPP/Temp/index.txt");

            if (!file.exists())
            {
                showDialogLogin();
            }
            else
            {
                fileInputStream = new FileInputStream(file);

                StringBuffer fileContent = new StringBuffer("");

                int content;
                byte[] buffer = new byte[1024];

                while ((content = fileInputStream.read(buffer)) != -1)
                {
                    fileContent.append(new String(buffer, 0, content));
                }

                readTxt = fileContent.toString();

                //Toasty.info(getApplicationContext(), readTxt, Toast.LENGTH_SHORT, true).show();
                fileInputStream.close();

                if (readTxt.equals("0"))
                {
                    showDialogLogin();
                }
                else
                {
                    String [] dataRead = readTxt.split(",");
                    String idparty  = dataRead[1];
                    String username = dataRead[2];

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("idparty", idparty);

                    Toasty.info(getApplicationContext(), "welcome back " + username, Toast.LENGTH_SHORT, true).show();
                    finish();
                    startActivity(intent);
                }
            }
        }
        catch (Exception ep)
        {
            ep.printStackTrace();
        }
    }

    private void loginApp(final String username, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCrypt = new MCrypt();
                String info2 = "failed";
                String info3 = "error";
                String info4 = "systemerror";
                String info5 = "failure";

                String data1 = "customer";
                String data2 = "idparty";
                try {
                    String failed   = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String error    = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String systemerror  = MCrypt.bytesToHex(mCrypt.encrypt(info4));
                    String failure  = MCrypt.bytesToHex(mCrypt.encrypt(info5));

                    String customer = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String idparty  = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.names().get(0).equals(failed)) {
                            Toasty.warning(getApplicationContext(), "Your account is inactive", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(error)) {
                            Toasty.error(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(systemerror))
                        {
                            Toasty.error(getApplicationContext(), "Account has been used in other device", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(failure))
                        {
                            Toasty.warning(getApplicationContext(), "Account not found, try different account", Toast.LENGTH_SHORT, true).show();
                        }
                        else {
                            String user = new String(mCrypt.decrypt(jsonObject.getString(customer)));
                            String id   = new String(mCrypt.decrypt(jsonObject.getString(idparty)));
                            showLoading();
                            Toasty.info(getApplicationContext(), "welcome " + user, Toast.LENGTH_SHORT, true).show();
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.putExtra("username", user);
                            intent.putExtra("idparty", id);

                            //Create temp login
                            String msg = "1," + id + "," + user + "," + datetime + "," + deviceInfo;
                            createTemp(msg);

                            //Update has login in database
                            updateIsLogin(id);

                            //Open dashboard
                            loading.dismiss();
                            startActivity(intent);
                            finish();
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
                Toasty.warning(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("password", password);

                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateIsLogin(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateIsOnlineURL, new Response.Listener<String>() {
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
                            //Toasty.success(getApplicationContext(), "Update info login success", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(invalid))
                        {
                            Toasty.error(getApplicationContext(), "Update data failed", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (jsonObject.names().get(0).equals(failure))
                        {
                            //Toasty.warning(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT, true).show();
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
                HashMap<String , String> hashMap = new HashMap<>();
                hashMap.put("id_party", id);
                hashMap.put("is_online", "0");
                hashMap.put("device_info", deviceInfo);
                hashMap.put("date_time", datetime);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showPass() {
        cb_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                {
                    txt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else {
                    txt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(MainActivity.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        loading.show();
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle("New App Version Has Been Detected");
        alertDialogBuilder.setMessage("Please update this application by click update now button !");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                openPlaystore();
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    finish();
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void homeProduk() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "main");
        homeFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbarmain_fragment_container, homeFragment);
        fragmentTransaction.commit();
    }

    private void frameProduk() {
        NewFrameFragment newFrameFragment = new NewFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "main");
        newFrameFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appbarmain_fragment_container, newFrameFragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.appbarmain_fragment_container);
        FragmentTransaction fragmentTransaction;

        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    private void addNewFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.appbarmain_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.appbarmain_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void ViewPdfByFilter(final String title, final String header)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ViewPdfURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    urlPdf = jsonObject.getString("url");

                    Intent intent = new Intent(MainActivity.this, FormPDFViewerActivity.class);
                    intent.putExtra("data", urlPdf);
                    intent.putExtra("title", header);
                    startActivity(intent);
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

    @Override
    public void onFragmentInteraction(String title) {
        txt_title.setText(title);
    }

    @Override
    public void countWishlist(int counter) {
        txt_countwishlist.setText(" " + counter + " ");
    }

    @Override
    public void countCartlist(int counter) {
        txt_countcart.setText(" " + counter + " ");
    }
}
