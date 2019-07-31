package com.sofudev.trackapptrl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Data.Data_uac;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fuddins on 28/09/2017.
 */

public class Adapter_uac extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;

    private List<Data_uac> items;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Adapter_uac (Activity activity, List<Data_uac> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_uac, null);
        }

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        TextView customer = (TextView) convertView.findViewById(R.id.listview_uac_title);
        BootstrapCircleThumbnail image = (BootstrapCircleThumbnail) convertView.findViewById(R.id.listview_uac_thumbnail);
        BootstrapButton status = (BootstrapButton) convertView.findViewById(R.id.listview_uac_status);
        TextView username = (TextView) convertView.findViewById(R.id.listview_uac_username);
        status.setFocusable(false);
        status.setClickable(false);

        Data_uac data_uac = items.get(position);

        customer.setText(data_uac.getTitle());
        //image.setImageUrl(data_uac.getImage(), imageLoader);
        Picasso.with(activity).load(data_uac.getImage()).into(image);
        username.setText(data_uac.getUsername());

        if (data_uac.getStatus().equals("Active"))
        {
            status.setText(data_uac.getStatus());
            status.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        }
        else if (data_uac.getStatus().equals("Inactive"))
        {
            status.setText(data_uac.getStatus());
            status.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
        }

        return convertView;
    }
}
