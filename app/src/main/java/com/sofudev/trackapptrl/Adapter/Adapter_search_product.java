package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_search_product;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_search_product extends RecyclerView.Adapter<Adapter_search_product.ViewHolder> {

    private Context context;
    private List<Data_search_product> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_search_product(Context context, List<Data_search_product> item, RecyclerViewOnClickListener onClick) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtTitle.setText(item.get(position).getSearchTitle());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtTitle, txtId;
        RippleView btnDelete;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.item_search_title);
            btnDelete= itemView.findViewById(R.id.item_search_btndelete);
            cardview = itemView.findViewById(R.id.item_search_cardview);

            btnDelete.setOnClickListener(this);
            cardview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txtTitle.getText().toString());
        }
    }
}
