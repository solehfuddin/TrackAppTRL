package com.sofudev.trackapptrl.Form;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_lenstype;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.DashboardActivity;
import com.sofudev.trackapptrl.Data.Data_lenstype;
import com.sofudev.trackapptrl.Data.Data_partai_header;
import com.sofudev.trackapptrl.Data.Data_partai_item;
import com.sofudev.trackapptrl.Data.Data_spheader;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormLensaPartaiActivity extends AppCompatActivity {
    private static final String TAG = "Download Task";
    Config config = new Config();
    private String downloadUrl = "http://180.250.96.154/trl-webs/assets/template/Order_partai.xls";
    String URLALLLENS = config.Ip_address + config.show_all_stocklens;
    String URLINSERTHEADER     = config.Ip_address + config.orderpartai_insertHeader;
    String URLINSERTITEM       = config.Ip_address + config.orderpartai_insertItem;
    String URLINSERTNONPAY     = config.Ip_address + config.orderpartai_nonpayment;
    String URL_INSERTSPHEADER  = config.Ip_address + config.ordersp_insert_spHeader;
    String URL_INSERTSAMTEMP   = config.Ip_address + config.ordersp_insert_samTemp;
    String URL_UPDATEEXCEL     = config.Ip_address + config.ordersp_update_excel;
    String URL_GETACTIVESALE   = config.Ip_address + config.flashsale_getActiveSale;
    String URL_GETPARTAIPRICE  = config.Ip_address + config.orderpartai_getPrice;

    ConstraintLayout constraintLayoutOpticName;
    CustomLoading customLoading;
    ImageView btnBack;
    UniversalFontTextView txtPrice, txtDisc, txtTotal;
    Spinner spinPilihPembayaran, spinCicilan, spinMulaiCicilan;
    BootstrapEditText txtLensa, txtQty, edJmlDp, edDiscount;
    Button btnSave;
    RippleView btnChoose;
    View animateView;
    RelativeLayout animateCard;
    LinearLayout linearTitleCicilan, linearContentCicilan;
    ImageView animateImg;
    Boolean isUp;

    String headerNoSp, headerTipeSp, headerSales, headerShipNumber, headerCustName, headerAddress, headerCity, headerOrderVia,
            headerDisc, headerCondition, headerInstallment, headerStartInstallment, headerShippingAddress, headerStatus,
            headerImage, headerSignedPath, flashsaleNote, id_lensa, desc_lensa;
    String opticId, opticName, opticUsername, opticProvince, opticCity, opticAddress, opticFlag;
    String itemId, itemCode, itemDesc, itemRl, fileString;
    int headerDp, flagDiscSale, isSp, itemPrice, itemQty, totalPrice;
    Double itemDisc;
    Boolean isQty, isLens, isFile;
    private DownloadManager downloadManager;
    UniversalFontTextView txtOpticName;

    List<Data_lenstype> item_stocklens = new ArrayList<>();
    Data_spheader dataSpHeader = new Data_spheader();

    Adapter_lenstype adapter_lenstype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lensa_partai);

        UniversalFontComponents.init(this);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        btnBack         = findViewById(R.id.form_lensapartai_btnback);
        btnSave         = findViewById(R.id.form_lensapartai_btnsave);
        txtLensa        = findViewById(R.id.form_lensapartai_txtlens);
        txtQty          = findViewById(R.id.form_lensapartai_txtqty);
        txtOpticName    = findViewById(R.id.form_lensapartai_txtopticname);
        txtPrice        = findViewById(R.id.form_lensapartai_txtitemprice);
        txtDisc         = findViewById(R.id.form_lensapartai_txtitemdisc);
        txtTotal        = findViewById(R.id.form_lensapartai_txttotalprice);
        spinPilihPembayaran = findViewById(R.id.form_lensapartai_txtPembayaran);
        spinCicilan     = findViewById(R.id.form_lensapartai_spinCicilan);
        spinMulaiCicilan= findViewById(R.id.form_lensapartai_spinMulaiCicilan);
        edJmlDp         = findViewById(R.id.form_lensapartai_txtJumlahDp);
        edDiscount      = findViewById(R.id.form_lensapartai_txtDiskon);
        linearTitleCicilan = findViewById(R.id.form_lensapartai_linearTitleCicilan);
        linearContentCicilan = findViewById(R.id.form_lensapartai_linearContentCicilan);
        btnChoose       = findViewById(R.id.form_lensapartai_btnChooselens);
        constraintLayoutOpticName = findViewById(R.id.form_lensapartai_layoutopticname);
        animateView = findViewById(R.id.form_lensapartai_rlopticname);
        animateCard = findViewById(R.id.form_lensapartai_handleopticname);
        animateImg = findViewById(R.id.form_lensapartai_imgopticname);

        animateView.setVisibility(View.VISIBLE);
        isUp = true;

        getData();
        getActiveSale();
        choosePaymentSp();
        chooseStartInstallment();
        chooseDurasiCicil();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAllLens();
            }
        });

        txtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calcPrice();
            }
        });

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

        edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edDiscount.removeTextChangedListener(this);
                if (!editable.toString().equals(""))
                {
                    String data = edDiscount.getText().toString().replace(".", "");

                    if(edDiscount.getText().length() > 2)
                    {
                        double amount = Double.valueOf(data);

                        amount = amount / 10;
                        edDiscount.setText(String.valueOf(amount));
                        edDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    }
                    else
                    {
                        edDiscount.setText(data);
                    }

                    edDiscount.setSelection(edDiscount.length());
                }
                calcPrice();
                edDiscount.addTextChangedListener(this);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileCheck();
                qtyCheck();
                lensCheck();

                int valDp = edJmlDp.getText().toString().length() > 0
                        ? Integer.parseInt(edJmlDp.getText().toString().replace(".", ""))
                        : 0;

                boolean isDp = dpCheck(valDp);

                if (isQty && isLens && isFile && isDp)
                {
//                    Toasty.info(getApplicationContext(), "Upload dan simpan data", Toast.LENGTH_SHORT).show();
//                    showLoading();
                    customLoading.showLoadingDialog();
                    updateExcel(fileString);
                }
                else
                {
                    if (!isFile)
                    {
                        Toasty.warning(getApplicationContext(), "File belum dipilih", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isQty)
                    {
                        Toasty.warning(getApplicationContext(), "Qty belum diisi", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isDp)
                    {
                        Toasty.warning(getApplicationContext(), "Masukkan jml bayar / dp", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Jenis lensa belum dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

        final RippleView btnDownload = findViewById(R.id.form_lensapartai_ripplebtndownload);
        final RippleView btnUpload   = findViewById(R.id.form_lensapartai_ripplebtnupload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet()) {
                    Log.d("Environment", "Environment extraData=" + getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/Order_partai_" + headerNoSp + ".xls");

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setAllowedOverMetered(true);
                    request.setAllowedOverRoaming(true);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setTitle("Order_partai_" + headerNoSp + ".xls");
                    request.setDescription("Downloading");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    {
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOCUMENTS, "/" +"Order_partai_" + headerNoSp + ".xls");
                    }
                    else
                    {
                        request.setDestinationInExternalPublicDir("", "/Android/data/com.sofudev.trackapptrl/files/Documents" + "/" + "Order_partai_" + headerNoSp + ".xls");
                    }

                    request.setVisibleInDownloadsUi(true);

                    downloadManager.enqueue(request);
                }
                else {
                    Toast.makeText(FormLensaPartaiActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogProperties properties = new DialogProperties();
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.FILE_SELECT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {

                    properties.root = new File(String.valueOf(getApplicationContext().getExternalFilesDir(null)));
                }
                else
                {
                    properties.root = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.sofudev.trackapptrl/files/Documents/");
                }

                properties.extensions = null;

                FilePickerDialog pickerDialog = new FilePickerDialog(FormLensaPartaiActivity.this, properties);
                pickerDialog.setTitle("Pilih file excel");
                pickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        Log.d("File path : ", files[0]);
                        fileString = files[0];
//                      String outputPath = Environment.getExternalStorageDirectory() + "/TRL APPS/temp/";
//                      String inputPath = part[0] + '/' + part[1] + '/' + part[2] + '/' + part[3] + '/' + part[4] + '/';

//                        showLoading();
//                        updateExcel(files[0]);
                    }
                });
                pickerDialog.show();
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 100:
                if (resultCode == Activity.RESULT_OK && data != null)
                {

                }
                break;
        }
    }

    private void choosePaymentSp() {
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

    private void chooseStartInstallment() {
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

    private void chooseDurasiCicil()
    {
//        final int [] durasi = getResources().getIntArray(R.array.durasi_cicilan);
        final String [] durasi = new String[] {"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spin_framemodel_item, durasi);
        spinCicilan.setAdapter(adapter);
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

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            opticId     = bundle.getString("idparty");
            opticName   = bundle.getString("opticname");
            opticProvince = bundle.getString("province");
            opticUsername = bundle.getString("usernameInfo");
            opticCity   = bundle.getString("city");
            opticFlag   = bundle.getString("flag");
            opticAddress= bundle.getString("province_address");
            isSp        = bundle.getInt("isSp", 0);
            headerNoSp  = bundle.getString("header_nosp");
            headerTipeSp= bundle.getString("header_tipesp");
            headerSales = bundle.getString("header_sales");
            headerShipNumber = bundle.getString("header_shipnumber");
            headerCustName = bundle.getString("header_custname");
            headerAddress  = bundle.getString("header_address");
            headerCity     = bundle.getString("header_city");
            headerOrderVia = bundle.getString("header_ordervia");
            headerDp       = bundle.getInt("header_dp");
//            headerDisc     = bundle.getString("header_disc");
            headerDisc = edDiscount.getText().toString().length() > 0 ? edDiscount.getText().toString() : "0";
            headerCondition= bundle.getString("header_condition");
            headerInstallment = bundle.getString("header_installment");
            headerStartInstallment = bundle.getString("header_startinstallment");
            headerShippingAddress = bundle.getString("header_shippingaddress");
            headerStatus   = bundle.getString("header_status");
            headerImage    = bundle.getString("header_image");
            headerSignedPath = bundle.getString("header_signedpath");

            txtOpticName.setText(opticName);
        }
    }

    private String cekBulanCicil()
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

    private void sentData(String path) {
        headerDp = edJmlDp.length() > 0 ? Integer.parseInt(edJmlDp.getText().toString().replace(".","")) : 0;
        headerCondition = spinPilihPembayaran.getSelectedItem().toString();
        headerInstallment = spinCicilan.getSelectedItem().toString();
        headerStartInstallment = cekBulanCicil();

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

        Data_partai_header data_header = new Data_partai_header();
        data_header.setOrderNumber(headerNoSp);
        data_header.setIdParty(Integer.parseInt(opticId.replace(",", "")));
        data_header.setOpticName(opticName.replace(",", ""));
        data_header.setOpticAddress(opticAddress);
        data_header.setOpticCity(opticCity);
        data_header.setOpticProvince(opticProvince);
        data_header.setFlashNote(flashsaleNote);
        data_header.setOrderSp(String.valueOf(isSp));

        Data_partai_item data_item = new Data_partai_item();
        data_item.setOrderNumber(headerNoSp);
        data_item.setItemId(Integer.parseInt(itemId));
        data_item.setItemCode(itemCode);
        data_item.setDescription(itemDesc);
        data_item.setSide(itemRl);
        data_item.setQty(itemQty);
        data_item.setPrice(itemPrice);
        data_item.setDiscount(itemDisc);
        data_item.setTotal_price(totalPrice);

        insertSpHeader(dataSpHeader, path);
        insertSP(URL_INSERTSAMTEMP, dataSpHeader, path);
        insertHeader(data_header);
        inserItem(data_item);
        insertNonPay(headerNoSp);

        try {
            Thread.sleep(2500);
//            hideLoading();
            customLoading.dismissLoadingDialog();
            successDialog();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateExcel(final String file) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL_UPDATEEXCEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error") || object.names().get(0).equals("invalid"))
                    {
                        Toasty.error(getApplicationContext(), "Data failed save", Toast.LENGTH_SHORT).show();
                    }
                    else if (object.names().get(0).equals("info")) {
                        Toasty.warning(getApplicationContext(), "File sudah pernah diupload", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d("Return file : ", object.getString("path"));

                        String [] part = file.split("/");
                        int len = part.length;
                        String inputFile = part[len - 1];
                        String inputPath = file.replace(inputFile, "");
                        String outputPath = Environment.getExternalStorageDirectory() + "/Android/data/com.sofudev.trackapptrl/files/temp/";

                        Log.d("Input path : ", inputPath);
                        Log.d("Input file : ", inputFile);
                        Log.d("Output path : ", outputPath);

                        moveFile(inputPath, inputFile, outputPath);
                        sentData(object.getString("path"));
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

        smr.addFile("file", file);

        AppController.getInstance().addToRequestQueue(smr);
    }
    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();
        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    private void insertHeader(final Data_partai_header item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLINSERTHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d(FormLensaPartaiActivity.class.getSimpleName(), "Success Insert Header");
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
                hashMap.put("order_number", item.getOrderNumber());
                hashMap.put("id_party", String.valueOf(item.getIdParty()));
                hashMap.put("optic_name", item.getOpticName());
                hashMap.put("optic_address", item.getOpticAddress());
                hashMap.put("optic_city", item.getOpticCity());
                hashMap.put("phone_number", "");
                hashMap.put("prod_div_type", "");
                hashMap.put("shipping_id", "0");
                hashMap.put("shipping_name", "");
                hashMap.put("optic_province", item.getOpticProvince());
                hashMap.put("shipping_service", "");
                hashMap.put("shipping_price", "0");
                hashMap.put("total_price", String.valueOf(totalPrice));
                hashMap.put("payment_cashcarry", "Non Payment Method");
                hashMap.put("flash_note", item.getFlashNote());
                hashMap.put("order_sp", item.getOrderSp());
                hashMap.put("salesname", headerSales);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void inserItem(final Data_partai_item item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLINSERTITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Log.d(FormLensaPartaiActivity.class.getSimpleName(), "Success Insert Item");
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
                map.put("order_number", item.getOrderNumber());
                map.put("item_id", String.valueOf(item.getItemId()));
                map.put("item_code", item.getItemCode());
                map.put("description", item.getDescription());
                map.put("side", item.getSide());
                map.put("qty", String.valueOf(item.getQty()));
                map.put("price", String.valueOf(item.getPrice()));
                map.put("discount_name", "");
//                map.put("discount", "0");
                map.put("discount", String.valueOf(item.getDiscount()));
                map.put("discount_sale", "0");
                map.put("total_price", String.valueOf(item.getTotal_price()));
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertNonPay(final String noSp)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLINSERTNONPAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Log.d(FormLensaPartaiActivity.class.getSimpleName(), "Success Insert Non payment");
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
                map.put("order_number", noSp);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertSP(final String URL, final Data_spheader item, final String excelname) {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Data has been save");
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
                hashMap.put("photo", "\\\\192.168.44.21\\ordertxt\\orderandroid\\Foto SP\\foto\\" + item.getPhoto());
                hashMap.put("status", item.getStatus());
                hashMap.put("excelpath", "\\\\192.168.44.21\\ordertxt\\orderandroid\\Foto SP\\excel\\" + excelname);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertSpHeader(final Data_spheader item, final String excelname) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTSPHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Success");

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
                hashMap.put("photo", config.Ip_address + "assets/images/ordersp/" + item.getPhoto());
                hashMap.put("path", item.getPhoto());
                hashMap.put("status", item.getStatus());
                hashMap.put("signedurl", config.Ip_address + "assets/images/signedsp/" + item.getSignedPath());
                hashMap.put("signedpath", item.getSignedPath());
                hashMap.put("excel", config.Ip_address + "assets/images/excel/" + excelname);
                hashMap.put("excelpath", excelname);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getActiveSale()
    {
        flashsaleNote = "";
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETACTIVESALE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        flashsaleNote = "";
                        Log.d("Status sale", "Tidak ada flashsale");

                        flagDiscSale = 0;
                    }
                    else
                    {
                        Log.d("Status sale", "Ada flashsale");

                        flashsaleNote = object.getString("title");
                        flagDiscSale = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                flashsaleNote = "";
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllStock()
    {
        item_stocklens.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLALLLENS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String deskripsi = object.getString("item_desc");
                        String kodelensa = object.getString("item_code_opto");
                        String gambar    = object.getString("image");

                        Data_lenstype dataLenstype = new Data_lenstype();
                        dataLenstype.setImage(gambar);
                        dataLenstype.setLenstype(kodelensa);
                        dataLenstype.setLensdescription(deskripsi);

                        item_stocklens.add(dataLenstype);
                    }

                    adapter_lenstype.notifyDataSetChanged();
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

    private void getPricePartai(final String key)
    {
        itemPrice = 0;
        itemQty = 1;
        itemId = "0";
        itemCode = "";
        itemDesc = "";
        itemRl = "";
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETPARTAIPRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        itemPrice = 0;
                        itemQty = 1;
                        itemId = "0";
                        itemCode = "";
                        itemDesc = "";
                        itemRl = "";
                    }
                    else
                    {
                        itemId    = jsonObject.getString("itemId");
                        itemCode  = jsonObject.getString("itemCode");
                        itemDesc  = jsonObject.getString("description");
                        itemRl    = jsonObject.getString("rl");
                        itemPrice = jsonObject.getInt("price");

                        calcPrice();
                        Log.i("Item Price : ", String.valueOf(itemPrice));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                itemPrice = 0;
                itemQty = 1;
                itemId = "0";
                itemCode = "";
                itemDesc = "";
                itemRl = "";
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("item_code", key);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void qtyCheck()
    {
        if (txtQty.getText().toString().isEmpty())
        {
            isQty = false;
            itemQty = 1;
        }
        else
        {
            isQty = true;
            itemQty   = Integer.parseInt(txtQty.getText().toString());
        }

//        totalPrice = itemPrice * itemQty;
//        String strTotal = String.valueOf(totalPrice);
//        Log.i("Total Price : ", strTotal);
    }

    private boolean dpCheck(int val)
    {
        return val > 0;
    }

    private void calcPrice(){
        itemQty = txtQty.getText().toString().length() > 0 ? Integer.parseInt(txtQty.getText().toString().trim()) : 0;
        headerDisc = edDiscount.getText().toString().length() > 0
                ? Double.parseDouble(edDiscount.getText().toString()) >= 90 ? "0" : edDiscount.getText().toString()
                : "0";
        headerDp = edJmlDp.length() > 0 ? Integer.parseInt(edJmlDp.getText().toString().replace(".","")) : 0;
        itemDisc = Double.parseDouble(headerDisc);

        double hitungTotalItem = itemPrice * itemQty;
        double hitungDiscItem = Double.parseDouble(headerDisc) / 100 * itemPrice * itemQty;
        String strTotalItem = "Rp. " + CurencyFormat(String.valueOf(hitungTotalItem));
        txtPrice.setText(strTotalItem);

        calcDisc(hitungDiscItem);
        calcTotal(hitungTotalItem, hitungDiscItem);
    }

    private void calcDisc(double hitungDiscItem){
        String strDiscItem = "Rp. " + CurencyFormat(String.valueOf(hitungDiscItem));
        txtDisc.setText(strDiscItem);
    }

    private void calcTotal(double hitungTotalItem, double hitungDiscItem){
        double grandTotal = hitungTotalItem - hitungDiscItem;
        String strGrandTotal = "Rp. " + CurencyFormat(String.valueOf(grandTotal));
        txtTotal.setText(strGrandTotal);
        totalPrice = (int) grandTotal;
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.#";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void lensCheck()
    {
        isLens = id_lensa != null;

        Log.i("Lensa check : ", isLens.toString());
    }

    private void fileCheck()
    {
        isFile = fileString != null;
        Log.i("File check : ", isFile.toString());
    }

    private void successDialog()
    {
        final Dialog dialog = new Dialog(FormLensaPartaiActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        lwindow.copyFrom(dialog.getWindow().getAttributes());
        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
        ImageView imgIcon  = dialog.findViewById(R.id.dialog_warning_imgIcon);
        UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
        txtTitle.setText("Success, order anda telah diterima Timur Raya");
        imgIcon.setImageResource(R.drawable.success_outline);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent("finishLp");
                sendBroadcast(intent);

                finish();
            }
        });

        if(!isFinishing())
        {
            dialog.show();
        }
        dialog.getWindow().setAttributes(lwindow);
    }

    private void dialogAllLens()
    {
        final ListView listView;
        final BootstrapButton btn_save;
        final Dialog dialog = new Dialog(FormLensaPartaiActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_choose_lensstock);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        adapter_lenstype = new Adapter_lenstype(FormLensaPartaiActivity.this, item_stocklens);

        listView    =  dialog.findViewById(R.id.choose_lensstock_listview);
        btn_save    =  dialog.findViewById(R.id.choose_lensstock_btnsave);

        getAllStock();
        listView.setAdapter(adapter_lenstype);

        btn_save.setEnabled(false);
        btn_save.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_lensa = item_stocklens.get(position).getLenstype();
                desc_lensa = item_stocklens.get(position).getLensdescription();

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
                txtLensa.setText(desc_lensa);
                getPricePartai(id_lensa);
                dialog.dismiss();
            }
        });

        if (!isFinishing())
        {
            dialog.show();
        }
    }
}