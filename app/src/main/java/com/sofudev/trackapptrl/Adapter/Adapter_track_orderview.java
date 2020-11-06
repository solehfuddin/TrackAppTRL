package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_track_order;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_track_orderview extends RecyclerView.Adapter<Adapter_track_orderview.AdapterOrderviewHolder>{
    private Context context;
    private List<Data_track_order> list;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_track_orderview(Context context, List<Data_track_order> list, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public AdapterOrderviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_track_order_view, parent, false);
        AdapterOrderviewHolder adapterOrderviewHolderholder = new AdapterOrderviewHolder(view);
        return adapterOrderviewHolderholder;
    }

    @Override
    public void onBindViewHolder(AdapterOrderviewHolder holder, int position) {
        holder.txt_custname.setText(list.get(position).getOrder_custname());
        holder.txt_entrydate.setText(list.get(position).getOrder_entrydate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdapterOrderviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        UniversalFontTextView txt_custname, txt_entrydate;

        public AdapterOrderviewHolder(View itemView) {
            super(itemView);

            txt_custname = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorderview_txt_custname);
            txt_entrydate= (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorderview_txt_entrydate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, getLayoutPosition(), list.get(getLayoutPosition()).getOrder_custname());
        }
    }
}
