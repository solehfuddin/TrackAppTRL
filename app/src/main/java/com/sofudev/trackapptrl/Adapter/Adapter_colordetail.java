package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.R;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

public class Adapter_colordetail extends RecyclerView.Adapter<Adapter_colordetail.ViewHolder> {
    private List<Data_color_filter> item;
    private Context context;

    public Adapter_colordetail(List<Data_color_filter> item, Context context) {
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_color_productdetail, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.checkBox.setUnCheckedColor(Color.parseColor(item.get(i).getColorCode()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewHolder.checkBox.setTooltipText(item.get(i).getColorName());
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomCheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.recycle_colorfilter_checkbox);
        }
    }
}
