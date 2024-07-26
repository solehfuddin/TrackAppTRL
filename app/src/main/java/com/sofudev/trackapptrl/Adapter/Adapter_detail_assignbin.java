package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_assignbin_line;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_detail_assignbin extends RecyclerView.Adapter<Adapter_detail_assignbin.ViewHolder> {
    private Context context;
    private List<Data_assignbin_line> list;

    public Adapter_detail_assignbin(Context context, List<Data_assignbin_line> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_detail_assignbin, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtItemName.setText(list.get(position).getItemCode());
        holder.txtItemDesc.setText(list.get(position).getItemDesc());
        holder.txtItemQty.setText(list.get(position).getItemQty() + " PCS");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemName, txtItemDesc, txtItemQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemName = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailassignbin_txtitemname);
            txtItemDesc = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailassignbin_txtitemdesc);
            txtItemQty  = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailassignbin_txtitemqty);
        }
    }
}
