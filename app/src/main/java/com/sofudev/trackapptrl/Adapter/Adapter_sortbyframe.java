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

/**
 * Created by sholeh on 5/3/2019.
 */

public class Adapter_sortbyframe extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int pos;

    private List<String> item;

    public Adapter_sortbyframe(Context context, List<String> item, int pos) {
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

        switch (pos)
        {
            default:
                title.setTextColor(Color.parseColor("#58595e"));
                img.setVisibility(View.INVISIBLE);
                break;

            case 1:
                title.setText(data);
                if (data.equals("Title (A-Z)"))
                {
                    title.setTextColor(Color.parseColor("#358bff"));
                    img.setVisibility(View.VISIBLE);
                }
                else
                {
                    title.setTextColor(Color.parseColor("#58595e"));
                    img.setVisibility(View.INVISIBLE);
                }
                break;

            case 2:
                title.setText(data);
                if (data.equals("Title (Z-A)"))
                {
                    title.setTextColor(Color.parseColor("#358bff"));
                    img.setVisibility(View.VISIBLE);
                }
                else
                {
                    title.setTextColor(Color.parseColor("#58595e"));
                    img.setVisibility(View.INVISIBLE);
                }
                break;

            case 3:
                title.setText(data);
                if (data.equals("Price (Low-High)"))
                {
                    title.setTextColor(Color.parseColor("#358bff"));
                    img.setVisibility(View.VISIBLE);
                }
                else
                {
                    title.setTextColor(Color.parseColor("#58595e"));
                    img.setVisibility(View.INVISIBLE);
                }
                break;

            case 4:
                title.setText(data);
                if (data.equals("Price (High-Low)"))
                {
                    title.setTextColor(Color.parseColor("#358bff"));
                    img.setVisibility(View.VISIBLE);
                }
                else
                {
                    title.setTextColor(Color.parseColor("#58595e"));
                    img.setVisibility(View.INVISIBLE);
                }
                break;
        }

        return convertView;
    }
}
