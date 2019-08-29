package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_recent_view;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_recent_view extends RecyclerView.Adapter<Adapter_recent_view.ViewHolder> {
    Context context;
    List<Data_recent_view> item;
    RecyclerViewOnClickListener itemClick;

    public Adapter_recent_view(Context context, List<Data_recent_view> item, RecyclerViewOnClickListener onClick) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
    }

    @Override
    public Adapter_recent_view.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recentview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_recent_view.ViewHolder holder, int position) {
        holder.txtid.setText(String.valueOf(item.get(position).getId()));
        Picasso.with(context).load(item.get(position).getImage()).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image, btnDelete;
        TextView txtid;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_recentview_cardview);
            image = itemView.findViewById(R.id.item_recentview_image);
            btnDelete = itemView.findViewById(R.id.item_recentview_delete);
            txtid = itemView.findViewById(R.id.item_recentview_txtid);

            image.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txtid.getText().toString());
        }
    }
}
