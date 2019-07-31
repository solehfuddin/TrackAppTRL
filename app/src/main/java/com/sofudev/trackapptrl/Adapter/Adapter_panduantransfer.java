package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.R;

import java.util.List;

/**
 * Created by sholeh on 27/12/2018.
 */

public class Adapter_panduantransfer extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> item;

    public Adapter_panduantransfer(Context context, List<String> item) {
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
            view = inflater.inflate(R.layout.listview_panduan_transfer, null);
        }

        UniversalFontTextView txtTitle = (UniversalFontTextView) view.findViewById(R.id.listview_panduantransfer_title);

        String title = item.get(i);
        txtTitle.setText(title);

        return view;
    }
}
