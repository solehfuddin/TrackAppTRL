package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_sortbycategory extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int pos;

    private List<String> item;

    public Adapter_sortbycategory(Context context, List<String> item, int pos) {
        this.context = context;
        this.item = item;
        this.pos  = pos;
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

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.recycle_sortby_frame, null);
        }

        ImageView img = convertView.findViewById(R.id.recycle_sortby_img);
        UniversalFontTextView title = convertView.findViewById(R.id.recycle_sortby_txttitle);

        String data = item.get(position);
        title.setText(data);

        //Check item position
        if (item.indexOf(data) == pos) {
            //Tandai item yang diseleksi
            title.setTextColor(Color.parseColor("#358bff"));
            img.setVisibility(View.VISIBLE);
        }
        else
        {
            title.setTextColor(Color.parseColor("#58595e"));
            img.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
