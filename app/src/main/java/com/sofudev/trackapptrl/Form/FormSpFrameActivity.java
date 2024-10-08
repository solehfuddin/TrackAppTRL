package com.sofudev.trackapptrl.Form;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageMultiResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_add_framesp;
import com.sofudev.trackapptrl.Adapter.Adapter_frame_brand;
import com.sofudev.trackapptrl.Adapter.Adapter_framesp;
import com.sofudev.trackapptrl.Adapter.Adapter_framesp_multi;
import com.sofudev.trackapptrl.Adapter.Adapter_framesp_qty;
import com.sofudev.trackapptrl.Adapter.Adapter_sorting_onhand;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.MultiSelectSpFrame;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_frame_brand;
import com.sofudev.trackapptrl.Data.Data_frame_header;
import com.sofudev.trackapptrl.Data.Data_frame_item;
import com.sofudev.trackapptrl.Data.Data_sortingonhand;
import com.sofudev.trackapptrl.Data.Data_spheader;
import com.sofudev.trackapptrl.LocalDb.Db.AddFrameSpHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelFrameSp;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class FormSpFrameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = FormSpFrameActivity.class.getSimpleName();
    Config config = new Config();

    String URLDETAILPRODUCT  = config.Ip_address + config.frame_showdetail_product;
    String URL_GETORAID      = config.Ip_address + config.frame_getoracle_id;
    String allDataUrl        = config.Ip_address + config.frame_bestproduct_showAllData;
//    String sortCategoryUrl   = config.Ip_address + config.frame_filterby_category;
//    String searchFrameUrl    = config.Ip_address + config.frame_filterby_keyword;
    String URL_GETSPHEADER   = config.Ip_address + config.ordersp_get_spHeader;
    String URL_INSERTHEADER  = config.Ip_address + config.frame_insert_header;
    String URL_INSERTNONTPAYMENT = config.Ip_address + config.frame_insert_statusnonpayment;
    String URL_INSERTLINETITEM   = config.Ip_address + config.frame_insert_lineitem;
    String URL_INSERTSPHEADER    = config.Ip_address + config.ordersp_insert_spHeader;
    String URL_INSERTDURATION    = config.Ip_address + config.ordersp_insert_duration;
    String URL_INSERTDURATIONBIN = config.Ip_address + config.ordersp_insert_durationbin;
    String URL_INSERTSAMTEMP     = config.Ip_address + config.ordersp_insert_samTemp;
    String URL_GETBRANDFRAME     = config.Ip_address + config.spframe_get_framebrand;
    String URL_GETFRAMEBYBRAND   = config.Ip_address + config.spframe_get_byframe;
    String URL_GETFRAMEBYBRANDFLAG = config.Ip_address + config.spframe_get_byframe_flag;
    String URL_GETFRAMESEARCH    = config.Ip_address + config.spframe_get_searchframe;
    String URL_GETFRAMESEARCHBRAND = config.Ip_address + config.spframe_get_searchframebrand;
    String URL_GETFRAMESEARCHBRANDFLAG = config.Ip_address + config.spframe_get_searchframebrandflag;
    String URL_GETFRAMEBARCODE   = config.Ip_address + config.spframe_get_searchbarcode;
    String URL_GETFRAMEBARCODEFLAG   = config.Ip_address + config.spframe_get_searchbarcodeflag;
    String URL_GETFRAMEBYITEMID  = config.Ip_address + config.spframe_get_byitemid;
    String URL_GETFRAMEBYITEMIDFLAG  = config.Ip_address + config.spframe_get_byitemidflag;
    String URL_CEKQTYITEM     = config.Ip_address + config.spframe_get_qtybyitemflag;
    String URL_UPDATESIGNED   = config.Ip_address + config.ordersp_update_digitalsigned;
    String URL_UPDATEPHOTO    = config.Ip_address + config.ordersp_update_photo;

    boolean isFotoUploaded = false;

    Adapter_framesp adapter_framesp;
    Adapter_frame_brand adapter_frame_brand;
//    Adapter_framesp_qty adapter_framesp_qty;
    Adapter_framesp_multi adapter_framesp_multi;
    Adapter_sorting_onhand adapter_sorting_onhand;

    private BottomDialog payBottomDialog;
    private View custom;
    ConstraintLayout constraintLayoutOpticName;
    LinearLayout progressLayout;
    ProgressWheel loader;
    ImageView imgFrameNotFound, imgPhoto;
    TextView txtCounter, txtTmp, txtCounterBrand, txtCounterTotal, txtImgLoc;
    Switch swFlag;
    RippleView btnPilihFrame;
    RecyclerView recyclerFrame, recyclerItemFrame;
    EditText txtBarcode;
    ImageView imgNotfound, btnBack;
    LinearLayout linearEmpty;
    CardView cardView, cardSummary;
//    ScrollView scrollView;
    NestedScrollView scrollView;
    Button btnSave;
    UniversalFontTextView txtPrice, txtDisc, txtShipping, txtTotalPrice, txtOpticName, txtFlag;
    UniversalFontTextView txtPriceBottom, txtDiscBottom, txtTotalQtyBottom, txtTotalBottom;
    BootstrapEditText edDiscBottom, edCustomDisc, edNotes, edNamaCustomer;
    EditText txtSearch;
    View animateView;
    LinearLayout linearCounter;
    RelativeLayout animateCard;
    ImageView animateImg;
    Boolean isUp;
    boolean isDigitalSigned;
    Uri img_uri, signed_uri;
    String filesigned, signedpath;

    List<ModelFrameSp> modelFrameSpList = new ArrayList<>();
    List<Data_sortingonhand> itemSorting = new ArrayList<>();
    List<Data_fragment_bestproduct> itemBestProduct = new ArrayList<>();
    List<Data_fragment_bestproduct> returnItemBestProduct = new ArrayList<>();
    List<Data_frame_brand> itemCategory = new ArrayList<>();
    String totalData, selectCategory, productId, shipmentPrice, addpos, newpos, dataCatItem,
            dataSortItem, itemSortId, itemSortDesc;
    String scanContent;
    Integer limit, from, hasil;
    int pos = 0, isLoad = 0, isInvalid = 0, totalQty = 0;
    int priceDisc, shippingId, flagPayment, headerDp;
    double totalDisc, totalPrice, totalAllPrice;
    String opticId, opticName, opticProvince, opticUsername, opticCity, opticAddress, subcustId, subcustLocId, idSp, flag, idPartySales;
    String headerNoSp;
    String headerTipeSp;
    String headerSales;
    String headerShipNumber, headerCustName;
    String headerAddress;
    String headerCity;
    String headerOrderVia;
    String headerDisc, headerCustomDisc;
    String headerCondition;
    String headerInstallment;
    String headerStartInstallment;
    String headerShippingAddress;
    String headerStatus;
    String headerImage, headerSignedPath, filename, imgpath;
    Boolean check, isBarcode, isClear, isImage;
    IntentResult scanningResult;
    String titleQtyBrand = "Total";
    String titleQtyTotal = "Total Qty : ";

    AddFrameSpHelper addFrameSpHelper;
    Adapter_add_framesp adapter_add_framesp;

    Data_frame_header dataFrameHeader;
    Data_frame_item dataFrameItem;
    Data_spheader dataSpHeader = new Data_spheader();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int qtyTemp;
    List<Boolean> checkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sp_frame);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        UniversalFontComponents.init(this);

        btnPilihFrame = findViewById(R.id.form_spframe_btnframe);
        recyclerItemFrame = findViewById(R.id.form_spframe_recyclerview);
        linearEmpty = findViewById(R.id.form_spframe_linearLayout);
        scrollView  = findViewById(R.id.form_spframe_scrollview);
        btnBack = findViewById(R.id.form_spframe_btnback);
        txtPrice = findViewById(R.id.form_spframe_txtitemprice);
        txtDisc  = findViewById(R.id.form_spframe_txtitemdisc);
        txtShipping = findViewById(R.id.form_spframe_txtitemship);
        txtTotalPrice = findViewById(R.id.form_spframe_txttotalprice);
        txtBarcode = findViewById(R.id.form_spframe_txtBarcode);
        txtFlag    = findViewById(R.id.form_spframe_txtflag);
        swFlag     = findViewById(R.id.form_spframe_swflag);
        txtTmp     = findViewById(R.id.form_spframe_txttmp);
        txtOpticName = findViewById(R.id.form_spframe_txtopticname);
        cardSummary = findViewById(R.id.form_spframe_cardsummary);
        cardView = findViewById(R.id.form_spframe_cardview);
        btnSave = findViewById(R.id.form_spframe_btncontinue);
        constraintLayoutOpticName = findViewById(R.id.form_spframe_layoutopticname);
        animateView = findViewById(R.id.form_spframe_rlopticname);
        animateCard = findViewById(R.id.form_spframe_handleopticname);
        animateImg = findViewById(R.id.form_spframe_imgopticname);

        animateView.setVisibility(View.VISIBLE);
        isUp = true;

        btnSave.setOnClickListener(this);
        btnPilihFrame.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        cardView.setOnClickListener(this);
        cardSummary.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            ActivityCompat.requestPermissions(FormSpFrameActivity.this,
                    permissions(),
                    1);
        }

        animateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUp) {
                    slideHide(animateView);
                    animateImg.setImageResource(R.drawable.ic_expanded);
                }
                else {
                    slideShow(animateView);
                    animateImg.setImageResource(R.drawable.ic_collapse);
                }
                isUp = !isUp;
            }
        });

        flagPayment = 1;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerItemFrame.setLayoutManager(layoutManager);

        addFrameSpHelper = AddFrameSpHelper.getINSTANCE(this);
        addFrameSpHelper.open();

        getIdOptic();
        getBrand();

        modelFrameSpList = addFrameSpHelper.getAllFrameSp();
        Log.d("Total Item", String.valueOf(modelFrameSpList.size()));

        handlerItemCart();

        totalQty   = addFrameSpHelper.countTotalQty();
        totalPrice = addFrameSpHelper.countTotalPrice();
//        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//        totalDisc  = totalPrice - priceDisc;

        //totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        txtShipping.setText("Rp. 0,00");
        shipmentPrice = "0";

        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

        txtBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
//                    Toast.makeText(getApplicationContext(), txtBarcode.getText().toString(), Toast.LENGTH_LONG).show();
                    showDetailProduk(txtBarcode.getText().toString());
                    txtBarcode.setText("");
                    return true;
                }
                return false;
            }
        });

        if (modelFrameSpList.size() > 0)
        {
            linearEmpty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);

            for (int i = 0; i < modelFrameSpList.size(); i++)
            {
                getStockItem(modelFrameSpList.get(i).getProductFlag(),
                        idPartySales,
                        String.valueOf(modelFrameSpList.get(i).getProductId()),
                        modelFrameSpList.get(i).getProductTempStock(),
                        modelFrameSpList.get(i).getProductQty(),
                        i);
            }
        }
        else
        {
            linearEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }

////        adapter_add_framesp.notifyDataSetChanged();
        initialisePref();

//        addpos = "LEINZ";
//        itemSortDesc   = "ASC";
//        newpos = "Qty Terendah";
//        storingPref();
        recyclerItemFrame.setAdapter(adapter_add_framesp);

        handleFlag();
        swFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    txtFlag.setText("BIN");
                    flag = "BIN";
                }
                else
                {
                    txtFlag.setText("STORE");
                    flag = "STORE";
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        modelFrameSpList = addFrameSpHelper.getAllFrameSp();
//        Log.d("Total Item", String.valueOf(modelFrameSpList.size()));
//
//        if (modelFrameSpList.size() > 0)
//        {
//            linearEmpty.setVisibility(View.GONE);
//            scrollView.setVisibility(View.VISIBLE);
//            cardView.setVisibility(View.VISIBLE);
//
//            for (int i = 0; i < modelFrameSpList.size(); i++)
//            {
//                getStockItem(modelFrameSpList.get(i).getProductFlag(),
//                        idPartySales,
//                        String.valueOf(modelFrameSpList.get(i).getProductId()),
//                        modelFrameSpList.get(i).getProductTempStock(),
//                        modelFrameSpList.get(i).getProductQty(),
//                        i);
//            }
//        }
//        else
//        {
//            linearEmpty.setVisibility(View.VISIBLE);
//            scrollView.setVisibility(View.GONE);
//            cardView.setVisibility(View.GONE);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
    };

    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        }
        else {
            p = storage_permissions;
        }
        return p;
    }

    public void enableButtonSubmit()
    {
        btnSave.setBackgroundColor(Color.parseColor("#ff9100"));
        btnSave.setTextColor(Color.parseColor("#FFFFFF"));
        btnSave.setEnabled(true);
    }

    public void disableButtonSubmit()
    {
        btnSave.setBackgroundColor(Color.parseColor("#b4b3b3"));
        btnSave.setTextColor(Color.parseColor("#58595e"));
        btnSave.setEnabled(false);
    }

    private void slideShow(View view){
        TranslateAnimation animate = new TranslateAnimation(
                -view.getWidth(), 0, 0, 0
        );
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);

        //Toasty.info(getApplicationContext(), "Show", Toast.LENGTH_SHORT).show();
    }

    private void slideHide(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0, -view.getWidth(), 0, 0
        );
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);

        //Toasty.info(getApplicationContext(), "Hide", Toast.LENGTH_SHORT).show();
    }

    private void getIdOptic() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            opticId     = bundle.getString("idparty");
            opticName   = bundle.getString("opticname");
            opticProvince= bundle.getString("province");
            opticUsername= bundle.getString("usernameInfo");
            opticCity   = bundle.getString("city");
            opticAddress= bundle.getString("province_address");
            idSp        = bundle.getString("idSp");
            headerNoSp  = bundle.getString("header_nosp");
            headerTipeSp= bundle.getString("header_tipesp");
            headerSales = bundle.getString("header_sales");
            headerShipNumber = bundle.getString("header_shipnumber");
            headerCustName = bundle.getString("header_custname");
            headerAddress  = bundle.getString("header_address");
            headerCity     = bundle.getString("header_city");
            headerOrderVia = bundle.getString("header_ordervia");
            headerDp       = bundle.getInt("header_dp");
            headerDisc     = bundle.getString("header_disc");
            headerCondition= bundle.getString("header_condition");
            headerInstallment = bundle.getString("header_installment");
            headerStartInstallment = bundle.getString("header_startinstallment");
            headerShippingAddress = bundle.getString("header_shippingaddress");
            headerStatus   = bundle.getString("header_status");
            headerImage    = bundle.getString("header_image");
            headerSignedPath = bundle.getString("header_signedpath");
            idPartySales   = bundle.getString("header_idparty");

            flag = "BIN";
            Log.d(FormSpFrameActivity.class.getSimpleName(), "Id sales : " + idPartySales);
            txtOpticName.setText(opticName);
//            getSp();
            getOraId(opticId);
        }
    }

    private static Bitmap convertToPng(Bitmap bitmap){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] src = baos.toByteArray();
            return BitmapFactory.decodeByteArray(src, 0, src.length);
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e("Error convert", "ByteArrayOutputStream was not closed");
                }
            }
        }
    }

    public Uri getImageUrl(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
//        String timeStamp = new SimpleDateFormat("yyMMdd", Locale.US).format(new Date());

        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                headerSales + "_" +  idSp + "_" + timeStamp, null);

        return Uri.parse(path);
    }

    private String getPath(Uri content) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), content, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormSpFrameActivity.this);
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

        img_status      =  dialog.findViewById(R.id.info_status_imageview);
        txt_information =  dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     =  dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          =  dialog.findViewById(R.id.info_status_btnOk);

        img_status.setImageResource(resource);
        txt_information.setText(info);
        txt_message.setText(message);
        btn_ok.setBootstrapBrand(defaultcolorbtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                finish();
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.form_spframe_btnframe :
//                showDialog();
                showDialogFrame();
                break;
            case R.id.form_spframe_btnback:
                finish();
                break;
            case R.id.form_spframe_btncontinue:
                int len = modelFrameSpList.size();
                List<Boolean> sisanya = new ArrayList<>();

                for (int i = 0; i < len; i++)
                {
                    String item = modelFrameSpList.get(i).getProductName();
                    int stock = modelFrameSpList.get(i).getProductStock();
                    int qty   = modelFrameSpList.get(i).getProductQty();
//                    int sisa  = stock - qty;

                    Log.d("Stock " + item, " Sisa = " + stock);
//                    modelFrameSpList.get(i).setProductStock(sisa);

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

                    final Dialog dialog = new Dialog(FormSpFrameActivity.this);
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
                    custom = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_payment_sp, null);
                    UniversalFontComponents.init(this);

                    txtPriceBottom = custom.findViewById(R.id.bottom_dialog_paysp_txtitemprice);
                    txtDiscBottom = custom.findViewById(R.id.bottom_dialog_paysp_txtitemdisc);
                    txtTotalQtyBottom = custom.findViewById(R.id.bottom_dialog_paysp_txttotalqty);
                    txtTotalBottom = custom.findViewById(R.id.bottom_dialog_paysp_txttotalprice);
                    edNotes = custom.findViewById(R.id.bottom_dialog_paysp_edtNote);
                    edNamaCustomer = custom.findViewById(R.id.bottom_dialog_paysp_txtNamaCustomer);
                    final Spinner spinPilihBayarBottom = custom.findViewById(R.id.bottom_dialog_paysp_txtPembayaran);
                    final Spinner spinCicilanBottom = custom.findViewById(R.id.bottom_dialog_paysp_spinCicilan);
                    final Spinner spinMulaiCicilBottom = custom.findViewById(R.id.bottom_dialog_paysp_spinMulaiCicilan);
                    LinearLayout linearTitleCicilBottom = custom.findViewById(R.id.bottom_dialog_paysp_linearTitleCicilan);
                    LinearLayout linearContentCicilBottom = custom.findViewById(R.id.bottom_dialog_paysp_linearContentCicilan);
                    final SignaturePad digitalSignature = custom.findViewById(R.id.bottom_dialog_paysp_imgdigitalsign);
                    imgPhoto = custom.findViewById(R.id.bottom_dialog_paysp_imgPhoto);
                    txtImgLoc = custom.findViewById(R.id.bottom_dialog_paysp_imglocation);
                    Button btnTakePicture = custom.findViewById(R.id.bottom_dialog_paysp_btnPicture);
                    Button btnClearSign = custom.findViewById(R.id.bottom_dialog_paysp_btndigitalsign);
                    final BootstrapEditText edJmlDpBottom = custom.findViewById(R.id.bottom_dialog_paysp_txtJumlahDp);
                    edCustomDisc = custom.findViewById(R.id.bottom_dialog_paysp_txtDiskonValue);
                    edDiscBottom = custom.findViewById(R.id.bottom_dialog_paysp_txtDiskon);
                    RippleView btnSave = custom.findViewById(R.id.bottom_dialog_paysp_btndetail);
                    Switch swDisc = custom.findViewById(R.id.bottom_dialog_paysp_checkDiskon);
                    final UniversalFontTextView txtPercent = custom.findViewById(R.id.bottom_dialog_paysp_txtPercent);
                    final UniversalFontTextView txtTitleDisc = custom.findViewById(R.id.bottom_dialog_paysp_txtTitleDiskon);

                    payBottomDialog = new BottomDialog.Builder(this)
                            .setTitle("Payment Info")
                            .setCustomView(custom)
                            .build();

                    choosePaymentSp(spinPilihBayarBottom, linearTitleCicilBottom, linearContentCicilBottom);
                    setDp(edJmlDpBottom);
                    setDiscCustom(edCustomDisc);
                    setDisc(edDiscBottom);
                    chooseDurasiCicil(spinCicilanBottom);
                    chooseStartInstallment(spinMulaiCicilBottom);

                    swDisc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b)
                            {
                                txtPercent.setVisibility(View.GONE);
                                edDiscBottom.setVisibility(View.GONE);
                                edCustomDisc.setVisibility(View.VISIBLE);

                                edDiscBottom.setText("");
                            }
                            else
                            {
                                txtPercent.setVisibility(View.VISIBLE);
                                edDiscBottom.setVisibility(View.VISIBLE);
                                edCustomDisc.setVisibility(View.GONE);

                                edCustomDisc.setText("");
                            }
                        }
                    });

                    btnTakePicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            {
                                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);

                                startActivityForResult(intent, 1);
                            }
                            else
                            {
                                ImagePicker.build(new DialogConfiguration()
                                                .setTitle("Choose")
                                                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL)
                                                .setResultImageDimension(719,  1089)
                                        , new ImageMultiResultListener() {
                                            @Override
                                            public void onImageResult(ArrayList<ImageResult> imageResult) {
                                                Log.e(LOG_TAG, "onImageResult:Number of image picked " + imageResult.size());
                                                Log.d("Image path : ", imageResult.get(0).getPath());

//                                            Toasty.info(getApplicationContext(), imageResult.get(0).getPath(), Toast.LENGTH_LONG).show();

                                                imgPhoto.setImageBitmap(imageResult.get(0).getBitmap());

                                                img_uri = getImageUrl(getApplicationContext(), imageResult.get(0).getBitmap());

                                                filename = getPath(img_uri);
//
                                                String[] delimit = filename.split("/");
//
                                                imgpath  = delimit[delimit.length - 1];

                                                txtImgLoc.setVisibility(View.GONE);
                                                txtImgLoc.setText(img_uri.getPath());
                                            }
                                        }).show(getSupportFragmentManager());
                            }
                        }
                    });

                    digitalSignature.setOnSignedListener(new SignaturePad.OnSignedListener() {
                        @Override
                        public void onStartSigning() {

                        }

                        @Override
                        public void onSigned() {
                            isDigitalSigned = true;
                            Bitmap bitmap = convertToPng(digitalSignature.getSignatureBitmap());
                            signed_uri = getImageUrl(getApplicationContext(), bitmap);
                            filesigned = getPath(signed_uri);

                            String[] delimit = filesigned.split("/");
                            signedpath  = delimit[delimit.length - 1];
                        }

                        @Override
                        public void onClear() {
                            isDigitalSigned = false;
                        }
                    });

                    btnClearSign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            digitalSignature.clear();
                        }
                    });

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            headerDp = edJmlDpBottom.getText().toString().length() > 0
                                    ? Integer.parseInt(edJmlDpBottom.getText().toString().replace(".", ""))
                                    : 0;
                            headerCondition = spinPilihBayarBottom.getSelectedItem().toString();
                            headerInstallment = spinCicilanBottom.getSelectedItem().toString();
                            headerStartInstallment = cekBulanCicil(spinPilihBayarBottom, spinMulaiCicilBottom);

                            headerImage = imgpath;
                            headerSignedPath = signedpath;

                            if (imgpath == null)
                            {
                                isImage = false;
//                                information("Pemberitahuan", "Mohon masukkan gambar/foto SP", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                            }
                            else
                            {
                                isImage = true;
                            }

                            if (isDigitalSigned) {
                                for (int i = 0; i < modelFrameSpList.size(); i++) {
                                    getQtyItem(flag,
                                               idPartySales,
                                               String.valueOf(modelFrameSpList.get(i).getProductId()),
                                               modelFrameSpList.get(i).getProductTempStock(),
                                               modelFrameSpList.get(i).getProductQty(),
                                               i);
                                }
                            }
                            else
                            {
                                Toasty.warning(getApplicationContext(), "Harap lengkapi tanda tangan digital", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    calcPrice();

                    payBottomDialog.show();
                }
                break;
        }
    }

    private void updatePhoto(Data_spheader item, final String url) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error") || object.names().get(0).equals("invalid"))
                    {
                        Toasty.error(getApplicationContext(), "Data failed save", Toast.LENGTH_SHORT).show();
                        isFotoUploaded = false;
                    }
                    else if (object.names().get(0).equals("info")) {
                        Toasty.warning(getApplicationContext(), "Gambar sudah pernah diupload", Toast.LENGTH_SHORT).show();
                        isFotoUploaded = false;
                    }
                    else
                    {
                        Log.d("Return image : ", object.getString("path"));
                        isFotoUploaded = true;

//                        insertHeader(dataFrameHeader, subcustId, subcustLocId, "");
//                        insertNonPaymentStatus(idSp);
//
//                        insertSP(URL_INSERTSAMTEMP, dataSpHeader);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isFotoUploaded = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isFotoUploaded = false;
            }
        });

        smr.addFile("image", filename);

        AppController.getInstance().addToRequestQueue(smr);
    }

    private void updateSigned() {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL_UPDATESIGNED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error") || object.names().get(0).equals("invalid"))
                    {
                        Toasty.error(getApplicationContext(), "Data failed save", Toast.LENGTH_SHORT).show();
                    }
                    else if (object.names().get(0).equals("info")) {
                        Toasty.warning(getApplicationContext(), "Tanda tangan digital sudah pernah diupload", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d("Return image : ", object.getString("path"));
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

        smr.addFile("image", filesigned);

        AppController.getInstance().addToRequestQueue(smr);
    }

    private String cekBulanCicil(Spinner spinPilihPembayaran, Spinner spinMulaiCicilan)
    {
        if (spinPilihPembayaran.getSelectedItem().toString().matches("Cicilan"))
        {
            if (spinMulaiCicilan.getSelectedItem().toString().matches("-- Pilih Bulan --"))
            {
                Toasty.warning(getApplicationContext(), "Harap pilih bulan", Toast.LENGTH_SHORT).show();
//                        spinMulaiCicilan.setError("Pilih Bulan");
//                isMulaiBayar = false;
//                cicilanVal = "";

                return "";
            }
            else
            {
//                isMulaiBayar = true;
//                cicilanVal = spinMulaiCicilan.getSelectedItem().toString();
                return spinMulaiCicilan.getSelectedItem().toString();
            }
        }
        else
        {
//            isMulaiBayar = true;
//            cicilanVal = "";
            return "";
        }
    }

    @SuppressLint("SetTextI18n")
    private void calcPrice() {
        txtPriceBottom.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        headerDisc = edDiscBottom.getText().toString().length() > 0
                ? Double.parseDouble(edDiscBottom.getText().toString()) >= 90 ? "0" : edDiscBottom.getText().toString()
                : "0";
        headerCustomDisc = edCustomDisc.getText().toString().length() > 0
                            ? edCustomDisc.getText().toString().replace(".", "")
                            : "0";

        Log.d(FormSpFrameActivity.class.getSimpleName(), "Custom disc val : " + headerCustomDisc);

//        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
        totalDisc = edDiscBottom.getText().toString().length() > 0
                        ? Double.parseDouble(headerDisc) * totalPrice / 100
                        : Double.parseDouble(headerCustomDisc);
        txtDiscBottom.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        totalAllPrice = totalPrice - totalDisc;
        txtTotalQtyBottom.setText( totalQty + " Pcs");
        txtTotalBottom.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
    }

    private void choosePaymentSp(final Spinner spinPilihPembayaran, final LinearLayout linearTitleCicilan,
                                 final LinearLayout linearContentCicilan) {
        final String [] model = getResources().getStringArray(R.array.payment_sp_array);
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this, R.layout.spin_framemodel_item, model);
        spinPilihPembayaran.setAdapter(spin_adapter);

        spinPilihPembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinPilihPembayaran.getSelectedItem().toString().equals("Cicilan"))
                {
                    linearTitleCicilan.setVisibility(View.VISIBLE);
                    linearContentCicilan.setVisibility(View.VISIBLE);
//                    spinMulaiCicilan.setEnabled(true);
//                    txtCicilan.setEnabled(true);
                }
                else
                {
                    linearTitleCicilan.setVisibility(View.GONE);
                    linearContentCicilan.setVisibility(View.GONE);
//                    spinMulaiCicilan.setEnabled(false);
//                    txtCicilan.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDp(final BootstrapEditText edJmlDp){
        edJmlDp.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(current)) {
                    edJmlDp.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = editable.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    edJmlDp.setText(clean);
                    edJmlDp.setSelection(clean.length());
                    edJmlDp.addTextChangedListener(this);
                }
            }
        });
    }

    private void setDisc(final BootstrapEditText edDiscount){
        edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().indexOf(".") == 1){
                    edDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                }
                else if (s.toString().indexOf(".") == 2)
                {
                    edDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }
                else
                {
                    edDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                    String data = edDiscount.getText().toString();

                    if (edDiscount.getText().length() > 2)
                    {
                        double amount = Double.parseDouble(data);
                        amount = amount / 10;

                        edDiscount.setText(String.valueOf(amount));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edDiscount.removeTextChangedListener(this);
                if (!editable.toString().equals(""))
                {
                    edDiscount.setSelection(edDiscount.length());
                }
                calcPrice();
                edDiscount.addTextChangedListener(this);
            }
        });
    }

    private void setDiscCustom(final BootstrapEditText edDiscount){
        edDiscount.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(current)) {
                    edDiscount.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = editable.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    edDiscount.setText(clean);
                    edDiscount.setSelection(clean.length());
                    edDiscount.addTextChangedListener(this);
                }
                calcPrice();
            }
        });
    }

    private void chooseDurasiCicil(final Spinner spinCicilan)
    {
//        final int [] durasi = getResources().getIntArray(R.array.durasi_cicilan);
        final String [] durasi = new String[] {"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spin_framemodel_item, durasi);
        spinCicilan.setAdapter(adapter);
    }

    private void chooseStartInstallment(final Spinner spinMulaiCicilan) {
        final String [] model = getResources().getStringArray(R.array.month_array);
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this, R.layout.spin_framemodel_item, model);
        spinMulaiCicilan.setAdapter(spin_adapter);

        spinMulaiCicilan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
//                spinMulaiCicilan.setsele(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveOrder() {
        String discCustom = edCustomDisc.getText().length() > 0 ? edCustomDisc.getText().toString().replace(".", "") : "0";
        String ownerName  = edNamaCustomer.getText().toString().trim();
        String notesSp    = edNotes.getText().toString().trim();

        dataFrameHeader = new Data_frame_header();
        dataFrameHeader.setOrderId(idSp);
        dataFrameHeader.setIdParty(Integer.valueOf(opticId));
        dataFrameHeader.setShippingId(0);
        dataFrameHeader.setShippingName("");
        dataFrameHeader.setShippingService("");
        dataFrameHeader.setOpticCity(opticCity);
        dataFrameHeader.setOpticProvince(opticProvince);
        dataFrameHeader.setShippingPrice(0);
        dataFrameHeader.setTotalPrice(totalAllPrice);
        dataFrameHeader.setOpticName(opticName);
        dataFrameHeader.setOpticAddress(opticAddress);
        dataFrameHeader.setPaymentCashCarry("Non Payment Method");
        dataFrameHeader.setCustomDisc(discCustom);
        insertHeader(dataFrameHeader, subcustId, subcustLocId, "");

        /*JSONObject jsonObject = new JSONObject();
        JSONObject objectFrameHeader = new JSONObject();
        try {
            objectFrameHeader.put("order_id", dataFrameHeader.getOrderId());
            objectFrameHeader.put("id_party", dataFrameHeader.getIdParty());
            objectFrameHeader.put("shipping_id", dataFrameHeader.getShippingId());
            objectFrameHeader.put("shipping_name", dataFrameHeader.getShippingName());
            objectFrameHeader.put("optic_city", dataFrameHeader.getOpticCity());
            objectFrameHeader.put("optic_province", dataFrameHeader.getOpticProvince());
            objectFrameHeader.put("shipping_service", dataFrameHeader.getShippingService());
            objectFrameHeader.put("shipping_price", dataFrameHeader.getShippingPrice());
            objectFrameHeader.put("custom_discount", dataFrameHeader.getCustomDisc());
            objectFrameHeader.put("total_price", dataFrameHeader.getTotalPrice());
            objectFrameHeader.put("optic_name", dataFrameHeader.getOpticName());
            objectFrameHeader.put("shipping_name", dataFrameHeader.getShippingName());
            objectFrameHeader.put("optic_address", dataFrameHeader.getOpticAddress());
            objectFrameHeader.put("optic_province", dataFrameHeader.getOpticProvince());
            objectFrameHeader.put("payment_cashcarry", dataFrameHeader.getPaymentCashCarry());
            objectFrameHeader.put("subcust_id", subcustId);
            objectFrameHeader.put("subcust_loc_id", subcustLocId);
            objectFrameHeader.put("flashSaleInfo", "");
            objectFrameHeader.put("salesname", headerSales);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject.put("frame_header", objectFrameHeader);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Output JSON", jsonObject.toString()); */

        for (int i = 0; i < modelFrameSpList.size(); i++)
        {
            dataFrameItem = new Data_frame_item();
            dataFrameItem.setOrderId(idSp);
            dataFrameItem.setLineNumber(i);
            dataFrameItem.setFrameId(modelFrameSpList.get(i).getProductId());
            dataFrameItem.setFrameCode(modelFrameSpList.get(i).getProductCode());
            dataFrameItem.setFrameName(modelFrameSpList.get(i).getProductName());
            dataFrameItem.setFrameQty(modelFrameSpList.get(i).getProductQty());
            dataFrameItem.setFrameRealPrice(modelFrameSpList.get(i).getProductPrice());
//            dataFrameItem.setFrameDisc(modelFrameSpList.get(i).getProductDisc());
            dataFrameItem.setFrameDisc(Double.parseDouble(headerDisc));
            dataFrameItem.setFrameDiscPrice(modelFrameSpList.get(i).getNewProductDiscPrice());

            Log.d("Diskon : ", String.valueOf(headerDisc));
            Log.d("Amount : ", String.valueOf(modelFrameSpList.get(i).getNewProductDiscPrice()));

            insertLineItem(dataFrameItem);
        }

        dataSpHeader.setNoSp(headerNoSp);
        dataSpHeader.setTypeSp(headerTipeSp);
        dataSpHeader.setSales(headerSales);
        dataSpHeader.setShipNumber(headerShipNumber);
        dataSpHeader.setCustName(headerCustName);
        dataSpHeader.setAddress(headerAddress);
        dataSpHeader.setCity(headerCity);
        dataSpHeader.setOrderVia(headerOrderVia);
        dataSpHeader.setDp(headerDp);
        dataSpHeader.setDisc(headerDisc);
        dataSpHeader.setCondition(headerCondition);
        dataSpHeader.setInstallment(headerInstallment);
        dataSpHeader.setStartInstallment(headerStartInstallment);
        dataSpHeader.setShipAddress(headerShippingAddress);
        dataSpHeader.setPhoto(headerImage);
        dataSpHeader.setStatus(headerStatus);
        dataSpHeader.setSignedPath(headerSignedPath);
        dataSpHeader.setCustomdisc(discCustom);
        dataSpHeader.setOwnerOptic(ownerName);
        dataSpHeader.setNotes(notesSp);

        Log.d(FormSpFrameActivity.class.getSimpleName(), "Out disc value : " + discCustom);

        updateSigned();
        if (isImage)
        {
            updatePhoto(dataSpHeader, URL_UPDATEPHOTO);
        }

        insertNonPaymentStatus(idSp);
        insertSP(URL_INSERTSAMTEMP, dataSpHeader);
    }

    private boolean isAngka(String input)
    {
        Pattern inputVal = Pattern.compile("(\\d+)");
        return inputVal.matcher(input).matches();
    }

    private void showDialogFrame()
    {
        final RippleView rpFilter, rpSort;
        final RecyclerView recyclerFrame;
        final BootstrapButton btnAdd;
        final ImageView imgClear, imgScan;

        final Dialog dialog = new Dialog(FormSpFrameActivity.this);
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
        RecyclerView.LayoutManager verticalGrid = new LinearLayoutManager(FormSpFrameActivity.this);
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

        adapter_framesp_multi = new Adapter_framesp_multi(FormSpFrameActivity.this, itemBestProduct, new MultiSelectSpFrame() {
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
//                    isClear = false;
                    if (check)
                    {
                        txtSearch.setText("");
                        txtSearch.setEnabled(true);
                        isBarcode = false;
                        imgClear.setImageResource(R.drawable.ic_search_black);

//                scanContent = "";
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
//                    isClear = true;
                    txtSearch.setText("");
                    isBarcode = false;
                    progressLayout.setVisibility(View.VISIBLE);
//
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
//                    isClear = true;
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                        adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                        adapter_framesp_qty.notifyDataSetChanged();

//                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                        adapter_framesp_multi.notifyDataSetChanged();

//                                        int prevSize = itemBestProduct.size();
//                                        adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                            request.setShouldCache(false);
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                    List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                    newItem.addAll(itemBestProduct);
                                    itemBestProduct.clear();
                                    adapter_framesp_multi.notifyDataSetChanged();
                                    itemBestProduct.addAll(newItem);// add new data
                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                    adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                    adapter_framesp_qty.notifyDataSetChanged();

//                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                    adapter_framesp_multi.notifyDataSetChanged();

//                                    int prevSize = itemBestProduct.size();
//                                    adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                        request.setShouldCache(false);
                        AppController.getInstance().addToRequestQueue(request);
                    }
                    else
                    {
                        progressLayout.setVisibility(View.VISIBLE);
//                        adapter_framesp_qty.notifyDataSetChanged();
//                        StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCH, new Response.Listener<String>() {
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                    List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                    newItem.addAll(itemBestProduct);
                                    itemBestProduct.clear();
                                    adapter_framesp_multi.notifyDataSetChanged();
                                    itemBestProduct.addAll(newItem);// add new data
                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                    adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                    adapter_framesp_qty.notifyDataSetChanged();

//                                    adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                    adapter_framesp_multi.notifyDataSetChanged();

//                                    int prevSize = itemBestProduct.size();
//                                    adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                        request.setShouldCache(false);
                        AppController.getInstance().addToRequestQueue(request);

//                        Toast.makeText(getApplicationContext(), "Item code", Toast.LENGTH_LONG).show();
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
                final Dialog dialog = new Dialog(FormSpFrameActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                itemSorting.clear();
                itemSorting.add(new Data_sortingonhand("0", "Qty Terendah"));
                itemSorting.add(new Data_sortingonhand("1", "Qty Tertinggi"));

                adapter_sorting_onhand = new Adapter_sorting_onhand(FormSpFrameActivity.this, itemSorting, newpos);

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
//                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCH, new Response.Listener<String>() {
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                            adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                            adapter_framesp_qty.notifyDataSetChanged();

//                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                            adapter_framesp_multi.notifyDataSetChanged();

//                                            int prevSize = itemBestProduct.size();
//                                            adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                                request.setShouldCache(false);
                                AppController.getInstance().addToRequestQueue(request);
                            }
                            else {
                                itemSortDesc = "DESC";
                                storingPref();
                                itemBestProduct.clear();
//                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCH, new Response.Listener<String>() {
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                            adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                            adapter_framesp_qty.notifyDataSetChanged();

//                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                            adapter_framesp_multi.notifyDataSetChanged();

//                                            int prevSize = itemBestProduct.size();
//                                            adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                                request.setShouldCache(false);
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
//                    adapter_framesp_qty.notifyDataSetChanged();
//                    handleRecyclerQty(itemBestProduct);

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                            txtCounter.setText(newItem.size() + " Data Ditemukan");
//                                            adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                            adapter_framesp_qty.notifyDataSetChanged();

//                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                            adapter_framesp_multi.notifyDataSetChanged();

//                                            int prevSize = itemBestProduct.size();
//                                            adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                                request.setShouldCache(false);
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
//                    adapter_framesp_qty.notifyDataSetChanged();
//                    handleRecyclerQty(itemBestProduct);

                                            List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                            newItem.addAll(itemBestProduct);
                                            itemBestProduct.clear();
                                            adapter_framesp_multi.notifyDataSetChanged();
                                            itemBestProduct.addAll(newItem);// add new data
                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                            txtCounter.setText(newItem.size() + " Data Ditemukan");
//                                            adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                            adapter_framesp_qty.notifyDataSetChanged();

//                                            adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                            adapter_framesp_multi.notifyDataSetChanged();

//                                            int prevSize = itemBestProduct.size();
//                                            adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                                request.setShouldCache(false);
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
                final Dialog dialog = new Dialog(FormSpFrameActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                adapter_frame_brand = new Adapter_frame_brand(FormSpFrameActivity.this, itemCategory, addpos);

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
//                                StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCH, new Response.Listener<String>() {
                            StringRequest request = new StringRequest(Request.Method.POST, URL_GETFRAMESEARCHBRANDFLAG, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                        loader.setVisibility(View.GONE);
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
//                                adapter_framesp_qty.notifyDataSetChanged();
//                                handleRecyclerQty(itemBestProduct);

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

//                                        adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                        adapter_framesp_qty.notifyDataSetChanged();

//                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                        adapter_framesp_multi.notifyDataSetChanged();

//                                        int prevSize = itemBestProduct.size();
//                                        adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                            request.setShouldCache(false);
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
//                    adapter_framesp_qty.notifyDataSetChanged();
//                    handleRecyclerQty(itemBestProduct);

                                        List<Data_fragment_bestproduct> newItem = new ArrayList<>();
                                        newItem.addAll(itemBestProduct);
                                        itemBestProduct.clear();
                                        adapter_framesp_multi.notifyDataSetChanged();
                                        itemBestProduct.addAll(newItem);// add new data
                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());

                                        txtCounter.setText(newItem.size() + " Data Ditemukan");
//                                        adapter_framesp_qty.notifyItemRangeInserted(0, itemBestProduct.size());// notify adapter of new data
//                                        adapter_framesp_qty.notifyDataSetChanged();

//                                        adapter_framesp_multi.notifyItemRangeInserted(0, itemBestProduct.size());
//                                        adapter_framesp_multi.notifyDataSetChanged();

//                                        int prevSize = itemBestProduct.size();
//                                        adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
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

//                            request.setShouldCache(false);
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
//                Log.d("ID PRODUK", productId);
//                showDetailProduk(productId);

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

//    private void handleRecyclerQty(List<Data_fragment_bestproduct> newItem) {
//        int currentSize = itemBestProduct.size();
//        itemBestProduct.clear();
//        itemBestProduct.addAll(newItem);
//        adapter_framesp_qty.notifyItemRangeRemoved(0, currentSize);
//        adapter_framesp_qty.notifyItemRangeInserted(0, newItem.size());
//    }

    @SuppressLint("SetTextI18n")
    private void handleFlag(){
        if (modelFrameSpList.size() > 0)
        {
           String itemFlag =  modelFrameSpList.get(0).getProductFlag();
           if (itemFlag.equals("STORE"))
           {
               flag = "STORE";
               txtFlag.setText("STORE");
               swFlag.setChecked(false);
           }
           else
           {
               flag = "BIN";
               txtFlag.setText("BIN");
               swFlag.setChecked(true);
           }
        }
    }

    int getIndexList(int idProduct)
    {
        int idx = -1;
        for (int i = 0; i < modelFrameSpList.size(); i++)
        {
            if (modelFrameSpList.get(i).getProductId() == idProduct)
            {
                idx = i;
            }
        }
        return idx;
    }

    public void handleQty(ModelFrameSp modelFrameSp, int pos, int inputTotalQty, int inputTotalPrice){
        if (modelFrameSpList.size() > 0)
        {
            int idx = getIndexList(modelFrameSp.getProductId());
//            int idx = modelFrameSpList.indexOf(modelFrameSp.getProductId());
//            modelFrameSpList.set(pos, modelFrameSp);
            modelFrameSpList.set(idx, modelFrameSp);

            totalQty   = inputTotalQty;
            totalPrice = inputTotalPrice;

//            totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
            totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

            txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
            txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
            txtShipping.setText("Rp. 0,00");
            shipmentPrice = "0";

            totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
        }

        if (!recyclerItemFrame.isComputingLayout() && recyclerItemFrame.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
        {
            adapter_add_framesp.notifyDataSetChanged();
        }
    }

    private void handlerItemCart() {
        adapter_add_framesp = new Adapter_add_framesp(FormSpFrameActivity.this, modelFrameSpList, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, final int pos, String id) {
                int btn = view.getId();

                switch (btn) {
                    case R.id.item_cartframesp_btnRemove:
                        addFrameSpHelper.deleteWishlist(modelFrameSpList.get(pos).getProductId());

                        modelFrameSpList.remove(pos);
                        adapter_add_framesp.notifyItemRemoved(pos);

                        if (modelFrameSpList.size() > 0) {
                            linearEmpty.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);

                            swFlag.setEnabled(false);
                        } else {
                            linearEmpty.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);

                            swFlag.setEnabled(true);
                        }

//                        adapter_add_framesp.notifyDataSetChanged();
//                        recyclerItemFrame.setAdapter(adapter_add_framesp);

                        addFrameSpHelper.open();

                        totalQty = addFrameSpHelper.countTotalQty();
                        totalPrice = addFrameSpHelper.countTotalPrice();
//                        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//                        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//                        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//                        totalDisc  = totalPrice - priceDisc;

//                        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        break;

                    case R.id.item_cartframesp_btnPlus:
                        addFrameSpHelper.open();

                        ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(modelFrameSpList.get(pos).getProductId());

                        int price = modelFrameSp.getProductPrice();
                        int qty = Math.max(modelFrameSp.getProductQty(), 1);
                        int stock = modelFrameSp.getProductStock();
                        int discprice = modelFrameSp.getProductDiscPrice();

                        qty = qty + 1;
                        int newprice = price * qty;
                        int newdiscprice = discprice * qty;

                        stock = stock - qty;
                        Log.d("Stok Add", String.valueOf(stock));
                        modelFrameSp.setProductStock(stock);

                        modelFrameSp.setNewProductPrice(newprice);
                        modelFrameSp.setProductQty(qty);
                        modelFrameSp.setNewProductDiscPrice(newdiscprice);
                        modelFrameSpList.set(pos, modelFrameSp);
                        addFrameSpHelper.updateFrameSpQty(modelFrameSp);
                        adapter_add_framesp.notifyDataSetChanged();

                        addFrameSpHelper.open();
                        totalQty = addFrameSpHelper.countTotalQty();
                        totalPrice = addFrameSpHelper.countTotalPrice();
//                        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//                        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//                        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//                        totalDisc  = totalPrice - priceDisc;

//                        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        break;

                    case R.id.item_cartframesp_btnMinus:
                        //                        aaddCartHelper.open();
                        ModelFrameSp mModelFrameSp = addFrameSpHelper.getAddFrameSp(modelFrameSpList.get(pos).getProductId());

                        int mPrice = mModelFrameSp.getProductPrice();
                        int mQty = mModelFrameSp.getProductQty();
                        int mDiscprice = mModelFrameSp.getProductDiscPrice();
                        int mStock = mModelFrameSp.getProductStock();
//                        int tempStock = 0;

                        mQty = mQty - 1;

//                        if (flagPayment == 1)
//                        {
                        if (mQty == 0) {
                            mQty = mQty + 1;
                        }

                        mStock = mStock - mQty;
                        Log.d("Stok Minus", String.valueOf(mStock));
                        mModelFrameSp.setProductStock(mStock);

                        modelFrameSpList.set(pos, mModelFrameSp);
                        addFrameSpHelper.updateFrameSpQty(mModelFrameSp);
                        adapter_add_framesp.notifyDataSetChanged();
//                        }

                        if (mQty == 0) {
                            Toasty.info(getApplicationContext(), "Minimal Order 1 pcs", Toast.LENGTH_SHORT).show();
                        } else {
                            int mNewPrice = mPrice * mQty;
                            int mNewdiscprice = mDiscprice * mQty;

                            //Toasty.info(getApplicationContext(), String.valueOf(qty), Toast.LENGTH_SHORT).show();

                            mModelFrameSp.setNewProductPrice(mNewPrice);
                            mModelFrameSp.setProductQty(mQty);
                            mModelFrameSp.setNewProductDiscPrice(mNewdiscprice);

                            modelFrameSpList.set(pos, mModelFrameSp);
                            addFrameSpHelper.updateFrameSpQty(mModelFrameSp);
                            adapter_add_framesp.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapterAddCart);
                        }

                        addFrameSpHelper.open();
                        totalQty = addFrameSpHelper.countTotalQty();
                        totalPrice = addFrameSpHelper.countTotalPrice();
//                        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//                        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//                        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//                        totalDisc  = totalPrice - priceDisc;

//                        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
                        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtShipping.setText("Rp. 0,00");
                        shipmentPrice = "0";

                        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
                        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        break;
                }
            }
        });

        adapter_add_framesp.notifyDataSetChanged();

        int len = modelFrameSpList.size();

        for (int i = 0; i < len; i++)
        {
            ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(modelFrameSpList.get(i).getProductId());

            String item = modelFrameSp.getProductName();
            int stock = modelFrameSp.getProductStock();
            int qty   = modelFrameSp.getProductQty();
            int sisa  = stock - qty;

            Log.d("Stock " + item, " Sisa = " + stock);
            modelFrameSp.setProductStock(sisa);

            modelFrameSpList.set(i, modelFrameSp);
            addFrameSpHelper.updateFrameSpQty(modelFrameSp);
            adapter_add_framesp.notifyDataSetChanged();
        }

        totalQty   = addFrameSpHelper.countTotalQty();
        totalPrice = addFrameSpHelper.countTotalPrice();
//        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//        totalDisc  = totalPrice - priceDisc;

//        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        txtShipping.setText("Rp. 0,00");
        shipmentPrice = "0";

        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

        if (modelFrameSpList.size() > 0)
        {
            linearEmpty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            recyclerItemFrame.setVisibility(View.VISIBLE);

            swFlag.setEnabled(false);

            adapter_add_framesp.notifyDataSetChanged();
        }
        else
        {
            linearEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            recyclerItemFrame.setVisibility(View.GONE);

            swFlag.setEnabled(true);
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

    private void showDetailProduk(final String id) {
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

                        addFrameSpHelper.open();

                        int sts = addFrameSpHelper.checkAddFrameSp(object.getInt("frame_id"));

                        if (sts < 1)
                        {
                            ModelFrameSp item = new ModelFrameSp();
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

                            long status = addFrameSpHelper.insertAddFrameSp(item);

                            Toasty.success(getApplicationContext(), "Item berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                            modelFrameSpList = addFrameSpHelper.getAllFrameSp();

                            handlerItemCart();
                            recyclerItemFrame.setAdapter(adapter_add_framesp);
                        }
                        else
                        {
                            tambahQty(id);
                        }
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
                return hashMap;
            }
        };

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

                        addFrameSpHelper.open();

                        int sts = addFrameSpHelper.checkAddFrameSp(object.getInt("frame_id"));

                        if (sts < 1)
                        {
                            ModelFrameSp item = new ModelFrameSp();
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

                            long status = addFrameSpHelper.insertAddFrameSp(item);

                            Toasty.success(getApplicationContext(), "Item berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                            modelFrameSpList = addFrameSpHelper.getAllFrameSp();

                            handlerItemCart();
                            recyclerItemFrame.setAdapter(adapter_add_framesp);
                        }
                        else
                        {
                            tambahQty(id);
                        }
                    }

                    for (int j = 0; j < modelFrameSpList.size(); j++)
                    {
                        Log.d(FormSpFrameActivity.class.getSimpleName(), "Db name (" + j + ") : "
                                + modelFrameSpList.get(j).getProductName());
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

    private void insertSP(final String URL, final Data_spheader item) {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Data has been save");

                        insertSpHeader(dataSpHeader);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("INSERT SP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type_sp", item.getTypeSp());
                hashMap.put("sales", item.getSales());
                hashMap.put("no_sp", item.getNoSp());
                hashMap.put("customer_name", item.getCustName());
                hashMap.put("address", item.getAddress());
                hashMap.put("city", item.getCity());
                hashMap.put("order_via", item.getOrderVia());
                hashMap.put("down_payment", String.valueOf(item.getDp()));
                hashMap.put("discount", String.valueOf(item.getDisc()));
                hashMap.put("conditions", item.getCondition());
                hashMap.put("installment", item.getInstallment());
                hashMap.put("start_installment", item.getStartInstallment());
                hashMap.put("shipping_address", item.getShipAddress());
//                hashMap.put("photo",  item.getPhoto() != null ? "\\\\192.168.44.21\\ordertxt\\orderandroid\\Foto SP\\foto\\" + item.getPhoto() : "");
//                hashMap.put("photo",  item.getPhoto() != null ? "\\\\192.168.44.37\\Foto SP\\DEV\\FOTO\\" + item.getPhoto() : "");
                hashMap.put("photo",  item.getPhoto() != null ? item.getPhoto() : "");
                hashMap.put("status", item.getStatus());
                hashMap.put("excelpath", "");
                hashMap.put("custom_discount", item.getCustomdisc());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getOraId(final String idParty) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETORAID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    subcustId = object.getString("subcust_id");
                    subcustLocId = object.getString("subcust_loc_id");

                    if (subcustId.equals("null"))
                    {
                        subcustId = "0";
                    }

                    if (subcustLocId.equals("null"))
                    {
                        subcustLocId = "0";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("ERROR GET ORAID : ", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idParty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertHeader(final Data_frame_header item, final String subcustid, final String subcustlocid, final String flashSale)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
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
                hashMap.put("order_id", item.getOrderId());
                hashMap.put("id_party", String.valueOf(item.getIdParty()));
                hashMap.put("shipping_id", String.valueOf(item.getShippingId()));
                hashMap.put("shipping_name", item.getShippingName());
                hashMap.put("shipping_service", item.getShippingService());
                hashMap.put("optic_city", item.getOpticCity());
                hashMap.put("optic_province", item.getOpticProvince());
                hashMap.put("shipping_price", String.valueOf(item.getShippingPrice()));
                hashMap.put("total_price", String.valueOf(item.getTotalPrice()));
                hashMap.put("optic_name", item.getOpticName());
                hashMap.put("optic_address", item.getOpticAddress());
                hashMap.put("payment_cashcarry", item.getPaymentCashCarry());
                hashMap.put("subcust_id", subcustid);
                hashMap.put("subcust_loc_id", subcustlocid);
                hashMap.put("salesname", headerSales);
                hashMap.put("custom_discount", item.getCustomDisc());
                hashMap.put("flashSaleInfo", "");

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertNonPaymentStatus(final String transNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTNONTPAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.names().get(0).equals("success")){
//                        Toasty.warning(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("order_id", transNumber);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertLineItem(final Data_frame_item item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTLINETITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        if (flag.equals("STORE"))
                        {
                            insertDuration(item);
                        }
                        else
                        {
                            insertDurationBin(item);
                        }
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
                hashMap.put("order_id", item.getOrderId());
                hashMap.put("line_number", String.valueOf(item.getLineNumber()));
                hashMap.put("frame_id", String.valueOf(item.getFrameId()));
                hashMap.put("frame_code", item.getFrameCode());
                hashMap.put("frame_name", item.getFrameName());
                hashMap.put("frame_qty", String.valueOf(item.getFrameQty()));
                hashMap.put("frame_realprice", String.valueOf(item.getFrameRealPrice()));
                hashMap.put("frame_disc", String.valueOf(item.getFrameDisc()));
                hashMap.put("frame_discprice", String.valueOf(item.getFrameDiscPrice()));

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertDuration(final Data_frame_item item) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTDURATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        addFrameSpHelper.open();
                        addFrameSpHelper.truncCart();
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
                hashMap.put("no_sp", item.getOrderId());
                hashMap.put("item_id", String.valueOf(item.getFrameId()));
                hashMap.put("qty", String.valueOf(item.getFrameQty()));
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertDurationBin(final Data_frame_item item) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTDURATIONBIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        addFrameSpHelper.open();
                        addFrameSpHelper.truncCart();
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
                hashMap.put("no_sp", item.getOrderId());
                hashMap.put("item_id", String.valueOf(item.getFrameId()));
                hashMap.put("qty", String.valueOf(item.getFrameQty()));
                hashMap.put("idsales", idPartySales);
                hashMap.put("namasales", headerSales);
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

//                            recyclerFrame.setVisibility(View.GONE);
                            txtCounter.setVisibility(View.GONE);
                            linearCounter.setVisibility(View.GONE);
                            imgFrameNotFound.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            check = true;

//                            recyclerFrame.setVisibility(View.VISIBLE);
//                            txtCounter.setVisibility(View.VISIBLE);
//                            imgFrameNotFound.setVisibility(View.GONE);
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

//                    int prevSize = itemBestProduct.size();
//                    adapter_framesp_multi.notifyItemRangeInserted(prevSize, itemBestProduct.size() - prevSize);
//                    adapter_framesp_multi.notifyDataSetChanged();
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

//        request.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertSpHeader(final Data_spheader item) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTSPHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Success");

                        Toasty.success(getApplicationContext(), "Data Berhasil disimpan", Toast.LENGTH_SHORT).show();

                        payBottomDialog.dismiss();

                        Intent intent = new Intent("finishFr");
                        sendBroadcast(intent);

                        finish();

//                        updatePhoto(dataHeader, URL_UPDATEPHOTO);
                    }
                    else if (object.names().get(0).equals("error"))
                    {
                        Log.d("INSERT SP", "Error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("INSERT SP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("no_sp", item.getNoSp());
                hashMap.put("type_sp", item.getTypeSp());
                hashMap.put("sales", item.getSales());
                hashMap.put("shipnumber", item.getShipNumber());
                hashMap.put("customer_name", item.getCustName());
                hashMap.put("address", item.getAddress());
                hashMap.put("city", item.getCity());
                hashMap.put("order_via", "ANDROID");
                hashMap.put("down_payment", String.valueOf(item.getDp()));
                hashMap.put("discount", String.valueOf(item.getDisc()));
                hashMap.put("conditions", item.getCondition());
                hashMap.put("installment", item.getInstallment());
                hashMap.put("start_installment", item.getStartInstallment());
                hashMap.put("shipping_address", item.getShipAddress());
                hashMap.put("photo", item.getPhoto() != null ? config.Ip_address + "assets/images/ordersp/" + item.getPhoto() : "");
                hashMap.put("path", item.getPhoto());
                hashMap.put("status", item.getStatus());
                hashMap.put("signedurl", item.getSignedPath() != null ? config.Ip_address + "assets/images/signedsp/" + item.getSignedPath() : "");
                hashMap.put("signedpath", item.getSignedPath());
                hashMap.put("custom_discount", item.getCustomdisc());
                hashMap.put("notes", item.getNotes());
                hashMap.put("nama_owner", item.getOwnerOptic());
                hashMap.put("flag", flag);
                hashMap.put("excel", "");
                hashMap.put("excelpath", "");
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void compareStock(String itemId, int lastQty, int lastOrder, int index)
    {
        if (lastQty != qtyTemp)
        {
            addFrameSpHelper.open();
            ModelFrameSp modelFrameSp = new ModelFrameSp();
            if (itemId != null)
            {
                modelFrameSp = addFrameSpHelper.getAddFrameSp(Integer.parseInt(itemId));
            }


            int stock = qtyTemp - lastOrder;
            Log.d("Stok Change", String.valueOf(stock));
            modelFrameSp.setProductStock(stock);
            modelFrameSp.setProductTempStock(qtyTemp);

            modelFrameSpList.set(index, modelFrameSp);

            addFrameSpHelper.updateFrameStock(modelFrameSp, qtyTemp);
            addFrameSpHelper.updateFrameStockTmp(modelFrameSp, qtyTemp);

            if (stock < 0)
            {
                if (index == modelFrameSpList.size() - 1)
                {
                    Log.d("Item minus", itemId);
                    payBottomDialog.dismiss();
                    Log.d("Information Cart", "Ada item yang minus");

                    final Dialog dialog = new Dialog(FormSpFrameActivity.this);
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

                            adapter_add_framesp.notifyDataSetChanged();
                        }
                    });

                    dialog.getWindow().setAttributes(lwindow);
                    if (!isFinishing()){
                        dialog.show();
                    }
                }
            }
            else
            {
                if (index == modelFrameSpList.size() - 1)
                {
                    Log.d("Eksekusi Order 1 : ", "Simpan order sp");
                    saveOrder();
                }
            }
        }
        else
        {
            if (index == modelFrameSpList.size() - 1)
            {
                Log.d("Eksekusi Order : ", "Simpan order sp");
                saveOrder();
            }
        }
    }

    private void updateStock(String itemId, int lastOrder, int index)
    {
        addFrameSpHelper.open();
        ModelFrameSp modelFrameSp = new ModelFrameSp();
        if (itemId != null)
        {
            modelFrameSp = addFrameSpHelper.getAddFrameSp(Integer.parseInt(itemId));
        }

        int stock = qtyTemp - lastOrder;
        Log.d("Stok Change 1 : ", String.valueOf(stock));
        modelFrameSp.setProductStock(stock);
        modelFrameSp.setProductTempStock(qtyTemp);

        modelFrameSpList.set(index, modelFrameSp);
        adapter_add_framesp.notifyDataSetChanged();

        addFrameSpHelper.updateFrameStock(modelFrameSp, qtyTemp);
        addFrameSpHelper.updateFrameStockTmp(modelFrameSp, qtyTemp);
    }

    private void getQtyItem(final String flag, final String idSales, final String itemId, final int lastQty, final int lastOrder, final int index){
        StringRequest request = new StringRequest(Request.Method.POST, URL_CEKQTYITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("invalid"))
                    {
                        qtyTemp = 0;
                        Log.d("Invalid : ", "Data tidak ditemukan");
                    }
                    else
                    {
                        qtyTemp = object.getInt("frame_qty");

                        Log.d("Last Qty : ", String.valueOf(lastQty));
                        Log.d("Realtime Qty", String.valueOf(qtyTemp));
                        Log.d("Last Order : ", String.valueOf(lastOrder));

                        compareStock(itemId, lastQty, lastOrder, index);
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

    private void tambahQty(String itemId){
        addFrameSpHelper.open();
        ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(Integer.valueOf(itemId));

//        Log.d(TAG, "item id : " + modelFrameSp.getProductId() + " item qty : " + modelFrameSp.getProductQty());
        List<String> temp = new ArrayList<>();
////        temp.clear();
//
//        Log.d(TAG, "item size : " + modelFrameSpList.size());
        for (int i = 0; i < modelFrameSpList.size(); i++)
        {
            Log.d(TAG, "item name : " + modelFrameSpList.get(i).getProductId() + " qty : " + modelFrameSpList.get(i).getProductQty());
            temp.add(String.valueOf(modelFrameSpList.get(i).getProductId()));
        }
//
        int position = -1;
        position = temp.lastIndexOf(itemId);
        if (position == -1) {
            Log.e(TAG, "Object not found in List");
        } else {
            Log.i(TAG, "" + position);
        }

//        ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(modelFrameSpList.get(position).getProductId());
//        ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(Integer.parseInt(temp.get(position)));
//        Log.d(TAG, "item name update : " + temp.get(position) + " qty : " + modelFrameSpList.get(position).getProductQty());

        int price = modelFrameSp.getProductPrice();
        int qty   = modelFrameSp.getProductQty();
        int stock = modelFrameSp.getProductStock();
        int discprice = modelFrameSp.getProductDiscPrice();

        qty = qty + 1;
        int newprice = price * qty;
        int newdiscprice = discprice * qty;

        stock = stock - qty;
        Log.d("Stok Add", String.valueOf(stock));
        modelFrameSp.setProductStock(stock);

        modelFrameSp.setNewProductPrice(newprice);
        modelFrameSp.setProductQty(qty);
        modelFrameSp.setNewProductDiscPrice(newdiscprice);
//        modelFrameSpList.set(pos, modelFrameSp);
        modelFrameSpList.set(position, modelFrameSp);
        addFrameSpHelper.updateFrameSpQty(modelFrameSp);
        adapter_add_framesp.notifyDataSetChanged();

        addFrameSpHelper.open();
        totalQty   = addFrameSpHelper.countTotalQty();
        totalPrice = addFrameSpHelper.countTotalPrice();
//                        priceDisc = addFrameSpHelper.countTotalDiscPrice();
//                        priceDisc  = Integer.valueOf(headerDisc) / 100 * totalPrice;
//                        priceDisc  = Integer.parseInt(headerDisc) * totalPrice / 100;

//                        totalDisc  = totalPrice - priceDisc;

//        totalDisc = Integer.parseInt(headerDisc) * totalPrice / 100;
        totalDisc = headerDisc != null ? Integer.parseInt(headerDisc) : 0 * totalPrice / 100;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
        txtShipping.setText("Rp. 0,00");
        shipmentPrice = "0";

        totalAllPrice = totalPrice - totalDisc + Integer.valueOf(shipmentPrice);
        txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
    }

    public void scanNow(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan 2d barcode");
        integrator.setResultDisplayDuration(0);
//        integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showDialogFrame();
        txtSearch.setText(txtTmp.getText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            Uri selectedImage;
            if (data != null)
            {
                selectedImage = data.getData();
                InputStream in;
                try {
                    assert selectedImage != null;
                    in = getContentResolver().openInputStream(selectedImage);
                    final Bitmap selected_img = BitmapFactory.decodeStream(in);
                    imgPhoto.setImageBitmap(selected_img);

                    img_uri = getImageUrl(getApplicationContext(), selected_img);

                    filename = getPath(img_uri);

                    String[] delimit = filename.split("/");

                    imgpath  = delimit[delimit.length - 1];

                    txtImgLoc.setVisibility(View.GONE);
                    txtImgLoc.setText(img_uri.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "An error occured!", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (scanningResult != null) {
            //we have a result
            scanContent = scanningResult.getContents();

            if (scanContent != null) {
//                showDialogFrame();
                txtTmp.setText(scanContent);
                txtSearch.setText(txtTmp.getText());
            } else {
                getItemByBrand(addpos, itemSortDesc, txtFlag.getText().toString(), idPartySales);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
//            getItemByBrand(addpos, itemSortDesc, txtFlag.getText().toString(), idPartySales);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
