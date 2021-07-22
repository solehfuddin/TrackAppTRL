package com.sofudev.trackapptrl.Form;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.DashboardActivity;
import com.sofudev.trackapptrl.Data.Data_partai_header;
import com.sofudev.trackapptrl.Data.Data_spheader;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressCustom;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class FormLensaPartaiActivity extends AppCompatActivity {
    private static final String TAG = "Download Task";
    Config config = new Config();
    private String downloadUrl = "http://180.250.96.154/trl-webs/assets/template/Order_partai.xls";
    String URLINSERTHEADER     = config.Ip_address + config.orderpartai_insertHeader;
    String URL_INSERTSPHEADER  = config.Ip_address + config.ordersp_insert_spHeader;
    String URL_INSERTSAMTEMP   = config.Ip_address + config.ordersp_insert_samTemp;
    String URL_UPDATEEXCEL     = config.Ip_address + config.ordersp_update_excel;
    String URL_GETACTIVESALE        = config.Ip_address + config.flashsale_getActiveSale;

    ACProgressFlower loading;
    String headerNoSp, headerTipeSp, headerSales, headerShipNumber, headerCustName, headerAddress, headerCity, headerOrderVia,
            headerDisc, headerCondition, headerInstallment, headerStartInstallment, headerShippingAddress, headerStatus,
            headerImage, headerSignedPath, flashsaleNote;
    String opticId, opticName, opticUsername, opticProvince, opticCity, opticAddress, opticFlag;
    int headerDp, flagDiscSale, isSp;
    private DownloadManager downloadManager;

    Data_spheader dataSpHeader = new Data_spheader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lensa_partai);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        getData();
        getActiveSale();

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

//                      String outputPath = Environment.getExternalStorageDirectory() + "/TRL APPS/temp/";
//                      String inputPath = part[0] + '/' + part[1] + '/' + part[2] + '/' + part[3] + '/' + part[4] + '/';

                        showLoading();
                        updateExcel(files[0]);
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
            headerDisc     = bundle.getString("header_disc");
            headerCondition= bundle.getString("header_condition");
            headerInstallment = bundle.getString("header_installment");
            headerStartInstallment = bundle.getString("header_startinstallment");
            headerShippingAddress = bundle.getString("header_shippingaddress");
            headerStatus   = bundle.getString("header_status");
            headerImage    = bundle.getString("header_image");
            headerSignedPath = bundle.getString("header_signedpath");
        }
    }

    private void sentData(String path) {
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

        insertSpHeader(dataSpHeader, path);
        insertSP(URL_INSERTSAMTEMP, dataSpHeader, path);
        insertHeader(data_header);

        try {
            Thread.sleep(2000);
            hideLoading();
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
                        Log.d(FormBatchOrderActivity.class.getSimpleName(), "Success Insert Header");
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
                hashMap.put("total_price", "0");
                hashMap.put("payment_cashcarry", "Non Payment Method");
                hashMap.put("flash_note", item.getFlashNote());
                hashMap.put("order_sp", item.getOrderSp());
                hashMap.put("salesname", headerSales);
                return hashMap;
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
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETACTIVESALE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
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
                if (!error.getMessage().isEmpty() || error.getMessage().equals(null))
                {
                    Log.d("Error Active Sale", error.getMessage());
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormLensaPartaiActivity.this)
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

        dialog.show();
        dialog.getWindow().setAttributes(lwindow);
    }
}