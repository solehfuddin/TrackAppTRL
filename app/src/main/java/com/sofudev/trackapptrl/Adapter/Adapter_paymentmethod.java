package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sholeh on 26/12/2018.
 */

public class Adapter_paymentmethod extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<Data_paymentmethod> item;

    public Adapter_paymentmethod(Context context, List<Data_paymentmethod> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null)
        {
            view = inflater.inflate(R.layout.listview_payment, null);
        }

        UniversalFontTextView txt_payment = (UniversalFontTextView) view.findViewById(R.id.listview_payment_title);
        ImageView img_payment = (ImageView) view.findViewById(R.id.listview_payment_image);

        Data_paymentmethod data = item.get(i);
        txt_payment.setText(data.getPaymentMethod());

        Picasso.with(context).load(data.getPaymentImage()).into(img_payment);

        return view;
    }
}
