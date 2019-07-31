package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_opticname;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_filter_optic extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;

    private List<Data_opticname> items;

    public TextView txt_username;

    public Adapter_filter_optic(Context context, List<Data_opticname> items) {
        this.context = context;
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
        if (layoutInflater == null)
        {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.listview_filter_optic, null);
        }

        UniversalFontTextView txt_opticname = (UniversalFontTextView) convertView.findViewById(R.id.listview_filter_opticname);
        BootstrapButton btn_status          = (BootstrapButton) convertView.findViewById(R.id.listview_filter_opticstatus);
        txt_username                        = (TextView) convertView.findViewById(R.id.listview_filter_username);

        Data_opticname data = items.get(position);

        txt_opticname.setText(data.getCustname());
        txt_username.setText(data.getUsername());

        if (data.getStatus().equals("A"))
        {
            btn_status.setText("Active");
            btn_status.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        }
        else if (data.getStatus().equals("I"))
        {
            btn_status.setText("Inactive");
            btn_status.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
        }

        return convertView;
    }
}
