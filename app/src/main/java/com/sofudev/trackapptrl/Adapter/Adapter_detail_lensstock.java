package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_item_lensstock;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_detail_lensstock extends RecyclerView.Adapter<Adapter_detail_lensstock.ViewHolder> {
    private Context context;
    private List<Data_item_lensstock> list;

    public Adapter_detail_lensstock(Context context, List<Data_item_lensstock> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_detail_lensstock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String qty = list.get(position).getQty() + " " + list.get(position).getLensuom();

        holder.txtItemName.setText(list.get(position).getLenscode());
        holder.txtItemDesc.setText(list.get(position).getLensname());
        holder.txtItemQty.setText(qty);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemName, txtItemDesc, txtItemQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemName = itemView.findViewById(R.id.recycler_detaillensstock_txtitemname);
            txtItemDesc = itemView.findViewById(R.id.recycler_detaillensstock_txtitemdesc);
            txtItemQty  = itemView.findViewById(R.id.recycler_detaillensstock_txtitemqty);
        }
    }
}
