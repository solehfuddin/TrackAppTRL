package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofudev.trackapptrl.Custom.CustomAdapterFrameColor;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.R;

//import net.igenius.customcheckbox.CustomCheckBox;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

public class Adapter_colorfilter extends RecyclerView.Adapter<Adapter_colorfilter.ViewHolder> {
    private List<Data_color_filter> item;
    private Context context;
    private CustomAdapterFrameColor Icommunication;
    private String chooseColor;

    public Adapter_colorfilter(Context context, List<Data_color_filter> item, CustomAdapterFrameColor Icommunication, String chooseColor) {
        this.item = item;
        this.context = context;
        this.Icommunication = Icommunication;
        this.chooseColor = chooseColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_color_filter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.checkBox.setUnCheckedColor(Color.parseColor(item.get(i).getColorCode()));
        viewHolder.checkBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (isChecked)
                {
                    //Toasty.info(context, item.get(i).getColorName(), Toast.LENGTH_SHORT).show();
                    Icommunication.response(item.get(i).getColorId(), item.get(i).getColorName());
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewHolder.checkBox.setTooltipText(item.get(i).getColorName());
        }

        int chooser = item.get(i).getColorId();
        if (chooseColor != null)
        {
            String value[] = chooseColor.split(",");

            for (String output: value)
            {
                if (chooser == Integer.valueOf(output.trim()))
                {
                    viewHolder.checkBox.setChecked(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomCheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.recycle_colorfilter_checkbox);
        }
    }
}
