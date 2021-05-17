package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_delivery_track;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_delivery_track extends RecyclerView.Adapter<Adapter_delivery_track.ViewHolder> {

    private Context context;
    private List<Data_delivery_track> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_delivery_track(Context context, List<Data_delivery_track> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delivery_tracking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data_delivery_track data = item.get(position);

        holder.txtAwb.setText(data.getAwbNumber());
        holder.txtDate.setText(data.getDatePickup());

        if (data.getServiceName().contains("ECO") || data.getServiceName().contains("REG"))
        {
            String title = "Regular Service";
            holder.txtService.setTextColor(Color.parseColor("#14a895"));
            holder.iconService.setImageResource(R.drawable.eco_21);
            holder.txtService.setText(title);
        }
        else if (data.getServiceName().contains("ONS"))
        {
            String title = "Over Night Service";
            holder.txtService.setTextColor(Color.parseColor("#ff9100"));
            holder.iconService.setImageResource(R.drawable.ons_21);
            holder.txtService.setText(title);
        }
        else if (data.getServiceName().contains("SDS") || data.getServiceName().contains("TDS"))
        {
            String title = "Same Day Service";
            holder.txtService.setTextColor(Color.parseColor("#d1395c"));
            holder.iconService.setImageResource(R.drawable.sds_21);
            holder.txtService.setText(title);
        }
        else if (data.getServiceName().contains("INTERNATIONAL") || data.getServiceName().contains("int"))
        {
            String title = "International";
            holder.txtService.setTextColor(Color.parseColor("#a854d4"));
            holder.iconService.setImageResource(R.drawable.inter_21);
            holder.txtService.setText(title);
        }
        else
        {
            String title = "City Courier";
            holder.txtService.setTextColor(Color.parseColor("#2278d4"));
            holder.iconService.setImageResource(R.drawable.citycourier_21);
            holder.txtService.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtAwb, txtService, txtDate;
        ImageView iconService;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAwb = itemView.findViewById(R.id.item_deliverytrack_txtawbnumber);
            txtService = itemView.findViewById(R.id.item_deliverytrack_txtservice);
            txtDate = itemView.findViewById(R.id.item_deliverytrack_txttgl);
            iconService = itemView.findViewById(R.id.item_deliverytrack_icon);
            constraintLayout = itemView.findViewById(R.id.item_deliverytrack_constraint);

            itemView.setOnClickListener(this);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(v, getLayoutPosition(), item.get(getLayoutPosition()).getAwbNumber());
                }
            });
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, getLayoutPosition(), item.get(getLayoutPosition()).getAwbNumber());
        }
    }
}
