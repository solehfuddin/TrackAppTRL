package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_frame_brand;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_frame_brand extends BaseAdapter {

    private LayoutInflater inflater;
    Context context;
    private List<Data_frame_brand> item;
    private String pos;

    public Adapter_frame_brand(Context context, List<Data_frame_brand> item, String pos) {
        this.context = context;
        this.item = item;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_power, null);
        }

        UniversalFontTextView txtPower = convertView.findViewById(R.id.listview_power_txtpower);
        ImageView imgCheck             = convertView.findViewById(R.id.listview_power_img);

        String power = item.get(position).getItem();
        txtPower.setText(power);

        if (power.equals(pos))
        {
            txtPower.setTextColor(Color.parseColor("#358bff"));
            imgCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            txtPower.setTextColor(Color.parseColor("#58595e"));
            imgCheck.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
