package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_item_einvoice;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_detail_einvoice extends RecyclerView.Adapter<Adapter_detail_einvoice.ViewHolder> {
    private Context context;
    private List<Data_item_einvoice> list;

    public Adapter_detail_einvoice(Context context, List<Data_item_einvoice> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_einvoice_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data_item_einvoice item = list.get(position);

        holder.txtTitle.setText(item.getTitle());
        holder.txtPrice.setText(item.getPrice());

        if (item.getFlag().equals("B"))
        {
            holder.txtPrice.setTextColor(Color.parseColor("#de002b"));
        }
        else {
            holder.txtPrice.setTextColor(Color.parseColor("#3c215c"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtTitle, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.item_detaileinv_txttitle);
            txtPrice = itemView.findViewById(R.id.item_detaileinv_txtprice);
        }
    }
}
