package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sofudev.trackapptrl.Custom.CustomAdapterFrameColor;
import com.sofudev.trackapptrl.Data.Data_color_filter;
import com.sofudev.trackapptrl.R;

//import net.igenius.customcheckbox.CustomCheckBox;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_colorfilter extends RecyclerView.Adapter<Adapter_colorfilter.ViewHolder> {
    private List<Data_color_filter> item;
    private Context context;
    private CustomAdapterFrameColor Icommunication;
    private String chooseColor;

    private ArrayList<String> checkedColor = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int pos = viewHolder.getAdapterPosition();

        viewHolder.checkBox.setUnCheckedColor(Color.parseColor(item.get(pos).getColorCode()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            viewHolder.checkBox.setTooltipText(item.get(i).getColorName());
//        }

        int chooser = item.get(pos).getColorId();

        if (chooseColor != null && !chooseColor.isEmpty())
        {
            String value[] = chooseColor.split(",");

            for (String output: value)
            {
                if (chooser == Integer.valueOf(output.trim()))
                {
                    viewHolder.checkBox.setChecked(true);

                    checkedColor.add(String.valueOf(chooser));
                }
            }
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (isChecked)
                {
                    //Toasty.info(context, item.get(i).getColorName(), Toast.LENGTH_SHORT).show();
//                    Icommunication.response(item.get(i).getColorId(), item.get(i).getColorName());

                    checkedColor.add(String.valueOf(item.get(pos).getColorId()));
                }
                else
                {
                    checkedColor.remove(String.valueOf(item.get(pos).getColorId()));
                }

                if (!checkedColor.isEmpty())
                {
                    String output = "AND item.`color_type` IN ( " + checkedColor.toString() + ")";

                    output = output.replace("[","").replace("]","").trim();

                    Icommunication.response(output, checkedColor.toString());

                    Toasty.info(context, output, Toast.LENGTH_SHORT).show();
                    Log.d("SELECTED COLOR", checkedColor.toString());
                }
                else
                {
                    Icommunication.response("", checkedColor.toString());
                }
            }
        });
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
