package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_news;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_news extends RecyclerView.Adapter<Adapter_news.ViewHolder> {
    private Context context;
    private List<Data_news> item;
    private RecyclerViewOnClickListener onClickListener;

    public Adapter_news(Context context, List<Data_news> item, RecyclerViewOnClickListener onClickListener) {
        this.context = context;
        this.item = item;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uploadBy = item.get(position).getCreate_by_user() + " - " + item.get(position).getCreate_date();
        holder.txtCategory.setText(item.get(position).getCategory());
        holder.txtTitle.setText(item.get(position).getTitle());
        holder.txtDate.setText(uploadBy);
        holder.txtId.setText(item.get(position).getId());

        Picasso.with(context).load(item.get(position).getImages()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RippleView rippleView;
        ImageView imageView;
        UniversalFontTextView txtId, txtCategory, txtTitle, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rippleView = itemView.findViewById(R.id.item_news_ripple);
            imageView = itemView.findViewById(R.id.item_news_imgnews);
            txtId = itemView.findViewById(R.id.item_news_txtid);
            txtCategory = itemView.findViewById(R.id.item_news_txtcategory);
            txtTitle = itemView.findViewById(R.id.item_news_txttitle);
            txtDate = itemView.findViewById(R.id.item_news_txtdate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(view, getAdapterPosition(), txtId.getText().toString());
        }
    }
}
