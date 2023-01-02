package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.CustomRecyclerOrderHistoryClick;
import com.sofudev.trackapptrl.Data.Data_orderhistory_optic;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_orderhistory_frame extends RecyclerView.Adapter<Adapter_orderhistory_frame.ViewHolder> {

    private Context context;
    private List<Data_orderhistory_optic> itemOrder;
    private CustomRecyclerOrderHistoryClick itemClick;

    public Adapter_orderhistory_frame(Context context, List<Data_orderhistory_optic> itemOrder, CustomRecyclerOrderHistoryClick itemClick) {
        this.context = context;
        this.itemOrder = itemOrder;
        this.itemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_ordersummary_frame, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data_orderhistory_optic data = itemOrder.get(position);

        holder.txtDate.setText(data.getTanggalOrder());
        holder.txtOrderid.setText(data.getNomorOrder());
        holder.txtTotal.setText(data.getTotalBiaya());
        holder.txtStatus.setText(data.getStatusOrder());

        String status = data.getStatusOrder();

        if (status.equals("Pending") || status.contains("Pending") || status.contentEquals("Pending"))
        {
            holder.txtStatus.setText(status);
            holder.txtStatus.setTextColor(Color.parseColor("#ffcc0000"));
        }
        else if (status.equals("Cancel") || status.contains("Cancel") || status.contentEquals("Cancel"))
        {
            holder.txtStatus.setText(status);
            holder.txtStatus.setTextColor(Color.parseColor("#FF9100"));
        }
        else if (status.equals("Failure") || status.contains("Failure") || status.contentEquals("Failure"))
        {
            holder.txtStatus.setText("Failure");
            holder.txtStatus.setTextColor(Color.parseColor("#ffcc0000"));
        }
    }

    @Override
    public int getItemCount() {
        return itemOrder.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtDate, txtOrderid, txtTotal, txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate     = itemView.findViewById(R.id.recycle_ordersummary_txt_date);
            txtOrderid  = itemView.findViewById(R.id.recycle_ordersummary_txt_ordernumber);
            txtTotal    = itemView.findViewById(R.id.recycle_ordersummary_txt_pricetotal);
            txtStatus   = itemView.findViewById(R.id.recycle_ordersummary_txt_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            itemClick.onItemClick(view, this.getLayoutPosition(), itemOrder.get(pos).getNomorOrder(),
                    itemOrder.get(pos).getStatusOrder(), itemOrder.get(pos).getNamaPasien(), itemOrder.get(pos).getPaymentType());
        }
    }
}
