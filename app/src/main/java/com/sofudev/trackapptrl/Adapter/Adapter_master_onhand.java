package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_master_onhand;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_master_onhand extends RecyclerView.Adapter<Adapter_master_onhand.ViewHolder> {

    private Context context;
    private List<Data_master_onhand> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_master_onhand(Context context, List<Data_master_onhand> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data_master_onhand data = item.get(i);

        int count = Integer.parseInt(data.getQty());

        viewHolder.txtItemCode.setText(data.getItemCode());
        viewHolder.txtItemName.setText(data.getItemDesc());
        viewHolder.txtCounter.setText(data.getQty());

        if (count > 0)
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#f90606"));
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemCode, txtItemName, txtCounter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemCode = itemView.findViewById(R.id.recycler_onhand_txtItemCode);
            txtItemName = itemView.findViewById(R.id.recycler_onhand_txtItemName);
            txtCounter  = itemView.findViewById(R.id.recycler_onhand_txtCounter);
        }
    }
}
