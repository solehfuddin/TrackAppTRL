package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_courier_dpodk extends RecyclerView.Adapter<Adapter_courier_dpodk.ViewHolder> {
    private Context context;
    private List<Data_courier> list;
    private RecyclerViewOnClickListener itemClick;
    private int typeCourier;

    public Adapter_courier_dpodk(Context context, List<Data_courier> list, int typeCourier, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
        this.typeCourier = typeCourier;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res;
        res = R.layout.recycle_courier_task_dpodk;
        View view = LayoutInflater.from(context).inflate(res, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtdpodknumber.setText(list.get(position).getNo_trx());
        holder.txtdatetime.setText(list.get(position).getTgl_kirim());
        int drawable;

        if (typeCourier == 1)
        {
            drawable = R.drawable.ic_package;
        }
        else
        {
            drawable = R.drawable.delivered_courier;
        }
        holder.imgIcon.setImageResource(drawable);

        String rute;

        if (list.get(position).getRute().length() >= 13)
        {
            rute = list.get(position).getRute().trim().substring(0, 12) + "...";
        }
        else
        {
            rute = list.get(position).getRute().trim();
        }

        holder.txtrute.setText(rute);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgIcon;
        UniversalFontTextView txtdpodknumber, txtdatetime, txtrute;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon        = (ImageView) itemView.findViewById(R.id.recycler_courierdpodk_icpackage);
            txtdpodknumber = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierdpodk_txtdpodknumber);
            txtdatetime    = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierdpodk_txtjamtgl);
            txtrute        = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierdpodk_txtrute);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), list.get(getLayoutPosition()).getNo_trx());
        }
    }
}
