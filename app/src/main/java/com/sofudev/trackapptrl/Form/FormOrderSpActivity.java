package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageMultiResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_filter_optic;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_opticname;
import com.sofudev.trackapptrl.Data.Data_spheader;
import android.Manifest;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;


public class FormOrderSpActivity extends AppCompatActivity {
    Config config = new Config();
    String URL_GETIDSP = config.Ip_address + config.ordersp_get_spId;
    String URL_GETIDSPFRAME   = config.Ip_address + config.ordersp_get_spIdFrame;
    String URL_INSERTSPHEADER = config.Ip_address + config.ordersp_insert_spHeader;
    String URL_AUTOSENTPHOTO  = config.Ip_address + config.ordersp_autosent_photo;
    String URL_INSERTSAMTEMP  = config.Ip_address + config.ordersp_insert_samTemp;
    String URL_INSERTTRXHEADER= config.Ip_address + config.ordersp_insert_trxHeader;
    String URL_UPDATEPHOTO    = config.Ip_address + config.ordersp_update_photo;
    String URL_UPDATESIGNED   = config.Ip_address + config.ordersp_update_digitalsigned;

    Calendar calendar = Calendar.getInstance();

//    BetterSpinner spinMulaiCicilan;
    ACProgressFlower loading;
    Spinner spinCicilan, spinPilihPembayaran, spinTipeSp, spinMulaiCicilan;
    BootstrapEditText txtNomorSp, txtNamaOptik, txtKotaOptik, txtAlamatOptik, txtDiskon, txtJumlahDp,
            txtCicilan, txtAlamatPengiriman, txtShipNumber;
    RippleView btnChooseOptik, btnSave, btnBack;
    LinearLayout linearTitleCicilan, linearContentCicilan;
    ImageView imgPhoto;
    TextView txtImgLoc;
    SignaturePad digitalSignature;
    Button btnTakePicture, btnClearSign;

    Data_spheader dataHeader;
    String username, status, province, idOptic, opticUsername, idpartySales, mobilenumber, filename, imgpath, fname, filesigned,
    signedpath;
    String cicilanVal;
    boolean isDigitalSigned, isMulaiBayar, isPilihOptik, isImage;
    MCrypt mCrypt;
    Uri img_uri, signed_uri;

    public static final int RequestPermissionCode  = 1 ;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_sp);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        spinCicilan = findViewById(R.id.form_ordersp_spinCicilan);
        spinTipeSp = findViewById(R.id.form_ordersp_spinTipeSp);
        spinPilihPembayaran = findViewById(R.id.form_ordersp_txtPembayaran);
        spinMulaiCicilan = findViewById(R.id.form_ordersp_spinMulaiCicilan);
        txtShipNumber = findViewById(R.id.form_ordersp_txtShipNumber);
        txtNomorSp = findViewById(R.id.form_ordersp_txtNomorSp);
        txtNamaOptik = findViewById(R.id.form_ordersp_txtNamaOptik);
        txtKotaOptik = findViewById(R.id.form_ordersp_txtKotaOptik);
        txtAlamatOptik = findViewById(R.id.form_ordersp_txtAlamatOptik);
        txtJumlahDp = findViewById(R.id.form_ordersp_txtJumlahDp);
        txtDiskon = findViewById(R.id.form_ordersp_txtDiskon);
        txtCicilan = findViewById(R.id.form_ordersp_txtCicilan);
        txtAlamatPengiriman = findViewById(R.id.form_ordersp_txtAlamatPengiriman);
        digitalSignature = findViewById(R.id.form_ordersp_imgdigitalsign);
        btnChooseOptik = findViewById(R.id.form_ordersp_btnChooser);
        btnSave = findViewById(R.id.form_ordersp_ripplebtnsave);
        btnBack = findViewById(R.id.form_ordersp_ripplebtnback);
        btnTakePicture = findViewById(R.id.form_ordersp_btnPicture);
        btnClearSign = findViewById(R.id.form_ordersp_btndigitalsign);
        imgPhoto = findViewById(R.id.form_ordersp_imgPhoto);
        txtImgLoc = findViewById(R.id.form_ordersp_imglocation);
        linearTitleCicilan = findViewById(R.id.form_ordersp_linearTitleCicilan);
        linearContentCicilan = findViewById(R.id.form_ordersp_linearContentCicilan);

        autoLoad();
//        EnableRuntimePermission();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case "finishLs":
                        finish();
                        break;
                    case "finishLp":
                        finish();
                        break;
                    case "finishFr":
                        finish();
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finishLs"));
        registerReceiver(broadcastReceiver, new IntentFilter("finishLp"));
        registerReceiver(broadcastReceiver, new IntentFilter("finishFr"));
    }

//    @Override
//    protected void onStop() {
//        unregisterReceiver(broadcastReceiver);
//        super.onStop();
//    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormOrderSpActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
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

    private void autoLoad()
    {
        dataHeader = new Data_spheader();

        showLoading();

        txtNomorSp.setEnabled(false);
        txtNamaOptik.setEnabled(false);
        txtKotaOptik.setEnabled(false);
        txtAlamatOptik.setEnabled(false);
        txtShipNumber.setEnabled(false);
//        linearTitleCicilan.setVisibility(View.GONE);
//        linearContentCicilan.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String otherDate = sdf.format(calendar.getTime());

        getIdSp(otherDate);

        txtJumlahDp.requestFocus();

        ambilIntent();
        selectOptic();
        saveHeader();
        chooseTypeSp();
        choosePaymentSp();
        chooseStartInstallment();
        chooseDurasiCicil();

        setCurrency(txtJumlahDp);
        setDecimal(txtDiskon);

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

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//              loading.dismiss();
//                startActivityForResult(intent, 7);
                ImagePicker.build(new DialogConfiguration()
                        .setTitle("Choose")
                        .setOptionOrientation(LinearLayoutCompat.HORIZONTAL)
//                        .setResultImageDimension(1089, 719)
                                .setResultImageDimension(719,  1089)
                        , new ImageMultiResultListener() {
                    @Override
                    public void onImageResult(ArrayList<ImageResult> imageResult) {
                        Log.e(LOG_TAG, "onImageResult:Number of image picked " + imageResult.size());
                        Log.d("Image path : ", imageResult.get(0).getPath());


//                        Toasty.info(getApplicationContext(), imageResult.get(0).getPath(), Toast.LENGTH_SHORT).show();

//                        File photo = new File(imageResult.get(0).getPath());
////                        String filename = photo.getName();
//
//                        String genfilename = txtNomorSp.getText().toString() + "_" + Calendar.getInstance().getTime();
//                        File newphoto = new File(genfilename);
//
//                        photo.renameTo(newphoto);

                        Toasty.info(getApplicationContext(), imageResult.get(0).getPath(), Toast.LENGTH_LONG).show();

                        imgPhoto.setImageBitmap(imageResult.get(0).getBitmap());

                        img_uri = getImageUrl(getApplicationContext(), imageResult.get(0).getBitmap());
//                        filename = getPath(img_uri);

//                        img_uri = imageResult.get(0).getUri();
//                        createImageFile(imageResult.get(0).getBitmap());
//                        img_uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/TRLAPP/Img/" + fname));

                        filename = getPath(img_uri);
//
                        String[] delimit = filename.split("/");
//
                        imgpath  = delimit[delimit.length - 1];

//                        filename = getPath(img_uri);

//                        String[] delimit = filename.split("/");
//
//                        imgpath  = delimit[delimit.length - 1];
//
//                        txtImgLoc.setText(imageResult.get(0).getPath());

                        txtImgLoc.setVisibility(View.GONE);
                        txtImgLoc.setText(img_uri.getPath());

                        /*if (imageResult.size() > 0) {
                            imgPhoto.setImageBitmap(imageResult.get(0).getBitmap());

                            img_uri = getImageUrl(getApplicationContext(), imageResult.get(0).getBitmap());
                            filename = getPath(img_uri);

                            String[] delimit = filename.split("/");

                            String last = delimit[delimit.length - 1];

                            imgpath = username + "_" + last;

//                            File photo = new File(filename);
//
//                            File newFile = new File("inifilenamenya");
//                            photo.renameTo(newFile);
//
//                            String renameImg = photo.getName();

                            Log.e("IMG NAME", filename);
                            Log.e("IMG NEWNAME", imgpath);

//                            Log.e("IMG RENAME", renameImg);

//                            updatePhoto(dataHeader, URL_UPDATEPHOTO);
                        }*/
                    }
                }).show(getSupportFragmentManager());
            }
        });

        btnClearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalSignature.clear();
            }
        });
    }

    public File createImageFile(Bitmap bm) {
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File mFileTemp = null;
//        File myDir = new File(Environment.getExternalStorageDirectory() + "TRLAPP/Img");
//        if(!myDir.exists()){
//            myDir.mkdirs();
//        }
//        try {
//            mFileTemp=File.createTempFile(imageFileName,".jpg",myDir.getAbsoluteFile());
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return mFileTemp;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File myDir = new File(Environment.getExternalStorageDirectory() + "/TRLAPP/Img");
        myDir.mkdirs();
        fname = "Image-" + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        return file;
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 7 && resultCode == RESULT_OK) {
//
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            img_uri = getImageUrl(getApplicationContext(), bitmap);
//            filename = getPath(img_uri);
//
//            imgPhoto.setImageBitmap(bitmap);
//
//            updatePhoto(dataHeader, URL_UPDATEPHOTO);
//
//        }
//    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(FormOrderSpActivity.this,
                Manifest.permission.CAMERA))
        {

//            Toast.makeText(FormOrderSpActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
            Log.i(FormOrderSpActivity.class.getSimpleName(), "CAMERA permission allows us to Access CAMERA app");
        } else {

            ActivityCompat.requestPermissions(FormOrderSpActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(FormOrderSpActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                    Log.i(FormOrderSpActivity.class.getSimpleName(), "CAMERA permission allows us to Access CAMERA app");
                } else {

                    Toast.makeText(FormOrderSpActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void ambilIntent() {
        Bundle data = getIntent().getExtras();
        username = data.getString("username");
        idpartySales = data.getString("idparty");

        if (idpartySales != null)
        {
            getUserInfo(idpartySales);
        }
    }

    private void chooseTypeSp() {
        final String [] model = getResources().getStringArray(R.array.type_sp_array);
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this, R.layout.spin_framemodel_item, model);
        spinTipeSp.setAdapter(spin_adapter);

        spinTipeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                spinTipeSp.setText(selectedItem);
//                spinTipeSp.setError(null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String otherDate = sdf.format(calendar.getTime());

                if (spinTipeSp.getSelectedItem().toString().equals("Frame"))
                {
//                    Log.d("SELECTED SP", "Frame");
                    getIdSpFrame(otherDate);
                }
                else
                {
//                    Log.d("SELECTED SP", "Lensa");
                    getIdSp(otherDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void setDecimal(final BootstrapEditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);
                if (!s.toString().equals(""))
                {
                    String data = edt.getText().toString().replace(".", "");

                    if(edt.getText().length() > 2)
                    {
                        double amount = Double.valueOf(data);

                        amount = amount / 10;
                        edt.setText(String.valueOf(amount));
                        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    }
                    else
                    {
                        edt.setText(data);
                    }

                    edt.setSelection(edt.length());
                }

                edt.addTextChangedListener(this);
            }
        });
    }

    private void setCurrency(final BootstrapEditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edt.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
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
                    edt.setText(clean);
                    edt.setSelection(clean.length());
                    edt.addTextChangedListener(this);
                }
            }
        });
    }

    private void getIdSp(final String date) {
//        showLoading();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETIDSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String idSp = object.getString("lastnumber");

                    txtNomorSp.setText(idSp);
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.d("ERROR GET IDSP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("date", date);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getIdSpFrame(final String date) {
        showLoading();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETIDSPFRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    String idSp = object.getString("lastnumber");

                    txtNomorSp.setText(idSp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.d("ERROR GET IDSP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("date", date);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getUserInfo(final String idParty) {
        Config config = new Config();
        String URL = config.Ip_address + config.profile_user_detail;
        mCrypt = new MCrypt();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String mobnumber    = "phone";

                try {
                    String info5 = MCrypt.bytesToHex(mCrypt.encrypt(mobnumber));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String data5 = new String(mCrypt.decrypt(jsonObject.getString(info5)));

                            mobilenumber = data5;

                            Log.d("NOMOR HPSALES", mobilenumber);

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
                hashmap.put("id_party", idParty);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void selectOptic() {
        btnChooseOptik.setOnClickListener(new View.OnClickListener() {
            Config config = new Config();
            String URLSHOWALLOPTIC      = config.Ip_address + config.filter_optic_showall;
            String URLSHOWOPTICBYNAME   = config.Ip_address + config.filter_optic_showbyname;
            String URLOPTICINFO         = config.Ip_address + config.ordersp_get_opticInfo;

            List<Data_opticname> data_opticnames = new ArrayList<>();
            Adapter_filter_optic adapter_filter_optic;
            Data_opticname data;

            MCrypt mCrypt;

            UniversalFontTextView txtCounter;
            ListView listview;
            RippleView btnSearch, btnPrev, btnNext, btnSelect;
            Button selectBtn;
            BootstrapButton prevBtn, nextBtn;
            MaterialEditText txtSearch;
            RelativeLayout rl_optic;
            ImageView imgView_opticname;
            View progress;

            String idparty;
            Integer req_start = 0, totalrow, lastitem, item;
            long lastclick = 0;

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FormOrderSpActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_choose_optic);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                txtSearch = dialog.findViewById(R.id.form_chooseoptic_txtsearch);
                btnSearch = dialog.findViewById(R.id.form_chooseoptic_btnsearch);
                btnPrev = dialog.findViewById(R.id.form_chooseoptic_ripplebtnprev);
                btnNext = dialog.findViewById(R.id.form_chooseoptic_ripplebtnnext);
                txtCounter = dialog.findViewById(R.id.form_chooseoptic_txtCounter);
                listview = dialog.findViewById(R.id.form_chooseoptic_listview);
                btnSelect = dialog.findViewById(R.id.form_chooseoptic_ripplebtnselect);
                prevBtn = dialog.findViewById(R.id.form_chooseoptic_btnprev);
                nextBtn = dialog.findViewById(R.id.form_chooseoptic_btnnext);
                selectBtn = dialog.findViewById(R.id.form_chooseoptic_btnselect);
                rl_optic = dialog.findViewById(R.id.form_chooseoptic_rl);
                progress    = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.footer_progress, null, false);

                adapter_filter_optic = new Adapter_filter_optic(getApplicationContext(), data_opticnames);
                listview.setAdapter(adapter_filter_optic);

                mCrypt      = new MCrypt();

                disableSelect();
                disableNext();
                disablePrev();
                selectOptic();
                searchOptic();
                showNextOptic();
                showPrevOptic();

                req_start = 0;
                data_opticnames.clear();
                showAllOptic(req_start);

                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOpticInfo(idparty);
                        req_start = 0;
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

            private void disableSelect() {
                btnSelect.setEnabled(false);
                selectBtn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
            }

            private void enableSelect() {
                btnSelect.setEnabled(true);
                selectBtn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
            }

            private void disablePrev() {
                btnPrev.setEnabled(false);
                prevBtn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
            }

            private void enablePrev() {
                btnPrev.setEnabled(true);
                prevBtn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
            }

            private void disableNext() {
                btnNext.setEnabled(false);
                nextBtn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
            }

            private void enableNext() {
                btnNext.setEnabled(true);
                nextBtn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
            }

            private void selectOptic() {
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        idparty = data_opticnames.get(position).getUsername();
                        //Toast.makeText(getApplicationContext(), idparty, Toast.LENGTH_SHORT).show();

                        if (idparty.isEmpty())
                        {
                            disableSelect();
                        }
                        else
                        {
                            enableSelect();
                        }
                    }
                });
            }

            private void showNextOptic() {
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - lastclick < 1500)
                        {
                            return;
                        }

                        lastclick = SystemClock.elapsedRealtime();

                        try {
                            Thread.sleep(300);

                            if (txtSearch.getText().length() == 0)
                            {
                                lastitem = 0;
                                req_start = req_start + 8;

                                item     = totalrow % 8;
                                lastitem = req_start + item;

                                enablePrev();
                                disableSelect();
                                listview.addHeaderView(progress);
                                showAllOptic(req_start);
                            }
                            else
                            {
                                lastitem = 0;
                                req_start = req_start + 8;

                                item     = totalrow % 8;
                                lastitem = req_start + item;

                                enablePrev();
                                disableSelect();
                                listview.addHeaderView(progress);
                                showOpticByName(txtSearch.getText().toString(),req_start);
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            private void showPrevOptic() {
                btnPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - lastclick < 1500)
                        {
                            return;
                        }

                        lastclick = SystemClock.elapsedRealtime();

                        try {
                            Thread.sleep(300);

                            if (txtSearch.getText().length() == 0)
                            {
                                req_start = req_start - 8;

                                if (req_start == 0)
                                {
                                    disablePrev();
                                }

                                disableSelect();
                                listview.addHeaderView(progress);
                                showAllOptic(req_start);
                            }
                            else
                            {
                                req_start = req_start - 8;

                                if (req_start == 0)
                                {
                                    disablePrev();
                                }

                                disableSelect();
                                listview.addHeaderView(progress);
                                showOpticByName(txtSearch.getText().toString(), req_start);
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            private void showErrorImage() {
                imgView_opticname = new ImageView(getApplicationContext());
                imgView_opticname.setImageResource(R.drawable.notfound);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                imgView_opticname.setLayoutParams(lp);
                rl_optic.addView(imgView_opticname);
            }

            private void searchOptic() {
                txtSearch.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        req_start = 0;
                        disableSelect();

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {
                            String check = txtSearch.getText().toString();
                            if (check.isEmpty())
                            {
                                Toasty.warning(getApplicationContext(), "Please fill optic name", Toast.LENGTH_SHORT, true).show();
                            }
                            else
                            {
                                showOpticByName(check, req_start);
                                disablePrev();
                            }
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
                        req_start = 0;
                        disableSelect();

                        if (check.isEmpty())
                        {
                            Toasty.warning(getApplicationContext(), "Please fill optic name", Toast.LENGTH_SHORT, true).show();
                        }
                        else
                        {
                            showOpticByName(check, req_start);
                            disablePrev();
                        }
                    }
                });
            }

            private void showAllOptic(int record) {
                adapter_filter_optic.notifyDataSetChanged();
                data_opticnames.clear();

                StringRequest stringRequest = new StringRequest(URLSHOWALLOPTIC + String.valueOf(record), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listview.removeHeaderView(progress);
                        String info1 = "username";
                        String info2 = "custname";
                        String info3 = "status";
                        String info4 = "total_row";
                        int start, until;

                        try {
                            String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                            String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                            String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                            String encrypt_info4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i=0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String username = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                                    String custname = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));
                                    String status   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info3)));
                                    String total    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info4)));

                                    totalrow = Integer.parseInt(total);

                                    start    = req_start + 1;
                                    until    = jsonArray.length() + req_start;

                                    String counter = "Show " + start + " - " + until + " from "+ totalrow + " records";
                                    txtCounter.setText(counter);

                                    data = new Data_opticname();
                                    data.setUsername(username);
                                    data.setCustname(custname);
                                    data.setStatus(status);

                                    data_opticnames.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (totalrow > 8)
                            {
                                enableNext();
                            }
                            else
                            {
                                disableNext();
                                disablePrev();
                            }

                            adapter_filter_optic.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toasty.error(getApplicationContext(), "Failed to encrypt message", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

                stringRequest.setShouldCache(false);
                AppController.getInstance().addToRequestQueue(stringRequest);
            }

            private void showOpticByName(final String key, int record) {
                adapter_filter_optic.notifyDataSetChanged();
                data_opticnames.clear();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSHOWOPTICBYNAME + String.valueOf(record), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listview.removeHeaderView(progress);
                        rl_optic.removeView(imgView_opticname);
                        String info1 = "username";
                        String info2 = "custname";
                        String info3 = "status";
                        String info4 = "total_row";
                        String info5 = "invalid";
                        int start, until;

                        try {
                            String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                            String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                            String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                            String encrypt_info4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));
                            String encrypt_info5 = MCrypt.bytesToHex(mCrypt.encrypt(info5));

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    if (jsonObject.names().get(0).equals(encrypt_info5))
                                    {
                                        showErrorImage();
                                        String info = "No record found";
                                        txtCounter.setText(info);
                                        disableNext();
                                        disablePrev();
                                        Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                                    }
                                    else
                                    {
                                        String username = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                                        String custname = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));
                                        String status   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info3)));
                                        String total    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info4)));

                                        data            = new Data_opticname();
                                        data.setUsername(username);
                                        data.setCustname(custname);
                                        data.setStatus(status);

                                        totalrow        = Integer.parseInt(total);

                                        start           = req_start + 1;
                                        until           = jsonArray.length() + req_start;

                                        String counter  = "Show " + start + " - " + until + " from " + totalrow + " records";

                                        txtCounter.setText(counter);

                                        if (totalrow > 8)
                                        {
                                            enableNext();
                                        }
                                        else
                                        {
                                            disableNext();
                                            disablePrev();
                                        }

                                        if (totalrow.equals(until))
                                        {
                                            disableNext();
                                        }

                                        data_opticnames.add(data);
                                    }
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            adapter_filter_optic.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toasty.warning(getApplicationContext(), "Failed to encrypt data", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT, true).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("custname", key);

                        return hashMap;
                    }
                };

                stringRequest.setShouldCache(false);
                AppController.getInstance().addToRequestQueue(stringRequest);
            }

            private void getOpticInfo(final String id) {
                StringRequest request = new StringRequest(Request.Method.POST, URLOPTICINFO, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            status = obj.getString("status");

                            if (status.equals("A"))
                            {
                                txtNamaOptik.setError(null);
                                txtAlamatOptik.setError(null);
                                txtKotaOptik.setError(null);
                                txtShipNumber.setError(null);

                                idOptic = idparty;
                                txtNamaOptik.setText(obj.getString("custName"));
                                txtKotaOptik.setText(obj.getString("city"));
                                txtAlamatOptik.setText(obj.getString("address"));
                                txtShipNumber.setText(obj.getString("username"));
                                province = obj.getString("province");
                                opticUsername = obj.getString("username");
                            }
                            else
                            {
                                Toasty.error(getApplicationContext(), "Akun Inactive tidak dapat membuat SP", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage().isEmpty())
                        {
                            Log.d("ERROR OPTIC INFO", error.getMessage());
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_party", id);
                        return map;
                    }
                };

                AppController.getInstance().addToRequestQueue(request);
            }
        });
    }

    private void saveHeader() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto(dataHeader, URL_UPDATEPHOTO);
                updateSigned();

//                if (spinTipeSp.getText().toString().matches("-- Pilih SP --"))
//                {
//                    Toasty.warning(getApplicationContext(), "Harap pilih tipe sp", Toast.LENGTH_SHORT).show();
//                    spinTipeSp.setError("Pilih Sp");
//                    isTipeSp = false;
//                }
//                else
//                {
//                    spinTipeSp.setError(null);
//                    isTipeSp = true;
//                }

                if (imgpath == null)
                {
                    isImage = false;
                    information("Pemberitahuan", "Mohon masukkan gambar/foto SP", R.drawable.failed_outline, DefaultBootstrapBrand.DANGER);
                }
                else
                {
                    isImage = true;
                }

                if (!isDigitalSigned)
                {
                    Toasty.warning(getApplicationContext(), "Harap isi tanda tangan digital", Toast.LENGTH_SHORT).show();
                }

                if (spinPilihPembayaran.getSelectedItem().toString().matches("Cicilan"))
                {
                    if (spinMulaiCicilan.getSelectedItem().toString().matches("-- Pilih Bulan --"))
                    {
                        Toasty.warning(getApplicationContext(), "Harap pilih bulan", Toast.LENGTH_SHORT).show();
//                        spinMulaiCicilan.setError("Pilih Bulan");
                        isMulaiBayar = false;
                        cicilanVal = "";
                    }
                    else
                    {
                        isMulaiBayar = true;
                        cicilanVal = spinMulaiCicilan.getSelectedItem().toString();
                    }
                }
                else
                {
                    isMulaiBayar = true;
//                    cicilanVal = spinMulaiCicilan.getSelectedItem().toString();
                    cicilanVal = "";
                }


                if (txtNamaOptik.length() > 0)
                {
                    txtNamaOptik.setError(null);
                    txtAlamatOptik.setError(null);
                    txtKotaOptik.setError(null);
                    isPilihOptik = true;
                }
                else
                {
                    txtNamaOptik.setError("Harap Pilih Optik");
                    txtAlamatOptik.setError("Harap Pilih Optik");
                    txtKotaOptik.setError("Harap Pilih Optik");
                    Toasty.warning(getApplicationContext(), "Harap pilih optik", Toast.LENGTH_SHORT).show();
                    isPilihOptik = false;
                }

                if (txtCicilan.length() == 0)
                {
                    txtCicilan.setText("1");
                }

                if (isMulaiBayar && isPilihOptik && isImage && isDigitalSigned)
                {
                    dataHeader.setNoSp(txtNomorSp.getText().toString());
                    dataHeader.setTypeSp(spinTipeSp.getSelectedItem().toString());
                    dataHeader.setSales(username);
                    dataHeader.setShipNumber(txtShipNumber.getText().toString());
                    dataHeader.setCustName(txtNamaOptik.getText().toString());
                    dataHeader.setAddress(txtAlamatOptik.getText().toString());
                    dataHeader.setCity(txtKotaOptik.getText().toString());
                    dataHeader.setOrderVia("ANDROID");
                    dataHeader.setDp(txtJumlahDp.length() > 0 ? Integer.valueOf(txtJumlahDp.getText().toString().replace(".","")) : 0);
                    dataHeader.setDisc(txtDiskon.length() > 0 ? txtDiskon.getText().toString() : "0");
                    dataHeader.setCondition(spinPilihPembayaran.getSelectedItem().toString());
//                    dataHeader.setInstallment(txtCicilan.getText().toString());
                    dataHeader.setInstallment(spinCicilan.getSelectedItem().toString());
                    dataHeader.setStartInstallment(cicilanVal);
                    dataHeader.setShipAddress(txtAlamatPengiriman.getText().toString());
                    dataHeader.setStatus("");

                    if (spinTipeSp.getSelectedItem().toString().equals("Lensa Satuan"))
                    {
                        Intent intent = new Intent(getApplicationContext(), FormOrderLensActivity.class);
                        intent.putExtra("idparty", idOptic);
                        intent.putExtra("opticname", txtNamaOptik.getText().toString());
                        intent.putExtra("province", province);
                        intent.putExtra("usernameInfo", opticUsername);
                        intent.putExtra("city", txtKotaOptik.getText().toString());
                        intent.putExtra("level", "1");
                        intent.putExtra("flag", "0");
                        intent.putExtra("idSp", dataHeader.getNoSp());
                        intent.putExtra("isSp", "1");
                        intent.putExtra("noHp", mobilenumber);
                        intent.putExtra("header_nosp", txtNomorSp.getText().toString());
                        intent.putExtra("header_tipesp", spinTipeSp.getSelectedItem().toString());
                        intent.putExtra("header_sales", username);
                        intent.putExtra("header_shipnumber", txtShipNumber.getText().toString());
                        intent.putExtra("header_custname", txtNamaOptik.getText().toString());
                        intent.putExtra("header_address", txtAlamatOptik.getText().toString());
                        intent.putExtra("header_city", txtKotaOptik.getText().toString());
                        intent.putExtra("header_ordervia", "ANDROID");
                        intent.putExtra("header_dp", dataHeader.getDp());
                        intent.putExtra("header_disc", dataHeader.getDisc());
                        intent.putExtra("header_condition", spinPilihPembayaran.getSelectedItem().toString());
//                        intent.putExtra("header_installment", txtCicilan.getText().toString());
                        intent.putExtra("header_installment", spinCicilan.getSelectedItem().toString());
                        intent.putExtra("header_startinstallment", cicilanVal);
                        intent.putExtra("header_shippingaddress", txtAlamatPengiriman.getText().toString());
                        intent.putExtra("header_status", "");
                        intent.putExtra("header_image", imgpath);
                        intent.putExtra("header_signedpath", signedpath);
                        intent.putExtra("sales", username);
                        startActivity(intent);
                    }
                    else if (spinTipeSp.getSelectedItem().toString().equals("Lensa Partai"))
                    {
//                        Toasty.info(getApplicationContext(), "Form Lensa Partai", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(getApplicationContext(), FormBatchOrderActivity.class);
                        Intent intent = new Intent(getApplicationContext(), FormLensaPartaiActivity.class);

                        intent.putExtra("idparty", idOptic);
                        intent.putExtra("opticname", txtNamaOptik.getText().toString());
                        intent.putExtra("province", province);
                        intent.putExtra("usernameInfo", opticUsername);
                        intent.putExtra("province_address", txtAlamatOptik.getText().toString());
                        intent.putExtra("city", txtKotaOptik.getText().toString());
                        intent.putExtra("flag", "0");
                        intent.putExtra("idSp", dataHeader.getNoSp());
                        intent.putExtra("isSp", 1);
                        intent.putExtra("header_nosp", txtNomorSp.getText().toString());
                        intent.putExtra("header_tipesp", spinTipeSp.getSelectedItem().toString());
                        intent.putExtra("header_sales", username);
                        intent.putExtra("header_shipnumber", txtShipNumber.getText().toString());
                        intent.putExtra("header_custname", txtNamaOptik.getText().toString());
                        intent.putExtra("header_address", txtAlamatOptik.getText().toString());
                        intent.putExtra("header_city", txtKotaOptik.getText().toString());
                        intent.putExtra("header_ordervia", "ANDROID");
                        intent.putExtra("header_dp", dataHeader.getDp());
                        intent.putExtra("header_disc", dataHeader.getDisc());
                        intent.putExtra("header_condition", spinPilihPembayaran.getSelectedItem().toString());
//                        intent.putExtra("header_installment", txtCicilan.getText().toString());
                        intent.putExtra("header_installment", spinCicilan.getSelectedItem().toString());
                        intent.putExtra("header_startinstallment", cicilanVal);
                        intent.putExtra("header_shippingaddress", txtAlamatPengiriman.getText().toString());
                        intent.putExtra("header_status", "");
                        intent.putExtra("header_image", imgpath);
                        intent.putExtra("header_signedpath", signedpath);
                        startActivity(intent);
                    }
                    else if (spinTipeSp.getSelectedItem().toString().equals("Frame"))
                    {
//                            Toasty.info(getApplicationContext(), "Form Frame", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), FormSpFrameActivity.class);
                        intent.putExtra("idparty", idOptic);
                        intent.putExtra("opticname", txtNamaOptik.getText().toString());
                        intent.putExtra("province", province);
                        intent.putExtra("usernameInfo", opticUsername);
                        intent.putExtra("city", txtKotaOptik.getText().toString());
                        intent.putExtra("province_address", txtAlamatOptik.getText().toString());
                        intent.putExtra("idSp", dataHeader.getNoSp());
                        intent.putExtra("header_nosp", txtNomorSp.getText().toString());
                        intent.putExtra("header_tipesp", spinTipeSp.getSelectedItem().toString());
                        intent.putExtra("header_sales", username);
                        intent.putExtra("header_shipnumber", txtShipNumber.getText().toString());
                        intent.putExtra("header_custname", txtNamaOptik.getText().toString());
                        intent.putExtra("header_address", txtAlamatOptik.getText().toString());
                        intent.putExtra("header_city", txtKotaOptik.getText().toString());
                        intent.putExtra("header_ordervia", "ANDROID");
                        intent.putExtra("header_dp", dataHeader.getDp());
                        intent.putExtra("header_disc", dataHeader.getDisc());
                        intent.putExtra("header_condition", spinPilihPembayaran.getSelectedItem().toString());
//                        intent.putExtra("header_installment", txtCicilan.getText().toString());
                        intent.putExtra("header_installment", spinCicilan.getSelectedItem().toString());
                        intent.putExtra("header_startinstallment", cicilanVal);
                        intent.putExtra("header_shippingaddress", txtAlamatPengiriman.getText().toString());
                        intent.putExtra("header_status", "");
                        intent.putExtra("header_image", imgpath);
                        intent.putExtra("header_signedpath", signedpath);

                        startActivity(intent);
                    }

//                    Intent intent = new Intent(getApplicationContext(), FormSpFrameActivity.class);
////                    intent.putExtra("idSp", dataHeader.getNoSp());
////                    intent.putExtra("isSp", "1");
//                    startActivity(intent);

//                    StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTSPHEADER, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject object = new JSONObject(response);
//
//                                if (object.names().get(0).equals("success"))
//                                {
//                                    Log.d("INSERT SP", "Success");
//
//                                    updatePhoto(dataHeader, URL_UPDATEPHOTO);
//                                }
//                                else if (object.names().get(0).equals("error"))
//                                {
//                                    Log.d("INSERT SP", "Error");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if (error.getMessage() != null)
//                            {
//                                Log.d("INSERT SP", error.getMessage());
//                            }
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("no_sp", txtNomorSp.getText().toString());
//                            hashMap.put("type_sp", spinTipeSp.getSelectedItem().toString());
//                            hashMap.put("sales", username);
//                            hashMap.put("customer_name", txtNamaOptik.getText().toString());
//                            hashMap.put("address", txtAlamatOptik.getText().toString());
//                            hashMap.put("city", txtKotaOptik.getText().toString());
//                            hashMap.put("order_via", "ANDROID");
//                            hashMap.put("down_payment", String.valueOf(dataHeader.getDp()));
//                            hashMap.put("discount", String.valueOf(dataHeader.getDisc()));
//                            hashMap.put("conditions", spinPilihPembayaran.getSelectedItem().toString());
//                            hashMap.put("installment", txtCicilan.getText().toString());
//                            hashMap.put("start_installment", cicilanVal);
//                            hashMap.put("shipping_address", txtAlamatPengiriman.getText().toString());
//                            hashMap.put("status","");
//                            return hashMap;
//                        }
//                    };
//
//                    AppController.getInstance().addToRequestQueue(request);
                }
            }
        });
    }

    public Uri getImageUrl(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
//        String timeStamp = new SimpleDateFormat("yyMMdd", Locale.US).format(new Date());

        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                username + "_" +  txtNomorSp.getText().toString() + "_" + timeStamp, null);

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

        final Dialog dialog = new Dialog(FormOrderSpActivity.this);
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
        dialog.show();
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

//                        if (spinTipeSp.getSelectedItem().toString().equals("Lensa Satuan"))
//                        {
//                            Intent intent = new Intent(getApplicationContext(), FormOrderLensActivity.class);
//                            intent.putExtra("idparty", idOptic);
//                            intent.putExtra("opticname", txtNamaOptik.getText().toString());
//                            intent.putExtra("province", province);
//                            intent.putExtra("usernameInfo", opticUsername);
//                            intent.putExtra("city", txtKotaOptik.getText().toString());
//                            intent.putExtra("flag", "0");
//                            intent.putExtra("idSp", dataHeader.getNoSp());
//                            intent.putExtra("isSp", "1");
//                            intent.putExtra("noHp", mobilenumber);
//                            startActivity(intent);
//                        }
//                        else if (spinTipeSp.getSelectedItem().toString().equals("Lensa Partai"))
//                        {
////                        Toasty.info(getApplicationContext(), "Form Lensa Partai", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(getApplicationContext(), FormBatchOrderActivity.class);
//
//                            intent.putExtra("idparty", idOptic);
//                            intent.putExtra("opticname", txtNamaOptik.getText().toString());
//                            intent.putExtra("province", province);
//                            intent.putExtra("usernameInfo", opticUsername);
//                            intent.putExtra("province_address", txtAlamatOptik.getText().toString());
//                            intent.putExtra("city", txtKotaOptik.getText().toString());
//                            intent.putExtra("flag", "0");
//                            intent.putExtra("idSp", dataHeader.getNoSp());
//                            intent.putExtra("isSp", "1");
//                            startActivity(intent);
//                        }
//                        else if (spinTipeSp.getSelectedItem().toString().equals("Frame"))
//                        {
////                            Toasty.info(getApplicationContext(), "Form Frame", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), FormSpFrameActivity.class);
//                            intent.putExtra("idparty", idOptic);
//                            intent.putExtra("opticname", txtNamaOptik.getText().toString());
//                            intent.putExtra("province", province);
//                            intent.putExtra("usernameInfo", opticUsername);
//                            intent.putExtra("city", txtKotaOptik.getText().toString());
//                            intent.putExtra("province_address", txtAlamatOptik.getText().toString());
//                            intent.putExtra("idSp", dataHeader.getNoSp());
//                            startActivity(intent);
//                        }
                    }
                    else if (object.names().get(0).equals("info")) {
                        Toasty.warning(getApplicationContext(), "Gambar sudah pernah diupload", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
//                        Toasty.success(getApplicationContext(), "Return image : " + object.getString("path"), Toast.LENGTH_SHORT).show();
                        Log.d("Return image : ", object.getString("path"));
//                        imgpath = object.getString("path");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.getMessage() != null)
//                {
//                    Log.d("ERROR INSERT SP", error.getMessage());
//                }
//                else
//                {
//                    Toasty.error(getApplicationContext(), "SP Number Redundant", Toast.LENGTH_SHORT).show();
//                }

                error.printStackTrace();
            }
        });

//        smr.addStringParam("no_sp", item.getNoSp());
//        smr.addStringParam("sales", username);
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
//                        Toasty.success(getApplicationContext(), "Return image : " + object.getString("path"), Toast.LENGTH_SHORT).show();
                        Log.d("Return image : ", object.getString("path"));
//                        imgpath = object.getString("path");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.getMessage() != null)
//                {
//                    Log.d("ERROR INSERT SP", error.getMessage());
//                }
//                else
//                {
//                    Toasty.error(getApplicationContext(), "SP Number Redundant", Toast.LENGTH_SHORT).show();
//                }

                error.printStackTrace();
            }
        });

        smr.addFile("image", filesigned);

        AppController.getInstance().addToRequestQueue(smr);
    }
}
