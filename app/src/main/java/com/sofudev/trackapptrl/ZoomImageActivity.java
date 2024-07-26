package com.sofudev.trackapptrl;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_slider_product;
import com.sofudev.trackapptrl.Fragment.DetailFrameFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;
import ozaydin.serkan.com.image_zoom_view.SaveFileListener;

public class ZoomImageActivity extends AppCompatActivity {
    WebView webView;
    UniversalFontTextView txtDescription;
    ImageViewZoom zoom;
    ImageView btnBack;
    FloatingActionButton fabDownload;

    String frameId, imgUrl;
    String [] permission = new String[] {
            Manifest.permission.READ_MEDIA_IMAGES
    };

    boolean is_images_permitted = false;

    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        UniversalFontComponents.init(getApplicationContext());

        webView = findViewById(R.id.zoom_webview);
        zoom = findViewById(R.id.zoom_image);
        btnBack = findViewById(R.id.zoom_btnBack);
        txtDescription = findViewById(R.id.zoom_txtDescription);
        fabDownload = findViewById(R.id.zoom_btnDownloadImage);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());

        getData();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//        {
//            request_permission_images.launch(permission[0]);
//            fabDownload.setVisibility(View.VISIBLE);
//
//            fabDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(DetailFrameFragment.class.getSimpleName(), "Download Image");
//                    final boolean[] status = new boolean[1];
//
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                status[0] = getBitmap(imgUrl);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                    thread.start();
//
//                    if(status[0])
//                    {
//                        Toasty.info(getApplicationContext(), "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        else
//        {
//            fabDownload.setVisibility(View.INVISIBLE);
//
//            ImageViewZoomConfig imageViewZoomConfig = new ImageViewZoomConfig
//                    .Builder()
//                    .saveProperty(true)
//                    .saveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.always)
//                    .build();
//
//            zoom.setConfig(imageViewZoomConfig);
//
//            zoom.saveImage(this, "ImageSave", "image", Bitmap.CompressFormat.JPEG, 100, imageViewZoomConfig, new SaveFileListener() {
//                @Override
//                public void onSuccess(File file) {
//                    Log.d(ZoomImageActivity.class.getSimpleName(), "Save image");
//                    Toasty.success(getApplicationContext(), "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFail(Exception e) {
//                    Log.d(ZoomImageActivity.class.getSimpleName(), e.toString());
//                    Toasty.warning(getApplicationContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean getBitmap(String imageurl) {
        InputStream in = null;
        boolean isSuccess;
        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
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

    @SuppressLint("RestrictedApi")
    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            imgUrl = bundle.getString("image_url");
            String description = bundle.getString("frame_description");
            frameId = bundle.getString("frame_id");

            Log.d(ZoomImageActivity.class.getSimpleName(), "Frame Id : " + frameId);

            webView.loadUrl("https://timurrayalab.com/adminproduct/description_product/long_description/" + frameId);

            Picasso.with(getApplicationContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.progress_zoom)
                    .error(R.drawable.pic_holder)
                    .fit()
                    .into(zoom);
//                    .into(zoom, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            zoom.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });

            txtDescription.setText(description);
            txtDescription.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            {
                request_permission_images.launch(permission[0]);
                fabDownload.setVisibility(View.VISIBLE);

                fabDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(DetailFrameFragment.class.getSimpleName(), "Download Image");
                        final boolean[] status = new boolean[1];

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    status[0] = getBitmap(imgUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                        if(status[0])
                        {
                            Toasty.info(getApplicationContext(), "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                fabDownload.setVisibility(View.VISIBLE);

                fabDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadImageNew(frameId, imgUrl);
                    }
                });

                /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                {
                    fabDownload.setVisibility(View.VISIBLE);

                    fabDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadImageNew(frameId, imgUrl);
                        }
                    });
                }
                else
                {
                    fabDownload.setVisibility(View.INVISIBLE);

                    ImageViewZoomConfig imageViewZoomConfig = new ImageViewZoomConfig
                            .Builder()
                            .saveProperty(true)
                            .saveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog)
                            .build();

                    zoom.setConfig(imageViewZoomConfig);

                    zoom.saveImage(this, "ImageSave", "image", Bitmap.CompressFormat.JPEG, 100, imageViewZoomConfig, new SaveFileListener() {
                        @Override
                        public void onSuccess(File file) {
                            Log.d(ZoomImageActivity.class.getSimpleName(), "Save image");
                            Toasty.success(getApplicationContext(), "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(Exception e) {
                            Log.d(ZoomImageActivity.class.getSimpleName(), e.toString());
                            Toasty.warning(getApplicationContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
            }
        }
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toasty.success(this, "Gambar berhasil disimpan", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toasty.error(this, "Gambar gagal disimpan", Toast.LENGTH_SHORT).show();
        }
    }
}