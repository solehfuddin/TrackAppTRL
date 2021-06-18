package com.sofudev.trackapptrl.Form;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class FormLensaPartaiActivity extends AppCompatActivity {
    private static final String TAG = "Download Task";
    Config config = new Config();
    private String downloadUrl = "http://180.250.96.154/trl-webs/assets/template/Order_partai.xls";
    String URL_UPDATEEXCEL   = config.Ip_address + config.ordersp_update_excel;
    private DownloadManager downloadManager;
    private Uri fileUri;
    private String nosp, pathFile;
    private ArrayList<Uri> filepaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lensa_partai);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            nosp = bundle.getString("header_nosp");
        }

        final RippleView btnDownload = findViewById(R.id.form_lensapartai_ripplebtndownload);
        final RippleView btnUpload   = findViewById(R.id.form_lensapartai_ripplebtnupload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet()) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setAllowedOverRoaming(false);
                    request.setTitle("Order_partai_" + nosp + ".xls");
                    request.setDescription("Order_partai_" + nosp + ".xls");
                    request.setDestinationInExternalPublicDir("", "/TRL APPS/" + "/" + "Order_partai_" + nosp + ".xls");
                    request.setVisibleInDownloadsUi(true);
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
//                            "/TRL APPS/" + "/" + "Order_partai_" + nosp + ".xls");

                    downloadManager.enqueue(request);

                    try {
                        Thread.sleep(2000);
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.setType("application/xls");
////                        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath()
////                                +  File.separator + "TRL APPS" + File.separator), "*/*");
//                        startActivityForResult(intent, 200);

//                        File file = new File(Environment.getExternalStorageDirectory()+ "/TRL APPS/" + "/" + "Order_partai_" + nosp + ".xls");
//                        Intent intent = new Intent(Intent.ACTION_EDIT);
//                        intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
//                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(FormLensaPartaiActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                intent = Intent.createChooser(intent, "Pilih file");
//                startActivityForResult(intent, 1);

                DialogProperties properties = new DialogProperties();
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.FILE_SELECT;
                properties.root = Environment.getExternalStorageDirectory();
                properties.extensions = null;

                FilePickerDialog pickerDialog = new FilePickerDialog(FormLensaPartaiActivity.this, properties);
                pickerDialog.setTitle("Pilih file excel");
                pickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        Log.d("File path : ", files[0]);

                        updateExcel(files[0]);
                    }
                });
                pickerDialog.show();
            }
        });
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
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                fileUri = data.getData();
//                pathFile = getPath(fileUri);
//                Log.d("File path : ", pathFile);
//
//                File source = new File(pathFile);
//                String filename = fileUri.getLastPathSegment();
//                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TRLTMP/" + filename);
//
//                copy(source, destination);
////                Toast.makeText(FormLensaPartaiActivity.this, pathFile , Toast.LENGTH_LONG).show();
////                updateExcel();
//            }
//        }
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    private void copy(File source, File destination) {

        FileChannel in = null;
        try {
            in = new FileInputStream(source).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel out = null;
        try {
            out = new FileOutputStream(destination).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            try {
                in.transferTo(0, in.size(), out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(Exception e){
            // post to log
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateExcel(String file) {
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
//                        Toasty.success(getApplicationContext(), "Return image : " + object.getString("path"), Toast.LENGTH_SHORT).show();
                        Log.d("Return file : ", object.getString("path"));
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

        smr.addFile("file", file);

        AppController.getInstance().addToRequestQueue(smr);
    }
}