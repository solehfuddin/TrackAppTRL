package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

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

//        ZoomInImageViewAttacher attacher = new ZoomInImageViewAttacher(imageView);
//        attacher.setZoomable(true);
        Picasso.with(context).load(listImage.get(position)).centerCrop().fit().into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
