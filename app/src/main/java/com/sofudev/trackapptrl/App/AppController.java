package com.sofudev.trackapptrl.App;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sofudev.trackapptrl.Util.LruBitmapCache;

public class AppController extends Application {
    //Variable untuk menampung Nama TAG
    public static final String TAG = AppController.class.getSimpleName();

    //Inisialisasi RequestQueue
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    //Inisialisasi AppController
    private static AppController mInstance;

    //Aktifkan AppController ketika diload pertama kali
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MyApp myApp = new MyApp();
        myApp.onCreate();
    }

    //Fungsi untuk sinkronisasi AppController
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    //Fungsi untuk requestQueue pada Volley
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    //Fungsi menambahkan requestQueue dengan Parameter Tag
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //Fungsi menambahkan requestQueue tanpa Tag
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //Fungsi untuk membatalkan requestQueue
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public class MyApp extends MultiDexApplication {
        @Override
        protected void attachBaseContext(Context context) {
            super.attachBaseContext(context);
            MultiDex.install(this);
        }
        @Override
        public void onCreate() {
            super.onCreate();
        }
    }
}
