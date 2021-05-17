package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_sub_categoryonhand;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_subcategory_onhand extends BaseAdapter {

    private LayoutInflater inflater;
    Context context;
    private List<Data_sub_categoryonhand> itemOnHand;
    private String pos;

    public Adapter_subcategory_onhand(Context context, List<Data_sub_categoryonhand> itemOnHand, String pos) {
        this.context = context;
        this.itemOnHand = itemOnHand;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return itemOnHand.size();
    }

    @Override
    public Object getItem(int position) {
        return itemOnHand.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
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

        String power = itemOnHand.get(position).getDesc();
        txtPower.setText(power);
        txtPower.setTextSize(16);
        txtPower.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 25, 30, 25);
        txtPower.setLayoutParams(params);
//        txtPower.setTextColor(Color.parseColor("#58595e"));

        if (power.contains(pos))
        {
            txtPower.setTextColor(Color.parseColor("#358bff"));
            imgCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            txtPower.setTextColor(Color.parseColor("#58595e"));
            imgCheck.setVisibility(View.INVISIBLE);
        }

//        imgCheck.setVisibility(View.GONE);

        return convertView;
    }
}
