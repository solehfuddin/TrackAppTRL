package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_sortingonhand;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_sorting_onhand extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<Data_sortingonhand> itemSorting;
    private String pos;

    public Adapter_sorting_onhand(Context context, List<Data_sortingonhand> itemSorting, String pos) {
        this.context = context;
        this.itemSorting = itemSorting;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return itemSorting.size();
    }

    @Override
    public Object getItem(int position) {
        return itemSorting.get(position);
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
            convertView = inflater.inflate(R.layout.listview_power, null);
        }

        UniversalFontTextView txtPower = convertView.findViewById(R.id.listview_power_txtpower);
        ImageView imgCheck             = convertView.findViewById(R.id.listview_power_img);

        String power = itemSorting.get(position).getDescription();
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
