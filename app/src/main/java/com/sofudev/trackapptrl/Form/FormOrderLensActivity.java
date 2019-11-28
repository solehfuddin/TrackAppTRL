package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;
import com.kyleduo.switchbutton.SwitchButton;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_coating;
import com.sofudev.trackapptrl.Adapter.Adapter_lenstype;
import com.sofudev.trackapptrl.Adapter.Adapter_tinting;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.SmsReceiver;
import com.sofudev.trackapptrl.Custom.VolleyOneCallBack;
import com.sofudev.trackapptrl.Data.Data_coating;
import com.sofudev.trackapptrl.Data.Data_lenstype;
import com.sofudev.trackapptrl.Data.Data_stok_satuan;
import com.sofudev.trackapptrl.Data.Data_tinting;
import com.sofudev.trackapptrl.LocalDb.Db.LensSatuanHelper;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.weiwangcn.betterspinner.library.BetterSpinner;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class FormOrderLensActivity extends AppCompatActivity {
    Adapter_lenstype adapter_lenstype;
    Adapter_tinting adapter_tinting;
    Adapter_coating adapter_coating;
    List<Data_lenstype> data = new ArrayList<>();
    List<Data_tinting> data_tintings = new ArrayList<>();
    List<Data_coating> data_coatings = new ArrayList<>();
    ArrayList<String> data_corridor = new ArrayList<>();
    ArrayList<String> data_tintinggroup = new ArrayList<>();
    ArrayList<String> data_province = new ArrayList<>();
    ArrayList<String> data_city     = new ArrayList<>();
    //ArrayList<String> data_coating = new ArrayList<>();
    Config config = new Config();
    String URL_CHECKCITY = config.Ip_address + config.spinner_shipment_getProvinceOptic;
    String URL_GETALLPROVINCE= config.Ip_address + config.spinner_shipment_getAllProvince;
    String URL_GETCITYBYPROV = config.Ip_address + config.spinner_shipment_getCity;
    String URL_UPDATECITY    = config.Ip_address + config.spinner_shipment_updateCity;
    String URL_CHECKSTOCK    = config.Ip_address + config.orderlens_check_stock;

    MCrypt mCrypt;

    private static final String[] carridor = new String[] {"Select Corridor"};
    private static final String[] tint_def = new String[] {"Color Type"};
    private static final String[] tint_dscr= new String[] {"Color Description"};
    private static final String[] coat_group = new String[] {"Coating"};
    private static final String[] coat_descr = new String[] {"Coating Description"};
    private static final String[] tint_descr = new String[] {"None"};
    private static final String[] spin_province = new String[] {"Choose Province"};

    ACProgressFlower loading;
    LovelyStandardDialog dialog;
    AlertDialog dialogUpload;
    RippleView btn_back, btn_done, btn_save, ripple_orderdetail, ripple_framedetail, ripple_facetdetail, ripple_refraksidetail,
             btn_lenstype;

    BootstrapEditText txt_orderNumber, txt_patientName, txt_orderInformation, txt_dbl, txt_hor, txt_ver, txt_framebrand,
                txt_sphl, txt_sphr, txt_cyll, txt_cylr, txt_axsl, txt_axsr, txt_mpdl, txt_mpdr, txt_pdl, txt_pds,
                txt_wrap, txt_phantose, txt_vertex, txt_segh, txt_addl, txt_addr, txt_lenstype, txt_lensdesc,
                txt_infofacet, txt_phonenumber;
    TextView txt_coatdescr;
    UniversalFontTextView txt_tinttitle;
    CheckBox cb_sideR, cb_sideL;
    RelativeLayout rl_error;

    ImageView  img_error;

    BetterSpinner spin_framemodel;
    Spinner spin_corridor, spin_tintgroup, spin_tint, spin_coatgroup;
    SwitchButton sw_facet;
    ExpandableLayout expandableLayout_orderdetail, expandableLayout_framedetail, expandableLayout_facetdetail,
                    expandableLayout_refraksidetail;

    String emailaddress, mobilenumber, userinfo, pin_number ,coating;
    String filename, id_lensa, desc_lensa, file;
    String allData, dateTime, opticId, opticName, dateOnly, timeOnly, todayDate, opticProvince, opticUsername, opticCity,
            orderNumber, opticFlag, phoneNumber, chooser;
    String model_frame, hor_frame, ver_frame, dbl_frame, frame_brand, infofacet, isfacet = "N", corridor;
    String typeLensa, description, categoryLens, purchaseLens, icon, lensDiv, itemIdR, itemIdL, itemIdSt, itemIdStR,
            itemIdStL, flagPasang, flagShipping, itemCodeR, descR, itemSphR, itemCylR, itemAxsR, itemAddR, powerR, itemQtyR,
            itemCodeL, descL, itemSphL, itemCylL, itemAxsL, itemAddL, powerL, itemQtyL, itemFacetCode, facetDescription,
            qtyFacet, itemTintingCode, tintingDescription, qtyTinting, flagRL;
    Integer itemPriceR, itemTotalPriceR, itemPriceL, itemTotalPriceL, itemPriceSt, itemPriceStR, itemPriceStL, oprDiscount,
            itemFacetPrice, itemTintPrice, itemWeight, countTemp, autoSum, totalFacet, totalTinting;
    int sisaStok, sisaStokR, sisaStokL;
    String sph_l, sph_r, cyl_l, cyl_r, axs_l, axs_r, mpd_l, mpd_r, pd_l, pd_s, seg_h, color_tint, color_descr, add_l, add_r,
            listLineNo, prod_attr_valL, prod_attr_valR, divName;
    String descriptionStock, descriptionStockR, descriptionStockL;
    private int onOff = 1;
    int smsFlag = 0, limiter = 1000, totalWeight = 0;
    float discountR, discountL;

    Calendar calendar = Calendar.getInstance();

    String hargaR, hargaL;

//    List<Data_stok_satuan> itemStokSatuan = new ArrayList<>();
    LensSatuanHelper lensSatuanHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_lens);

        btn_back                = (RippleView) findViewById(R.id.form_lensorder_ripplebtnback);
        btn_done                = (RippleView) findViewById(R.id.form_lensorder_ripplebtndone);
        btn_save                = (RippleView) findViewById(R.id.form_lensorder_ripplebtnsave);
        ripple_orderdetail      = (RippleView) findViewById(R.id.form_lensorder_rippleorderdetail);
        ripple_framedetail      = (RippleView) findViewById(R.id.form_lensorder_rippleframedetail);
        ripple_facetdetail      = (RippleView) findViewById(R.id.form_lensorder_ripplefacetdetail);
        ripple_refraksidetail   = (RippleView) findViewById(R.id.form_lensorder_ripplerefraksidetail);
        btn_lenstype            = (RippleView) findViewById(R.id.form_lensorder_btnlenstype);

        txt_tinttitle           = (UniversalFontTextView) findViewById(R.id.form_lensorder_txttinttitle);
        txt_orderNumber         = (BootstrapEditText) findViewById(R.id.form_lensorder_txtOrderNumber);
        txt_patientName         = (BootstrapEditText) findViewById(R.id.form_lensorder_txtPatientName);
        txt_orderInformation    = (BootstrapEditText) findViewById(R.id.form_lensorder_txtOrderInformation);
        txt_phonenumber         = (BootstrapEditText) findViewById(R.id.form_lensorder_txtPatientContact);
        txt_dbl                 = (BootstrapEditText) findViewById(R.id.form_lensorder_txtdbl);
        txt_hor                 = (BootstrapEditText) findViewById(R.id.form_lensorder_txthor);
        txt_ver                 = (BootstrapEditText) findViewById(R.id.form_lensorder_txtver);
        txt_framebrand          = (BootstrapEditText) findViewById(R.id.form_lensorder_txtframebrand);
        txt_sphl                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtsphl);
        txt_sphr                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtsphr);
        txt_cyll                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtcyll);
        txt_cylr                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtcylr);
        txt_axsl                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtaxsl);
        txt_axsr                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtaxsr);
        txt_mpdl                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtmpdl);
        txt_mpdr                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtmpdr);
        //txt_pdl                 = (BootstrapEditText) findViewById(R.id.form_lensorder_txtpdl);
        //txt_pds                 = (BootstrapEditText) findViewById(R.id.form_lensorder_txtpds);
        txt_wrap                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtwrap);
        txt_phantose            = (BootstrapEditText) findViewById(R.id.form_lensorder_txtphantose);
        txt_vertex              = (BootstrapEditText) findViewById(R.id.form_lensorder_txtvertex);
        txt_segh                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtsegh);
        txt_addl                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtaddl);
        txt_addr                = (BootstrapEditText) findViewById(R.id.form_lensorder_txtaddr);
        txt_lenstype            = (BootstrapEditText) findViewById(R.id.form_lensorder_txtlenstype);
        txt_lensdesc            = (BootstrapEditText) findViewById(R.id.form_lensorder_txtlensdesc);
        txt_infofacet           = (BootstrapEditText) findViewById(R.id.form_lensorder_txtinfofacet);
        txt_coatdescr           = (TextView) findViewById(R.id.form_lensorder_txtcoatdescr);

        cb_sideL                = (CheckBox) findViewById(R.id.form_lensorder_cbsidel);
        cb_sideR                = (CheckBox) findViewById(R.id.form_lensorder_cbsider);

        spin_framemodel         = (BetterSpinner) findViewById(R.id.form_lensorder_spinframemodel);
        spin_corridor           = (Spinner) findViewById(R.id.form_lensorder_spincorridor);
        spin_tintgroup          = (Spinner) findViewById(R.id.form_lensorder_spincolorgrup);
        spin_tint               = (Spinner) findViewById(R.id.form_lensorder_spincolordescr);
        spin_coatgroup          = (Spinner) findViewById(R.id.form_lensorder_spincoatgroup);

        sw_facet                = (SwitchButton) findViewById(R.id.form_lensorder_swfacet);

        expandableLayout_orderdetail    = (ExpandableLayout) findViewById(R.id.form_lensorder_expandlayoutorderdetail);
        expandableLayout_framedetail    = (ExpandableLayout) findViewById(R.id.form_lensorder_expandlayoutframedetail);
        expandableLayout_facetdetail    = (ExpandableLayout) findViewById(R.id.form_lensorder_expandlayoutfacetdetail);
        expandableLayout_refraksidetail = (ExpandableLayout) findViewById(R.id.form_lensorder_expandlayoutrefraksidetail);

        btn_done.setEnabled(false);
        spin_corridor.setAdapter(new ArrayAdapter<>(this, R.layout.spin_framemodel_item, carridor));
        spin_corridor.setEnabled(false);

        spin_tintgroup.setAdapter(new ArrayAdapter<>(this, R.layout.spin_framemodel_item, tint_def));
        spin_tint.setAdapter(new ArrayAdapter<>(this, R.layout.spin_framemodel_item, tint_dscr));
        spin_coatgroup.setAdapter(new ArrayAdapter<>(this, R.layout.spin_framemodel_item, coat_group));

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf3.format(calendar.getTime());
        String otherDate = sdf4.format(calendar.getTime());

        backToDashboard();
        doneBackDashboard();
        getIdOptic();
        getUserInfo();
        getOrderNumber(opticUsername,otherDate);
        countOrderTemp(opticUsername,todayDate);
        sumWeightOrder(opticUsername,todayDate);
        showDateTime();
        createOrder();
        selectLenstype();

        expandableLayout_orderdetail.expand();
        expandableLayout_framedetail.expand();
        expandableLayout_facetdetail.expand();
        expandableLayout_refraksidetail.expand();

        chooseFrameModel();
        checkFacet();

        setAutoDecimal(txt_sphr);
        setAutoDecimal(txt_sphl);
        setAutoDecimal(txt_cylr);
        setAutoDecimal(txt_cyll);
        setAutoDecimal(txt_addr);
        setAutoDecimal(txt_addl);

        txt_orderNumber.setEnabled(false);
        txt_patientName.requestFocus();

        spin_tintgroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    getTintingGroup();
                }
                return false;
            }
        });

        spin_corridor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!data_corridor.isEmpty())
                {
                    //Toasty.info(getApplicationContext(), data_corridor.get(position), Toast.LENGTH_SHORT, true).show();
                    corridor = data_corridor.get(position);
                }
                else
                {
                    //Initialisasi data
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tintgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooser = spin_tintgroup.getSelectedItem().toString();

                if (!data_tintinggroup.isEmpty())
                {
                    getTinting(data_tintinggroup.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toasty.success(getApplicationContext(), data_tintings.get(position).getTint_code(), Toast.LENGTH_SHORT, true).show();
                if (!data_tintings.isEmpty())
                {
                    color_tint = data_tintings.get(position).getTint_code();
                    color_descr= data_tintings.get(position).getTint_description();
                }
                else
                {
                    //initialise here
                    color_tint = "";
                    color_descr= "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_coatgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!data_coatings.isEmpty())
                {
                    txt_coatdescr.setText(data_coatings.get(position).getCoat_description());
                    coating = data_coatings.get(position).getCoat_code();
                    //Toast.makeText(getApplicationContext(), coating, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txt_coatdescr.setText("Not Coating");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cb_sideR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    txt_sphr.setEnabled(true);
                    txt_cylr.setEnabled(true);
                    txt_axsr.setEnabled(true);
                    txt_addr.setEnabled(true);
                    txt_mpdr.setEnabled(true);
                }
                else
                {
                    txt_sphr.setEnabled(false);
                    txt_cylr.setEnabled(false);
                    txt_axsr.setEnabled(false);
                    txt_addr.setEnabled(false);
                    txt_mpdr.setEnabled(false);
                }
            }
        });

        cb_sideL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    txt_sphl.setEnabled(true);
                    txt_cyll.setEnabled(true);
                    txt_axsl.setEnabled(true);
                    txt_addl.setEnabled(true);
                    txt_mpdl.setEnabled(true);
                }
                else
                {
                    txt_sphl.setEnabled(false);
                    txt_cyll.setEnabled(false);
                    txt_axsl.setEnabled(false);
                    txt_addl.setEnabled(false);
                    txt_mpdl.setEnabled(false);
                }
            }
        });

        lensSatuanHelper = LensSatuanHelper.getINSTANCE(FormOrderLensActivity.this);
        lensSatuanHelper.open();

        lensSatuanHelper.truncAllLensSatuan();
        
        txt_segh.setEnabled(true);
        txt_wrap.setEnabled(false);
        txt_phantose.setEnabled(false);
        txt_vertex.setEnabled(false);
        txt_infofacet.setEnabled(false);
//        txt_dbl.setEnabled(false);
//        txt_hor.setEnabled(false);
//        txt_ver.setEnabled(false);
        spin_framemodel.setVisibility(View.GONE);

        txt_sphr.setLongClickable(false);
        txt_sphl.setLongClickable(false);
        txt_cylr.setLongClickable(false);
        txt_cyll.setLongClickable(false);
        txt_axsr.setLongClickable(false);
        txt_axsl.setLongClickable(false);
        txt_addr.setLongClickable(false);
        txt_addl.setLongClickable(false);
    }

    private void backToDashboard()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doneBackDashboard()
    {
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                //file = sample.toString();

                //uploadTxt(file);
            }
        });
    }

    private void checkFacet()
    {
        sw_facet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw_facet.isChecked())
                {
                    txt_segh.setEnabled(true);
                    txt_wrap.setEnabled(true);
                    txt_phantose.setEnabled(true);
                    txt_vertex.setEnabled(true);
                    txt_infofacet.setEnabled(true);
                    spin_framemodel.setVisibility(View.VISIBLE);

//                    txt_dbl.setEnabled(true);
//                    txt_hor.setEnabled(true);
//                    txt_ver.setEnabled(true);

                    //Toasty.info(getApplicationContext(), "Facet", Toast.LENGTH_SHORT, true).show();
                    isfacet = "G";
                    //Toasty.warning(getApplicationContext(), isfacet, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txt_segh.setEnabled(true);
                    txt_wrap.setEnabled(false);
                    txt_phantose.setEnabled(false);
                    txt_vertex.setEnabled(false);
                    txt_infofacet.setEnabled(false);
//                    txt_dbl.setEnabled(false);
//                    txt_hor.setEnabled(false);
//                    txt_ver.setEnabled(false);
                    spin_framemodel.setVisibility(View.GONE);

//                    txt_segh.setText("");
                    txt_wrap.setText("");
                    txt_phantose.setText("");
                    txt_vertex.setText("");
                    txt_infofacet.setText("");
//                    txt_dbl.setText("");
//                    txt_hor.setText("");
//                    txt_ver.setText("");
                    //Toasty.warning(getApplicationContext(), "Not facet", Toast.LENGTH_SHORT, true).show();

                    String hor = txt_hor.getText().toString();
                    String ver = txt_ver.getText().toString();

                    if (!hor.isEmpty() && !ver.isEmpty())
                    {
                        isfacet = "F";
                    }
                    else
                    {
                        isfacet = "N";
                    }
                }
            }
        });
    }

    private void getIdOptic()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            opticId     = bundle.getString("idparty");
            opticName   = bundle.getString("opticname");
            opticProvince = bundle.getString("province");
            opticUsername = bundle.getString("usernameInfo");
            opticCity   = bundle.getString("city");
            opticFlag   = bundle.getString("flag");
        }
        opticId     = opticId + ",";
        opticName   = opticName + ",";

        //Toast.makeText(getApplicationContext(), opticUsername, Toast.LENGTH_SHORT).show();
    }

    private void showDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd,HH:mm:ss,");
        SimpleDateFormat sdf1= new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2= new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf3= new SimpleDateFormat("dd-MM-yyyy");
        dateTime = sdf.format(calendar.getTime());
        dateOnly = sdf1.format(calendar.getTime());
        timeOnly = sdf2.format(calendar.getTime());
        todayDate= sdf3.format(calendar.getTime());
    }

    private void setAutoDecimal(final BootstrapEditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String delimit = s + ".";

                Selection.setSelection(editText.getText(), editText.length());

                if (s.toString().matches("[0-9]"))
                {
                    editText.setText(delimit);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                }
                else if (s.toString().matches("^-?[0-9]"))
                {
                    editText.setText(delimit);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }

                if (s.toString().length() == 3)
                {
                    if (s.toString().charAt(2) == '0' || s.toString().charAt(2) == '5')
                    {
                        String addZ = s.toString() + "0";
                        editText.setText(addZ);
                    }
                    else if (s.toString().charAt(2) == '2' || s.toString().charAt(2) == '7')
                    {
                        String addF = s.toString() + "5";
                        editText.setText(addF);
                    }
                }
                else if (s.toString().length() == 4)
                {
                    if (s.charAt(0) == '-')
                    {
                        if (s.toString().charAt(3) == '0' || s.toString().charAt(3) == '5')
                        {
                            String addZ = s.toString() + "0";
                            editText.setText(addZ);
                        }
                        else if (s.toString().charAt(3) == '2' || s.toString().charAt(3) == '7')
                        {
                            String addF = s.toString() + "5";
                            editText.setText(addF);
                        }
                    }
                }

                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL)
                        {
                            editText.setText("");
                        }
                        return false;
                    }
                });
            }
        });
    }

    private void createOrder()
    {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sphr         = txt_sphr.getText().toString();
                final String cylr         = txt_cylr.getText().toString();
                final String axsr         = txt_axsr.getText().toString();
                final String addr         = txt_addr.getText().toString();

                final String sphl         = txt_sphl.getText().toString();
                final String cyll         = txt_cyll.getText().toString();
                final String axsl         = txt_axsl.getText().toString();
                final String addl         = txt_addl.getText().toString();

                final String kodeLensa = txt_lenstype.getText().toString();
                String koridor   = spin_corridor.getSelectedItem().toString().trim();
                if (koridor.equals("Select Corridor"))
                {
                    koridor = "";
                }

                String frameType = spin_framemodel.getText().toString();
                final String kodeitemR, kodeitemL;

                if (categoryLens.equals("S"))
                {
                    //Toasty.info(getApplicationContext(), "Ini Lensa Stock", Toast.LENGTH_SHORT).show();
                    Log.d(FormOrderLensActivity.class.getSimpleName(), "Stock Lens");

                    checkStockRLHandle(kodeLensa, new VolleyOneCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                flagRL = jsonObject.getString("rl");

                                if (flagRL.contentEquals("B") || flagRL.contains("B") || flagRL.equals("B"))
                                {
                                    if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                                    {
                                        getItemStockRHandle(kodeLensa, sphr, cylr, addr, "B", new VolleyOneCallBack() {
                                            @Override
                                            public void onSuccess(String result) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);

                                                    if (jsonObject.names().get(0).equals("error"))
                                                    {
                                                        Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        itemIdStR = jsonObject.getString("item_id");
                                                        descriptionStockR = jsonObject.getString("item_desc") + " " + sphr + " " + cylr;

                                                        getItemPriceStRHandle(itemIdStR, new VolleyOneCallBack() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                                if (result != null)
                                                                {
                                                                    //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                                    hargaR = "True";
                                                                }
                                                                else
                                                                {
                                                                    hargaR = "False";
                                                                }

                                                                //Jika side L di-isi
                                                                if (!sphl.isEmpty() | !cyll.isEmpty() | !addl.isEmpty())
                                                                {
                                                                    getItemStockLHandle(kodeLensa, sphl, cyll, addl, "B", new VolleyOneCallBack() {
                                                                        @Override
                                                                        public void onSuccess(String result) {
                                                                            try {
                                                                                JSONObject jsonObject = new JSONObject(result);

                                                                                if (jsonObject.names().get(0).equals("error"))
                                                                                {
                                                                                    Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                else
                                                                                {
                                                                                    itemIdStL = jsonObject.getString("item_id");
                                                                                    descriptionStockL = jsonObject.getString("item_desc") + " " + sphl + " " + cyll;

                                                                                    getItemPriceStLHandle(itemIdStL, new VolleyOneCallBack() {
                                                                                        @Override
                                                                                        public void onSuccess(String result) {
                                                                                            if (result != null)
                                                                                            {
                                                                                                //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                                                                hargaL = "True";
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                hargaL = "False";
                                                                                            }

                                                                                            if (itemIdStR.contentEquals(itemIdStL) || itemIdStR.contains(itemIdStL))
                                                                                            {
                                                                                                descriptionStock = descriptionStockR;
                                                                                                Log.d("AREA 1", "Check stock : " + itemIdStR);

                                                                                                final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                                                                cekStok(itemIdStR, new VolleyOneCallBack() {
                                                                                                    @Override
                                                                                                    public void onSuccess(String result) {
                                                                                                        try {
                                                                                                            JSONObject object1 = new JSONObject(result);

                                                                                                            sisaStok = object1.getInt("qty_stock");

//                                                                                                                int sisa = sisaStok - 2;

                                                                                                            itemSatuan.setId(1);
                                                                                                            itemSatuan.setDescription(descriptionStock);
                                                                                                            itemSatuan.setQty(2);
                                                                                                            itemSatuan.setStock(sisaStok);

                                                                                                            Log.d("AREA 1", "Id satuan : " + itemSatuan.getId());
                                                                                                            Log.d("AREA 1", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                                            Log.d("AREA 1", "Qty Order : " + itemSatuan.getQty());
                                                                                                            Log.d("AREA 1", "Sisa Stok : " + sisaStok);

                                                                                                            lensSatuanHelper.open();
                                                                                                            long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                                            if (status > 0)
                                                                                                            {
                                                                                                                Log.d("AREA 1" ,"Item has been added to cart");
                                                                                                            }

//                                                                                                            itemStokSatuan.add(itemSatuan);
                                                                                                            List<Data_stok_satuan> itemSatuan;
                                                                                                            itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                                                            Log.d("AREA 1", "Item Stok Satuan : " + itemSatuan.size());
                                                                                                            Log.d("AREA 1", "Deskripsi lensa : " + itemSatuan.get(0).getDescription());

                                                                                                            List<Boolean> sisanya = new ArrayList<>();

                                                                                                            for (int i = 0; i < itemSatuan.size(); i++)
                                                                                                            {
                                                                                                                String item = itemSatuan.get(i).getDescription();
                                                                                                                int stock = itemSatuan.get(i).getStock();
                                                                                                                int qty   = itemSatuan.get(i).getQty();
                                                                                                                int sisa  = stock - qty;

                                                                                                                Log.d("AREA 1", "Deskripsi : " + item);
                                                                                                                Log.d("AREA 1", "Stock : " + stock);
                                                                                                                Log.d("AREA 1", "qty : " + qty);
                                                                                                                Log.d("AREA 1", "sisa : " + sisa);

                                                                                                                if (sisa < 0)
                                                                                                                {
                                                                                                                    sisanya.add(i, false);
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    sisanya.add(i, true);
                                                                                                                }
                                                                                                            }

                                                                                                            boolean cek = sisanya.contains(false);

                                                                                                            if (cek) {
                                                                                                                Log.d("Information Stok", "Ada item yang minus");

                                                                                                                final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                                                WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                                                                dialog.setContentView(R.layout.dialog_warning);
                                                                                                                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                                                                lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                                                                lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                                                                lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                                                                UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                                                                txtTitle.setText("Stok " + itemSatuan.get(0).getDescription() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                                                                ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                                                                imgClose.setOnClickListener(new View.OnClickListener() {
                                                                                                                    @Override
                                                                                                                    public void onClick(View v) {
                                                                                                                        dialog.dismiss();
                                                                                                                    }
                                                                                                                });

                                                                                                                dialog.show();
                                                                                                                dialog.getWindow().setAttributes(lwindow);
                                                                                                            }
                                                                                                            else
                                                                                                            {
                                                                                                                Log.d("Information Stok", "Aman lanjutkan");

                                                                                                                String order = txt_orderNumber.getText().toString();
                                                                                                                String none = spin_corridor.getSelectedItem().toString().trim();

                                                                                                                if (sw_facet.isChecked())
                                                                                                                {
                                                                                                                    isfacet = "G";
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    String hor = txt_hor.getText().toString();
                                                                                                                    String ver = txt_ver.getText().toString();

                                                                                                                    if (!hor.isEmpty() && !ver.isEmpty())
                                                                                                                    {
                                                                                                                        isfacet = "F";
                                                                                                                    }
                                                                                                                    else
                                                                                                                    {
                                                                                                                        isfacet = "N";
                                                                                                                    }
                                                                                                                }

                                                                                                                Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                                                                if (none.equalsIgnoreCase("none"))
                                                                                                                {
                                                                                                                    Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                                                                }
                                                                                                                else if (order.isEmpty())
                                                                                                                {
                                                                                                                    Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                                                                }
                                                                                                                else {
                                                                                                                    dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                                                                    dialog.setMessage("Are you sure all data is correct ?");
                                                                                                                    dialog.setTopTitle("Confirmation Order");
                                                                                                                    dialog.setTopTitleColor(Color.WHITE);
                                                                                                                    dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                                                                    dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                                                                    dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                                                                    dialog.setCancelable(false);

                                                                                                                    dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(View v) {
                                                                                                                            saveOrder();
                                                                                                                        }
                                                                                                                    });

                                                                                                                    dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(View v) {
                                                                                                                            dialog.dismiss();
                                                                                                                        }
                                                                                                                    });
                                                                                                                    dialog.show();
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                Log.d("AREA 1", "Check stock R : " + itemIdStR);
                                                                                                Log.d("AREA 1", "Check stock L : " + itemIdStL);

                                                                                                final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                                                                cekStokR(itemIdStR, new VolleyOneCallBack() {
                                                                                                    @Override
                                                                                                    public void onSuccess(String result) {
                                                                                                        try {
                                                                                                            JSONObject object1 = new JSONObject(result);

                                                                                                            sisaStok = object1.getInt("qty_stock");

                                                                                                            itemSatuan.setId(1);
                                                                                                            itemSatuan.setDescription(descriptionStockR);
                                                                                                            itemSatuan.setQty(1);
                                                                                                            itemSatuan.setStock(sisaStok);

                                                                                                            Log.d("AREA 1", "Id satuan : " + itemSatuan.getId());
                                                                                                            Log.d("AREA 1", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                                            Log.d("AREA 1", "Qty Order : " + itemSatuan.getQty());
                                                                                                            Log.d("AREA 1", "Sisa Stok : " + sisaStok);

                                                                                                            lensSatuanHelper.open();
                                                                                                            long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                                            if (status > 0)
                                                                                                            {
                                                                                                                Log.d("AREA 1" ,"Item has been added to cart");
                                                                                                            }

                                                                                                            cekStokL(itemIdStL, new VolleyOneCallBack() {
                                                                                                                @Override
                                                                                                                public void onSuccess(String result) {
                                                                                                                    try {
                                                                                                                        JSONObject object2 = new JSONObject(result);

                                                                                                                        sisaStok = object2.getInt("qty_stock");

                                                                                                                        itemSatuan.setId(2);
                                                                                                                        itemSatuan.setDescription(descriptionStockL);
                                                                                                                        itemSatuan.setQty(1);
                                                                                                                        itemSatuan.setStock(sisaStok);

                                                                                                                        Log.d("AREA 1", "Id satuan : " + itemSatuan.getId());
                                                                                                                        Log.d("AREA 1", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                                                        Log.d("AREA 1", "Qty Order : " + itemSatuan.getQty());
                                                                                                                        Log.d("AREA 1", "Sisa Stok : " + sisaStok);

                                                                                                                        lensSatuanHelper.open();
                                                                                                                        long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                                                        if (status > 0)
                                                                                                                        {
                                                                                                                            Log.d("AREA 1" ,"Item has been added to cart");
                                                                                                                        }

                                                                                                                        List<Data_stok_satuan> itemSatuan;
                                                                                                                        itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                                                                        Log.d("AREA 1", "Item Stok Satuan : " + itemSatuan.size());

                                                                                                                        List<Boolean> sisanya = new ArrayList<>();

                                                                                                                        for (int i = 0; i < itemSatuan.size(); i++)
                                                                                                                        {
                                                                                                                            String item = itemSatuan.get(i).getDescription();
                                                                                                                            int stock = itemSatuan.get(i).getStock();
                                                                                                                            int qty   = itemSatuan.get(i).getQty();
                                                                                                                            int sisa  = stock - qty;

                                                                                                                            Log.d("AREA 1", "Deskripsi : " + item);
                                                                                                                            Log.d("AREA 1", "Stock : " + stock);
                                                                                                                            Log.d("AREA 1", "qty : " + qty);
                                                                                                                            Log.d("AREA 1", "sisa : " + sisa);

                                                                                                                            if (sisa < 0)
                                                                                                                            {
                                                                                                                                sisanya.add(i, false);
                                                                                                                            }
                                                                                                                            else
                                                                                                                            {
                                                                                                                                sisanya.add(i, true);
                                                                                                                            }
                                                                                                                        }

                                                                                                                        boolean cek = sisanya.contains(false);

                                                                                                                        if (cek) {
                                                                                                                            Log.d("Information Stok", "Ada item yang minus");

                                                                                                                            StringBuilder log = new StringBuilder();

                                                                                                                            final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                                                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                                                            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                                                                            dialog.setContentView(R.layout.dialog_warning);
                                                                                                                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                                                                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                                                                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                                                                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                                                                            int stockR  = itemSatuan.get(0).getStock();
                                                                                                                            int qtyR    = itemSatuan.get(0).getQty();
                                                                                                                            int sisaR   = stockR - qtyR;

                                                                                                                            int stockL  = itemSatuan.get(1).getStock();
                                                                                                                            int qtyL    = itemSatuan.get(1).getQty();
                                                                                                                            int sisaL   = stockL - qtyL;

                                                                                                                            if (sisaR < 0 && sisaL < 0)
                                                                                                                            {
                                                                                                                                log.append(itemSatuan.get(0).getDescription());
                                                                                                                                log.append(" & ");
                                                                                                                                log.append(itemSatuan.get(1).getDescription());
                                                                                                                            }
                                                                                                                            else if (sisaR < 0)
                                                                                                                            {
                                                                                                                                log.append(itemSatuan.get(0).getDescription());
                                                                                                                            }
                                                                                                                            else
                                                                                                                            {
                                                                                                                                log.append(itemSatuan.get(1).getDescription());
                                                                                                                            }

                                                                                                                            UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                                                                            txtTitle.setText("Stok " + log.toString() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                                                                            ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                                                                            imgClose.setOnClickListener(new View.OnClickListener() {
                                                                                                                                @Override
                                                                                                                                public void onClick(View v) {
                                                                                                                                    dialog.dismiss();
                                                                                                                                }
                                                                                                                            });

                                                                                                                            dialog.show();
                                                                                                                            dialog.getWindow().setAttributes(lwindow);
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            Log.d("Information Stok", "Aman lanjutkan");

                                                                                                                            String order = txt_orderNumber.getText().toString();
                                                                                                                            String none = spin_corridor.getSelectedItem().toString().trim();

                                                                                                                            if (sw_facet.isChecked())
                                                                                                                            {
                                                                                                                                isfacet = "G";
                                                                                                                            }
                                                                                                                            else
                                                                                                                            {
                                                                                                                                String hor = txt_hor.getText().toString();
                                                                                                                                String ver = txt_ver.getText().toString();

                                                                                                                                if (!hor.isEmpty() && !ver.isEmpty())
                                                                                                                                {
                                                                                                                                    isfacet = "F";
                                                                                                                                }
                                                                                                                                else
                                                                                                                                {
                                                                                                                                    isfacet = "N";
                                                                                                                                }
                                                                                                                            }

                                                                                                                            Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                                                                            if (none.equalsIgnoreCase("none"))
                                                                                                                            {
                                                                                                                                Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                                                                            }
                                                                                                                            else if (order.isEmpty())
                                                                                                                            {
                                                                                                                                Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                                                                            }
                                                                                                                            else {
                                                                                                                                dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                                                                                dialog.setMessage("Are you sure all data is correct ?");
                                                                                                                                dialog.setTopTitle("Confirmation Order");
                                                                                                                                dialog.setTopTitleColor(Color.WHITE);
                                                                                                                                dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                                                                                dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                                                                                dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                                                                                dialog.setCancelable(false);

                                                                                                                                dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onClick(View v) {
                                                                                                                                        saveOrder();
                                                                                                                                    }
                                                                                                                                });

                                                                                                                                dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onClick(View v) {
                                                                                                                                        dialog.dismiss();
                                                                                                                                    }
                                                                                                                                });
                                                                                                                                dialog.show();
                                                                                                                            }
                                                                                                                        }

                                                                                                                    } catch (JSONException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                }
                                                                                                            });

                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                                //Toasty.info(getApplicationContext(), "Item Id L = " + itemIdStL, Toast.LENGTH_SHORT).show();
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                //Jika side L di-isi

                                                                //Jika Side L kosong
                                                                else
                                                                {
                                                                    Log.d("AREA 2", "Check stock R : " + itemIdStR);
                                                                    final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                                    cekStokR(itemIdStR, new VolleyOneCallBack() {
                                                                        @Override
                                                                        public void onSuccess(String result) {
                                                                            try {
                                                                                JSONObject object1 = new JSONObject(result);

                                                                                sisaStok = object1.getInt("qty_stock");

                                                                                itemSatuan.setId(1);
                                                                                itemSatuan.setDescription(descriptionStockR);
                                                                                itemSatuan.setQty(1);
                                                                                itemSatuan.setStock(sisaStok);

                                                                                Log.d("AREA 2", "Id satuan : " + itemSatuan.getId());
                                                                                Log.d("AREA 2", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                Log.d("AREA 2", "Qty Order : " + itemSatuan.getQty());
                                                                                Log.d("AREA 2", "Sisa Stok : " + sisaStok);

                                                                                lensSatuanHelper.open();
                                                                                long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                if (status > 0)
                                                                                {
                                                                                    Log.d("AREA 2" ,"Item has been added to cart");
                                                                                }

//                                                                                                            itemStokSatuan.add(itemSatuan);
                                                                                List<Data_stok_satuan> itemSatuan;
                                                                                itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                                Log.d("AREA 2", "Item Stok Satuan : " + itemSatuan.size());
                                                                                Log.d("AREA 2", "Deskripsi lensa : " + itemSatuan.get(0).getDescription());

                                                                                List<Boolean> sisanya = new ArrayList<>();

                                                                                for (int i = 0; i < itemSatuan.size(); i++)
                                                                                {
                                                                                    String item = itemSatuan.get(i).getDescription();
                                                                                    int stock = itemSatuan.get(i).getStock();
                                                                                    int qty   = itemSatuan.get(i).getQty();
                                                                                    int sisa  = stock - qty;

                                                                                    Log.d("AREA 2", "Deskripsi : " + item);
                                                                                    Log.d("AREA 2", "Stock : " + stock);
                                                                                    Log.d("AREA 2", "qty : " + qty);
                                                                                    Log.d("AREA 2", "sisa : " + sisa);

                                                                                    if (sisa < 0)
                                                                                    {
                                                                                        sisanya.add(i, false);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        sisanya.add(i, true);
                                                                                    }
                                                                                }

                                                                                boolean cek = sisanya.contains(false);

                                                                                if (cek) {
                                                                                    Log.d("Information Stok", "Ada item yang minus");

                                                                                    final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                    WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                                    dialog.setContentView(R.layout.dialog_warning);
                                                                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                                    lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                                    lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                                    lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                                    UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                                    txtTitle.setText("Stok " + itemSatuan.get(0).getDescription() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                                    ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                                    imgClose.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    });

                                                                                    dialog.show();
                                                                                    dialog.getWindow().setAttributes(lwindow);
                                                                                }
                                                                                else
                                                                                {
                                                                                    Log.d("Information Stok", "Aman lanjutkan");

                                                                                    String order = txt_orderNumber.getText().toString();
                                                                                    String none = spin_corridor.getSelectedItem().toString().trim();

                                                                                    if (sw_facet.isChecked())
                                                                                    {
                                                                                        isfacet = "G";
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        String hor = txt_hor.getText().toString();
                                                                                        String ver = txt_ver.getText().toString();

                                                                                        if (!hor.isEmpty() && !ver.isEmpty())
                                                                                        {
                                                                                            isfacet = "F";
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            isfacet = "N";
                                                                                        }
                                                                                    }

                                                                                    Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                                    if (none.equalsIgnoreCase("none"))
                                                                                    {
                                                                                        Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                                    }
                                                                                    else if (order.isEmpty())
                                                                                    {
                                                                                        Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                                        dialog.setMessage("Are you sure all data is correct ?");
                                                                                        dialog.setTopTitle("Confirmation Order");
                                                                                        dialog.setTopTitleColor(Color.WHITE);
                                                                                        dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                                        dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                                        dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                                        dialog.setCancelable(false);

                                                                                        dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                saveOrder();
                                                                                            }
                                                                                        });

                                                                                        dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        });
                                                                                        dialog.show();
                                                                                    }
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                //Jika Side L kosong

//                                                                Toasty.info(getApplicationContext(), "R Value: " + hargaR + " L Value: " + hargaL, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                    //Toasty.info(getApplicationContext(), "Item Id R = " + itemIdStR, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }

                                    //Jika Side R Kosong
                                    else
                                    {
                                        getItemStockLHandle(kodeLensa, sphl, cyll, addl, "B", new VolleyOneCallBack() {
                                            @Override
                                            public void onSuccess(String result) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(result);

                                                if (jsonObject.names().get(0).equals("error"))
                                                {
                                                    Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    itemIdStL = jsonObject.getString("item_id");
                                                    descriptionStockL = jsonObject.getString("item_desc") + " " + sphl + " " + cyll;

                                                    getItemPriceStLHandle(itemIdStL, new VolleyOneCallBack() {
                                                        @Override
                                                        public void onSuccess(String result) {

                                                        if (result != null)
                                                        {
                                                            //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                            hargaL = "True";
                                                        }
                                                        else
                                                        {
                                                            hargaL = "False";
                                                        }

                                                        Log.d("AREA 3", "Check stock : " + itemIdStL);
                                                        final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                        cekStokL(itemIdStL, new VolleyOneCallBack() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                                try {
                                                                    JSONObject object1 = new JSONObject(result);

                                                                    sisaStok = object1.getInt("qty_stock");

                                                                    itemSatuan.setId(1);
                                                                    itemSatuan.setDescription(descriptionStockL);
                                                                    itemSatuan.setQty(1);
                                                                    itemSatuan.setStock(sisaStok);

                                                                    Log.d("AREA 3", "Id satuan : " + itemSatuan.getId());
                                                                    Log.d("AREA 3", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                    Log.d("AREA 3", "Qty Order : " + itemSatuan.getQty());
                                                                    Log.d("AREA 3", "Sisa Stok : " + sisaStok);

                                                                    lensSatuanHelper.open();
                                                                    long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                    if (status > 0)
                                                                    {
                                                                        Log.d("AREA 3" ,"Item has been added to cart");
                                                                    }

                                                                    List<Data_stok_satuan> itemSatuan;
                                                                    itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                    Log.d("AREA 3", "Item Stok Satuan : " + itemSatuan.size());
                                                                    Log.d("AREA 3", "Deskripsi lensa : " + itemSatuan.get(0).getDescription());

                                                                    List<Boolean> sisanya = new ArrayList<>();

                                                                    for (int i = 0; i < itemSatuan.size(); i++)
                                                                    {
                                                                        String item = itemSatuan.get(i).getDescription();
                                                                        int stock = itemSatuan.get(i).getStock();
                                                                        int qty   = itemSatuan.get(i).getQty();
                                                                        int sisa  = stock - qty;

                                                                        Log.d("AREA 3", "Deskripsi : " + item);
                                                                        Log.d("AREA 3", "Stock : " + stock);
                                                                        Log.d("AREA 3", "qty : " + qty);
                                                                        Log.d("AREA 3", "sisa : " + sisa);

                                                                        if (sisa < 0)
                                                                        {
                                                                            sisanya.add(i, false);
                                                                        }
                                                                        else
                                                                        {
                                                                            sisanya.add(i, true);
                                                                        }
                                                                    }

                                                                    boolean cek = sisanya.contains(false);

                                                                    if (cek) {
                                                                        Log.d("Information Stok", "Ada item yang minus");

                                                                        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                        dialog.setContentView(R.layout.dialog_warning);
                                                                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                        lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                        UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                        txtTitle.setText("Stok " + itemSatuan.get(0).getDescription() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                        imgClose.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });

                                                                        dialog.show();
                                                                        dialog.getWindow().setAttributes(lwindow);
                                                                    }
                                                                    else
                                                                    {
                                                                        Log.d("Information Stok", "Aman lanjutkan");

                                                                        String order = txt_orderNumber.getText().toString();
                                                                        String none = spin_corridor.getSelectedItem().toString().trim();

                                                                        if (sw_facet.isChecked())
                                                                        {
                                                                            isfacet = "G";
                                                                        }
                                                                        else
                                                                        {
                                                                            String hor = txt_hor.getText().toString();
                                                                            String ver = txt_ver.getText().toString();

                                                                            if (!hor.isEmpty() && !ver.isEmpty())
                                                                            {
                                                                                isfacet = "F";
                                                                            }
                                                                            else
                                                                            {
                                                                                isfacet = "N";
                                                                            }
                                                                        }

                                                                        Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                        if (none.equalsIgnoreCase("none"))
                                                                        {
                                                                            Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                        }
                                                                        else if (order.isEmpty())
                                                                        {
                                                                            Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                        }
                                                                        else {
                                                                            dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                            dialog.setMessage("Are you sure all data is correct ?");
                                                                            dialog.setTopTitle("Confirmation Order");
                                                                            dialog.setTopTitleColor(Color.WHITE);
                                                                            dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                            dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                            dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                            dialog.setCancelable(false);

                                                                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    saveOrder();
                                                                                }
                                                                            });

                                                                            dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });
                                                                            dialog.show();
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        }
                                                    });
                                                }
                                                    //Toasty.info(getApplicationContext(), "Item Id L = " + itemIdStL, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    //Jika Side R Kosong
                                }
                                else
                                {
                                    //Jika kedua Side RL Diisi dan terdapat Sisi R dan L
                                    if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                                    {
                                        getItemStockRHandle(kodeLensa, sphr, cylr, addr, "R", new VolleyOneCallBack() {
                                            @Override
                                            public void onSuccess(String result) {
                                            try
                                            {
                                                JSONObject jsonObject = new JSONObject(result);

                                                if (jsonObject.names().get(0).equals("error"))
                                                {
                                                    Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    itemIdStR = jsonObject.getString("item_id");
                                                    descriptionStockR = jsonObject.getString("item_desc") + " R " + sphr + " " + cylr + " ADD " + addr;

                                                    getItemPriceStRHandle(itemIdStR, new VolleyOneCallBack() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                        if (result != null)
                                                        {
                                                                    //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                            hargaR = "True";
                                                        }
                                                        else
                                                        {
                                                            hargaR = "False";
                                                        }

                                                        if (!sphl.isEmpty() | !cyll.isEmpty() | !addl.isEmpty())
                                                        {
                                                            getItemStockLHandle(kodeLensa, sphl, cyll, addl, "L", new VolleyOneCallBack() {
                                                                @Override
                                                                public void onSuccess(String result) {
                                                                try
                                                                {
                                                                    JSONObject jsonObject = new JSONObject(result);

                                                                    if (jsonObject.names().get(0).equals("error"))
                                                                    {
                                                                        Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else
                                                                    {
                                                                        itemIdStL = jsonObject.getString("item_id");
                                                                        descriptionStockL = jsonObject.getString("item_desc") + " L " + sphl + " " + cyll + " ADD " + addl;

                                                                        getItemPriceStLHandle(itemIdStL, new VolleyOneCallBack() {
                                                                            @Override
                                                                            public void onSuccess(String result) {
                                                                            if (result != null)
                                                                            {
                                                                                //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                                                hargaL = "True";
                                                                            }
                                                                            else
                                                                            {
                                                                                hargaL = "False";
                                                                            }

                                                                            Log.d("AREA 4", "Check stock R : " + itemIdStR);
                                                                            Log.d("AREA 4", "Check stock L : " + itemIdStL);

                                                                            final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                                            cekStokR(itemIdStR, new VolleyOneCallBack() {
                                                                                @Override
                                                                                public void onSuccess(String result) {
                                                                                    try {
                                                                                        JSONObject object1 = new JSONObject(result);

                                                                                        sisaStok = object1.getInt("qty_stock");

                                                                                        itemSatuan.setId(1);
                                                                                        itemSatuan.setDescription(descriptionStockR);
                                                                                        itemSatuan.setQty(1);
                                                                                        itemSatuan.setStock(sisaStok);

                                                                                        Log.d("AREA 4", "Id satuan : " + itemSatuan.getId());
                                                                                        Log.d("AREA 4", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                        Log.d("AREA 4", "Qty Order : " + itemSatuan.getQty());
                                                                                        Log.d("AREA 4", "Sisa Stok : " + sisaStok);

                                                                                        lensSatuanHelper.open();
                                                                                        long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                        if (status > 0)
                                                                                        {
                                                                                            Log.d("AREA 4" ,"Item has been added to cart");
                                                                                        }

                                                                                        cekStokL(itemIdStL, new VolleyOneCallBack() {
                                                                                            @Override
                                                                                            public void onSuccess(String result) {
                                                                                                try {
                                                                                                    JSONObject object2 = new JSONObject(result);

                                                                                                    sisaStok = object2.getInt("qty_stock");

                                                                                                    itemSatuan.setId(2);
                                                                                                    itemSatuan.setDescription(descriptionStockL);
                                                                                                    itemSatuan.setQty(1);
                                                                                                    itemSatuan.setStock(sisaStok);

                                                                                                    Log.d("AREA 4", "Id satuan : " + itemSatuan.getId());
                                                                                                    Log.d("AREA 4", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                                    Log.d("AREA 4", "Qty Order : " + itemSatuan.getQty());
                                                                                                    Log.d("AREA 4", "Sisa Stok : " + sisaStok);

                                                                                                    lensSatuanHelper.open();
                                                                                                    long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                                    if (status > 0)
                                                                                                    {
                                                                                                        Log.d("AREA 4" ,"Item has been added to cart");
                                                                                                    }

                                                                                                    List<Data_stok_satuan> itemSatuan;
                                                                                                    itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                                                    Log.d("AREA 4", "Item Stok Satuan : " + itemSatuan.size());

                                                                                                    List<Boolean> sisanya = new ArrayList<>();

                                                                                                    for (int i = 0; i < itemSatuan.size(); i++)
                                                                                                    {
                                                                                                        String item = itemSatuan.get(i).getDescription();
                                                                                                        int stock = itemSatuan.get(i).getStock();
                                                                                                        int qty   = itemSatuan.get(i).getQty();
                                                                                                        int sisa  = stock - qty;

                                                                                                        Log.d("AREA 4", "Deskripsi : " + item);
                                                                                                        Log.d("AREA 4", "Stock : " + stock);
                                                                                                        Log.d("AREA 4", "qty : " + qty);
                                                                                                        Log.d("AREA 4", "sisa : " + sisa);

                                                                                                        if (sisa < 0)
                                                                                                        {
                                                                                                            sisanya.add(i, false);
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            sisanya.add(i, true);
                                                                                                        }
                                                                                                    }

                                                                                                    boolean cek = sisanya.contains(false);

                                                                                                    if (cek) {
                                                                                                        Log.d("Information Stok", "Ada item yang minus");

                                                                                                        StringBuilder log = new StringBuilder();

                                                                                                        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                                        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                                                        dialog.setContentView(R.layout.dialog_warning);
                                                                                                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                                                        lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                                                        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                                                        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                                                        int stockR  = itemSatuan.get(0).getStock();
                                                                                                        int qtyR    = itemSatuan.get(0).getQty();
                                                                                                        int sisaR   = stockR - qtyR;

                                                                                                        int stockL  = itemSatuan.get(1).getStock();
                                                                                                        int qtyL    = itemSatuan.get(1).getQty();
                                                                                                        int sisaL   = stockL - qtyL;

                                                                                                        if (sisaR < 0 && sisaL < 0)
                                                                                                        {
                                                                                                            log.append(itemSatuan.get(0).getDescription());
                                                                                                            log.append(" & ");
                                                                                                            log.append(itemSatuan.get(1).getDescription());
                                                                                                        }
                                                                                                        else if (sisaR < 0)
                                                                                                        {
                                                                                                            log.append(itemSatuan.get(0).getDescription());
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            log.append(itemSatuan.get(1).getDescription());
                                                                                                        }

                                                                                                        UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                                                        txtTitle.setText("Stok " + log.toString() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                                                        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                                                        imgClose.setOnClickListener(new View.OnClickListener() {
                                                                                                            @Override
                                                                                                            public void onClick(View v) {
                                                                                                                dialog.dismiss();
                                                                                                            }
                                                                                                        });

                                                                                                        dialog.show();
                                                                                                        dialog.getWindow().setAttributes(lwindow);
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                        Log.d("Information Stok", "Aman lanjutkan");

                                                                                                        String order = txt_orderNumber.getText().toString();
                                                                                                        String none = spin_corridor.getSelectedItem().toString().trim();

                                                                                                        if (sw_facet.isChecked())
                                                                                                        {
                                                                                                            isfacet = "G";
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            String hor = txt_hor.getText().toString();
                                                                                                            String ver = txt_ver.getText().toString();

                                                                                                            if (!hor.isEmpty() && !ver.isEmpty())
                                                                                                            {
                                                                                                                isfacet = "F";
                                                                                                            }
                                                                                                            else
                                                                                                            {
                                                                                                                isfacet = "N";
                                                                                                            }
                                                                                                        }

                                                                                                        Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                                                        if (none.equalsIgnoreCase("none"))
                                                                                                        {
                                                                                                            Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                                                        }
                                                                                                        else if (order.isEmpty())
                                                                                                        {
                                                                                                            Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                                                        }
                                                                                                        else {
                                                                                                            dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                                                            dialog.setMessage("Are you sure all data is correct ?");
                                                                                                            dialog.setTopTitle("Confirmation Order");
                                                                                                            dialog.setTopTitleColor(Color.WHITE);
                                                                                                            dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                                                            dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                                                            dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                                                            dialog.setCancelable(false);

                                                                                                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(View v) {
                                                                                                                    saveOrder();
                                                                                                                }
                                                                                                            });

                                                                                                            dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(View v) {
                                                                                                                    dialog.dismiss();
                                                                                                                }
                                                                                                            });
                                                                                                            dialog.show();
                                                                                                        }
                                                                                                    }

                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }

                                                                                    }
                                                                                });
                                                                             }
                                                                            });
                                                                        }
                                                                                //Toasty.info(getApplicationContext(), "Item Id L = " + itemIdStL, Toast.LENGTH_SHORT).show();
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        }
                                                                    });
                                                                }
                                                                //Jika kedua Side RL Diisi

                                                                //Jika Side L Tidak Diisi
                                                                else
                                                                {
                                                                    Log.d("AREA 5", "Check stock R : " + itemIdStR);
                                                                    final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                                    cekStokR(itemIdStR, new VolleyOneCallBack() {
                                                                        @Override
                                                                        public void onSuccess(String result) {
                                                                            try {
                                                                                JSONObject object1 = new JSONObject(result);

                                                                                sisaStok = object1.getInt("qty_stock");

                                                                                itemSatuan.setId(1);
                                                                                itemSatuan.setDescription(descriptionStockR);
                                                                                itemSatuan.setQty(1);
                                                                                itemSatuan.setStock(sisaStok);

                                                                                Log.d("AREA 5", "Id satuan : " + itemSatuan.getId());
                                                                                Log.d("AREA 5", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                                Log.d("AREA 5", "Qty Order : " + itemSatuan.getQty());
                                                                                Log.d("AREA 5", "Sisa Stok : " + sisaStok);

                                                                                lensSatuanHelper.open();
                                                                                long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                                if (status > 0)
                                                                                {
                                                                                    Log.d("AREA 5" ,"Item has been added to cart");
                                                                                }

//                                                                                                            itemStokSatuan.add(itemSatuan);
                                                                                List<Data_stok_satuan> itemSatuan;
                                                                                itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                                Log.d("AREA 5", "Item Stok Satuan : " + itemSatuan.size());
                                                                                Log.d("AREA 5", "Deskripsi lensa : " + itemSatuan.get(0).getDescription());

                                                                                List<Boolean> sisanya = new ArrayList<>();

                                                                                for (int i = 0; i < itemSatuan.size(); i++)
                                                                                {
                                                                                    String item = itemSatuan.get(i).getDescription();
                                                                                    int stock = itemSatuan.get(i).getStock();
                                                                                    int qty   = itemSatuan.get(i).getQty();
                                                                                    int sisa  = stock - qty;

                                                                                    Log.d("AREA 5", "Deskripsi : " + item);
                                                                                    Log.d("AREA 5", "Stock : " + stock);
                                                                                    Log.d("AREA 5", "qty : " + qty);
                                                                                    Log.d("AREA 5", "sisa : " + sisa);

                                                                                    if (sisa < 0)
                                                                                    {
                                                                                        sisanya.add(i, false);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        sisanya.add(i, true);
                                                                                    }
                                                                                }

                                                                                boolean cek = sisanya.contains(false);

                                                                                if (cek) {
                                                                                    Log.d("Information Stok", "Ada item yang minus");

                                                                                    final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                    WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                                    dialog.setContentView(R.layout.dialog_warning);
                                                                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                                    lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                                    lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                                    lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                                    UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                                    txtTitle.setText("Stok " + itemSatuan.get(0).getDescription() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                                    ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                                    imgClose.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    });

                                                                                    dialog.show();
                                                                                    dialog.getWindow().setAttributes(lwindow);
                                                                                }
                                                                                else
                                                                                {
                                                                                    Log.d("Information Stok", "Aman lanjutkan");

                                                                                    String order = txt_orderNumber.getText().toString();
                                                                                    String none = spin_corridor.getSelectedItem().toString().trim();

                                                                                    if (sw_facet.isChecked())
                                                                                    {
                                                                                        isfacet = "G";
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        String hor = txt_hor.getText().toString();
                                                                                        String ver = txt_ver.getText().toString();

                                                                                        if (!hor.isEmpty() && !ver.isEmpty())
                                                                                        {
                                                                                            isfacet = "F";
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            isfacet = "N";
                                                                                        }
                                                                                    }

                                                                                    Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                                    if (none.equalsIgnoreCase("none"))
                                                                                    {
                                                                                        Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                                    }
                                                                                    else if (order.isEmpty())
                                                                                    {
                                                                                        Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                                        dialog.setMessage("Are you sure all data is correct ?");
                                                                                        dialog.setTopTitle("Confirmation Order");
                                                                                        dialog.setTopTitleColor(Color.WHITE);
                                                                                        dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                                        dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                                        dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                                        dialog.setCancelable(false);

                                                                                        dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                saveOrder();
                                                                                            }
                                                                                        });

                                                                                        dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        });
                                                                                        dialog.show();
                                                                                    }
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                //Jika Side L Tidak Diisi

//                                                                Toasty.info(getApplicationContext(), "R Value: " + hargaR + " L Value: " + hargaL, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                    //Toasty.info(getApplicationContext(), "Item Id R = " + itemIdStR, Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        getItemStockLHandle(kodeLensa, sphl, cyll, addl, "L", new VolleyOneCallBack() {
                                            @Override
                                            public void onSuccess(String result) {
                                            try
                                            {
                                                JSONObject jsonObject = new JSONObject(result);

                                                if (jsonObject.names().get(0).equals("error"))
                                                {
                                                    Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    itemIdStL = jsonObject.getString("item_id");
                                                    descriptionStockL = jsonObject.getString("item_desc") + " L " + sphl + " " + cyll + " ADD " + addl;

                                                    getItemPriceStLHandle(itemIdStL, new VolleyOneCallBack() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                        if (result != null)
                                                        {
                                                                    //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                            hargaL = "True";
                                                        }
                                                        else
                                                        {
                                                            hargaL = "False";
                                                        }

                                                        Log.d("AREA 6", "Check stock : " + itemIdStL);
                                                        final Data_stok_satuan itemSatuan = new Data_stok_satuan();

                                                        cekStokL(itemIdStL, new VolleyOneCallBack() {
                                                            @Override
                                                            public void onSuccess(String result) {
                                                                try
                                                                {
                                                                    JSONObject object1 = new JSONObject(result);

                                                                    sisaStok = object1.getInt("qty_stock");

                                                                    itemSatuan.setId(1);
                                                                    itemSatuan.setDescription(descriptionStockL);
                                                                    itemSatuan.setQty(1);
                                                                    itemSatuan.setStock(sisaStok);

                                                                    Log.d("AREA 6", "Id satuan : " + itemSatuan.getId());
                                                                    Log.d("AREA 6", "Deskripsi satuan : " + itemSatuan.getDescription());
                                                                    Log.d("AREA 6", "Qty Order : " + itemSatuan.getQty());
                                                                    Log.d("AREA 6", "Sisa Stok : " + sisaStok);

                                                                    lensSatuanHelper.open();
                                                                    long status = lensSatuanHelper.insertLensSatuan(itemSatuan);

                                                                    if (status > 0)
                                                                    {
                                                                        Log.d("AREA 6" ,"Item has been added to cart");
                                                                    }

                                                                    List<Data_stok_satuan> itemSatuan;
                                                                    itemSatuan = lensSatuanHelper.getAllLensSatuan();
                                                                    Log.d("AREA 6", "Item Stok Satuan : " + itemSatuan.size());
                                                                    Log.d("AREA 6", "Deskripsi lensa : " + itemSatuan.get(0).getDescription());

                                                                    List<Boolean> sisanya = new ArrayList<>();

                                                                    for (int i = 0; i < itemSatuan.size(); i++)
                                                                    {
                                                                        String item = itemSatuan.get(i).getDescription();
                                                                        int stock = itemSatuan.get(i).getStock();
                                                                        int qty   = itemSatuan.get(i).getQty();
                                                                        int sisa  = stock - qty;

                                                                        Log.d("AREA 6", "Deskripsi : " + item);
                                                                        Log.d("AREA 6", "Stock : " + stock);
                                                                        Log.d("AREA 6", "qty : " + qty);
                                                                        Log.d("AREA 6", "sisa : " + sisa);

                                                                        if (sisa < 0)
                                                                        {
                                                                            sisanya.add(i, false);
                                                                        }
                                                                        else
                                                                        {
                                                                            sisanya.add(i, true);
                                                                        }
                                                                    }

                                                                    boolean cek = sisanya.contains(false);

                                                                    if (cek) {
                                                                        Log.d("Information Stok", "Ada item yang minus");

                                                                        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                                                                        dialog.setContentView(R.layout.dialog_warning);
                                                                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                                                        lwindow.copyFrom(dialog.getWindow().getAttributes());
                                                                        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                                                                        UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                                                                        txtTitle.setText("Stok " + itemSatuan.get(0).getDescription() + " kosong. Harap hubungi Customer Service kami di (021) 4610154 untuk proses order lebih lanjut");

                                                                        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                                                                        imgClose.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });

                                                                        dialog.show();
                                                                        dialog.getWindow().setAttributes(lwindow);
                                                                    }
                                                                    else
                                                                    {
                                                                        Log.d("Information Stok", "Aman lanjutkan");

                                                                        String order = txt_orderNumber.getText().toString();
                                                                        String none = spin_corridor.getSelectedItem().toString().trim();

                                                                        if (sw_facet.isChecked())
                                                                        {
                                                                            isfacet = "G";
                                                                        }
                                                                        else
                                                                        {
                                                                            String hor = txt_hor.getText().toString();
                                                                            String ver = txt_ver.getText().toString();

                                                                            if (!hor.isEmpty() && !ver.isEmpty())
                                                                            {
                                                                                isfacet = "F";
                                                                            }
                                                                            else
                                                                            {
                                                                                isfacet = "N";
                                                                            }
                                                                        }

                                                                        Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                                                        if (none.equalsIgnoreCase("none"))
                                                                        {
                                                                            Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                                                        }
                                                                        else if (order.isEmpty())
                                                                        {
                                                                            Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                                                        }
                                                                        else
                                                                        {
                                                                            dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                                                            dialog.setMessage("Are you sure all data is correct ?");
                                                                            dialog.setTopTitle("Confirmation Order");
                                                                            dialog.setTopTitleColor(Color.WHITE);
                                                                            dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                                                            dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                                                            dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                                                            dialog.setCancelable(false);

                                                                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    saveOrder();
                                                                                }
                                                                            });

                                                                            dialog.setNegativeButton("No", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });
                                                                            dialog.show();
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        }
                                                        });
                                                    }
                                                    //Toasty.info(getApplicationContext(), "Item Id L = " + itemIdStL, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                {
                    //Toasty.success(getApplicationContext(), "RX Lens", Toast.LENGTH_SHORT).show();
                    Log.d(FormOrderLensActivity.class.getSimpleName(), "RX Lens");

                    if (cylr.equals("null") || cylr.contentEquals("0.00") || cylr.isEmpty())
                    {
                        kodeitemR = "SPH";
                    }
                    else if (sphr.equals("null") || sphr.contentEquals("0.00") || sphr.isEmpty())
                    {
                        kodeitemR = "CYL";
                    }
                    else
                    {
                        kodeitemR = "SPH";
                    }

                    if (cyll.equals("null") || cyll.contentEquals("0.00") || cyll.isEmpty())
                    {
                        kodeitemL = "SPH";
                    }
                    else if (sphl.equals("null") || sphl.contentEquals("0.00") || sphl.isEmpty())
                    {
                        kodeitemL = "CYL";
                    }
                    else
                    {
                        kodeitemL = "SPH";
                    }

                    if (!addr.isEmpty() && !cylr.isEmpty() && !axsr.isEmpty())
                    {
                        final String finalKoridor = koridor;
                        checkItemIdR(kodeLensa, koridor, kodeitemR, new VolleyOneCallBack() {
                            @Override
                            public void onSuccess(String result) {
                                checkItemPriceR(result, new VolleyOneCallBack() {
                                    @Override
                                    public void onSuccess(String result) {
                                        if (result != null)
                                        {
                                            //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                            hargaR = "True";
                                        }
                                        else
                                        {
                                            hargaR = "False";
                                        }


                                        if (!addl.isEmpty() && !cyll.isEmpty() && !axsl.isEmpty())
                                        {
                                            checkItemIdL(kodeLensa, finalKoridor, kodeitemL, new VolleyOneCallBack() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    checkItemPriceL(result, new VolleyOneCallBack() {
                                                        @Override
                                                        public void onSuccess(String result) {
                                                            if (result != null)
                                                            {
                                                                //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                                                hargaL = "True";
                                                            }
                                                            else
                                                            {
                                                                hargaL = "False";
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }

//                                        Toasty.info(getApplicationContext(), "R Value: " + hargaR + " L Value: " + hargaL, Toast.LENGTH_SHORT).show();
                                        String order = txt_orderNumber.getText().toString();
                                        String none = spin_corridor.getSelectedItem().toString().trim();

                                        if (sw_facet.isChecked())
                                        {
                                            isfacet = "G";
                                        }
                                        else
                                        {
                                            String hor = txt_hor.getText().toString();
                                            String ver = txt_ver.getText().toString();

                                            if (!hor.isEmpty() && !ver.isEmpty())
                                            {
                                                isfacet = "F";
                                            }
                                            else
                                            {
                                                isfacet = "N";
                                            }
                                        }

                                        Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                        if (none.equalsIgnoreCase("none"))
                                        {
                                            Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                        }
                                        else if (order.isEmpty())
                                        {
                                            Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                        }
                                        else
                                        {
                                            dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                            dialog.setMessage("Are you sure all data is correct ?");
                                            dialog.setTopTitle("Confirmation Order");
                                            dialog.setTopTitleColor(Color.WHITE);
                                            dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                            dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                            dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                            dialog.setCancelable(false);

                                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    saveOrder();
                                                }
                                            });

                                            dialog.setNegativeButton("No", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        checkItemIdL(kodeLensa, koridor, kodeitemL, new VolleyOneCallBack() {
                            @Override
                            public void onSuccess(String result) {
                                checkItemPriceL(result, new VolleyOneCallBack() {
                                    @Override
                                    public void onSuccess(String result) {
                                        if (result != null)
                                        {
                                            //Toasty.info(getApplicationContext(), "Price R: " + result, Toast.LENGTH_SHORT).show();
                                            hargaL = "True";
                                        }
                                        else
                                        {
                                            hargaL = "False";
                                        }

//                                        Toasty.info(getApplicationContext(), "R Value: " + hargaR + " L Value: " + hargaL, Toast.LENGTH_SHORT).show();

                                        String order = txt_orderNumber.getText().toString();
                                        String none = spin_corridor.getSelectedItem().toString().trim();

                                        if (sw_facet.isChecked())
                                        {
                                            isfacet = "G";
                                        }
                                        else
                                        {
                                            String hor = txt_hor.getText().toString();
                                            String ver = txt_ver.getText().toString();

                                            if (!hor.isEmpty() && !ver.isEmpty())
                                            {
                                                isfacet = "F";
                                            }
                                            else
                                            {
                                                isfacet = "N";
                                            }
                                        }

                                        Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();

                                        if (none.equalsIgnoreCase("none"))
                                        {
                                            Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
                                        }
                                        else if (order.isEmpty())
                                        {
                                            Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
                                        }
                                        else
                                        {
                                            dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
                                            dialog.setMessage("Are you sure all data is correct ?");
                                            dialog.setTopTitle("Confirmation Order");
                                            dialog.setTopTitleColor(Color.WHITE);
                                            dialog.setTopColorRes(R.color.bootstrap_brand_info);
                                            dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
                                            dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
                                            dialog.setCancelable(false);

                                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    saveOrder();
                                                }
                                            });

                                            dialog.setNegativeButton("No", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }

//                String order = txt_orderNumber.getText().toString();
//                String none = spin_corridor.getSelectedItem().toString().trim();
//
//                if (sw_facet.isChecked())
//                {
//                    isfacet = "G";
//                }
//                else
//                {
//                    String hor = txt_hor.getText().toString();
//                    String ver = txt_ver.getText().toString();
//
//                    if (!hor.isEmpty() && !ver.isEmpty())
//                    {
//                        isfacet = "F";
//                    }
//                    else
//                    {
//                        isfacet = "N";
//                    }
//                }
//
////                checkFacet();
//                //Toasty.info(getApplicationContext(), none, Toast.LENGTH_SHORT, true).show();
//                Toasty.info(getApplicationContext(), "Facet : " + isfacet, Toast.LENGTH_SHORT).show();
//
//                if (none.equalsIgnoreCase("none"))
//                {
//                    Toasty.error(getApplicationContext(), "Please check corridor !!!", Toast.LENGTH_LONG, true).show();
//                }
//                else if (order.isEmpty())
//                {
//                    Toasty.error(getApplicationContext(), "Please fill order number first", Toast.LENGTH_SHORT, true).show();
//                }
//                else
//                {
//                    dialog = new LovelyStandardDialog(FormOrderLensActivity.this);
//                    dialog.setMessage("Are you sure all data is correct ?");
//                    dialog.setTopTitle("Confirmation Order");
//                    dialog.setTopTitleColor(Color.WHITE);
//                    dialog.setTopColorRes(R.color.bootstrap_brand_info);
//                    dialog.setPositiveButtonColorRes(R.color.bootstrap_brand_success);
//                    dialog.setNegativeButtonColorRes(R.color.bootstrap_brand_danger);
//                    dialog.setCancelable(false);
//
//                    dialog.setPositiveButton("Yes", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            saveOrder();
//                        }
//                    });
//
//                    dialog.setNegativeButton("No", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                }
            }
        });
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ||
                        keyCode == KeyEvent.KEYCODE_APP_SWITCH)
                {
                    //donothing
                }
                return false;
            }
        });
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
                finish();
            }
        });
        dialog.show();
    }

    private void confirmOrder()
    {
        UniversalFontTextView lbl_refnumber, lbl_patientname, lbl_remark;
        UniversalFontTextView lbl_lenses, lbl_sphr, lbl_cylr, lbl_axsr, lbl_addr, lbl_prisr, lbl_sphl, lbl_cyll, lbl_axsl,
                                lbl_addl, lbl_prisl, lbl_pdl, lbl_pds, lbl_colortint, lbl_infofacet;
        UniversalFontTextView lbl_mpdr, lbl_mpdl, lbl_segh;
        UniversalFontTextView lbl_framemodel, lbl_dbl, lbl_hora, lbl_verb, lbl_framecode;

        final BootstrapButton btn_editorder, btn_sentorder;

        AlertDialog.Builder builder = new AlertDialog.Builder(FormOrderLensActivity.this);

        LayoutInflater inflater = FormOrderLensActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.form_verify_order, null);
        builder.setView(dialogView);

        /*Area order details */
        lbl_refnumber       = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblrefnumber);
        lbl_patientname     = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblpatientname);
        lbl_remark          = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblremark);

        /*Area lens details */
        lbl_lenses          = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lbllenses);
        lbl_sphr            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblsphr);
        lbl_cylr            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblcylr);
        lbl_axsr            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblaxsr);
        lbl_addr            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lbladdr);
        lbl_prisr           = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblprisr);
        lbl_sphl            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblsphl);
        lbl_cyll            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblcyll);
        lbl_axsl            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblaxsl);
        lbl_addl            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lbladdl);
        lbl_prisl           = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblprisl);
        lbl_pdl             = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblpdl);
        lbl_pds             = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblpds);
        lbl_colortint       = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblcolortint);

        /*Area facet details */
        lbl_mpdr            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblmpdr);
        lbl_mpdl            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblmpdl);
        lbl_segh            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblsegh);

        /*Area frame details */
        lbl_framemodel      = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblframemodel);
        lbl_dbl             = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lbldbl);
        lbl_hora            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblhora);
        lbl_verb            = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblverb);
        lbl_framecode       = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblframecode);
        lbl_infofacet       = (UniversalFontTextView) dialogView.findViewById(R.id.form_verifyorder_lblinfofacet);

        /*Area button sent and edit*/
        btn_editorder       = (BootstrapButton) dialogView.findViewById(R.id.form_verifyorder_btneditorder);
        btn_sentorder       = (BootstrapButton) dialogView.findViewById(R.id.form_verifyorder_btnsentorder);

        /*Show data in area order details*/
        String wrap         = txt_wrap.getText().toString();
        String phantose     = txt_phantose.getText().toString();
        String vertex       = txt_vertex.getText().toString();
        String facettrl     = isfacet.replace(",", "");

        String order_number = txt_orderNumber.getText().toString();
        String patient_name = txt_patientName.getText().toString();
        ArrayList <String> info_list = new ArrayList<>();
        info_list.add(txt_orderInformation.getText().toString());

        if (!wrap.isEmpty())
        {
            info_list.add(" wrap " + wrap);
            //lbl_remark.setText(order_info);
        }
        else
        {
            if (info_list.contains(" wrap "))
            {
                info_list.remove(info_list.indexOf(" wrap "));
            }
        }

        if (!phantose.isEmpty())
        {
            info_list.add(" panto " + phantose);
        }
        else
        {
            if (info_list.contains(" panto "))
            {
                info_list.remove(info_list.indexOf(" panto "));
            }
        }

        if (!vertex.isEmpty())
        {
            info_list.add(" vertex " + vertex);
        }
        else
        {
            if (info_list.contains(" vertex "))
            {
                info_list.remove(info_list.indexOf(" vertex "));
            }
        }

        if (!facettrl.isEmpty())
        {
            info_list.add(facettrl);
        }
        else
        {
            if (info_list.contains(" facettrl "))
            {
                info_list.remove(info_list.indexOf(" facettrl "));
            }
        }

        //Toast.makeText(getApplicationContext(), data_corridor.toString(), Toast.LENGTH_SHORT).show();

        if (corridor != null)
        {
            info_list.add(" corridor " + corridor);
        }
        else
        {
            if (info_list.contains("corridor"))
            {
                info_list.remove(info_list.indexOf("corridor"));
            }
        }

        if (color_tint.trim().equalsIgnoreCase("none"))
        {
            color_tint = "";
        }

        lbl_refnumber.setText(order_number);
        lbl_patientname.setText(patient_name);
        lbl_remark.setText(info_list.toString().replace("[", "").replace("]", "").replace(",", ""));

        /*Show data in area lens details*/
        String lenses       = "(" + txt_lenstype.getText().toString() + ") " + txt_lensdesc.getText().toString();
        String sphr         = txt_sphr.getText().toString();
        String cylr         = txt_cylr.getText().toString();
        String axsr         = txt_axsr.getText().toString();
        String addr         = txt_addr.getText().toString();
        String prisr        = "0.0";
        String sphl         = txt_sphl.getText().toString();
        String cyll         = txt_cyll.getText().toString();
        String axsl         = txt_axsl.getText().toString();
        String addl         = txt_addl.getText().toString();
        String prisl        = "0.0";
        String pdl          = "";
        String pds          = "";
        String colortint    = color_tint.replace(",", "");

        lbl_lenses.setText(lenses);
        lbl_sphr.setText(sphr);
        lbl_cylr.setText(cylr);
        lbl_axsr.setText(axsr);
        lbl_addr.setText(addr);
        lbl_prisr.setText(prisr);
        lbl_sphl.setText(sphl);
        lbl_cyll.setText(cyll);
        lbl_axsl.setText(axsl);
        lbl_addl.setText(addl);
        lbl_prisl.setText(prisl);
        lbl_pdl.setText(pdl);
        lbl_pds.setText(pds);
        lbl_colortint.setText(colortint);

        /* Show data in area facet details*/
        String mpdr         = txt_mpdr.getText().toString();
        String mpdl         = txt_mpdl.getText().toString();
        String segh         = txt_segh.getText().toString();
        String infofacet    = txt_infofacet.getText().toString();

        lbl_mpdr.setText(mpdr);
        lbl_mpdl.setText(mpdl);
        lbl_segh.setText(segh);
        lbl_infofacet.setText(infofacet);

        /* Show data in area frame details*/
        String framemodel   = spin_framemodel.getText().toString();
        String dbl          = txt_dbl.getText().toString();
        String hora         = txt_hor.getText().toString();
        String verb         = txt_ver.getText().toString();
        String framecode    = txt_framebrand.getText().toString();

        lbl_framemodel.setText(framemodel);
        lbl_dbl.setText(dbl);
        lbl_hora.setText(hora);
        lbl_verb.setText(verb);
        lbl_framecode.setText(framecode);

        if (cb_sideR.isChecked() && cb_sideL.isChecked() && (!sphr.isEmpty() && !sphl.isEmpty() || !addr.isEmpty() &&
            !addl.isEmpty()))
        {
            flagPasang = "2";
        }
        else
        {
            flagPasang = "1";
        }

        //Toasty.info(getApplicationContext(), "Jumlah PCS = " + flagPasang, Toast.LENGTH_SHORT).show();

        //Cek kategory lensa (Stock atau RX)
        if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
        {
            String kodeLensa = txt_lenstype.getText().toString();
            String koridor   = spin_corridor.getSelectedItem().toString().trim();
            String frameType = spin_framemodel.getText().toString();
            String kodeitemR, kodeitemL;

            Toasty.warning(getApplicationContext(), "Koridor : " + koridor, Toast.LENGTH_SHORT).show();

            if (cylr.equals("null") || cylr.contentEquals("0.00") || cylr.isEmpty())
            {
                kodeitemR = "SPH";
            }
            else if (sphr.equals("null") || sphr.contentEquals("0.00") || sphr.isEmpty())
            {
                kodeitemR = "CYL";
            }
            else
            {
                kodeitemR = "SPH";
            }

            if (cyll.equals("null") || cyll.contentEquals("0.00") || cyll.isEmpty())
            {
                kodeitemL = "SPH";
            }
            else if (sphl.equals("null") || sphl.contentEquals("0.00") || sphl.isEmpty())
            {
                kodeitemL = "CYL";
            }
            else
            {
                kodeitemL = "SPH";
            }

            if (koridor.equals("Select Corridor") || koridor.contentEquals("Select Corridor") || koridor.contains("Select Corridor"))
            {
                koridor = "";
            }

            if (!sphr.isEmpty() | !addr.isEmpty())
            {
                getItemIdR(kodeLensa, koridor, kodeitemR);
//                getItemIdR(kodeLensa, koridor, "SPH");
            }
            else
            {
                itemPriceR = 0;
                itemCodeR = "";
            }

            if (!sphl.isEmpty() | !addl.isEmpty())
            {
                getItemIdL(kodeLensa, koridor, kodeitemL);
            }
            else
            {
                itemPriceL = 0;
                itemCodeL = "";
            }

            //Get Price Facet
            if (isfacet.equals("G") | isfacet.contentEquals("G") | isfacet.contains("G"))
            {
                //Parameter frameType, typeLensa, lensDiv
//                getFacetItem(frameType, lensDiv, typeLensa);
                getLensFacet(kodeLensa, frameType);
                itemWeight = 233;
                //Toasty.info(getApplicationContext(), frameType, Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toasty.warning(getApplicationContext(), "Tidak mengaktifkan FACET TRL", Toast.LENGTH_SHORT).show();
                itemFacetPrice = 0;
                itemWeight = 62;
            }

            //Counter Weight
            if (countTemp > 0)
            {
                totalWeight = autoSum + itemWeight;
                //Toasty.info(getApplicationContext(), "Total weight = " + totalWeight, Toast.LENGTH_SHORT).show();

                if (totalWeight > limiter)
                {
                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                    String todayDate = sdf3.format(calendar.getTime());
                    //db.moveToRealTbl();
                    //db.deleteAllOrder();
                    deleteOrderTemp(userinfo, todayDate);
                    flagShipping = "1";
                }
                else
                {
                    flagShipping = "0";
                }
            }
            else
            {
                flagShipping = "1";
            }

            //Get Price Tinting
            if (color_tint.isEmpty() | color_tint.equals("") | colortint.contentEquals(""))
            {
                itemTintPrice = 0;
            }
            else
            {
                if (typeLensa.contentEquals("nikon") | typeLensa.equals("nikon") | typeLensa.contains("nikon"))
                {
                    if (purchaseLens.contentEquals("T") | purchaseLens.equals("T") | typeLensa.contains("T"))
                    {
                        //Cari harga sesuai purchaselens (T) Untuk TRL
                        getOtherTintingPrice("NIKON TRL");
                    }
                    else if (purchaseLens.contentEquals("IT") | purchaseLens.equals("IT") | typeLensa.contains("IT"))
                    {
                        //Cari harga sesuai purchaselens (IT) Untuk INDENT THAILAND
                        getOtherTintingPrice("NIKON THAILAND");
                    }
                    else if (purchaseLens.contentEquals("IJ") | purchaseLens.equals("IJ") | typeLensa.contains("IJ"))
                    {
                        //cari harga sesuai purchaselens (IJ) Untuk INDENT JEPANG
                        getOtherTintingPrice("NIKON JEPANG");
                    }
                }
                else
                {
                    getOtherTintingPrice(typeLensa);
                }
            }
            //Toasty.info(getApplicationContext(), color_tint, Toast.LENGTH_SHORT).show();
        }
        else if (categoryLens.contentEquals("S") || categoryLens.equals("S"))
        {
            String kodeLensa = txt_lenstype.getText().toString();
            String frameType = spin_framemodel.getText().toString();

            checkStockRL(kodeLensa);

            /*if (flagRL.contentEquals("B") || flagRL.contains("B") || flagRL.equals("B"))
            {
                if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                {
                    getItemStockR(kodeLensa, sphr, cylr, addr, "B");
                }
                else
                {
                    itemPriceStR = 0;
                }
            }
            else
            {
                if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                {
                    getItemStockR(kodeLensa, sphr, cylr, addr, "R");
                }
                else
                {
                    itemPriceStR = 0;
                }

                if (!sphl.isEmpty() | !cyll.isEmpty() | !addl.isEmpty())
                {
                    getItemStockL(kodeLensa, sphl, cyll, addl, "L");
                }
                else
                {
                    itemPriceStL = 0;
                }
            }*/

            //getItemStock(kodeLensa, sph, cyl, add);

            itemTintPrice = 0;

            //Get Price Facet
            if (isfacet.equals("G") | isfacet.contentEquals("G") | isfacet.contains("G"))
            {
                //Parameter frameType, typeLensa, lensDiv
//                getFacetItem(frameType, lensDiv, typeLensa);
                getLensFacet(kodeLensa, frameType);
                itemWeight = 233;
            }
            else
            {
                //Toasty.warning(getApplicationContext(), "Tidak mengaktifkan FACET TRL", Toast.LENGTH_SHORT).show();
                itemFacetPrice = 0;
                itemWeight = 62;
            }

            //Counter Weight
//            if (countTemp > 0)
//            {
//                totalWeight = autoSum + itemWeight;
//                //Toasty.info(getApplicationContext(), "Total weight = " + totalWeight, Toast.LENGTH_SHORT).show();
//
//                if (totalWeight > limiter)
//                {
//                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
//                    String todayDate = sdf3.format(calendar.getTime());
//                    //db.moveToRealTbl();
//                    //db.deleteAllOrder();
//                    deleteOrderTemp(userinfo, todayDate);
//                    flagShipping = "1";
//                }
//                else
//                {
//                    flagShipping = "0";
//                }
//            }
//            else
//            {
//                flagShipping = "1";
//            }

            flagShipping = "1";
        }

        dialogUpload = builder.create();
        dialogUpload.setCancelable(false);
        dialogUpload.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialogUpload.dismiss();
                }

                return false;
            }
        });

        /*Event button sent and edit */
        btn_editorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpload.dismiss();
            }
        });

        btn_sentorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String custom_opticname = opticName.replace(',', ' ');
                //emailVerification(txt_orderNumber.getText().toString(), emailaddress, userinfo, custom_opticname,
                       //txt_lensdesc.getText().toString(), txt_patientName.getText().toString());

                smsVerification(userinfo, mobilenumber, txt_orderNumber.getText().toString());
            }
        });

        dialogUpload.show();
    }

    private void verifyOrder()
    {
        final UniversalFontTextView lbl_timer, lbl_error;
        final VerificationCodeEditText txt_pin;
        final BootstrapButton btn_confirm, btn_resend;
        smsFlag = 1;

        final Dialog dialogVerify = new Dialog(FormOrderLensActivity.this);
        dialogVerify.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogVerify.setContentView(R.layout.form_auth_order);
        dialogVerify.setCancelable(false);

        dialogVerify.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogVerify.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lbl_timer = (UniversalFontTextView) dialogVerify.findViewById(R.id.form_authorder_lbltimer);
        lbl_error = (UniversalFontTextView) dialogVerify.findViewById(R.id.form_authorder_lblerror);
        txt_pin   = (VerificationCodeEditText) dialogVerify.findViewById(R.id.form_authorder_txtpin);
        btn_confirm = (BootstrapButton) dialogVerify.findViewById(R.id.form_authorder_btnconfirm);
        btn_resend  = (BootstrapButton) dialogVerify.findViewById(R.id.form_authorder_btnresend);

        setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend);

        btn_resend.setEnabled(false);
        if (smsFlag == 1)
        {
            enableBroadcastReceiver(FormOrderLensActivity.this);
            //readNewSMS(txt_pin);
        }

        txt_pin.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onInputCompleted(CharSequence s) {
                String pin = txt_pin.getText().toString();

                if (pin.equals(pin_number))
                {
                    lbl_error.setVisibility(View.GONE);
                    //Toasty.info(getApplicationContext(), "Data has been sent", Toast.LENGTH_SHORT, true).show();
                    /*File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                    file = sample.toString();

                    uploadTxt(file);*/

                    checkCity(opticUsername);

//                    if (opticFlag.contentEquals("1"))
//                    {
//                        Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
//                        Float lensdiscount = 0f;
//                        Float totalPrice = 0f;
//                        String lensdesc   = txt_lensdesc.getText().toString();
//
//                        if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
//                        {
//                            lensprice = itemPriceR + itemPriceL;
//                            float discR = oprDiscount * itemPriceR / 100;
//                            float discL = oprDiscount * itemPriceL / 100;
//
//                            discountR = discR;
//                            discountL = discL;
//
//                            lensdiscount = discR + discL;
//                        }
//                        else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
//                        {
//                            lensprice = itemPriceStR + itemPriceStL;
//                            float discR = oprDiscount * itemPriceStR / 100;
//                            float discL = oprDiscount * itemPriceStL / 100;
//                            //Integer disc = oprDiscount * itemPriceSt / 100;
//
//                            discountR = discR;
//                            discountL = discL;
//                            lensdiscount = discR + discL;
//                        }
//
//                        if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
//                        {
//                            lensfacet   = itemFacetPrice + itemFacetPrice;
//                            lenstinting = itemTintPrice + itemTintPrice;
//
//                            qtyFacet = "2";
//                            totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;
//
//                            qtyTinting = "2";
//                            totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
//                        }
//                        else
//                        {
//                            //lensprice   = (lensprice / 2);
//                            //lensdiscount= (lensdiscount / 2);
//                            lensfacet   = itemFacetPrice;
//                            lenstinting = itemTintPrice;
//
//                            qtyFacet   = "1";
//                            totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;
//
//                            qtyTinting = "1";
//                            totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
//
//                            itemWeight  = itemWeight / 2;
//                        }
//
//                        totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;
//
//                        File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
//                        file = sample.toString();
//
//                        uploadTempTxt(file);
//
//
//
//                        Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
//                        intent.putExtra("id_party", opticId.replace(",", ""));
//                        intent.putExtra("city_info", opticCity);
//                        intent.putExtra("price_lens", lensprice);
//                        intent.putExtra("description_lens", lensdesc);
//                        intent.putExtra("discount_lens", lensdiscount);
//                        intent.putExtra("facet_lens", lensfacet);
//                        intent.putExtra("tinting_lens", lenstinting);
//                        intent.putExtra("item_weight", itemWeight);
//                        intent.putExtra("flag_pasang", flagPasang);
//                        intent.putExtra("username_info", opticUsername);
//                        intent.putExtra("flag_shipping", flagShipping);
//                        intent.putExtra("order_number", txt_orderNumber.getText().toString());
//                        intent.putExtra("patient_name", txt_patientName.getText().toString());
//                        intent.putExtra("margin_lens", "35");
//                        intent.putExtra("extramargin_lens", "15");
//
//                        //Area Lens R
//                        intent.putExtra("itemcode_R", itemCodeR);
//                        intent.putExtra("description_R", descR);
//                        intent.putExtra("power_R", powerR);
//                        intent.putExtra("qty_R", itemQtyR);
//                        intent.putExtra("itemprice_R", itemPriceR);
//                        intent.putExtra("itemtotal_R", itemTotalPriceR);
//
//                        //Area Lens L
//                        intent.putExtra("itemcode_L", itemCodeL);
//                        intent.putExtra("description_L", descL);
//                        intent.putExtra("power_L", powerL);
//                        intent.putExtra("qty_L", itemQtyL);
//                        intent.putExtra("itemprice_L", itemPriceL);
//                        intent.putExtra("itemtotal_L", itemTotalPriceL);
//
//                        //Area Diskon
//                        intent.putExtra("description_diskon", listLineNo);
//                        intent.putExtra("discount_r", discountR);
//                        intent.putExtra("discount_l", discountL);
//                        intent.putExtra("extra_margin_discount", "10");
//
//                        //Area Facet
//                        intent.putExtra("itemcode_facet", itemFacetCode);
//                        intent.putExtra("description_facet", facetDescription);
//                        intent.putExtra("qty_facet", qtyFacet);
//                        intent.putExtra("price_facet", itemFacetPrice);
//                        intent.putExtra("total_facet", totalFacet);
//                        intent.putExtra("margin_facet", "8");
//                        intent.putExtra("extra_margin_facet", "8");
//
//                        //Area Tinting
//                        intent.putExtra("itemcode_tinting", itemTintingCode);
//                        intent.putExtra("description_tinting", tintingDescription);
//                        intent.putExtra("qty_tinting", qtyTinting);
//                        intent.putExtra("price_tinting", itemTintPrice);
//                        intent.putExtra("total_tinting", totalTinting);
//                        intent.putExtra("margin_tinting", "8");
//                        intent.putExtra("extra_margin_tinting", "8");
//
//                        intent.putExtra("total_price", totalPrice);
//
//                        startActivity(intent);
//
//                        finish();
//                    }
//                    else
//                    {
//                        File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
//                        file = sample.toString();
//
//                        uploadTxt(file);
//                    }

                    smsFlag = 0;
                    disableBroadcastReceiver(FormOrderLensActivity.this);

                    dialogUpload.dismiss();
                    dialogVerify.dismiss();
                }
                else
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

                dialogVerify.dismiss();

                //String custom_opticname = opticName.replace(',', ' ');
                //emailVerification(txt_orderNumber.getText().toString(), emailaddress, userinfo, custom_opticname,
                       // txt_lensdesc.getText().toString(), txt_patientName.getText().toString());

                smsVerification(userinfo, mobilenumber, txt_orderNumber.getText().toString());
                setTimer(lbl_timer, lbl_error, btn_confirm, btn_resend);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = txt_pin.getText().toString();
                if (pin.equals(pin_number))
                {
                    lbl_error.setVisibility(View.GONE);
                    //Toasty.info(getApplicationContext(), "Data has been sent", Toast.LENGTH_SHORT, true).show();
                    /*File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                    file = sample.toString();

                    uploadTxt(file);*/

                    checkCity(opticUsername);

//                    if (opticProvince.equals("JAWA BARAT"))
//                    {
//                        Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
//                        Float lensdiscount = 0f;
//                        Float totalPrice = 0f;
//                        String lensdesc   = txt_lensdesc.getText().toString();
//
//                        if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
//                        {
//                            lensprice = itemPriceR + itemPriceL;
//                            float discR = oprDiscount * itemPriceR / 100;
//                            float discL = oprDiscount * itemPriceL / 100;
//
//                            discountR = discR;
//                            discountL = discL;
//
//                            lensdiscount = discR + discL;
//                        }
//                        else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
//                        {
//                            lensprice = itemPriceStR + itemPriceStL;
//                            float discR = oprDiscount * itemPriceStR / 100;
//                            float discL = oprDiscount * itemPriceStL / 100;
//                            //Integer disc = oprDiscount * itemPriceSt / 100;
//
//                            discountR = discR;
//                            discountL = discL;
//                            lensdiscount = discR + discL;
//                        }
//
//                        if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
//                        {
//                            lensfacet   = itemFacetPrice + itemFacetPrice;
//                            lenstinting = itemTintPrice + itemTintPrice;
//
//                            qtyFacet = "2";
//                            totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;
//
//                            qtyTinting = "2";
//                            totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
//                        }
//                        else
//                        {
//                            //lensprice   = (lensprice / 2);
//                            //lensdiscount= (lensdiscount / 2);
//                            lensfacet   = itemFacetPrice;
//                            lenstinting = itemTintPrice;
//
//                            qtyFacet   = "1";
//                            totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;
//
//                            qtyTinting = "1";
//                            totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
//
//                            itemWeight  = itemWeight / 2;
//                        }
//
//                        totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;
//
//                        File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
//                        file = sample.toString();
//
//                        uploadTempTxt(file);
//
////                        Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
////                        intent.putExtra("id_party", opticId.replace(",", ""));
////                        intent.putExtra("city_info", opticCity);
////                        intent.putExtra("price_lens", lensprice);
////                        intent.putExtra("description_lens", lensdesc);
////                        intent.putExtra("discount_lens", lensdiscount);
////                        intent.putExtra("facet_lens", lensfacet);
////                        intent.putExtra("tinting_lens", lenstinting);
////                        intent.putExtra("item_weight", itemWeight);
////                        intent.putExtra("flag_pasang", flagPasang);
////                        intent.putExtra("username_info", opticUsername);
////                        intent.putExtra("flag_shipping", flagShipping);
////                        intent.putExtra("order_number", txt_orderNumber.getText().toString());
////                        intent.putExtra("patient_name", txt_patientName.getText().toString());
////                        intent.putExtra("margin_lens", "35");
////                        intent.putExtra("extramargin_lens", "15");
////
////                        //Area Lens R
////                        intent.putExtra("itemcode_R", itemCodeR);
////                        intent.putExtra("description_R", descR);
////                        intent.putExtra("power_R", powerR);
////                        intent.putExtra("qty_R", itemQtyR);
////                        intent.putExtra("itemprice_R", itemPriceR);
////                        intent.putExtra("itemtotal_R", itemTotalPriceR);
////
////                        //Area Lens L
////                        intent.putExtra("itemcode_L", itemCodeL);
////                        intent.putExtra("description_L", descL);
////                        intent.putExtra("power_L", powerL);
////                        intent.putExtra("qty_L", itemQtyL);
////                        intent.putExtra("itemprice_L", itemPriceL);
////                        intent.putExtra("itemtotal_L", itemTotalPriceL);
////
////                        //Area Diskon
////                        intent.putExtra("description_diskon", listLineNo);
////                        intent.putExtra("discount_r", discountR);
////                        intent.putExtra("discount_l", discountL);
////                        intent.putExtra("extra_margin_discount", "10");
////
////                        //Area Facet
////                        intent.putExtra("itemcode_facet", itemFacetCode);
////                        intent.putExtra("description_facet", facetDescription);
////                        intent.putExtra("qty_facet", qtyFacet);
////                        intent.putExtra("price_facet", itemFacetPrice);
////                        intent.putExtra("total_facet", totalFacet);
////                        intent.putExtra("margin_facet", "8");
////                        intent.putExtra("extra_margin_facet", "8");
////
////                        //Area Tinting
////                        intent.putExtra("itemcode_tinting", itemTintingCode);
////                        intent.putExtra("description_tinting", tintingDescription);
////                        intent.putExtra("qty_tinting", qtyTinting);
////                        intent.putExtra("price_tinting", itemTintPrice);
////                        intent.putExtra("total_tinting", totalTinting);
////                        intent.putExtra("margin_tinting", "8");
////                        intent.putExtra("extra_margin_tinting", "8");
////
////                        intent.putExtra("total_price", totalPrice);
////
////                        startActivity(intent);
//
//                        finish();
//                    }
//                    else
//                    {
//                        File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
//                        file = sample.toString();
//
//                        uploadTxt(file);
//                    }

                    dialogUpload.dismiss();
                    dialogVerify.dismiss();
                }
                else
                {
                    lbl_error.setText("Sorry your input pin not correct");
                    lbl_error.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogVerify.show();
    }

    private void setTimer(final UniversalFontTextView lbl_timer, final UniversalFontTextView lbl_error,
                          final BootstrapButton btn_confirm, final BootstrapButton btn_resend)
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
                pin_number = "";
            }
        }.start();
    }

    private void getUserInfo()
    {
        Config config = new Config();
        String URL = config.Ip_address + config.profile_user_detail;
        mCrypt = new MCrypt();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String username     = "username";
                String email        = "email";
                String error        = "error";
                String mobnumber    = "phone";

                try {
                    String info1 = MCrypt.bytesToHex(mCrypt.encrypt(username));
                    String info4 = MCrypt.bytesToHex(mCrypt.encrypt(email));
                    String info5 = MCrypt.bytesToHex(mCrypt.encrypt(mobnumber));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String data1 = new String(mCrypt.decrypt(jsonObject.getString(info1)));
                            String data4 = new String(mCrypt.decrypt(jsonObject.getString(info4)));
                            String data5 = new String(mCrypt.decrypt(jsonObject.getString(info5)));

                            emailaddress = data4;
                            userinfo     = data1;
                            mobilenumber = data5;

                            //Toasty.info(getApplicationContext(), emailaddress + " " + userinfo, Toast.LENGTH_SHORT, true).show();
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
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String , String> hashmap = new HashMap<>();
                hashmap.put("id_party", opticId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void smsVerification(final String username, final String mobileNumber, final String reff_id)
    {
        showLoading();
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_verifyorder_sms;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCrypt = new MCrypt();

                String data1 = "success";
                String data2 = "error";

                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.names().get(0).equals(encrypt1))
                        {
                            loading.dismiss();
                            verifyOrder();

                            pin_number = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                            Toasty.info(getApplicationContext(), "Please check pin number on your mail or SMS", Toast.LENGTH_SHORT, true).show();

                            String pesan = mobileNumber + ",Order anda dengan nomor bon " + reff_id + " akan segera dikirim ke Timur Raya. " +
                                            "Mohon masukkan nomor pin = " + pin_number + " untuk verifikasi dan konfirmasi order anda.";
                            String enc_pin = MCrypt.bytesToHex(mCrypt.encrypt(pin_number));
                            String filename = "neworder_" + userinfo + "_" + mobileNumber + "_" + enc_pin + ".txt";

                            createLogsms(pesan, filename);
                            readLogSMS(filename);

                            String custom_opticname = opticName.replace(',', ' ');
                            emailVerification(txt_orderNumber.getText().toString(), emailaddress, userinfo, custom_opticname,
                                    txt_lensdesc.getText().toString(), txt_patientName.getText().toString(), pin_number);
                        }
                        else if (jsonObject.names().get(0).equals(encrypt2))
                        {
                            String error = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));
                            Toasty.warning(getApplicationContext(), error, Toast.LENGTH_SHORT, true).show();
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
                hideLoading();
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("mobile_number", mobileNumber);
                hashMap.put("user_info", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void emailVerification(final String ordernumber, final String emailaddress, final String userinfo,
                                   final String opticname, final String lensdescription, final String patientname,
                                   final String pincode)
    {
        //showLoading();
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_verifyorder_mail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //loading.dismiss();
                        //verifyOrder();
                        //Toasty.info(getApplicationContext(), "Please check pin number on your mail or SMS", Toast.LENGTH_SHORT, true).show();
                        //Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                        pin_number = jsonObject.getString("success");
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                //hideLoading();
                informationEmail("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", ordernumber);
                hashMap.put("email_address", emailaddress);
                hashMap.put("user_info", userinfo);
                hashMap.put("optic_name", opticname);
                hashMap.put("patient_name", patientname);
                hashMap.put("lens_description", lensdescription);
                hashMap.put("pin_code", pincode);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
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

    private void readLogSMS(String filename)
    {
        File path = new File(Environment.getExternalStorageDirectory(), "TRLAPP/smslog/" + filename);

        if (path.exists())
        {
            uploadVerifyPhoneTxt(path.toString());
        }
    }

    public void uploadVerifyPhoneTxt(final String filepath)
    {
        Config config = new Config();
        String upload_txtfile = config.Ip_address + config.dashboard_upload_txtphone;

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, upload_txtfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String psn = jsonObject.getString("info");

                    if (psn.equals("File uploaded"))
                    {
                        //Toasty.info(getApplicationContext(), "Check SMS verification code", Toast.LENGTH_LONG, true).show();
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

    private void saveOrder()
    {
        btn_done.setEnabled(true);
        btn_back.setVisibility(View.GONE);

        String wrap         = txt_wrap.getText().toString() + ",";
        String phantose     = txt_phantose.getText().toString() + ",";
        String vertex       = txt_vertex.getText().toString() + ",";
        String facettrl     = isfacet.replace(",", "") + ",";

        //Area Order Detail
        String order_number     = txt_orderNumber.getText().toString().replace(",", "") + ",";
        String patient_name     = txt_patientName.getText().toString().replace(",", "") + ",";
        phoneNumber             = txt_phonenumber.getText().toString().trim();
        String order_information = txt_orderInformation.getText().toString().replace(",", "");

        phoneNumber = phoneNumber.replace("+62", "0").replaceAll("\\s","");

        //Area Frame Detail
        model_frame      = spin_framemodel.getText().toString() + ",";
        if (model_frame.contains("Model"))
        {
            model_frame = "" + ",";
        }

        if (color_tint.trim().equalsIgnoreCase("none"))
        {
            color_tint = "";
        }

        if (coating != null)
        {
            if (coating.contains("None") || coating.isEmpty() || coating.equals("null"))
            {
                coating = "";
            }
        }
        else
        {
            coating = "";
        }

        hor_frame        = txt_hor.getText().toString();
        ver_frame        = txt_ver.getText().toString();
        dbl_frame        = txt_dbl.getText().toString();
        frame_brand      = "," + txt_framebrand.getText().toString().replace(",", "") + ",";


        dbl_frame        = checkFirstZero(dbl_frame);
        ver_frame        = checkFirstZero(ver_frame);
        hor_frame        = checkFirstZero(hor_frame);

        //Area Lens Detail
        sph_l            = txt_sphl.getText().toString();
        sph_r            = txt_sphr.getText().toString();
        cyl_l            = txt_cyll.getText().toString();
        cyl_r            = txt_cylr.getText().toString();
        axs_l            = txt_axsl.getText().toString();
        axs_r            = txt_axsr.getText().toString();
        mpd_l            = txt_mpdl.getText().toString();
        mpd_r            = txt_mpdr.getText().toString();
        pd_l             = "";
        pd_s             = "";
        seg_h            = txt_segh.getText().toString();

        //color_tint       = color_tint;
        infofacet        = "," + txt_infofacet.getText().toString().replace(",", "") + ",";
        add_l            = txt_addl.getText().toString();
        add_r            = txt_addr.getText().toString();

        sph_l            = checkPositiveValue(sph_l);
        sph_r            = checkPositiveValue(sph_r);
        cyl_l            = checkPositiveValue(cyl_l);
        cyl_r            = checkPositiveValue(cyl_r);
        add_l            = checkPositiveValue(add_l);
        add_r            = checkPositiveValue(add_r);
        axs_l            = checkFirstZero(axs_l);
        axs_r            = checkFirstZero(axs_r);
        mpd_l            = checkFirstZero(mpd_l);
        mpd_r            = checkFirstZero(mpd_r);
        pd_l             = checkFirstZero(pd_l);
        pd_s             = checkFirstZeroCustom(pd_s);
        seg_h            = checkFirstZero(seg_h);

        id_lensa = txt_lenstype.getText().toString();
        if (id_lensa.isEmpty())
        {
            id_lensa = "" + ",";
        }
        else
        {
            id_lensa = id_lensa + ",";
        }

        allData  = opticId + dateTime + order_number + opticName + order_information + frame_brand + model_frame + hor_frame + ver_frame
                + patient_name + id_lensa + color_tint + "," + sph_r + cyl_r + axs_r + add_r + id_lensa + color_tint  + "," + sph_l
                + cyl_l + axs_l + add_l + dbl_frame + mpd_r + mpd_l + seg_h + pd_l + pd_s + infofacet + wrap + phantose + vertex
                + facettrl + corridor + "," + coating + "," + phoneNumber;

        filename = "order_" + opticId.replace(",", "") + "_" + txt_orderNumber.getText().toString() + "_"
                    + txt_patientName.getText().toString() + ".txt";
        createTxt(allData);
    }

    private void insertTemp()
    {
        Config config = new Config();
        String URL    = config.Ip_address + config.orderlens_insert_tmporder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                        updateLensDetail(txt_orderNumber.getText().toString());
                        updateFacetDetail(txt_orderNumber.getText().toString());
                        updateFrameDetail(txt_orderNumber.getText().toString());
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("invalid"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("tanggal", dateOnly);
                hashMap.put("waktu", timeOnly);
                hashMap.put("optic_id", opticId.replace(",", ""));
                hashMap.put("optic_name", opticName.replace(",",""));
                hashMap.put("order_number", txt_orderNumber.getText().toString());
                hashMap.put("remark", txt_orderInformation.getText().toString());
                hashMap.put("patient", txt_patientName.getText().toString());
                hashMap.put("filename", filename);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateLensDetail(final String id)
    {
        Config config = new Config();
        String URL    = config.Ip_address + config.orderlens_update_lensDetail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.success(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("invalid"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("lens_type", txt_lenstype.getText().toString());
                hashMap.put("sph_r", txt_sphr.getText().toString());
                hashMap.put("cyl_r", txt_cylr.getText().toString());
                hashMap.put("axs_r", txt_axsr.getText().toString());
                hashMap.put("add_r", txt_addr.getText().toString());
                hashMap.put("sph_l", txt_sphl.getText().toString());
                hashMap.put("cyl_l", txt_cyll.getText().toString());
                hashMap.put("axs_l", txt_axsl.getText().toString());
                hashMap.put("add_l", txt_addl.getText().toString());
                hashMap.put("pd_jauh", "");
                hashMap.put("pd_dekat", "");
                //hashMap.put("color_tint", txt_color.getText().toString());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateFacetDetail(final String id)
    {
        Config config = new Config();
        String URL    = config.Ip_address + config.orderlens_update_facetDetail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.success(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("invalid"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("mpd_r", txt_mpdr.getText().toString());
                hashMap.put("mpd_l", txt_mpdl.getText().toString());
                hashMap.put("seg_h", txt_segh.getText().toString());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateFrameDetail(final String id)
    {
        Config config = new Config();
        String URL    = config.Ip_address + config.orderlens_update_frameDetail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.success(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("invalid"), Toast.LENGTH_SHORT, true).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("frame_model", spin_framemodel.getText().toString());
                hashMap.put("dbl", txt_dbl.getText().toString());
                hashMap.put("hor_a", txt_hor.getText().toString());
                hashMap.put("ver_b", txt_ver.getText().toString());
                hashMap.put("frame_code", txt_framebrand.getText().toString());
                return hashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/x-www-form-urlencoded");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createTxt(String msg)
    {
        FileOutputStream fileOutputStream;
        File rootFolder;
        File childFolder;
        //File txtfile;

        try {
            rootFolder = new File(Environment.getExternalStorageDirectory(), "TRLAPP");
            childFolder = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp");
            //txtfile = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);

            //Check if root folder not found
            if (!rootFolder.exists()) {
                rootFolder.mkdirs();
            }

            if (!childFolder.exists()) {
                childFolder.mkdirs();
            }

            fileOutputStream = new FileOutputStream(childFolder + "/" + filename);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();

            confirmOrder();


            /*if (txtfile.exists())
            {
                //Toast.makeText(getApplicationContext(), "Sorry temporary files has been created", Toast.LENGTH_SHORT).show();
                information("FAILED", "Sorry temporary files has been created", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
            }
            else
            {
                fileOutputStream = new FileOutputStream(childFolder + "/" + filename);
                fileOutputStream.write(msg.getBytes());
                fileOutputStream.close();

                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                //upload("Order successfully save", "Do you want to send this order ?", R.drawable.success_outline, DefaultBootstrapBrand.SUCCESS);
                confirmOrder();
            }*/
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void moveTxt(String filename)
    {
        FileInputStream fileIS;
        FileOutputStream fileOS;
        File rootFolder;
        File tempFolder;
        File uploadFolder;
        byte [] buffer = new byte[10240];
        int read;

        try
        {
            rootFolder   = new File(Environment.getExternalStorageDirectory(), "TRLAPP");
            tempFolder   = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp");
            uploadFolder = new File(Environment.getExternalStorageDirectory(), "TRLAPP/Upload");

            if (!rootFolder.exists())
            {
                rootFolder.mkdirs();
            }

            if (!tempFolder.exists())
            {
                tempFolder.mkdirs();
            }

            if (!uploadFolder.exists())
            {
                uploadFolder.mkdirs();
            }

            fileIS = new FileInputStream(tempFolder + "/" + filename);
            fileOS = new FileOutputStream(uploadFolder + "/" + filename);

            //copy txt file to upload folder
            while ((read = fileIS.read(buffer)) != 0)
            {
                fileOS.write(buffer, 0 , read);
                new File(tempFolder + "/" + filename).delete();
                //Toast.makeText(getApplicationContext(), "Data has been move", Toast.LENGTH_SHORT).show();
            }

            fileIS.close();
            fileOS.flush();
            fileOS.close();
        }
        catch (FileNotFoundException fnf)
        {
            fnf.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Sory file not found", Toast.LENGTH_SHORT).show();
            information("ERROR", "Sory temp file not found", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormOrderLensActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
    }

    private void hideLoading()
    {
        loading.dismiss();
    }

    public void informationUpload(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
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
                uploadTxt(file);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void informationEmail(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormOrderLensActivity.this);
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
                //String custom_opticname = opticName.replace(',', ' ');
                //emailVerification(txt_orderNumber.getText().toString(), emailaddress, userinfo, custom_opticname,
                        //txt_lensdesc.getText().toString(), txt_patientName.getText().toString());

                smsVerification(userinfo, mobilenumber, txt_orderNumber.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void informationEmailNewOrder(final String orderNumber, final String opticName, final String patientName,
                                          final String lensDescription)
    {
        Config config = new Config();
        String URL_INFORMATIONNEWORDER = config.Ip_address + config.orderlens_sentorder_information;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INFORMATIONNEWORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT, true).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", orderNumber);
                hashMap.put("optic_name", opticName);
                hashMap.put("patient_name", patientName);
                hashMap.put("lens_description", lensDescription);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void autoReplyEmailToOptic(final String orderNumber, final String emailAddress, final String userInfo,
                                    final String opticName, final String patientName)
    {
        Config config = new Config();
        String URL_AUTOREPLY = config.Ip_address + config.orderlens_autoreplyorder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_AUTOREPLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT, true).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id", orderNumber);
                hashMap.put("email_address", emailAddress);
                hashMap.put("user_info", userInfo);
                hashMap.put("optic_name", opticName);
                hashMap.put("patient_name", patientName);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void autoReplyEmailToOpticPayment(final String orderNumber, final String emailAddress, final String userInfo,
                                       final String opticName, final String patientName)
    {
        Config config = new Config();
        String URL_AUTOREPLY = config.Ip_address + config.orderlens_autoreplyorderpayment;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_AUTOREPLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT, true).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT, true).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("id", orderNumber);
                hashMap.put("email_address", emailAddress);
                hashMap.put("user_info", userInfo);
                hashMap.put("optic_name", opticName);
                hashMap.put("patient_name", patientName);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void autoReplySMStoOptic(final String mobileNumber, final String reff_id)
    {
        MCrypt mCrypt = new MCrypt();
        String pesan = mobileNumber + ",Nomor bon #" + reff_id + " sudah diterima Timur Raya. " +
                "Order akan segera diproses oleh Customer Service kami.";
        String enc_pin = null;

        try {
            enc_pin = MCrypt.bytesToHex(mCrypt.encrypt(reff_id));

            String filename = "feedbackorder_" + userinfo + "_" + mobileNumber + "_" + enc_pin + ".txt";

            createLogsms(pesan, filename);
            readLogSMS(filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoReplySMStoOpticPayment(final String mobileNumber, final String reff_id)
    {
        MCrypt mCrypt = new MCrypt();
        String pesan = mobileNumber + ",Harap segera lakukan pembayaran agar Nomor bon #" + reff_id +
                " segera diproses oleh Timur Raya Lestari";
        String enc_pin = null;

        try {
            enc_pin = MCrypt.bytesToHex(mCrypt.encrypt(reff_id));

            String filename = "feedbackorder_" + userinfo + "_" + mobileNumber + "_" + enc_pin + ".txt";

            createLogsms(pesan, filename);
            readLogSMS(filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadTxt(final String file)
    {
        Config config   = new Config();
        String URL_UPLOAD = config.Ip_address + config.orderlens_upload_txtfile;
        showLoading();

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "info";
                hideLoading();
                dialogUpload.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString(info);

                    //Show back control and disable other
                    btn_save.setEnabled(false);
                    btn_done.setVisibility(View.GONE);
                    btn_back.setVisibility(View.VISIBLE);

                    //autoSentftp(filename);
                    insertTemp();
//                    information("INFORMATION", msg, R.drawable.success_outline, DefaultBootstrapBrand.SUCCESS);
                    moveTxt(filename);

                    //Auto reply and info new order
                    String ordernumber      = txt_orderNumber.getText().toString();
                    String customopticname  = opticName.replace(",", " ");
                    String patientname      = txt_patientName.getText().toString();
                    String lensdescription  = txt_lensdesc.getText().toString();

                    informationEmailNewOrder(ordernumber, customopticname, patientname, lensdescription);
                    autoReplyEmailToOptic(ordernumber, emailaddress, userinfo, customopticname, patientname);
                    autoReplySMStoOptic(mobilenumber, ordernumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    information("ERROR", e.getMessage(), R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                informationUpload("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
                //Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        smr.addFile("file", file);

        AppController.getInstance().addToRequestQueue(smr);
    }

    private void uploadTempTxt(final String file)
    {
        Config config   = new Config();
        String URL_UPLOAD = config.Ip_address + config.orderlens_upload_temptxtfile;
        showLoading();

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "info";
                hideLoading();
                dialogUpload.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString(info);

                    //Show back control and disable other
                    btn_save.setEnabled(false);
                    btn_done.setVisibility(View.GONE);
                    btn_back.setVisibility(View.VISIBLE);

                    //autoSentftp(filename);
                    insertTemp();
                    //information("INFORMATION", msg, R.drawable.success_outline, DefaultBootstrapBrand.SUCCESS);
                    moveTxt(filename);

                    //Auto reply and info new order
                    String ordernumber      = txt_orderNumber.getText().toString();
                    String customopticname  = opticName.replace(",", " ");
                    String patientname      = txt_patientName.getText().toString();
                    String lensdescription  = txt_lensdesc.getText().toString();

                    informationEmailNewOrder(ordernumber, customopticname, patientname, lensdescription);
                    autoReplyEmailToOpticPayment(ordernumber, emailaddress, userinfo, customopticname, patientname);
                    autoReplySMStoOpticPayment(mobilenumber, ordernumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    information("ERROR", e.getMessage(), R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                informationUpload("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
                //Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        smr.addFile("file", file);

        AppController.getInstance().addToRequestQueue(smr);
    }

    private void expandCollapseControl(RippleView rippleView, final ExpandableLayout expandableLayout)
    {
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOff == 1)
                {
                    expandableLayout.expand();
                    onOff = 0;
                }
                else
                {
                    expandableLayout.collapse();
                    onOff = 1;
                }
            }
        });
    }

    private void expandCollapseOrderDetail()
    {
        expandCollapseControl(ripple_orderdetail, expandableLayout_orderdetail);
    }

    private void expandCollapseFrameDetail()
    {
        expandCollapseControl(ripple_framedetail, expandableLayout_framedetail);
    }

    private void expandCollapseFacetDetail()
    {
        expandCollapseControl(ripple_facetdetail, expandableLayout_facetdetail);
    }

    private void expandCollapseRefraksiDetail()
    {
        expandCollapseControl(ripple_refraksidetail, expandableLayout_refraksidetail);
    }

    private String removeFirstZero(String data)
    {
        char b = data.charAt(1);
        char c = data.charAt(2);
        data   = String.valueOf(b) + String.valueOf(c);

        return data;
    }

    private String checkFirstZero(String data)
    {
        if (data.startsWith("0") && data.length() > 1)
        {
            data = removeFirstZero(data) + ",";
        }
        else
        {
            data = data + ",";
        }

        return data;
    }

    private String checkFirstZeroCustom(String data)
    {
        if (data.startsWith("0") && data.length() > 1)
        {
            data = removeFirstZero(data);
        }

        return data;
    }

    private String checkPositiveValue(String data)
    {
        if (data.isEmpty())
        {
            data = ",";
        }
        else
        {
            Float data1 = Float.parseFloat(data);

            if (data1 == .0f || data1 < 0.0f)
            {
                data     = data + ",";
            }
            else
            {
                data     = "+" + data + ",";
            }
        }

        return data;
    }

    private void chooseFrameModel()
    {
        final String [] model = getResources().getStringArray(R.array.frame_model_array);
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this, R.layout.spin_framemodel_item, model);
        spin_framemodel.setAdapter(spin_adapter);

        spin_framemodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                spin_framemodel.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void selectLenstype()
    {
        btn_lenstype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListView listView;
                final BootstrapButton btn_save;
                final MaterialEditText txt_search;
                final RippleView btn_search;
                final BetterSpinner spin_typelens;
                final String [] data_lensa = getResources().getStringArray(R.array.type_lensa_array);

                final Dialog dialog = new Dialog(FormOrderLensActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_choose_lenstype);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                listView    = (ListView) dialog.findViewById(R.id.choose_lenstype_listview);
                btn_save    = (BootstrapButton) dialog.findViewById(R.id.choose_lenstype_btnsave);
                txt_search  = (MaterialEditText) dialog.findViewById(R.id.choose_lenstype_txtsearch);
                btn_search  = (RippleView) dialog.findViewById(R.id.choose_lenstype_btnsearch);
                rl_error    = (RelativeLayout) dialog.findViewById(R.id.choose_lenstype_rl);
                spin_typelens = (BetterSpinner) dialog.findViewById(R.id.choose_lenstype_spinlenstype);

                btn_save.setEnabled(false);
                btn_save.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(FormOrderLensActivity.this,
                        R.layout.spin_typelensmodel_item, data_lensa);
                spin_typelens.setAdapter(spin_adapter);

                adapter_lenstype = new Adapter_lenstype(FormOrderLensActivity.this, data);
                String lenstype  = spin_typelens.getText().toString();
                //showAllLenstype(adapter_lenstype, data);
                filterLenstype(lenstype, adapter_lenstype, data);
                listView.setAdapter(adapter_lenstype);

                dialog.show();

                /*AREA EVENT CONTROL*/
                spin_typelens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedTypelens = parent.getItemAtPosition(position).toString();
                        spin_typelens.setText(selectedTypelens);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spin_typelens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemSelected = parent.getItemAtPosition(position).toString();
                        data.clear();
                        adapter_lenstype.notifyDataSetChanged();

                        filterLenstype(itemSelected, adapter_lenstype, data);
                        listView.setAdapter(adapter_lenstype);

                        //Toasty.normal(getApplicationContext(), itemSelected, Toast.LENGTH_SHORT).show();
                        //getCoating(itemSelected);
                        //product_type = itemSelected;
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_lensa = data.get(position).getLenstype();
                        desc_lensa = data.get(position).getLensdescription();

                        if (id_lensa.isEmpty())
                        {
                            btn_save.setEnabled(false);
                            btn_save.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btn_save.setEnabled(true);
                            btn_save.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_lenstype.setText(id_lensa);
                        txt_lensdesc.setText(desc_lensa);
                        getCorridorInfo(id_lensa);
                        getProduct(id_lensa);
                        getInfoLens(id_lensa);

                        dialog.hide();
                    }
                });

                txt_search.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        InputMethodManager inputManager = (InputMethodManager) FormOrderLensActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {
                            String key = txt_search.getText().toString();

                            if (key.isEmpty())
                            {
                                Toasty.warning(FormOrderLensActivity.this, "Please fill lenscode before search", Toast.LENGTH_SHORT, true).show();
                            }
                            else
                            {
                                searchLenstypeByID(adapter_lenstype, data, key);
                            }
                        }
                        return false;
                    }
                });

                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = txt_search.getText().toString();

                        if (key.isEmpty())
                        {
                            Toasty.warning(FormOrderLensActivity.this, "Please fill lenscode before search", Toast.LENGTH_SHORT, true).show();
                        }
                        else
                        {
                            searchLenstypeByID(adapter_lenstype, data, key);
                        }
                    }
                });
            }
        });
    }

    private void showErrorImage()
    {
        img_error = new ImageView(getApplicationContext());
        img_error.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_error.setLayoutParams(lp);
        rl_error.addView(img_error);
    }

    private void getTintingGroup()
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_get_tintgroup;
        mCrypt = new MCrypt();

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data_tintinggroup.clear();
                String data1 = "tint_group";

                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String data2 = "none";
                        data_tintinggroup.add(data2);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String output1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                            output1 = output1.toLowerCase();

                            data_tintinggroup.add(output1);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(spin_tintgroup.getContext(), R.layout.spin_framemodel_item, data_tintinggroup);
                        arrayAdapter.setDropDownViewResource(R.layout.spin_framemodel_item);
                        spin_tintgroup.setAdapter(arrayAdapter);
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
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getTinting(final String id)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_get_tinting;
        mCrypt = new MCrypt();
        adapter_tinting = new Adapter_tinting(this, data_tintings);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data1 = "tint_code";
                String data2 = "tint_descr";

                data_tintings.clear();
                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String data3 = "none";

                        Data_tinting item1 = new Data_tinting();
                        item1.setTint_code(data3);
                        item1.setTint_description(data3);

                        data_tintings.add(item1);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String output1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                            String output2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));

                            Data_tinting item = new Data_tinting();
                            item.setTint_code(output1);
                            item.setTint_description(output2);

                            data_tintings.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter_tinting.notifyDataSetChanged();
                    spin_tint.setAdapter(adapter_tinting);
                } catch (Exception e) {
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
                hashMap.put("group_tinting", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getCorridorInfo(final String lenstype)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_get_corridorinfo;
        mCrypt = new MCrypt();

        data_corridor.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "id_lensa";
                String info2 = "corridor";
                String info3 = "invalid";

                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String data1= "None";
                        data_corridor.add(data1);

                        for(int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String data = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));

                            boolean checkwhitespace = data.matches("^\\s*$");

                            if (!checkwhitespace)
                            {
                                spin_corridor.setEnabled(true);
                            }
                            else
                            {
                                spin_corridor.setEnabled(false);
                            }

                            data_corridor.add(data);
                        }

                        spin_corridor.setAdapter(new ArrayAdapter<>(FormOrderLensActivity.this, R.layout.spin_framemodel_item, data_corridor));

                    } catch (JSONException e) {
                        e.printStackTrace();

                        String data1= "None";
                        data_corridor.add(data1);
                        spin_corridor.setAdapter(new ArrayAdapter<>(FormOrderLensActivity.this, R.layout.spin_framemodel_item, carridor));
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_lensa", lenstype);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void filterLenstype(final String typelens, final Adapter_lenstype adapter_lenstype, final List<Data_lenstype> data_lenstypes)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_filter_lenstype;
        mCrypt     = new MCrypt();

        adapter_lenstype.notifyDataSetChanged();
        data_lenstypes.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "id_lensa";
                String info2 = "description";
                String info3 = "icon";
                rl_error.removeView(img_error);

                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));

                    try {
                    JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String data1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                            String data2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));
                            String data3 = new String(mCrypt.decrypt(jsonObject.getString(encrypt3)));

                            Data_lenstype data_lenstype = new Data_lenstype();
                            data_lenstype.setLenstype(data1);
                            data_lenstype.setLensdescription(data2);
                            data_lenstype.setImage(data3);

                            data_lenstypes.add(data_lenstype);
                        }
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                adapter_lenstype.notifyDataSetChanged();
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
                hashMap.put("type_lensa", typelens);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void searchLenstypeByID(final Adapter_lenstype adapter_lenstype, final List<Data_lenstype> data_lenstypes,
                                    final String key)
    {
        Config config   = new Config();
        String URL      = config.Ip_address + config.orderlens_show_lenstypebyid;
        mCrypt          = new MCrypt();

        adapter_lenstype.notifyDataSetChanged();
        data_lenstypes.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "id_lensa";
                String info2 = "description";
                String info3 = "invalid";
                String info4 = "icon";
                rl_error.removeView(img_error);

                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String encrypt4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(encrypt3))
                            {
                                showErrorImage();
                                String info = "No record found";
                                Toasty.error(getApplicationContext(), info, Toast.LENGTH_LONG, true).show();
                            }
                            else {
                                String data1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                                String data2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));
                                String data4 = new String(mCrypt.decrypt(jsonObject.getString(encrypt4)));

                                Data_lenstype data = new Data_lenstype();
                                data.setLenstype(data1);
                                data.setLensdescription(data2);
                                data.setImage(data4);

                                data_lenstypes.add(data);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter_lenstype.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(FormOrderLensActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", key);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void enableBroadcastReceiver(Context context)
    {
        ComponentName receiver = new ComponentName(context, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    public void disableBroadcastReceiver(Context context){
        ComponentName receiver = new ComponentName(context, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
    }

   /* private void readNewSMS(final VerificationCodeEditText txt_pincode)
    {
        SmsReceiver.getNewMessage(new SmsListener() {
            @Override
            public void ReceiveMsg(String msg) {
                if (msg.contains("="))
                {
                    String [] pesan = msg.split("=");

                    String pin = pesan[1].trim().substring(0, 6);

                    txt_pincode.setText(pin);
                }
                else
                {
                    Toasty.error(getApplicationContext(), "Incorrect sms format", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    } */

    private void getProduct(final String lens_id)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_get_product;
        mCrypt = new MCrypt();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data1 = "type_lensa";
                //String data2 = "coat_id";
                //String data3 = "corridor";

                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    //String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    //String encrypt3 = MCrypt.bytesToHex(mCrypt.encrypt(data3));

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String decrypt1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                        //String decrypt2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));
                        //String decrypt3 = new String(mCrypt.decrypt(jsonObject.getString(encrypt3)));

                        //Toast.makeText(getApplicationContext(), decrypt1, Toast.LENGTH_SHORT).show();
                        //Toasty.info(getApplicationContext(), lensdiv, Toast.LENGTH_SHORT).show();
                        getCoating(decrypt1);
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
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("id_lensa", lens_id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getCoating(final String produk)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.orderlens_get_coating;
        mCrypt = new MCrypt();
        adapter_coating = new Adapter_coating(this, data_coatings);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data1 = "coat_code";
                String data2 = "description";
                //data_coating.clear();
                data_coatings.clear();
                try {
                    String encrypt1 = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String encrypt2 = MCrypt.bytesToHex(mCrypt.encrypt(data2));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //data_coating.add("None");
                        Data_coating item1 = new Data_coating();
                        item1.setCoat_code("None");
                        item1.setCoat_description("Not Coating");

                        data_coatings.add(item1);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String decrypt1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt1)));
                            String decrypt2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt2)));

                            Data_coating item2 = new Data_coating();
                            //data_coating.add(decrypt1);
                            item2.setCoat_code(decrypt1);
                            item2.setCoat_description(decrypt2);

                            data_coatings.add(item2);
                        }
                        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(spin_coatgroup.getContext(), R.layout.spin_framemodel_item, data_coating);
                        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(spin_coatgroup.getContext(), R.layout.spin_framemodel_item, data_coating);
                        arrayAdapter.setDropDownViewResource(R.layout.spin_framemodel_item);*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter_coating.notifyDataSetChanged();
                    spin_coatgroup.setAdapter(adapter_coating);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("product", produk);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getInfoLens(final String kodeLensa)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_rx_getinfolens;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    typeLensa    = jsonObject.getString("type_lensa");
                    description  = jsonObject.getString("description");
                    categoryLens = jsonObject.getString("category_lens");
                    purchaseLens = jsonObject.getString("purchase_lens");
                    lensDiv      = jsonObject.getString("lens_div");
                    icon         = jsonObject.getString("icon");

                    if (categoryLens.contentEquals("S"))
                    {
                        txt_tinttitle.setVisibility(View.GONE);
                        spin_tintgroup.setVisibility(View.GONE);
                        spin_tint.setVisibility(View.GONE);
                    }
                    else
                    {
                        txt_tinttitle.setVisibility(View.VISIBLE);
                        spin_tintgroup.setVisibility(View.VISIBLE);
                        spin_tint.setVisibility(View.VISIBLE);
                    }

                    //Toasty.info(getApplicationContext(), description, Toast.LENGTH_SHORT, true).show();
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
                hashMap.put("kode_lensa", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkItemIdR(final String kodeLensa, final String koridor, final String kodeItem, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_rx_getitemrx;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        hargaR = "False";

                        Toasty.warning(getApplicationContext(), "Stok kosong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemIdR     = jsonObject.getString("item_id");
                        itemCodeR   = jsonObject.getString("item_code");
                        descR       = jsonObject.getString("description");
                        itemSphR    = txt_sphr.getText().toString();
                        if (itemSphR == null || itemSphR.isEmpty())
                        {
                            itemSphR = "0.00";
                        }
                        itemCylR    = txt_cylr.getText().toString();
                        if (itemCylR == null || itemCylR.isEmpty())
                        {
                            itemCylR = "0.00";
                        }
                        itemAxsR    = txt_axsr.getText().toString();
                        if (itemAxsR == null || itemAxsR.isEmpty())
                        {
                            itemAxsR = "0";
                        }
                        itemAddR    = txt_addr.getText().toString();
                        if (itemAddR == null || itemAddR.isEmpty())
                        {
                            itemAddR = "0.00";
                        }
                        powerR      = "SPH " + itemSphR + " CYL " + itemCylR + " AXS " + itemAxsR + " ADD " + itemAddR;
                        itemQtyR    = "1";

                        //output[0] = String.valueOf(checkItemPriceR(itemIdR));
                        callBack.onSuccess(itemIdR);

                        hargaR = "True";
                    }
                    //getItemPriceR(itemIdR);
                    //getStbStockItemR(itemIdR);
                    //Toasty.success(getApplicationContext(), itemIdR, Toast.LENGTH_LONG).show();

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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("koridor", koridor);
                hashMap.put("kode_item", kodeItem);
                hashMap.put("rl", "R");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkItemIdL(final String kodeLensa, final String koridor, final String kodeItem, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_rx_getitemrx;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        hargaR = "False";

                        Toasty.warning(getApplicationContext(), "Stok kosong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemIdL     = jsonObject.getString("item_id");
                        itemCodeL   = jsonObject.getString("item_code");
                        descL       = jsonObject.getString("description");
                        itemSphL    = txt_sphl.getText().toString();
                        if (itemSphL == null || itemSphL.isEmpty())
                        {
                            itemSphL = "0.00";
                        }
                        itemCylL    = txt_cyll.getText().toString();
                        if (itemCylL == null || itemCylL.isEmpty())
                        {
                            itemCylL = "0.00";
                        }
                        itemAxsL    = txt_axsl.getText().toString();
                        if (itemAxsL == null || itemAxsL.isEmpty())
                        {
                            itemAxsL = "0";
                        }
                        itemAddL    = txt_addl.getText().toString();
                        if (itemAddL == null || itemAddL.isEmpty())
                        {
                            itemAddL = "0.00";
                        }
                        powerL      = "SPH " + itemSphL + " CYL " + itemCylL + " AXS " + itemAxsL + " ADD " + itemAddL;
                        itemQtyL    = "1";

                        callBack.onSuccess(itemIdL);
                    }
                    //Toasty.success(getApplicationContext(), itemIdL, Toast.LENGTH_LONG).show();
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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("koridor", koridor);
                hashMap.put("kode_item", kodeItem);
                hashMap.put("rl", "L");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkItemPriceR(final String itemId, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int result = Integer.parseInt(jsonObject.getString("price_list"));

                    callBack.onSuccess(String.valueOf(result));
                    //Toasty.info(getApplicationContext(), "Lens R Price = Rp" + itemPriceR.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkItemPriceL(final String itemId, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        itemPriceL = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemPriceL = Integer.parseInt(jsonObject.getString("price_list"));

                    callBack.onSuccess(String.valueOf(itemPriceL));

                    //Toasty.info(getApplicationContext(), "Lens L Price = Rp" + itemPriceL.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemIdR(final String kodeLensa, final String koridor, final String kodeItem)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_rx_getitemrx;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemIdR     = jsonObject.getString("item_id");
                    itemCodeR   = jsonObject.getString("item_code");
                    descR       = jsonObject.getString("description");
                    itemSphR    = txt_sphr.getText().toString();
                    if (itemSphR == null || itemSphR.isEmpty())
                    {
                        itemSphR = "0.00";
                    }
                    itemCylR    = txt_cylr.getText().toString();
                    if (itemCylR == null || itemCylR.isEmpty())
                    {
                        itemCylR = "0.00";
                    }
                    itemAxsR    = txt_axsr.getText().toString();
                    if (itemAxsR == null || itemAxsR.isEmpty())
                    {
                        itemAxsR = "0";
                    }
                    itemAddR    = txt_addr.getText().toString();
                    if (itemAddR == null || itemAddR.isEmpty())
                    {
                        itemAddR = "0.00";
                    }
                    powerR      = "SPH " + itemSphR + " CYL " + itemCylR + " AXS " + itemAxsR + " ADD " + itemAddR;
                    itemQtyR    = "1";


                    getItemPriceR(itemIdR);
                    getStbStockItemR(itemIdR);
                    //Toasty.success(getApplicationContext(), itemIdR, Toast.LENGTH_LONG).show();

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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("koridor", koridor);
                hashMap.put("kode_item", kodeItem);
                hashMap.put("rl", "R");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemIdL(final String kodeLensa, final String koridor, final String kodeItem)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_rx_getitemrx;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemIdL     = jsonObject.getString("item_id");
                    itemCodeL   = jsonObject.getString("item_code");
                    descL       = jsonObject.getString("description");
                    itemSphL    = txt_sphl.getText().toString();
                    if (itemSphL == null || itemSphL.isEmpty())
                    {
                        itemSphL = "0.00";
                    }
                    itemCylL    = txt_cyll.getText().toString();
                    if (itemCylL == null || itemCylL.isEmpty())
                    {
                        itemCylL = "0.00";
                    }
                    itemAxsL    = txt_axsl.getText().toString();
                    if (itemAxsL == null || itemAxsL.isEmpty())
                    {
                        itemAxsL = "0";
                    }
                    itemAddL    = txt_addl.getText().toString();
                    if (itemAddL == null || itemAddL.isEmpty())
                    {
                        itemAddL = "0.00";
                    }
                    powerL      = "SPH " + itemSphL + " CYL " + itemCylL + " AXS " + itemAxsL + " ADD " + itemAddL;
                    itemQtyL    = "1";

                    getItemPriceL(itemIdL);
                    getStbStockItemL(itemIdL);
                    //Toasty.success(getApplicationContext(), itemIdL, Toast.LENGTH_LONG).show();
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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("koridor", koridor);
                hashMap.put("kode_item", kodeItem);
                hashMap.put("rl", "L");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkStockRLHandle(final String kodeLensa, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_check_stockrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
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
                hashMap.put("kode_lensa", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkStockRL(final String kodeLensa)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_check_stockrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    flagRL = jsonObject.getString("rl");

                    String sphr = txt_sphr.getText().toString();
                    String cylr = txt_cylr.getText().toString();
                    String addr = txt_addr.getText().toString();
                    String sphl = txt_sphl.getText().toString();
                    String cyll = txt_cyll.getText().toString();
                    String addl = txt_addl.getText().toString();

                    if (flagRL.contentEquals("B") || flagRL.contains("B") || flagRL.equals("B"))
                    {
                        if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                        {
                            getItemStockR(kodeLensa, sphr, cylr, addr, "B");
                            itemQtyR    = "1";
                        }
                        else
                        {
                            itemPriceStR = 0;
                        }

                        if (!sphl.isEmpty() | !cyll.isEmpty() | !addl.isEmpty())
                        {
                            getItemStockL(kodeLensa, sphl, cyll, addl, "B");
                            itemQtyL   = "1";
                        }
                        else
                        {
                            itemPriceStL = 0;
                        }
                    }
                    else
                    {
                        if (!sphr.isEmpty() | !cylr.isEmpty() | !addr.isEmpty())
                        {
                            getItemStockR(kodeLensa, sphr, cylr, addr, "R");
                            itemQtyR    = "1";
                        }
                        else
                        {
                            itemPriceStR = 0;
                        }

                        if (!sphl.isEmpty() | !cyll.isEmpty() | !addl.isEmpty())
                        {
                            getItemStockL(kodeLensa, sphl, cyll, addl, "L");
                            itemQtyL    = "1";
                        }
                        else
                        {
                            itemPriceStL = 0;
                        }
                    }

                    //Toasty.success(getApplicationContext(), flagRL, Toast.LENGTH_SHORT).show();
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
                hashMap.put("kode_lensa", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemStockRHandle(final String kodeLensa, final String sph, final String cyl, final String add, final String rl,
                                     final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_st_getitemstock;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("sph", sph);
                hashMap.put("cyl", cyl);
                hashMap.put("add", add);
                hashMap.put("rl", rl);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemStockR(final String kodeLensa, final String sph, final String cyl, final String add, final String rl)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_st_getitemstock;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemIdStR = jsonObject.getString("item_id");
                        itemIdR = itemIdStR;

                        if (!itemIdStR.isEmpty() | !itemIdStR.contentEquals("Null"))
                        {
                            getItemPriceStR(itemIdStR);
                            getStbStockItemR(itemIdStR);
                        }
                        else
                        {
                            itemPriceStR = 0;
                        }
                    }
                    //Toasty.info(getApplicationContext(), "Item Id R = " + itemIdStR, Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("sph", sph);
                hashMap.put("cyl", cyl);
                hashMap.put("add", add);
                hashMap.put("rl", rl);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemStockLHandle(final String kodeLensa, final String sph, final String cyl, final String add, final String rl,
                                     final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_st_getitemstock;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
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
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("sph", sph);
                hashMap.put("cyl", cyl);
                hashMap.put("add", add);
                hashMap.put("rl", rl);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemStockL(final String kodeLensa, final String sph, final String cyl, final String add, final String rl)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_st_getitemstock;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemIdStL = jsonObject.getString("item_id");
                        itemIdL = itemIdStL;

                        if (!itemIdStL.isEmpty() | !itemIdStL.contentEquals("null"))
                        {
                            getItemPriceStL(itemIdStL);
                            getStbStockItemL(itemIdStL);
                        }
                        else
                        {
                            itemPriceStL = 0;
                        }
                    }
                    //Toasty.info(getApplicationContext(), "Item Id L = " + itemIdStL, Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("kode_lensa", kodeLensa);
                hashMap.put("sph", sph);
                hashMap.put("cyl", cyl);
                hashMap.put("add", add);
                hashMap.put("rl", rl);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceR(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        itemPriceR = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemPriceR = Integer.parseInt(jsonObject.getString("price_list"));
                    divName    = jsonObject.getString("div_name");

                    itemTotalPriceR = Integer.parseInt(itemQtyR) * itemPriceR;

                    //Toasty.info(getApplicationContext(), "Lens R Price = Rp" + itemPriceR.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceL(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        itemPriceL = null;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemPriceL = Integer.parseInt(jsonObject.getString("price_list"));
                    divName    = jsonObject.getString("div_name");

                    itemTotalPriceL = Integer.parseInt(itemQtyL) * itemPriceL;

                    //Toasty.info(getApplicationContext(), "Lens L Price = Rp" + itemPriceL.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceStRHandle(final String itemId, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Stok kosong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemPriceStR = Integer.parseInt(jsonObject.getString("price_list"));

                        callBack.onSuccess(itemPriceStR.toString());
                    }
                    //Toasty.info(getApplicationContext(), "Stock R Price = Rp" + itemPriceStR.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceStR(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemPriceStR = Integer.parseInt(jsonObject.getString("price_list"));
                    divName    = jsonObject.getString("div_name");

                    itemPriceR = itemPriceStR;
                    itemTotalPriceR = Integer.parseInt(itemQtyR) * itemPriceR;

                    //Toasty.info(getApplicationContext(), "Stock R Price = Rp" + itemPriceStR.toString(), Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceStLHandle(final String itemId, final VolleyOneCallBack callBack)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Stok kosong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        itemPriceStL = Integer.parseInt(jsonObject.getString("price_list"));

                        callBack.onSuccess(itemPriceStL.toString());
                    }
                    //Toasty.info(getApplicationContext(), "Stock L Price = Rp" + itemPriceStL.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    itemPriceStL = 0;
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getItemPriceStL(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.price_all_getitemprice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemPriceStL = Integer.parseInt(jsonObject.getString("price_list"));
                    divName    = jsonObject.getString("div_name");

                    itemPriceL = itemPriceStL;
                    itemTotalPriceL = Integer.parseInt(itemQtyL) * itemPriceL;

                    //Toasty.info(getApplicationContext(), "Stock L Price = Rp" + itemPriceStL.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    itemPriceStL = 0;
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getStbStockItemR(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.discount_item_getStbItem;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String segment1 = jsonObject.getString("segment_1");
                    String segment2 = jsonObject.getString("segment_2");
                    String segment3 = jsonObject.getString("segment_3");
                    String segment4 = jsonObject.getString("segment_4");
                    String segment5 = jsonObject.getString("segment_5");
                    String segment6 = jsonObject.getString("segment_6");
                    String segment7 = jsonObject.getString("segment_7");
                    String segment8 = jsonObject.getString("segment_8");

                    itemCodeR   = jsonObject.getString("item_code");
                    descR       = jsonObject.getString("description");

                    StringBuilder sb = new StringBuilder();
                    sb.append(segment1).append('.')
                            .append(segment2).append('.')
                            .append(segment3).append('.')
                            .append(segment4).append('.')
                            .append(segment5).append('.')
                            .append(segment6).append('.')
                            .append(segment7).append('.')
                            .append(segment8);

                    prod_attr_valR = sb.toString();
                    getDiscountItem(prod_attr_valR, opticUsername);
                    //Toasty.info(getApplicationContext(), prod_attr_val, Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getStbStockItemL(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.discount_item_getStbItem;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String segment1 = jsonObject.getString("segment_1");
                    String segment2 = jsonObject.getString("segment_2");
                    String segment3 = jsonObject.getString("segment_3");
                    String segment4 = jsonObject.getString("segment_4");
                    String segment5 = jsonObject.getString("segment_5");
                    String segment6 = jsonObject.getString("segment_6");
                    String segment7 = jsonObject.getString("segment_7");
                    String segment8 = jsonObject.getString("segment_8");

                    itemCodeL   = jsonObject.getString("item_code");
                    descL       = jsonObject.getString("description");

                    StringBuilder sb = new StringBuilder();
                    sb.append(segment1).append('.')
                            .append(segment2).append('.')
                            .append(segment3).append('.')
                            .append(segment4).append('.')
                            .append(segment5).append('.')
                            .append(segment6).append('.')
                            .append(segment7).append('.')
                            .append(segment8);

                    prod_attr_valL = sb.toString();
                    getDiscountItem(prod_attr_valL, opticUsername);
                    //Toasty.info(getApplicationContext(), prod_attr_val, Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getDiscountItem(final String prodAttr, final String custNumber)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.discount_item_getDiscount;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    oprDiscount = jsonObject.getInt("operand");
                    listLineNo  = jsonObject.getString("list_line");

                    //Toasty.success(getApplicationContext(), "Diskon = " + oprDiscount + "%", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    oprDiscount = 0;

//                    Toasty.error(getApplicationContext(), oprDiscount.toString(), Toast.LENGTH_SHORT).show();
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
                hashMap.put("prod_attr", prodAttr);
                hashMap.put("customer_number", custNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getFacetItem(final String frametype, final String lensdiv, final String divname)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.facet_item_getFacetItem;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String itemId = jsonObject.getString("item_id");

                    //Toasty.info(getApplicationContext(), "Item id Facet " + itemId, Toast.LENGTH_SHORT).show();

                    getFacetPrice(itemId);
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("frame_type", frametype);
                hashMap.put("lensdiv", lensdiv);
                hashMap.put("divname", divname);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getLensFacet(final String lenstype, final String frametype)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.facet_item_getLensFacet;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String itemId = object.getString("item_id");

                    Log.d("ITEM ID FACET ", itemId);

                    getFacetPrice(itemId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("Error Get Lens Facet", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("lenstype", lenstype);
                hashMap.put("frametype", frametype);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getFacetPrice(final String itemId)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.facet_item_getFacetPrice;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemFacetPrice  = jsonObject.getInt("price_list_item");
                    itemFacetCode   = jsonObject.getString("div_name");
                    facetDescription= jsonObject.getString("product_attr_val_disp");

                    //Toasty.warning(getApplicationContext(), "Facet price = Rp" + itemFacetPrice, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    itemFacetPrice = 0;
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getOtherTintingPrice(final String frame_type)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.tint_item_getOtherTint;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    itemTintPrice   = jsonObject.getInt("price");
                    itemTintingCode = "Tinting " + typeLensa + " " + color_tint;
                    tintingDescription = color_descr;

                    //Toasty.success(getApplicationContext(), "Tinting = Rp" + itemTintPrice, Toast.LENGTH_SHORT, true).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    itemTintPrice = 0;
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
                hashMap.put("frame_type", frame_type);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getOrderNumber(final String user, final String date)
    {
        Config config = new Config();
//        String URL = config.Ip_address + config.lens_summary_getOrderNumber;
        String URL = config.Ip_address + config.orderlens_get_orderid;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    txt_orderNumber.setText(jsonObject.getString("lastnumber"));

                    //Toasty.success(getApplicationContext(), totalOrder, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    //totalOrder = "00000";

                    //Toasty.info(getApplicationContext(), totalOrder, Toast.LENGTH_SHORT).show();
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
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("user", user);
                hashMap.put("date", date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void countOrderTemp(final String user, final String date)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.lens_summary_countTempOrder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    countTemp = jsonObject.getInt("output");
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
                hashMap.put("user", "ALO" + user);
                hashMap.put("date", date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void sumWeightOrder(final String user, final String date)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.lens_summary_sumWeightOrder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    autoSum = jsonObject.getInt("output");
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
                hashMap.put("user", "ALO" + user);
                hashMap.put("date", date);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void deleteOrderTemp(final String user, final String date)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.lens_summary_deleteTempOrder;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("user", "ALO" + user);
                hashMap.put("date", date);
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

    private void updateCity(final String namaOptik, final String alamat, final String provinsi, final String kota)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATECITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), "Information address has been update", Toast.LENGTH_SHORT).show();

                        if (opticFlag.contentEquals("1"))
                        {
                            Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
                            Float lensdiscount = 0f;
                            Float totalPrice = 0f;
                            String lensdesc   = txt_lensdesc.getText().toString();

                            if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
                            {
                                lensprice = itemPriceR + itemPriceL;
                                float discR = oprDiscount * itemPriceR / 100;
                                float discL = oprDiscount * itemPriceL / 100;

                                discountR = discR;
                                discountL = discL;

                                lensdiscount = discR + discL;
                            }
                            else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
                            {
                                lensprice = itemPriceStR + itemPriceStL;
                                float discR = oprDiscount * itemPriceStR / 100;
                                float discL = oprDiscount * itemPriceStL / 100;
                                //Integer disc = oprDiscount * itemPriceSt / 100;

                                discountR = discR;
                                discountL = discL;
                                lensdiscount = discR + discL;
                            }

                            if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
                            {
                                lensfacet   = itemFacetPrice + itemFacetPrice;
                                lenstinting = itemTintPrice + itemTintPrice;

                                qtyFacet = "2";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "2";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
                            }
                            else
                            {
                                //lensprice   = (lensprice / 2);
                                //lensdiscount= (lensdiscount / 2);
                                lensfacet   = itemFacetPrice;
                                lenstinting = itemTintPrice;

                                qtyFacet   = "1";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "1";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;

                                itemWeight  = itemWeight / 2;
                            }

                            totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;

                            File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                            file = sample.toString();

                            uploadTempTxt(file);

                            opticCity = kota;

                            Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
                            intent.putExtra("id_party", opticId.replace(",", ""));
                            intent.putExtra("city_info", opticCity);
                            intent.putExtra("province", opticProvince);
                            intent.putExtra("price_lens", lensprice);
                            intent.putExtra("description_lens", lensdesc);
                            intent.putExtra("discount_lens", lensdiscount);
                            intent.putExtra("facet_lens", lensfacet);
                            intent.putExtra("tinting_lens", lenstinting);
                            intent.putExtra("item_weight", itemWeight);
                            intent.putExtra("flag_pasang", flagPasang);
                            intent.putExtra("username_info", opticUsername);
                            intent.putExtra("optic_flag", opticFlag);
                            intent.putExtra("flag_shipping", flagShipping);
                            intent.putExtra("order_number", txt_orderNumber.getText().toString());
                            intent.putExtra("patient_name", txt_patientName.getText().toString());
                            intent.putExtra("phone_number", txt_phonenumber.getText().toString());
                            intent.putExtra("note", txt_orderInformation.getText().toString());
                            intent.putExtra("prodDivType", divName);
                            intent.putExtra("lenstype", txt_lenstype.getText().toString());
                            intent.putExtra("lensdesc", txt_lensdesc.getText().toString());
                            intent.putExtra("categoryLens", categoryLens);
                            Log.d("FORM ORDER : ", categoryLens);

                            intent.putExtra("sphR", txt_sphr.getText().toString());
                            intent.putExtra("sphL", txt_sphl.getText().toString());
                            intent.putExtra("cylR", txt_cylr.getText().toString());
                            intent.putExtra("cylL", txt_cyll.getText().toString());
                            intent.putExtra("axsR", txt_axsr.getText().toString());
                            intent.putExtra("axsL", txt_axsl.getText().toString());
                            intent.putExtra("addR", txt_addr.getText().toString());
                            intent.putExtra("addL", txt_addl.getText().toString());
                            intent.putExtra("coatCode", coating);
                            intent.putExtra("coatDesc", txt_coatdescr.getText().toString());
                            intent.putExtra("tintCode", color_tint);
                            intent.putExtra("tintDesc", color_descr);
                            intent.putExtra("corridor", corridor);
                            intent.putExtra("mpdR", txt_mpdr.getText().toString());
                            intent.putExtra("mpdL", txt_mpdl.getText().toString());
                            intent.putExtra("pv", txt_segh.getText().toString());
                            intent.putExtra("wrap", txt_wrap.getText().toString());
                            intent.putExtra("panto", txt_phantose.getText().toString());
                            intent.putExtra("vd", txt_vertex.getText().toString());
                            intent.putExtra("facetInfo", txt_infofacet.getText().toString());
                            intent.putExtra("frameModel", spin_framemodel.getText().toString());
                            intent.putExtra("dbl", txt_dbl.getText().toString());
                            intent.putExtra("hor", txt_hor.getText().toString());
                            intent.putExtra("ver", txt_ver.getText().toString());
                            intent.putExtra("frameCode", txt_framebrand.getText().toString());

                            intent.putExtra("margin_lens", "35");
                            intent.putExtra("extramargin_lens", "15");

                            //Area Lens R
                            intent.putExtra("itemid_R", itemIdR);
                            intent.putExtra("itemcode_R", itemCodeR);
                            intent.putExtra("description_R", descR);
                            intent.putExtra("power_R", powerR);
                            intent.putExtra("qty_R", itemQtyR);
                            intent.putExtra("itemprice_R", itemPriceR);
                            intent.putExtra("itemtotal_R", itemTotalPriceR);

                            //Area Lens L
                            intent.putExtra("itemid_L", itemIdL);
                            intent.putExtra("itemcode_L", itemCodeL);
                            intent.putExtra("description_L", descL);
                            intent.putExtra("power_L", powerL);
                            intent.putExtra("qty_L", itemQtyL);
                            intent.putExtra("itemprice_L", itemPriceL);
                            intent.putExtra("itemtotal_L", itemTotalPriceL);

                            //Area Diskon
                            intent.putExtra("description_diskon", listLineNo);
                            intent.putExtra("discount_r", discountR);
                            intent.putExtra("discount_l", discountL);
                            intent.putExtra("extra_margin_discount", "10");
                            intent.putExtra("prod_attr_valR", prod_attr_valR);
                            intent.putExtra("prod_attr_valL", prod_attr_valL);

                            //Area Facet
                            intent.putExtra("itemcode_facet", itemFacetCode);
                            intent.putExtra("description_facet", facetDescription);
                            intent.putExtra("qty_facet", qtyFacet);
                            intent.putExtra("price_facet", itemFacetPrice);
                            intent.putExtra("total_facet", totalFacet);
                            intent.putExtra("margin_facet", "8");
                            intent.putExtra("extra_margin_facet", "8");

                            //Area Tinting
                            intent.putExtra("itemcode_tinting", itemTintingCode);
                            intent.putExtra("description_tinting", tintingDescription);
                            intent.putExtra("qty_tinting", qtyTinting);
                            intent.putExtra("price_tinting", itemTintPrice);
                            intent.putExtra("total_tinting", totalTinting);
                            intent.putExtra("margin_tinting", "8");
                            intent.putExtra("extra_margin_tinting", "8");

                            intent.putExtra("total_price", totalPrice);

                            startActivity(intent);

                            finish();
                        }
                        else
                        {
                            Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
                            Float lensdiscount = 0f;
                            Float totalPrice = 0f;
                            String lensdesc   = txt_lensdesc.getText().toString();

                            if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
                            {
                                lensprice = itemPriceR + itemPriceL;
                                float discR = oprDiscount * itemPriceR / 100;
                                float discL = oprDiscount * itemPriceL / 100;

                                discountR = discR;
                                discountL = discL;

                                lensdiscount = discR + discL;
                            }
                            else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
                            {
                                lensprice = itemPriceStR + itemPriceStL;
                                float discR = oprDiscount * itemPriceStR / 100;
                                float discL = oprDiscount * itemPriceStL / 100;
                                //Integer disc = oprDiscount * itemPriceSt / 100;

                                discountR = discR;
                                discountL = discL;
                                lensdiscount = discR + discL;
                            }

                            if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
                            {
                                lensfacet   = itemFacetPrice + itemFacetPrice;
                                lenstinting = itemTintPrice + itemTintPrice;

                                qtyFacet = "2";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "2";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
                            }
                            else
                            {
                                //lensprice   = (lensprice / 2);
                                //lensdiscount= (lensdiscount / 2);
                                lensfacet   = itemFacetPrice;
                                lenstinting = itemTintPrice;

                                qtyFacet   = "1";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "1";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;

                                itemWeight  = itemWeight / 2;
                            }

                            totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;

                            File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                            file = sample.toString();

                            uploadTxt(file);

                            Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
                            intent.putExtra("id_party", opticId.replace(",", ""));
                            intent.putExtra("city_info", opticCity);
                            intent.putExtra("province", opticProvince);
                            intent.putExtra("price_lens", lensprice);
                            intent.putExtra("description_lens", lensdesc);
                            intent.putExtra("discount_lens", lensdiscount);
                            intent.putExtra("facet_lens", lensfacet);
                            intent.putExtra("tinting_lens", lenstinting);
                            intent.putExtra("item_weight", itemWeight);
                            intent.putExtra("flag_pasang", flagPasang);
                            intent.putExtra("username_info", opticUsername);
                            intent.putExtra("optic_flag", opticFlag);
                            intent.putExtra("flag_shipping", flagShipping);
                            intent.putExtra("order_number", txt_orderNumber.getText().toString());
                            intent.putExtra("patient_name", txt_patientName.getText().toString());
                            intent.putExtra("phone_number", txt_phonenumber.getText().toString());
                            intent.putExtra("note", txt_orderInformation.getText().toString());
                            intent.putExtra("prodDivType", divName);
                            intent.putExtra("lenstype", txt_lenstype.getText().toString());
                            intent.putExtra("lensdesc", txt_lensdesc.getText().toString());
                            intent.putExtra("categoryLens", categoryLens);
                            Log.d("FORM ORDER : ", categoryLens);

                            intent.putExtra("sphR", txt_sphr.getText().toString());
                            intent.putExtra("sphL", txt_sphl.getText().toString());
                            intent.putExtra("cylR", txt_cylr.getText().toString());
                            intent.putExtra("cylL", txt_cyll.getText().toString());
                            intent.putExtra("axsR", txt_axsr.getText().toString());
                            intent.putExtra("axsL", txt_axsl.getText().toString());
                            intent.putExtra("addR", txt_addr.getText().toString());
                            intent.putExtra("addL", txt_addl.getText().toString());
                            intent.putExtra("coatCode", coating);
                            intent.putExtra("coatDesc", txt_coatdescr.getText().toString());
                            intent.putExtra("tintCode", color_tint);
                            intent.putExtra("tintDesc", color_descr);
                            intent.putExtra("corridor", corridor);
                            intent.putExtra("mpdR", txt_mpdr.getText().toString());
                            intent.putExtra("mpdL", txt_mpdl.getText().toString());
                            intent.putExtra("pv", txt_segh.getText().toString());
                            intent.putExtra("wrap", txt_wrap.getText().toString());
                            intent.putExtra("panto", txt_phantose.getText().toString());
                            intent.putExtra("vd", txt_vertex.getText().toString());
                            intent.putExtra("facetInfo", txt_infofacet.getText().toString());
                            intent.putExtra("frameModel", spin_framemodel.getText().toString());
                            intent.putExtra("dbl", txt_dbl.getText().toString());
                            intent.putExtra("hor", txt_hor.getText().toString());
                            intent.putExtra("ver", txt_ver.getText().toString());
                            intent.putExtra("frameCode", txt_framebrand.getText().toString());

                            intent.putExtra("margin_lens", "35");
                            intent.putExtra("extramargin_lens", "15");

                            //Area Lens R
                            intent.putExtra("itemid_R", itemIdR);
                            intent.putExtra("itemcode_R", itemCodeR);
                            intent.putExtra("description_R", descR);
                            intent.putExtra("power_R", powerR);
                            intent.putExtra("qty_R", itemQtyR);
                            intent.putExtra("itemprice_R", itemPriceR);
                            intent.putExtra("itemtotal_R", itemTotalPriceR);

                            //Area Lens L
                            intent.putExtra("itemid_L", itemIdL);
                            intent.putExtra("itemcode_L", itemCodeL);
                            intent.putExtra("description_L", descL);
                            intent.putExtra("power_L", powerL);
                            intent.putExtra("qty_L", itemQtyL);
                            intent.putExtra("itemprice_L", itemPriceL);
                            intent.putExtra("itemtotal_L", itemTotalPriceL);

                            //Area Diskon
                            intent.putExtra("description_diskon", listLineNo);
                            intent.putExtra("discount_r", discountR);
                            intent.putExtra("discount_l", discountL);
                            intent.putExtra("extra_margin_discount", "10");
                            intent.putExtra("prod_attr_valR", prod_attr_valR);
                            intent.putExtra("prod_attr_valL", prod_attr_valL);

                            //Area Facet
                            intent.putExtra("itemcode_facet", itemFacetCode);
                            intent.putExtra("description_facet", facetDescription);
                            intent.putExtra("qty_facet", qtyFacet);
                            intent.putExtra("price_facet", itemFacetPrice);
                            intent.putExtra("total_facet", totalFacet);
                            intent.putExtra("margin_facet", "8");
                            intent.putExtra("extra_margin_facet", "8");

                            //Area Tinting
                            intent.putExtra("itemcode_tinting", itemTintingCode);
                            intent.putExtra("description_tinting", tintingDescription);
                            intent.putExtra("qty_tinting", qtyTinting);
                            intent.putExtra("price_tinting", itemTintPrice);
                            intent.putExtra("total_tinting", totalTinting);
                            intent.putExtra("margin_tinting", "8");
                            intent.putExtra("extra_margin_tinting", "8");

                            intent.putExtra("total_price", totalPrice);

                            startActivity(intent);

                            finish();
                        }
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

    private void checkCity(final String namaOptik)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKCITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String city = object.getString("city");

                    if (city.equals("-") || city.isEmpty())
                    {
//                        Toasty.error(getApplicationContext(), "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();

                        final UniversalFontTextView lblAddress, lblProvince, lblCity;
                        final BootstrapEditText txtAddress;
                        final BetterSpinner spinCity, spinProvince;
                        BootstrapButton btnUpdate, btnCancel;

                        getProvince();

                        final LovelyCustomDialog lovelyCustomDialog = new LovelyCustomDialog(FormOrderLensActivity.this);
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

                        spinProvince.setAdapter(new ArrayAdapter<>(FormOrderLensActivity.this,
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

                                spinCity.setAdapter(new ArrayAdapter<>(FormOrderLensActivity.this,
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
                                    updateCity(namaOptik, alamat, provinsi, kota);

                                    lovelyCustomDialog.dismiss();
                                }
                             }
                        });

                        lovelyCustomDialog.show();
                    }
                    else
                    {
                        if (opticFlag.contentEquals("1"))
                        {
                            Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
                            Float lensdiscount = 0f;
                            Float totalPrice = 0f;
                            String lensdesc   = txt_lensdesc.getText().toString();

                            if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
                            {
                                lensprice = itemPriceR + itemPriceL;
                                float discR = oprDiscount * itemPriceR / 100;
                                float discL = oprDiscount * itemPriceL / 100;

                                discountR = discR;
                                discountL = discL;

                                lensdiscount = discR + discL;
                            }
                            else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
                            {
                                lensprice = itemPriceStR + itemPriceStL;
                                float discR = oprDiscount * itemPriceStR / 100;
                                float discL = oprDiscount * itemPriceStL / 100;
                                //Integer disc = oprDiscount * itemPriceSt / 100;

                                discountR = discR;
                                discountL = discL;
                                lensdiscount = discR + discL;
                            }

                            if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
                            {
                                lensfacet   = itemFacetPrice + itemFacetPrice;
                                lenstinting = itemTintPrice + itemTintPrice;

                                qtyFacet = "2";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "2";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
                            }
                            else
                            {
                                //lensprice   = (lensprice / 2);
                                //lensdiscount= (lensdiscount / 2);
                                lensfacet   = itemFacetPrice;
                                lenstinting = itemTintPrice;

                                qtyFacet   = "1";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "1";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;

                                itemWeight  = itemWeight / 2;
                            }

                            totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;

                            File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                            file = sample.toString();

                            uploadTempTxt(file);



                            Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
                            intent.putExtra("id_party", opticId.replace(",", ""));
                            intent.putExtra("city_info", opticCity);
                            intent.putExtra("province", opticProvince);
                            intent.putExtra("price_lens", lensprice);
                            intent.putExtra("description_lens", lensdesc);
                            intent.putExtra("discount_lens", lensdiscount);
                            intent.putExtra("facet_lens", lensfacet);
                            intent.putExtra("tinting_lens", lenstinting);
                            intent.putExtra("item_weight", itemWeight);
                            intent.putExtra("flag_pasang", flagPasang);
                            intent.putExtra("username_info", opticUsername);
                            intent.putExtra("optic_flag", opticFlag);
                            intent.putExtra("flag_shipping", flagShipping);
                            intent.putExtra("order_number", txt_orderNumber.getText().toString());
                            intent.putExtra("patient_name", txt_patientName.getText().toString());
                            intent.putExtra("phone_number", txt_phonenumber.getText().toString());
                            intent.putExtra("note", txt_orderInformation.getText().toString());
                            intent.putExtra("prodDivType", divName);
                            intent.putExtra("lenstype", txt_lenstype.getText().toString());
                            intent.putExtra("lensdesc", txt_lensdesc.getText().toString());
                            intent.putExtra("categoryLens", categoryLens);
                            Log.d("FORM ORDER : ", categoryLens);

                            intent.putExtra("sphR", txt_sphr.getText().toString());
                            intent.putExtra("sphL", txt_sphl.getText().toString());
                            intent.putExtra("cylR", txt_cylr.getText().toString());
                            intent.putExtra("cylL", txt_cyll.getText().toString());
                            intent.putExtra("axsR", txt_axsr.getText().toString());
                            intent.putExtra("axsL", txt_axsl.getText().toString());
                            intent.putExtra("addR", txt_addr.getText().toString());
                            intent.putExtra("addL", txt_addl.getText().toString());
                            intent.putExtra("coatCode", coating);
                            intent.putExtra("coatDesc", txt_coatdescr.getText().toString());
                            intent.putExtra("tintCode", color_tint);
                            intent.putExtra("tintDesc", color_descr);
                            intent.putExtra("corridor", corridor);
                            intent.putExtra("mpdR", txt_mpdr.getText().toString());
                            intent.putExtra("mpdL", txt_mpdl.getText().toString());
                            intent.putExtra("pv", txt_segh.getText().toString());
                            intent.putExtra("wrap", txt_wrap.getText().toString());
                            intent.putExtra("panto", txt_phantose.getText().toString());
                            intent.putExtra("vd", txt_vertex.getText().toString());
                            intent.putExtra("facetInfo", txt_infofacet.getText().toString());
                            intent.putExtra("frameModel", spin_framemodel.getText().toString());
                            intent.putExtra("dbl", txt_dbl.getText().toString());
                            intent.putExtra("hor", txt_hor.getText().toString());
                            intent.putExtra("ver", txt_ver.getText().toString());
                            intent.putExtra("frameCode", txt_framebrand.getText().toString());

                            intent.putExtra("margin_lens", "35");
                            intent.putExtra("extramargin_lens", "15");

                            //Area Lens R
                            intent.putExtra("itemid_R", itemIdR);
                            intent.putExtra("itemcode_R", itemCodeR);
                            intent.putExtra("description_R", descR);
                            intent.putExtra("power_R", powerR);
                            intent.putExtra("qty_R", itemQtyR);
                            intent.putExtra("itemprice_R", itemPriceR);
                            intent.putExtra("itemtotal_R", itemTotalPriceR);

                            //Area Lens L
                            intent.putExtra("itemid_L", itemIdL);
                            intent.putExtra("itemcode_L", itemCodeL);
                            intent.putExtra("description_L", descL);
                            intent.putExtra("power_L", powerL);
                            intent.putExtra("qty_L", itemQtyL);
                            intent.putExtra("itemprice_L", itemPriceL);
                            intent.putExtra("itemtotal_L", itemTotalPriceL);

                            //Area Diskon
                            intent.putExtra("description_diskon", listLineNo);
                            intent.putExtra("discount_r", discountR);
                            intent.putExtra("discount_l", discountL);
                            intent.putExtra("extra_margin_discount", "10");
                            intent.putExtra("prod_attr_valR", prod_attr_valR);
                            intent.putExtra("prod_attr_valL", prod_attr_valL);

                            //Area Facet
                            intent.putExtra("itemcode_facet", itemFacetCode);
                            intent.putExtra("description_facet", facetDescription);
                            intent.putExtra("qty_facet", qtyFacet);
                            intent.putExtra("price_facet", itemFacetPrice);
                            intent.putExtra("total_facet", totalFacet);
                            intent.putExtra("margin_facet", "8");
                            intent.putExtra("extra_margin_facet", "8");

                            //Area Tinting
                            intent.putExtra("itemcode_tinting", itemTintingCode);
                            intent.putExtra("description_tinting", tintingDescription);
                            intent.putExtra("qty_tinting", qtyTinting);
                            intent.putExtra("price_tinting", itemTintPrice);
                            intent.putExtra("total_tinting", totalTinting);
                            intent.putExtra("margin_tinting", "8");
                            intent.putExtra("extra_margin_tinting", "8");

                            intent.putExtra("total_price", totalPrice);

                            startActivity(intent);

                            finish();
                        }
                        else
                        {
                            Integer lensprice = 0, lensfacet = 0, lenstinting = 0;
                            Float lensdiscount = 0f;
                            Float totalPrice = 0f;
                            String lensdesc   = txt_lensdesc.getText().toString();

                            if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
                            {
                                lensprice = itemPriceR + itemPriceL;
                                float discR = oprDiscount * itemPriceR / 100;
                                float discL = oprDiscount * itemPriceL / 100;

                                discountR = discR;
                                discountL = discL;

                                lensdiscount = discR + discL;
                            }
                            else if (categoryLens.equals("S") || categoryLens.contentEquals("S"))
                            {
                                lensprice = itemPriceStR + itemPriceStL;
                                float discR = oprDiscount * itemPriceStR / 100;
                                float discL = oprDiscount * itemPriceStL / 100;
                                //Integer disc = oprDiscount * itemPriceSt / 100;

                                discountR = discR;
                                discountL = discL;
                                lensdiscount = discR + discL;
                            }

                            if (flagPasang.equals("2") | flagPasang.contains("2") | flagPasang.contentEquals("2"))
                            {
                                lensfacet   = itemFacetPrice + itemFacetPrice;
                                lenstinting = itemTintPrice + itemTintPrice;

                                qtyFacet = "2";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "2";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;
                            }
                            else
                            {
                                //lensprice   = (lensprice / 2);
                                //lensdiscount= (lensdiscount / 2);
                                lensfacet   = itemFacetPrice;
                                lenstinting = itemTintPrice;

                                qtyFacet   = "1";
                                totalFacet = Integer.parseInt(qtyFacet) * itemFacetPrice;

                                qtyTinting = "1";
                                totalTinting = Integer.parseInt(qtyTinting) * itemTintPrice;

                                itemWeight  = itemWeight / 2;
                            }

                            totalPrice =  ((float) lensprice - lensdiscount) + (float) lensfacet + (float) lenstinting;

                            File sample = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp/" + filename);
                            file = sample.toString();

                            uploadTxt(file);


                            Intent intent = new Intent(FormOrderLensActivity.this, FormLensSummaryActivity.class);
                            intent.putExtra("id_party", opticId.replace(",", ""));
                            intent.putExtra("city_info", opticCity);
                            intent.putExtra("province", opticProvince);
                            intent.putExtra("price_lens", lensprice);
                            intent.putExtra("description_lens", lensdesc);
                            intent.putExtra("discount_lens", lensdiscount);
                            intent.putExtra("facet_lens", lensfacet);
                            intent.putExtra("tinting_lens", lenstinting);
                            intent.putExtra("item_weight", itemWeight);
                            intent.putExtra("flag_pasang", flagPasang);
                            intent.putExtra("username_info", opticUsername);
                            intent.putExtra("optic_flag", opticFlag);
                            intent.putExtra("flag_shipping", flagShipping);
                            intent.putExtra("order_number", txt_orderNumber.getText().toString());
                            intent.putExtra("patient_name", txt_patientName.getText().toString());
                            intent.putExtra("phone_number", txt_phonenumber.getText().toString());
                            intent.putExtra("note", txt_orderInformation.getText().toString());
                            intent.putExtra("prodDivType", divName);
                            intent.putExtra("lenstype", txt_lenstype.getText().toString());
                            intent.putExtra("lensdesc", txt_lensdesc.getText().toString());
                            intent.putExtra("categoryLens", categoryLens);
                            Log.d("FORM ORDER : ", categoryLens);

                            intent.putExtra("sphR", txt_sphr.getText().toString());
                            intent.putExtra("sphL", txt_sphl.getText().toString());
                            intent.putExtra("cylR", txt_cylr.getText().toString());
                            intent.putExtra("cylL", txt_cyll.getText().toString());
                            intent.putExtra("axsR", txt_axsr.getText().toString());
                            intent.putExtra("axsL", txt_axsl.getText().toString());
                            intent.putExtra("addR", txt_addr.getText().toString());
                            intent.putExtra("addL", txt_addl.getText().toString());
                            intent.putExtra("coatCode", coating);
                            intent.putExtra("coatDesc", txt_coatdescr.getText().toString());
                            intent.putExtra("tintCode", color_tint);
                            intent.putExtra("tintDesc", color_descr);
                            intent.putExtra("corridor", corridor);
                            intent.putExtra("mpdR", txt_mpdr.getText().toString());
                            intent.putExtra("mpdL", txt_mpdl.getText().toString());
                            intent.putExtra("pv", txt_segh.getText().toString());
                            intent.putExtra("wrap", txt_wrap.getText().toString());
                            intent.putExtra("panto", txt_phantose.getText().toString());
                            intent.putExtra("vd", txt_vertex.getText().toString());
                            intent.putExtra("facetInfo", txt_infofacet.getText().toString());
                            intent.putExtra("frameModel", spin_framemodel.getText().toString());
                            intent.putExtra("dbl", txt_dbl.getText().toString());
                            intent.putExtra("hor", txt_hor.getText().toString());
                            intent.putExtra("ver", txt_ver.getText().toString());
                            intent.putExtra("frameCode", txt_framebrand.getText().toString());

                            intent.putExtra("margin_lens", "35");
                            intent.putExtra("extramargin_lens", "15");

                            //Area Lens R
                            intent.putExtra("itemid_R", itemIdR);
                            intent.putExtra("itemcode_R", itemCodeR);
                            intent.putExtra("description_R", descR);
                            intent.putExtra("power_R", powerR);
                            intent.putExtra("qty_R", itemQtyR);
                            intent.putExtra("itemprice_R", itemPriceR);
                            intent.putExtra("itemtotal_R", itemTotalPriceR);

                            //Area Lens L
                            intent.putExtra("itemid_L", itemIdL);
                            intent.putExtra("itemcode_L", itemCodeL);
                            intent.putExtra("description_L", descL);
                            intent.putExtra("power_L", powerL);
                            intent.putExtra("qty_L", itemQtyL);
                            intent.putExtra("itemprice_L", itemPriceL);
                            intent.putExtra("itemtotal_L", itemTotalPriceL);

                            //Area Diskon
                            intent.putExtra("description_diskon", listLineNo);
                            intent.putExtra("discount_r", discountR);
                            intent.putExtra("discount_l", discountL);
                            intent.putExtra("extra_margin_discount", "10");
                            intent.putExtra("prod_attr_valR", prod_attr_valR);
                            intent.putExtra("prod_attr_valL", prod_attr_valL);

                            //Area Facet
                            intent.putExtra("itemcode_facet", itemFacetCode);
                            intent.putExtra("description_facet", facetDescription);
                            intent.putExtra("qty_facet", qtyFacet);
                            intent.putExtra("price_facet", itemFacetPrice);
                            intent.putExtra("total_facet", totalFacet);
                            intent.putExtra("margin_facet", "8");
                            intent.putExtra("extra_margin_facet", "8");

                            //Area Tinting
                            intent.putExtra("itemcode_tinting", itemTintingCode);
                            intent.putExtra("description_tinting", tintingDescription);
                            intent.putExtra("qty_tinting", qtyTinting);
                            intent.putExtra("price_tinting", itemTintPrice);
                            intent.putExtra("total_tinting", totalTinting);
                            intent.putExtra("margin_tinting", "8");
                            intent.putExtra("extra_margin_tinting", "8");

                            intent.putExtra("total_price", totalPrice);

                            startActivity(intent);

                            finish();
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

    private void cekStok(final String itemid, final VolleyOneCallBack callBack)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKSTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.d("Cek Stok", response);
//
//                    sisaStok = jsonObject.getInt("qty_stock");
//                    Log.d("Cek Stok","qty stock : " + sisaStok);
//                    descriptionStock = jsonObject.getString("desc_stock");
//                    Log.d("Cek Stok","description stock : " + descriptionStock);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Cek Stok", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemid);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void cekStokR(final String itemid, final VolleyOneCallBack callBack)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKSTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.d("Cek Stok R", response);
//
//                    sisaStokR = jsonObject.getInt("qty_stock");
//                    Log.d("Cek Stok R", "Qty stock R : " + sisaStokR);
////                    descriptionStockR = jsonObject.getString("desc_stock");
////                    Log.d("Cek Stok R", "Description stock R : " + descriptionStockR);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Cek Stok R", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemid);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void cekStokL(final String itemid, final VolleyOneCallBack callBack)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKSTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.d("Cek Stok L", response);
//
//                    sisaStokL = jsonObject.getInt("qty_stock");
//                    Log.d("Cek Stok L", "Qty stock L : " + sisaStokL);
////                    descriptionStockR = jsonObject.getString("desc_stock");
////                    Log.d("Cek Stok R", "Description stock R : " + descriptionStockR);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Cek Stok R", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemid);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}