package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.MultiSelectLensProduct;
import com.sofudev.trackapptrl.Data.Data_compare_category;
import com.sofudev.trackapptrl.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_compare_product extends RecyclerView.Adapter<Adapter_compare_product.ViewHolder> {
    private Context context;
    private List<Data_compare_category> itemList;
    private List<Data_compare_category> tmpItemList = new ArrayList<>();
    private ArrayList<Data_compare_category> returnItem = new ArrayList<>();
    private MultiSelectLensProduct callback;

    public Adapter_compare_product(Context context, List<Data_compare_category> itemList, MultiSelectLensProduct callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(context).inflate(R.layout.item_compare_product, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tmpItemList.size() < 1)
        {
            tmpItemList.addAll(itemList);
        }

        holder.txtTitle.setText(itemList.get(position).getTitle());
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        UniversalFontTextView txtTitle, txtId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.item_compare_layout);
            txtId = itemView.findViewById(R.id.item_compare_id);
            txtTitle = itemView.findViewById(R.id.item_compare_title);
        }

        void bind(final Data_compare_category item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (returnItem.size() < 2)
                    {
                        item.setChecked(!item.isChecked());
                        txtTitle.setTextColor(item.isChecked() ? Color.parseColor("#FFFFFF") : Color.parseColor("#418DC8"));
                        layout.setBackgroundResource(item.isChecked() ? R.drawable.round_corner_selected : R.drawable.round_corner);

                        if (item.isChecked()){
                            if (returnItem.size() == 1)
                            {
                                if (returnItem.get(0) == null)
                                {
                                    returnItem.add(0, item);
                                }
                            }

                            returnItem.add(item);
                        }
                        else {
                            returnItem.remove(item);
                        }
                    }
                    else
                    {
                        if (item.isChecked()){
                            item.setChecked(!item.isChecked());
                            txtTitle.setTextColor(item.isChecked() ? Color.parseColor("#FFFFFF") : Color.parseColor("#418DC8"));
                            layout.setBackgroundResource(item.isChecked() ? R.drawable.round_corner_selected : R.drawable.round_corner);
                            returnItem.remove(item);
                        }
                        else
                        {
                            Toasty.warning(context, "Hanya bisa memilih 2 item saja", Toast.LENGTH_SHORT).show();
                        }
                    }

                    for(int i = 0; i < returnItem.size(); i++)
                    {
                        Log.d("Category Adapter : ", returnItem.get(i).getTitle());
                    }
                    callback.passResult(returnItem);
                }
            });
        }
    }
}
