package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_delivery_iteminv extends RecyclerView.Adapter<Adapter_delivery_iteminv.ViewHolder> {

    private Context context;
    private List<String> item;

    public Adapter_delivery_iteminv(Context context, List<String> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_deliverytrack_iteminv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNote.setText(item.get(position));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        UniversalFontTextView txtNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNote = itemView.findViewById(R.id.layout_delivey_iteminv_txtnote);
        }
    }
}
