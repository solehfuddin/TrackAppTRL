package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.sofudev.trackapptrl.Data.Data_order_history;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_order_history_stock extends RecyclerView.Adapter<Adapter_order_history_stock.TimelineViewHolder> {

    private List<Data_order_history> list;
    private Context context;
    private String level;

    public Adapter_order_history_stock(List<Data_order_history> list, Context context, String level) {
        this.list = list;
        this.context = context;
        this.level = level;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_order_history, parent, false);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.txt_custname.setText(list.get(position).getCustname());
        holder.txt_datetime.setText(list.get(position).getDatetime());
        holder.txt_lensname.setText(list.get(position).getLensname());
        holder.txt_status.setText(list.get(position).getStatus());
        holder.txt_statusdesc.setText(list.get(position).getStatusdesc());
        String status = list.get(position).getStatus();

        holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));

        if (status.equals("CS"))
        {
            holder.rl_header.setBackgroundColor(Color.parseColor("#ff9100"));
            holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.dot_orangelight));
            holder.imageView.setImageResource(R.drawable.img_cs);
            //holder.timelineView.setEndLine(Color.parseColor("#ff9100"), position);
        }
        else if (status.equals("WARE"))
        {
            holder.imageView.setImageResource(R.drawable.img_log);
        }
        else if (status.equals("ADMIN"))
        {
            holder.imageView.setImageResource(R.drawable.img_adm);

            if (level.equals("1"))
            {
                holder.txt_statusdesc.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.txt_statusdesc.setVisibility(View.GONE);
            }
        }
        else if (status.equals("SHIP"))
        {
            holder.rl_header.setBackgroundColor(Color.parseColor("#358bff"));
            holder.imageView.setImageResource(R.drawable.ic_shipping_moto);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_custname, txt_datetime, txt_lensname, txt_status, txt_statusdesc;
        private ImageView imageView;
        private RelativeLayout rl_header;
        private TimelineView timelineView;
        private CardView cardView;

        public TimelineViewHolder(@NonNull View itemView, int viewTYpe) {
            super(itemView);

            txt_custname    = (TextView) itemView.findViewById(R.id.recy_orderhistory_txt_custname);
            txt_datetime    = (TextView) itemView.findViewById(R.id.recy_orderhistory_txt_datetime);
            txt_lensname    = (TextView) itemView.findViewById(R.id.recy_orderhistory_txt_lensname);
            txt_status      = (TextView) itemView.findViewById(R.id.recy_orderhistory_txt_status);
            txt_statusdesc  = (TextView) itemView.findViewById(R.id.recy_orderhistory_txt_statusdesc);
            imageView       = (ImageView) itemView.findViewById(R.id.recy_orderhistory_imageview);
            rl_header       = (RelativeLayout) itemView.findViewById(R.id.recy_orderhistory_rl_Header);
            timelineView    = (TimelineView) itemView.findViewById(R.id.recy_orderhistory_timeline);
            cardView        = (CardView) itemView.findViewById(R.id.recy_orderhistory_cardview);

            timelineView.initLine(viewTYpe);
        }
    }
}
