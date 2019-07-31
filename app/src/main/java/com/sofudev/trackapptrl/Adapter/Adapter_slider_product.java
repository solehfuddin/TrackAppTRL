package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        ImageView imageView = view.findViewById(R.id.list_sliderproduct_image);
        Picasso.with(context).load(listImage.get(position)).centerCrop().fit().into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
