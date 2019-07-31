package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_spinner_shipment extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Data_spin_shipment> list;

    public Adapter_spinner_shipment(Context context, List<Data_spin_shipment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.spin_shipping_detail, null);
        }

        TextView txtKurir  = (TextView) convertView.findViewById(R.id.spin_shipping_txtkurir);
        ImageView imgKurir = (ImageView) convertView.findViewById(R.id.spin_shipping_imgkurir);

        Data_spin_shipment data_spin_shipment;
        data_spin_shipment = list.get(position);
        txtKurir.setText(data_spin_shipment.getKurir());
        Picasso.with(context).load(data_spin_shipment.getIcon()).into(imgKurir);

        return convertView;
    }
}
