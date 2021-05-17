package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.sofudev.trackapptrl.Custom.CustomAdapterFrameBrand;
import com.sofudev.trackapptrl.Data.Data_brand_filter;
import com.sofudev.trackapptrl.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class Adapter_brandfilter extends RecyclerView.Adapter<Adapter_brandfilter.ViewHolder> {
    private Context context;
    private List<Data_brand_filter> item;
    private CustomAdapterFrameBrand Icommunication;
    private String chooseBrand;

    private ArrayList<String> checkedBrand = new ArrayList<>();

    public Adapter_brandfilter(Context context, List<Data_brand_filter> item, CustomAdapterFrameBrand Icommunication, String chooseBrand) {
        this.context = context;
        this.item = item;
        this.Icommunication = Icommunication;
        this.chooseBrand = chooseBrand;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_brand_filter, viewGroup, false);
        return new Adapter_brandfilter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int pos = viewHolder.getAdapterPosition();
        viewHolder.cb_brand.setText(item.get(pos).getBrandName());

        int chooser = item.get(pos).getBrandId();

        if (chooseBrand != null && !chooseBrand.isEmpty())
        {
            String value[] = chooseBrand.split(",");

            for (String output : value) {
                //Toasty.success(context, output, Toast.LENGTH_SHORT).show();

                if (chooser == Integer.valueOf(output.trim()))
                {
                    viewHolder.cb_brand.setChecked(true);

                    checkedBrand.add(String.valueOf(chooser));
                }

//                checkedBrand.add(String.valueOf(output.trim()));
            }
        }

        viewHolder.cb_brand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {

//                    Icommunication.response(item.get(pos).getBrandId(), item.get(pos).getBrandName());


                    checkedBrand.add(String.valueOf(item.get(pos).getBrandId()));

                }
                else
                {
//                    checkedBrand.remove(item.get(pos).getBrandId());
                    checkedBrand.remove(String.valueOf(item.get(pos).getBrandId()));
                }

                if (!checkedBrand.isEmpty())
                {
                    String output = "AND category.`id` IN ( " + checkedBrand.toString() + ")";

                    output = output.replace("[","").replace("]","").trim();

                    Icommunication.response(output, checkedBrand.toString());

                    Toasty.info(context, output, Toast.LENGTH_SHORT).show();
                    Log.d("SELECTED BRAND", checkedBrand.toString());
                }
                else
                {
                    Icommunication.response("", checkedBrand.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_brand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cb_brand = itemView.findViewById(R.id.recycler_brandfilter_checkbox);
        }
    }
}
