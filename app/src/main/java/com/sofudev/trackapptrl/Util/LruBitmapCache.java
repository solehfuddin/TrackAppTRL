package com.sofudev.trackapptrl.Util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageCache;

//import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Fuddins on 28/09/2017.
 */

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    @Override
    public void invalidateBitmap(String url) {

    }

    @Override
    public void clear() {

    }
}
