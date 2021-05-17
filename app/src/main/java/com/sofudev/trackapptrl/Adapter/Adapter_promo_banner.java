package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_promo_banner;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_promo_banner extends RecyclerView.Adapter<Adapter_promo_banner.ViewHolder> {
    private Context context;
    private List<Data_promo_banner> item;
    private RecyclerViewOnClickListener onClickListener;

    public Adapter_promo_banner(Context context, List<Data_promo_banner> item, RecyclerViewOnClickListener onClickListener) {
        this.context = context;
        this.item = item;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_promo_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(context).load(item.get(position).getUrl()).fit().placeholder(R.drawable.logo_trl3).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.layout_promobanner_imageview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v, this.getLayoutPosition(), "kosong");
        }
    }
}
