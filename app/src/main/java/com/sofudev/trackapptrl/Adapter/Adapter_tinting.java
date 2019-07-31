package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofudev.trackapptrl.Data.Data_tinting;
import com.sofudev.trackapptrl.R;

import java.util.List;

/**
 * Created by sholeh on 30/04/2018.
 */

public class Adapter_tinting extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Data_tinting> list;

    public Adapter_tinting(Context context, List<Data_tinting> list) {
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
            convertView = inflater.inflate(R.layout.spin_framemodel_item, null);
        }

        TextView txt_item = (TextView) convertView.findViewById(R.id.spin_framemodel_itemtxt);

        Data_tinting data_tinting;
        data_tinting = list.get(position);
        txt_item.setText(data_tinting.getTint_description());

        return convertView;
    }
}
