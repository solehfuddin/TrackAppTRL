package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_filter_onhand extends RecyclerView.Adapter<Adapter_filter_onhand.ViewHolder> {

    private Context context;
    private List<String> item;
    private RecyclerViewOnClickListener itemClick;
    private String TAG;

    public Adapter_filter_onhand(Context context, List<String> item, RecyclerViewOnClickListener itemClick, String TAG) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_filter_onhand, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(item.get(position));

        if (TAG.contains(item.get(position)))
        {
            holder.txtTitle.setBackgroundResource(R.drawable.filter_rectangle_green);
            holder.txtTitle.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtTitle.setPadding(10, 5, 10, 8);
        }
        else
        {
            holder.txtTitle.setBackgroundResource(R.drawable.filter_rectangle_black);
            holder.txtTitle.setTextColor(Color.parseColor("#58595e"));
            holder.txtTitle.setPadding(10, 5, 10, 8);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private UniversalFontTextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.layout_filteronhand_txttitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txtTitle.getText().toString());
        }
    }
}
