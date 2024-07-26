package com.sofudev.trackapptrl.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sofudev.trackapptrl.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;
import ozaydin.serkan.com.image_zoom_view.SaveFileListener;

public class Adapter_slider_product extends PagerAdapter {
    private List<String> listImage;
    Context context;

    public Adapter_slider_product(List<String> listImage, Context context) {
        this.listImage = listImage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_slider_product, container, false);
//        PhotoView imageView = view.findViewById(R.id.list_sliderproduct_image);
        ImageViewZoom imageView = view.findViewById(R.id.list_sliderproduct_image);
//        ImageViewZoomConfig imageViewZoomConfig = new ImageViewZoomConfig();
//        imageViewZoomConfig.saveProperty(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//        {
//            imageViewZoomConfig.setImageViewZoomConfigSaveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.always);
//        }
//        else
//        {
//            imageViewZoomConfig.setImageViewZoomConfigSaveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog);
//        }

        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            ImageViewZoomConfig imageViewZoomConfig = new ImageViewZoomConfig
                    .Builder()
                    .saveProperty(true)
                    .saveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog)
                    .build();

            imageView.setConfig(imageViewZoomConfig);

            imageView.saveImage((Activity) context, "ImageSave", "image", Bitmap.CompressFormat.JPEG, 100, imageViewZoomConfig, new SaveFileListener() {
                @Override
                public void onSuccess(File file) {
                    Log.d(Adapter_slider_product.class.getSimpleName(), "Save image");
                    Toasty.success(context, "Sukses menyimpan gambar", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Exception e) {
                    Log.d(Adapter_slider_product.class.getSimpleName(), e.toString());
                    Toasty.warning(context, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

//        ZoomInImageViewAttacher attacher = new ZoomInImageViewAttacher(imageView);
//        attacher.setZoomable(true);
        //Picasso.with(context).load(listImage.get(position)).centerCrop().fit().into(imageView);
        Picasso.with(context).load(listImage.get(position)).placeholder(R.drawable.progress_zoom).error(R.drawable.pic_holder).into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
