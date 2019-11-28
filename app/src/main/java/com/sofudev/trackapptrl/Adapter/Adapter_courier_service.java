package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_courier_service extends RecyclerView.Adapter<Adapter_courier_service.ViewHolder> {
    private Context context;
    private List<Data_spin_shipment> item;
    private RecyclerViewOnClickListener itemClick;

    private int checkPos = 0;

    public Adapter_courier_service(Context context, List<Data_spin_shipment> item, RecyclerViewOnClickListener onClick) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
    }

    @NonNull
    @Override
    public Adapter_courier_service.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_courier_service, parent, false);
        return new Adapter_courier_service.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_courier_service.ViewHolder holder, int position) {
        Data_spin_shipment data = item.get(position);
        String courier = data.getKurir() + " - " + data.getService();

        holder.txt_courier.setText(courier);
        holder.txt_idshipping.setText(String.valueOf(data.getId()));
        holder.txt_city.setText(data.getCity());
        holder.txt_province.setText(data.getProvince());
        holder.txt_estimasi.setText(data.getEstimasi());
        Picasso.with(context).load(data.getIcon()).fit().into(holder.img_icon);

        if (checkPos == -1)
        {
            holder.img_check.setVisibility(View.INVISIBLE);
        }
        else
        {
            if (checkPos == position)
            {
                holder.img_check.setVisibility(View.VISIBLE);
//                notifyItemChanged(position);
            }
            else
            {
                holder.img_check.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txt_courier, txt_city, txt_province, txt_idshipping, txt_estimasi;
        ImageView img_icon, img_check;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_idshipping = itemView.findViewById(R.id.listitem_courier_service_txtIdCourier);
            txt_courier = itemView.findViewById(R.id.listitem_courier_service_txtCourierName);
            txt_city = itemView.findViewById(R.id.listitem_courier_service_txtCity);
            txt_province = itemView.findViewById(R.id.listitem_courier_service_txtProvince);
            img_icon = itemView.findViewById(R.id.listitem_courier_service_imgCourier);
            img_check= itemView.findViewById(R.id.listitem_courier_service_imgCheck);
            txt_estimasi = itemView.findViewById(R.id.listitem_courier_service_txtEstimate);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    img_check.setVisibility(View.VISIBLE);
//                    if (checkPos != getAdapterPosition())
//                    {
//                        notifyItemChanged(checkPos);
//                        checkPos = getAdapterPosition();
//                    }
//                }
//            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txt_idshipping.getText().toString());

            img_check.setVisibility(View.VISIBLE);
            if (checkPos != getAdapterPosition())
            {
                notifyItemChanged(checkPos);
                checkPos = getAdapterPosition();
            }
        }
    }
}
