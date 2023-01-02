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
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_courier_track extends RecyclerView.Adapter<Adapter_courier_track.ViewHolder> {

    private Context context;
    private List<Data_courier> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_courier_track(Context context, List<Data_courier> item, RecyclerViewOnClickListener itemClick) {
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
        final Data_courier data = item.get(position);

        holder.txtInvNumber.setText(data.getNo_inv());
        holder.txtDate.setText(data.getTgl_kirim());

        String title = "City Courier";
        holder.txtService.setTextColor(Color.parseColor("#2278d4"));
        holder.imgService.setImageResource(R.drawable.citycourier_21);
        holder.txtService.setText(title);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtInvNumber, txtService, txtDate;
        ImageView imgService;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtInvNumber = (UniversalFontTextView) itemView.findViewById(R.id.item_deliverytrack_txtawbnumber);
            txtService   = (UniversalFontTextView) itemView.findViewById(R.id.item_deliverytrack_txtservice);
            txtDate      = (UniversalFontTextView) itemView.findViewById(R.id.item_deliverytrack_txttgl);
            imgService   = (ImageView) itemView.findViewById(R.id.item_deliverytrack_icon);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.item_deliverytrack_constraint);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onItemClick(view, getLayoutPosition(), item.get(getLayoutPosition()).getNo_inv());
                }
            });
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), item.get(getLayoutPosition()).getNo_inv());
        }
    }
}
