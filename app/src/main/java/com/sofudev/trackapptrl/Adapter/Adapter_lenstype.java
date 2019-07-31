package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_lenstype;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_lenstype extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<Data_lenstype> item;

    public Adapter_lenstype(Context context, List<Data_lenstype> item) {
        this.context = context;
        this.item = item;
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
            convertView = inflater.inflate(R.layout.listview_lenstype, null);
        }

        UniversalFontTextView txt_lenstype = (UniversalFontTextView) convertView.findViewById(R.id.listview_lenstype_lenstype);
        UniversalFontTextView txt_lensdesc = (UniversalFontTextView) convertView.findViewById(R.id.listview_lenstype_lensdescription);
        ImageView img_info                 = (ImageView) convertView.findViewById(R.id.listview_lenstype_image);

        Data_lenstype data = item.get(position);
        txt_lenstype.setText(data.getLenstype());
        txt_lensdesc.setText(data.getLensdescription());
        img_info.setImageResource(R.drawable.square);

        Picasso.with(context).load(data.getImage()).into(img_info);

        return convertView;
    }
}
