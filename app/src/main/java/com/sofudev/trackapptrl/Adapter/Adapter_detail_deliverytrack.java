package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_delivery_history_status;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_detail_deliverytrack extends RecyclerView.Adapter<Adapter_detail_deliverytrack.ViewHolder> {
    private Context context;
    private List<Data_delivery_history_status> item;
    private int courrier;
    DateFormat format = new DateFormat();

    public Adapter_detail_deliverytrack(Context context, List<Data_delivery_history_status> item, int courrier) {
        this.context = context;
        this.item = item;
        this.courrier = courrier;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_delivery_tracking, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_delivery_history_status dt = item.get(position);
        if (courrier == 0)
        {
            //21 Express
            String desc;

            if (!dt.getBranch_id().contains("null"))
            {
                desc  = dt.getStatus() + " BY " + dt.getStatus_by() + " (" + dt.getBranch_id() + ")";
            }
            else
            {
                desc  = dt.getStatus() + " BY " + dt.getStatus_by();
            }

            holder.txtDesc.setText(desc);
            holder.txtDate.setText(format.Indotime(dt.getTanggal()));

            if (dt.getStatus().contains("PICK UP"))
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkorange));
            }
            else if (dt.getStatus().contains("RECEIVED"))
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkgreen));
            }
            else
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkblue));
            }
        }
        else
        {
            //Tiki
            String desc = dt.getStatus_note() + " (" + dt.getBranch_id() + ")";
            holder.txtDesc.setText(desc);
            holder.txtDate.setText(format.Indotime(dt.getTanggal()));

            if (dt.getStatus().contains("MDE 02"))
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkorange));
            }
            else if (dt.getStatus().contains("POD 01"))
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkgreen));
            }
            else
            {
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.checkblue));
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private UniversalFontTextView txtDesc, txtDate;
        private TimelineView timelineView;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            txtDesc = itemView.findViewById(R.id.recy_deliverytrack_txtdesc);
            txtDate = itemView.findViewById(R.id.recy_deliverytrack_txtdate);
            timelineView = itemView.findViewById(R.id.recy_deliverytrack_timeline);
            timelineView.initLine(viewType);
        }
    }
}
