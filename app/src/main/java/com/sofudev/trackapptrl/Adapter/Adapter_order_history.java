package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.sofudev.trackapptrl.Data.Data_order_history;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_order_history extends RecyclerView.Adapter<Adapter_order_history.TimelineViewHolder> {

    private List<Data_order_history> list;
    private Context context;

    public Adapter_order_history(Context context, List<Data_order_history> list ) {
        this.list = list;
        this.context = context;
    }


    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_order_history, parent, false);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        holder.txt_custname.setText(list.get(position).getCustname());
        holder.txt_datetime.setText(list.get(position).getDatetime());
        holder.txt_lensname.setText(list.get(position).getLensname());
        holder.txt_status.setText(list.get(position).getStatus());
        holder.txt_statusdesc.setText(list.get(position).getStatusdesc());

        holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));

        String status = list.get(position).getStatus();
        if (status.equals("PRNT"))
        {
            holder.rl_header.setBackgroundColor(Color.parseColor("#ff9100"));
            holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.dot_orangelight));
            holder.imageView.setImageResource(R.drawable.history_print);
            //holder.timelineView.setEndLine(Color.parseColor("#ff9100"), position);
        }
        else if (status.equals("PREP"))
        {
            holder.imageView.setImageResource(R.drawable.history_print);
        }
        else if (status.equals("WARE"))
        {
            holder.imageView.setImageResource(R.drawable.history_warehouse);
        }
        else if (status.equals("SURF"))
        {
            holder.imageView.setImageResource(R.drawable.history_surfacing);
        }
        else if (status.equals("COAT"))
        {
            holder.imageView.setImageResource(R.drawable.history_coating);
        }
        else if (status.equals("EDGE"))
        {
            holder.imageView.setImageResource(R.drawable.history_edging);
        }
        else if (status.equals("QUI"))
        {
            holder.imageView.setImageResource(R.drawable.history_qui);
        }
        else if (status.equals("SHIP"))
        {
            holder.rl_header.setBackgroundColor(Color.parseColor("#358bff"));
            holder.imageView.setImageResource(R.drawable.history_shipping);
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

        private TimelineViewHolder(View itemView, int viewType) {
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

            timelineView.initLine(viewType);
        }
    }
}
